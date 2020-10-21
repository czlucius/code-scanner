package com.czlucius.scan.objects;

import android.media.Image;
import android.util.Log;

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
    private static String TAG = "CodeAnalyser";
    private SuccessCallback mCallBack;
    private FailureHandler mExceptionHandler;




    public CodeAnalyser(SuccessCallback scanCallback, FailureHandler exceptionHandler) {
        mCallBack = scanCallback;
        mExceptionHandler = exceptionHandler;

    }






    @Override
    public void analyze(@NonNull ImageProxy image) {
        Image barcodeImage = image.getImage(); // Ignore experimental usage
        if (barcodeImage != null) {
            InputImage inputImage = InputImage.fromMediaImage(barcodeImage, image.getImageInfo().getRotationDegrees());
            //Pass to ML Kit API

            BarcodeScanner barcodeScanner = BarcodeScanning.getClient();
            Task<List<Barcode>> result = barcodeScanner.process(inputImage)
                    .addOnSuccessListener(barcodes -> {
                        Log.i(TAG, "77837");
                        mCallBack.scannedBarcode(barcodes);
                    })
                    .addOnFailureListener(exception -> {
                        mExceptionHandler.handleException(exception);
                    })
                    .addOnCompleteListener(task -> {
                        image.close();
                        barcodeScanner.close();
                    });



        }
    }


    public interface SuccessCallback {
        void scannedBarcode(List<Barcode> barcodes);
    }

    public interface FailureHandler {
        void handleException(Exception e);
    }

}
