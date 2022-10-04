/*
 * Code Scanner. An android app to scan and create codes(barcodes, QR codes, etc)
 * Copyright (C) 2022 czlucius
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.czlucius.scan.ui;

import static android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.camera.core.UseCase;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.preference.PreferenceManager;

import com.czlucius.scan.R;
import com.czlucius.scan.callbacks.CameraFailureCallback;
import com.czlucius.scan.callbacks.CameraShutdownCallback;
import com.czlucius.scan.callbacks.Consumer;
import com.czlucius.scan.callbacks.SetTouchListenerCallback;
import com.czlucius.scan.callbacks.UseCaseCreator;
import com.czlucius.scan.databinding.FragmentScannerBinding;
import com.czlucius.scan.exceptions.NoCameraException;
import com.czlucius.scan.exceptions.ReferenceInvalidException;
import com.czlucius.scan.objects.Availability;
import com.czlucius.scan.objects.CamAccess;
import com.czlucius.scan.objects.ScanningWrapper;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.common.InputImage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ScannerFragment extends Fragment {
    private final String cameraPermission = Manifest.permission.CAMERA;
    private CamAccess camAccessObj;
    private CameraShutdownCallback cameraShutdownCallback;
    private FragmentScannerBinding binding;
    private ExecutorService camExecutor;
    private final static String TAG = "ScannerFragment";
    private ScanViewModel vm;
    private final BarcodeScanner barcodeScanner = BarcodeScanning.getClient();

    private static final int CAMERA_REQUEST_CODE = 1000;
    private static final int GALLERY_REQUEST_CODE = 1001;





    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        super.onCreateView(inflater, container, savedInstanceState);
        binding = FragmentScannerBinding.inflate(getLayoutInflater());
        setHasOptionsMenu(true);


        // Camera Use Cases:
        UseCaseCreator useCaseCreator = () -> {

            // Use Case 1: Preview
            Preview preview = new Preview.Builder().build();
            if (binding != null && binding.previewView != null) {
                preview.setSurfaceProvider(binding.previewView.getSurfaceProvider());
            }

            // Use case 2: Barcode analysis
            ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build();

            imageAnalysis.setAnalyzer(camExecutor, vm.getCodeAnalyser());

            return new UseCase[]{preview, imageAnalysis};
        };

        try {
            camAccessObj = new CamAccess(new WeakReference<>(getContext()), useCaseCreator);
        } catch (NoCameraException e) {
            noCamera();
        }
        return binding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_scan, menu);
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (camAccessObj.getFlash() == Availability.UNAVAILABLE) {
            menu.findItem(R.id.flash_toggle).setEnabled(false);
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.flash_toggle) {
            flashToggle();
        } else if (itemId == R.id.open_from_gallery) {
            analyzeFromGallery();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        // Handle intent.
        Intent intent = requireActivity().getIntent();
        if (!(intent == null || intent.getType() == null || !intent.getType().startsWith("image/"))) {
            // Get data.
            Uri uri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
            processImageUri(uri);
            requireActivity().setIntent(null); // do not want multiple popups
        }





        // Initialise ViewModel
        vm = new ViewModelProvider.AndroidViewModelFactory(requireActivity().getApplication()).create(ScanViewModel.class);

        camExecutor = Executors.newSingleThreadExecutor();

        vm.getCodes().observe(getViewLifecycleOwner(), scanningWrappers -> {
            for (ScanningWrapper s : scanningWrappers) {
                s.display(getContext(), !vm.getBatchScanEnabled());
            }
        });


        vm.getModelDownloaded().observe(getViewLifecycleOwner(), downloaded -> {
            if (downloaded == Boolean.FALSE) {
                // Not downloaded yet
                Snackbar.make(view, R.string.no_model, Snackbar.LENGTH_LONG).show();
            }
        });

        // Check if batch scan has been activated.
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(requireContext());
        if (prefs.contains("batch_scan")) {
            vm.setBatchScanEnabled(prefs.getBoolean("batch_scan", false));
        }
        vm.getBatchScanEnabledLiveData().observe(getViewLifecycleOwner(), enabled -> {
            binding.floatingTooltip.floatingTooltipRoot.setClickable(enabled);
        });

        vm.getNumberOfCodesScannedLiveData().observe(getViewLifecycleOwner(), num -> {
            if (vm.getBatchScanEnabled()) {
                binding.floatingTooltip.batchScanProceed.setVisibility(num > 0 ? View.VISIBLE : View.GONE);

                String tooltipText = num > 0 ? getString(R.string.codes_scanned, num) : getString(R.string.position_code);
                binding.floatingTooltip.floatingTooltipDescription.setText(tooltipText);
            }
        });
        binding.floatingTooltip.floatingTooltipRoot.setOnClickListener(v -> {
            // Batch scan
            if (vm.getBatchScanEnabled() && vm.getNumberOfCodesScanned() > 0) {
                // Navigate to History
                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_scannerFragment_to_historyFragment);
            }
        });


        com.czlucius.scan.preferences.Settings globalSettings = com.czlucius.scan.preferences.Settings.getInstance(getContext());

        // Request for permission only if the on-boarding is cleared.
        if (!globalSettings.getShouldShowOnboarding()) {
            int permissionStatus = requireContext().checkSelfPermission(cameraPermission);
            if (permissionStatus == PackageManager.PERMISSION_GRANTED) {

                startCamera();

            } else if (shouldShowRequestPermissionRationale(cameraPermission)) {
                displayRationale();
            } else {
                requestPm();
            }
        }


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (vm.getCodes() == null) return;
        if (vm.getCodes().getValue() == null) return;

        for (ScanningWrapper s: vm.getCodes().getValue()) {
            s.dismissDialog();
        }
        binding = null;
        if (cameraShutdownCallback != null) {
            // Shutdown the camera if there is a callback, if not, most likely the camera is not even open anyway.
            cameraShutdownCallback.shutdown();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (camExecutor != null) {
            camExecutor.shutdown();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImg = data.getData();
            processImageUri(selectedImg);
        }
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permissions has been granted.
                startCamera();
            } else if (shouldShowRequestPermissionRationale(cameraPermission)) {
                displayRationale();
            } else if (requireContext().checkSelfPermission(cameraPermission) == PackageManager.PERMISSION_DENIED
                    && !shouldShowRequestPermissionRationale(cameraPermission)) {
                permissionDeniedPermanently(); // Permanent denial
            }
        }
    }

    private void processImageUri(Uri selectedImg) {
        if (selectedImg == null) {
            imageNotFound(getContext());
            return;
        }

        InputStream inputStream = openInputStream(selectedImg);
        if (inputStream == null) return;

        Bitmap bmp = BitmapFactory.decodeStream(inputStream);
        // Send bitmap to ML Kit image analyser
        processImage(inputStream, bmp, detected -> {
            if (!detected) {
                Snackbar.make(requireView(), R.string.no_codes_detected, Snackbar.LENGTH_SHORT).show();
            }
        });
    }





    private void processImage(InputStream inputStream, Bitmap bmp, Consumer<Boolean> detected) {

        // Analysing as if image is upright as API should be able to detect rotated codes.
        InputImage inputImage = InputImage.fromBitmap(bmp, 0);
        barcodeScanner.process(inputImage)
                .addOnSuccessListener(barcodes -> {
                    vm.scanBarcodes(barcodes);
                    detected.accept(barcodes.size() > 0);
                })
                .addOnCompleteListener(task -> {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }


    private InputStream openInputStream(Uri selectedImg) {
        ContentResolver cr = requireActivity().getContentResolver();
        InputStream inputStream;
        try {
            inputStream = cr.openInputStream(selectedImg);
            if (inputStream == null) {
                imageNotFound(getContext());
                return null;
            }
        } catch (FileNotFoundException e) {
            imageNotFound(getContext());
            return null;
        }
        return inputStream;
    }

    private void imageNotFound(Context context) {
        Toast.makeText(context, R.string.img_not_found, Toast.LENGTH_SHORT).show();
    }


    private void flashToggle() {
        if (camAccessObj.getFlash() == Availability.ON) {
            camAccessObj.toggleFlash(Availability.OFF);
            //item.setIcon(R.drawable.ic_baseline_flash_off_24);
        } else {
            camAccessObj.toggleFlash(Availability.ON);
            //item.setIcon(R.drawable.ic_baseline_flash_on_24);
        }
    }


    public void analyzeFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, EXTERNAL_CONTENT_URI);
        intent.putExtra(Intent.EXTRA_MIME_TYPES, "image/*");
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }

    private void requestPm() {
        // Permission denied. Request the permission.
        requestPermissions(new String[]{cameraPermission}, CAMERA_REQUEST_CODE);
    }

    private void displayRationale() {
        // Display a permission rationale.
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.cam_pm)
                .setMessage(R.string.grant_pm_rationale)
                .setPositiveButton(R.string.grant, (dialog, which) -> requestPm())
                .setNegativeButton(R.string.cancel, (dialog, which) -> permissionDenied())
                .setCancelable(false)
                .create()
                .show();
    }

    private void permissionDenied() {

        Snackbar snack = Snackbar.make(binding.coordinatorLayout,
                R.string.pm_denial, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.grant, v -> requestPm());

        setMargins(snack);
        snack.setBehavior(new DisableSwipeBehavior());

        snack.show();
    }

    private void permissionDeniedPermanently() {
        Snackbar snack = Snackbar.make(binding.previewView, R.string.pm_denial_permanant, Snackbar.LENGTH_INDEFINITE);
        snack.setAction(R.string.grant, v -> {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri pkg = Uri.fromParts("package", requireContext().getPackageName(), null);
            intent.setData(pkg);
            startActivity(intent);
        });
        setMargins(snack);
        snack.show();
    }

    private void setMargins(Snackbar snack) {
        CoordinatorLayout.LayoutParams params = ((CoordinatorLayout.LayoutParams) snack.getView().getLayoutParams());
        if (requireActivity().findViewById(R.id.bottomNav) != null) {
            params.bottomMargin = requireActivity().findViewById(R.id.bottomNav).getHeight();
        }
        snack.getView().setLayoutParams(params);
    }


    @SuppressLint("ClickableViewAccessibility")
    public void startCamera() {
        CameraFailureCallback cameraFailureCallback = cameraFailureDialog();
        // Al‌‍‌‌‍‍‌‌‍‍‌‌‍‍‌‌‍‍‌‍‍‌‌‌‍‍‌‌‍‍‌‌‌‍‌‌‍‍‌‌‍‌‌‌‍‍‌‌‌‍‌‍‍‌‌‍‌‌‌‍‍‌‌‌‌‍‌‍‍‌‌‌‌‍‌‍‍‌‌‌‍‍‌‌‍‍‌‍‍‌‌‌‍‍‌‌‍‌‌‌‍‍‌‌‌‍‌‍‍‌‌‍‌‌‌‍‍‌‌‌‌‍‌‌‍‍‍‌‌‌‌‌‍‍‌‍‌‌‌‍‍‌‌‌‍‌‌‌‍‍‌‌‌‍‌‍‍‌‌‌‌‍‌‌‍‍‌‍‌‌‌‍‍‌‌‍‍‌‌‌‍‍‌‌‍‍‌‌‍‍‌‌‌‍‌‌‍‍‍‌‌‍‌‌‍‍‌‌‌‌‌‌‍‍‌‍‌‍‌‌‍‍‌‍‍‌‌‍‍‌‌‌‍‍‌‌‍‍‌‌‍‍‌‌‍‍‌‍‍‌‌‍‍‌‌‍‍‌‌‌‍‍‌‍‍‍‌‌‍‍‍‌‌‌‌‌‍‍‌‌‌‍‌‌‍‍‌‌‌‌‌‌‍‍‍‌‌‍‌‌‍‍‌‍‍‌‌‍‍‌‌‌‌‍‌‌‍‍‌‌‍‍‌‍‍‌‌‌‍‌‌‍‍‌‌‍‍‌‌‌‍‍‌‌‍‍‌‌‍‍‍‌‌‍‌‌‍‍‌‍‌‍‌‍‍‌‌‌‍‌‌‌‍‍‌‌‌‌‌‌‍‍‌‍‍‌‌‌‍‍‌‍‍‌‌‌‍‍‌‌‍‍‌‌‍‍‌‌‌‌‌‍‍‌‌‌‍‌‌‌‍‍‌‍‌‌‌‌‍‍‌‍‍‍‌‍‍‌‌‌‍‌‌‌‍‍‍‌‌‌‌‌‍‍‌‍‌‍‌‌‍‍‌‍‌‍‌‍‍‌‌‌‍‌‌‍‍‌‌‍‌‍‌‌‍‍‌‌‌‍‌‍‍‌‌‍‌‌‌‌‍‍‌‌‌‍‌‌‍‍‍‌‌‍low the class to modify the touch listener to accept zoom (on touches in the second future) in the first future. (async is complicated)
        SetTouchListenerCallback touchListenerCallback = listener -> binding.previewView.setOnTouchListener(listener);

        try {
            cameraShutdownCallback = camAccessObj.startCamera(getViewLifecycleOwner(), cameraFailureCallback, touchListenerCallback);

        } catch (ReferenceInvalidException e) {
            Log.e(TAG, "Context invalid.");
        }

    }


    @NonNull
    private CameraFailureCallback cameraFailureDialog() {
        return e -> Log.e(TAG, "Camera unavailable.");
    }

    private void noCamera() {
        AlertDialog cameraWarningDialog = new MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.no_cam)
                .setMessage(R.string.no_cam_msg)
                .create();

        cameraWarningDialog.show();
    }

    private static class DisableSwipeBehavior extends BaseTransientBottomBar.Behavior {
        @Override
        public boolean canSwipeDismissView(@NonNull View view) {
            return false;
        }
    }


}