package com.czlucius.scan.ui;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.czlucius.scan.objects.CodeAnalyser;
import com.czlucius.scan.objects.ScanningWrapper;
import com.google.mlkit.common.MlKitException;
import com.google.mlkit.vision.barcode.Barcode;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


public class ScanViewModel extends AndroidViewModel {
    private final MutableLiveData<Boolean> modelDownloaded;
    private final CodeAnalyser codeAnalyser;


    private final MutableLiveData<Set<ScanningWrapper>> codes;


    public ScanViewModel(Application app) {
        super(app);

        modelDownloaded = new MutableLiveData<>();
        modelDownloaded.setValue(true);

        codes = new MutableLiveData<>();
        codes.setValue(Collections.synchronizedSet(new HashSet<>()));

        codeAnalyser = new CodeAnalyser(
                barcodes -> {
                    if (barcodes.size() > 0) {
                        scanBarcodes(barcodes);
                    }
                },
                (e) -> {
                    if (e instanceof MlKitException) {
                        // Barcode not downloaded.
                        modelDownloaded.setValue(false);
                    }
                }
        );

    }

    public MutableLiveData<Set<ScanningWrapper>> getCodes() {
        return codes;
    }

    public LiveData<Boolean> getModelDownloaded() {
        return modelDownloaded;
    }

    public CodeAnalyser getCodeAnalyser() {
        return codeAnalyser;
    }





    public void scanBarcodes(Iterable<Barcode> barcodes) {
        boolean changed = false;
        for (Barcode barcode: barcodes) {
            ScanningWrapper scanningWrapper = new ScanningWrapper(barcode, (instance) -> {
                if (codes.getValue() != null) {
                    codes.getValue().remove(instance);
                }
            });
            assert codes.getValue() != null;
            codes.getValue().add(scanningWrapper);
            changed = true;
        }

        if (changed) {
            codes.setValue(codes.getValue());
        }
    }


}
