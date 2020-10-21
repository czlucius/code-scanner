package com.czlucius.scan.ui;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.czlucius.scan.callbacks.CameraFailureCallback;
import com.czlucius.scan.R;
import com.czlucius.scan.callbacks.UseCaseCreator;
import com.czlucius.scan.database.HistoryDatabase;
import com.czlucius.scan.databinding.FragmentScannerBinding;
import com.czlucius.scan.exceptions.NoCameraException;
import com.czlucius.scan.exceptions.ReferenceInvalidException;
import com.czlucius.scan.objects.Availability;
import com.czlucius.scan.objects.CamAccess;
import com.czlucius.scan.objects.Code;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.mlkit.vision.barcode.Barcode;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.common.InputImage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;


public class ScannerFragment extends Fragment {
    private final String cameraPermission = Manifest.permission.CAMERA;
    private CamAccess camAccessObj;
    private FragmentScannerBinding binding;
    private ExecutorService camExecutor;
    private static String TAG = "ScannerFragment";
    private ScanVM vm;
    private final BarcodeScanner barcodeScanner = BarcodeScanning.getClient();

    private static final int CAMERA_REQUEST_CODE = 1000;
    private static final int GALLERY_REQUEST_CODE = 1001;


    public ScannerFragment() {
        // Required empty public constructor
    }


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
            preview.setSurfaceProvider(binding.previewView.getSurfaceProvider());


            // Use case 2: Barcode analysis
            ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
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

        // Initialise ViewModel
        vm = new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication()).create(ScanVM.class);

        camExecutor = Executors.newSingleThreadExecutor();

        //Handle the changes in the array list of codes. Pull up the ResultFragment.
        vm.getCodes().observe(getViewLifecycleOwner(), new Observer<ArrayList<Code>>() {
            @Override
            public void onChanged(ArrayList<Code> codes) {
                if (codes.size() >= 1) {
                    for (Code code : codes) {
                        ResultDisplayDialog dialog = new ResultDisplayDialog(getContext(), code);
                        dialog.setOnCancelListener(dialogInterface -> {
                            // When cancel, remove the instance of Code from the ViewModel's list.
                            vm.getCodes().getValue().remove(code);
                        });
                        dialog.show();
                    }
                }
            }
        });


        // Request for permission
        int permissionStatus = requireContext().checkSelfPermission(cameraPermission);
        if (permissionStatus == PackageManager.PERMISSION_GRANTED) {

            startCamera();

        } else if (shouldShowRequestPermissionRationale(cameraPermission)) {
            displayRationale();
        } else {
            requestPm();
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

            if (selectedImg == null) {
                imageNotFound(getContext());
                return;
            }
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            ContentResolver cr = getActivity().getContentResolver();
            InputStream inputStream;
            try {
                inputStream = cr.openInputStream(selectedImg);
                if (inputStream == null) {
                    imageNotFound(getContext());
                    return;
                }
            } catch (FileNotFoundException e) {
                imageNotFound(getContext());
                return;
            }

            Bitmap bmp = BitmapFactory.decodeStream(inputStream);
            // Send bitmap to ML Kit image analyser
            InputImage inputImage = InputImage.fromBitmap(bmp, 0); //TODO get rotation degree.
            barcodeScanner.process(inputImage)
                    .addOnSuccessListener(barcodes -> {
                        for (Barcode barcode : barcodes) {
                            Code c = new Code(barcode);
                            vm.getCodes().getValue().add(c);
                            // Add to history
                            HistoryDatabase.insertCode(getContext(), c);
                        }
                        vm.notifyLiveDataChanged();


                    })
                    .addOnCompleteListener(task -> {
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });


        }
    }

    private void imageNotFound(Context context) {
        Toast.makeText(context, "Image not found.", Toast.LENGTH_SHORT).show();
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
        new MaterialAlertDialogBuilder(getContext())
                .setTitle("Camera permission")
                .setMessage("Please grant camera permission.\nThis is for this app to scan codes(barcode/QR code).")
                .setPositiveButton("Grant", (dialog, which) -> requestPm())
                .setNegativeButton("Cancel", (dialog, which) -> permissionDenied())
                .setCancelable(false)
                .create()
                .show();
    }

    private void permissionDenied() {

        Snackbar snack = Snackbar.make(binding.coordinatorLayout,
                "Camera permission has been denied.", Snackbar.LENGTH_INDEFINITE)
                .setAction("Grant", v -> requestPm());

        setMargins(snack);
        snack.setBehavior(new DisableSwipeBehavior());

        snack.show();
    }

    private void permissionDeniedPermanently() {
        Snackbar snack = Snackbar.make(binding.previewView, "Camera permission has been denied permanently. Please go to system settings.", Snackbar.LENGTH_INDEFINITE);
        snack.setAction("Grant", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri pkg = Uri.fromParts("package", getContext().getPackageName(), null);
                intent.setData(pkg);
                startActivity(intent);
            }
        });
        setMargins(snack);
        snack.show();
    }

    private void setMargins(Snackbar snack) {
        CoordinatorLayout.LayoutParams params = ((CoordinatorLayout.LayoutParams) snack.getView().getLayoutParams());
        params.bottomMargin = getActivity().findViewById(R.id.bottomNav).getHeight();
        snack.getView().setLayoutParams(params);
    }


    public void startCamera() {
        CameraFailureCallback cameraFailureCallback = cameraFailureDialog();
        try {
            camAccessObj.startCamera(getViewLifecycleOwner(), cameraFailureCallback);
        } catch (ReferenceInvalidException e) {
            Log.e(TAG, "Context invalid.");
        }
    }
    
    public static Bitmap drawableToBitmap (Drawable drawable) {

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable)drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }


    @NonNull
    private CameraFailureCallback cameraFailureDialog() {
        return e -> {
            Toast.makeText(getActivity().getApplicationContext(), "Camera unavailable at the moment", Toast.LENGTH_SHORT).show();


//            new MaterialAlertDialogBuilder(getContext())
//                    .setTitle("Camera unavailable at the moment")
//                    .setMessage("Camera is currently in use, or not accessible through this app. \nPlease try enabling permissions or closing other apps currently using the camera.")
//                    .setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            startCamera();
//                        }
//                    });
        };
    }

    private void noCamera() {
        AlertDialog cameraWarningDialog = new MaterialAlertDialogBuilder(getContext())
                .setTitle("No Camera found")
                .setMessage("Device does not have a hardware camera, hence no QR codes/barcodes can be scanned.\nTry using an USB OTG camera to add camera functionality to your device.")
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