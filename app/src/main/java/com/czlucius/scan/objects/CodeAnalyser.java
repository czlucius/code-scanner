/*
 * Code Scanner. An android app to scan and create codes(barcodes, QR codes, etc)
 * Copyright (C) 2020 Lucius Chee Zihan
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

package com.czlucius.scan.objects;

import android.media.Image;

import androidx.annotation.NonNull;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;

import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.barcode.Barcode;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.common.InputImage;

import java.util.List;

public class CodeAnalyser implements ImageAnalysis.Analyzer {

    private SuccessCallback mCallBack;
    private FailureHandler mExceptionHandler;




    public CodeAnalyser(SuccessCallback scanCallback, FailureHandler exceptionHandler) {

        mCallBack = scanCallback;
        mExceptionHandler = exceptionHandler;
    }






    @Override
    @androidx.camera.core.ExperimentalGetImage
    public void analyze(@NonNull ImageProxy imageProxy) {
        Image barcodeImage = imageProxy.getImage();
        if (barcodeImage != null) {
            InputImage inputImage = InputImage.fromMediaImage(barcodeImage, imageProxy.getImageInfo().getRotationDegrees());
            //Pass to ML Kit API

            BarcodeScanner barcodeScanner = BarcodeScanning.getClient();
            Task<List<Barcode>> result = barcodeScanner.process(inputImage)
                    .addOnSuccessListener(barcodes -> {
                        mCallBack.scannedBarcodes(barcodes);
                    })
                    .addOnFailureListener(exception -> {
                        mExceptionHandler.handleException(exception);

                    })
                    .addOnCompleteListener(task -> {
                        imageProxy.close();
                        barcodeScanner.close();
                    });



        }
    }



    public interface SuccessCallback {
        void scannedBarcodes(List<Barcode> barcodes);
    }

    public interface FailureHandler {
        void handleException(Exception e);
    }

}
