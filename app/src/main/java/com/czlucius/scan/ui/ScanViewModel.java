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

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.czlucius.scan.objects.CodeAnalyser;
import com.czlucius.scan.objects.ScanningWrapper;
import com.google.mlkit.common.MlKitException;

import com.google.mlkit.vision.barcode.common.Barcode;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


public class ScanViewModel extends AndroidViewModel {
    private final MutableLiveData<Boolean> modelDownloaded;
    private final CodeAnalyser codeAnalyser;


    private final MutableLiveData<Set<ScanningWrapper>> codes;
    private MutableLiveData<Boolean> batchScanEnabled;
    private MutableLiveData<Integer> numberOfCodesScanned;



    public ScanViewModel(Application app) {
        super(app);

        modelDownloaded = new MutableLiveData<>();
        modelDownloaded.setValue(true);

        codes = new MutableLiveData<>();
        codes.setValue(Collections.synchronizedSet(new HashSet<>()));

        batchScanEnabled = new MutableLiveData<>();
        batchScanEnabled.setValue(false);

        numberOfCodesScanned = new MutableLiveData<>();
        numberOfCodesScanned.setValue(0);

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

    public LiveData<Boolean> getBatchScanEnabledLiveData() {
        return batchScanEnabled;
    }

    public boolean getBatchScanEnabled() {
        return batchScanEnabled.getValue();
    }

    public void setBatchScanEnabled(boolean batchScanEnabled) {
        this.batchScanEnabled.setValue(batchScanEnabled);
    }

    public LiveData<Integer> getNumberOfCodesScannedLiveData() {
        return numberOfCodesScanned;
    }

    public int getNumberOfCodesScanned() {
        return numberOfCodesScanned.getValue();
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
            // More codes detected
            codes.setValue(codes.getValue());
            numberOfCodesScanned.setValue(codes.getValue().size());
        }
    }


}
