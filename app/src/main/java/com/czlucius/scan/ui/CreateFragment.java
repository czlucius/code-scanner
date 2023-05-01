

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

import static com.czlucius.scan.ui.CreateViewModel.EditState.BACKGROUND;
import static com.czlucius.scan.ui.CreateViewModel.EditState.CONTENTS;
import static com.czlucius.scan.ui.CreateViewModel.EditState.FOREGROUND;
import static com.czlucius.scan.ui.CreateViewModel.EditState.NONE;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.azeesoft.lib.colorpicker.ColorPickerDialog;
import com.czlucius.scan.R;
import com.czlucius.scan.databinding.ContentsDialogBinding;
import com.czlucius.scan.databinding.CreateBinding;
import com.czlucius.scan.objects.data.created.CreatedText;
import com.czlucius.scan.objects.data.created.CreatedWiFi;
import com.czlucius.scan.objects.data.created.ICreatedData;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.WriterException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class CreateFragment extends Fragment {

    private static final String TAG = "CreateFragment";
    private static final int SAVE_IMAGE = 103;
    private CreateBinding binding;
    private CurrentEditState currentEditState = CurrentEditState.TEXT;
    private final ArrayList<AlertDialog> alertDialogsOpen = new ArrayList<>();


    private CreateViewModel vm;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        vm = new ViewModelProvider(this).get(CreateViewModel.class);

        vm.getCurrentState().observe(getViewLifecycleOwner(), editState -> {
            switch (editState) {
                case CONTENTS:
                    displayContentsDialog();
                    break;
                case FOREGROUND:
                    displayForegroundDialog();
                    break;
                case BACKGROUND:
                    displayBackgroundDialog();
                    break;
                case NONE:
                    break;
            }
        });

        vm.getQr().observe(getViewLifecycleOwner(), newQr -> regenerateQRImage());

        binding = CreateBinding.inflate(inflater, container, false);

        // Process intent filter data

        Intent intent = requireActivity().getIntent();
        if (!(intent == null || intent.getType() == null || !intent.getType().startsWith("text/"))) {
            // Get data.
            String text = intent.getStringExtra(Intent.EXTRA_TEXT);
            // TODO enable other types (e.g. contact)
            ICreatedData data = new CreatedText(text);
            vm.setContents(data);
            requireActivity().setIntent(null);
        }


        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.contentsBtn.setOnClickListener(v -> vm.setCurrentState(CONTENTS));
        binding.colorBtn.setOnClickListener(v -> vm.setCurrentState(FOREGROUND));
        binding.backgroundColorBtn.setOnClickListener(v -> vm.setCurrentState(BACKGROUND));

        binding.shareButton.setOnClickListener(this::share);
        binding.saveToGalleryButton.setOnClickListener(v -> {
            // Prompt user to create a png file.
            DateFormat dateFormat = SimpleDateFormat.getDateTimeInstance();
            createFile("QR @ " + dateFormat.format(new Date()) + ".png"); // TODO add customisation options, shouldn't be that hard

        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SAVE_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            // Get data from storage access framework.
            Uri uri = data.getData();
            save(uri);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        for (AlertDialog dialog : alertDialogsOpen) {
            dialog.dismiss();
        }
        vm.getQr().removeObservers(getViewLifecycleOwner());
    }

    private void save(Uri uri) {

        try (OutputStream os = requireContext().getContentResolver().openOutputStream(uri)) {
            vm.getQrValue().writeBitmapToOutput(os);
        } catch (IOException | WriterException e) {
            errorSnackbar();
            return;
        }

        Snackbar.make(requireView(), R.string.image_saved, Snackbar.LENGTH_SHORT)
                .show();
    }

    private void createFile(String fileName) {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/png");
        intent.putExtra(Intent.EXTRA_TITLE, fileName);

        startActivityForResult(intent, SAVE_IMAGE);
    }

    private void share(View v) {

        // Use current date/time as name of image.
        String fileName = "qr_image_" + System.currentTimeMillis() + ".png";
        File bmpFile = new File(requireContext().getExternalCacheDir(), fileName);
        OutputStream os = null;
        try {
            os = new FileOutputStream(bmpFile);
            vm.getQrValue().writeBitmapToOutput(os);
        } catch (IOException | WriterException e) {
            errorSnackbar();
            return;
        } finally {
            try {
                if (os != null) {
                    os.flush();
                    os.close();
                }
            } catch (IOException e) {
                errorSnackbar();
            }
        }

        launchShareIntent(bmpFile);
    }

    private void launchShareIntent(File bmpFile) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        Uri fileUri = FileProvider.getUriForFile(requireContext(), "com.czlucius.scan.provider", bmpFile);
        intent.putExtra(Intent.EXTRA_STREAM, fileUri);
        intent.setType("image/png");
        startActivity(intent);
    }


    private void errorSnackbar() {
        Snackbar.make(requireView(), R.string.error, Snackbar.LENGTH_SHORT)
                .show();
    }

    private void regenerateQRImage() {

        if (vm.getContents() == null || vm.getContents().isEmpty()) {
            binding.qrImage.setImageDrawable(null);

            // Make the share options invisible
            binding.saveOptions.setVisibility(View.GONE);
        } else {
            binding.saveOptions.setVisibility(View.VISIBLE);
        }


        try {
            binding.qrImage.setImageBitmap(vm.generateImage());
        } catch (WriterException e) {
            errorSnackbar();
        }
    }

    private void displayContentsDialog() {
        ContentsDialogBinding binding = ContentsDialogBinding.inflate(getLayoutInflater());
        MaterialButtonToggleGroup toggleGroup = binding.typeToggle;
        ViewFlipper flipper = binding.viewFlipper;
        Spinner spinner = binding.enterWifiEncModeCreate;


        toggleGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            // The bug in switching was caused by not checking the isChecked value. Hence, when Material Design updated their API, the bug occurs.
//            Log.i(TAG, "displayContentsDialog: index is " + index + " isChecked is " + isChecked);
            if (isChecked) {
                int index = group.indexOfChild(group.findViewById(checkedId)); // This is an expensive call, hence we don't want to unnecessarily call it
                flipper.setDisplayedChild(index);
                currentEditState = CurrentEditState.values()[index];
            }
        });
        toggleGroup.check(toggleGroup.getChildAt(currentEditState.index).getId());



        // Pre-populate the fields
        CurrentEditState.populateFields(binding, vm.getContents());

        ArrayAdapter<CreatedWiFi.EncryptionType> encryptionTypeAdapter =
                new ArrayAdapter<>(getContext(), android.R.layout.simple_expandable_list_item_1, CreatedWiFi.EncryptionType.values());
        spinner.setAdapter(encryptionTypeAdapter);

        AlertDialog contentsDialog = new MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.contents)
                .setView(binding.getRoot())
                .setPositiveButton(R.string.ok, (dialog, which) -> {
                    ICreatedData data = currentEditState.createData(flipper.getCurrentView());
                    vm.setContents(data);
                    vm.setCurrentState(NONE);
                }).setNegativeButton(R.string.cancel, (dialog, which) -> vm.setCurrentState(NONE)).setCancelable(false)
                .create();
        contentsDialog.show();
        alertDialogsOpen.add(contentsDialog);
    }

    private void displayForegroundDialog() {
        displayColorPicker(vm.getForegroundColor(),
                (color, hexVal) -> vm.setForegroundColor(color));
    }


    private void displayBackgroundDialog() {
        displayColorPicker(vm.getBackgroundColor(),
                (color, hexVal) -> vm.setBackgroundColor(color));
    }


    private void displayColorPicker(Integer initialColor, ColorPickerDialog.OnColorPickedListener listener) {
        ColorPickerDialog colorPickerDialog = ColorPickerDialog.createColorPickerDialog(requireContext());
        if (initialColor != null) {
            colorPickerDialog.setLastColor(initialColor);
        }
        colorPickerDialog.setPositiveActionText(getString(R.string.ok));
        colorPickerDialog.setNegativeActionText(getString(R.string.cancel));
        colorPickerDialog.setOnColorPickedListener(listener);
        colorPickerDialog.hideOpacityBar();
        colorPickerDialog.show();
    }
}
