package com.czlucius.scan.ui;

import android.app.Application;

import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.czlucius.scan.Utils;
import com.czlucius.scan.database.HistoryDatabase;
import com.czlucius.scan.objects.Code;
import com.czlucius.scan.objects.CodeAnalyser;
import com.google.mlkit.vision.barcode.Barcode;

import java.util.ArrayList;

public class ScanVM extends AndroidViewModel {
    private MutableLiveData<ArrayList<Code>> codes;
    //Temporary field to compare barcode objects without initialisation of Code objects.

    private CodeAnalyser codeAnalyser;

    public LiveData<ArrayList<Code>> getCodes() {
        return codes;
    }

    @Nullable
    public Code getLatestCode() {
        if (codes == null || codes.getValue().size() < 1) {
            return null;
        }
        return codes.getValue().get(0);
    }

    public void notifyLiveDataChanged() {
        codes.setValue(codes.getValue());
    }

    public CodeAnalyser getCodeAnalyser() {
        return codeAnalyser;
    }

    public ScanVM(Application app) {
        super(app);
        codes = new MutableLiveData<>();
        codes.setValue(new ArrayList<>());
        codeAnalyser = new CodeAnalyser(
                barcodes -> {
                    if (barcodes.size() > 0) {
                        // Codes detected. Update the codes LiveData variable.
                        for (Barcode barcode : barcodes) {
                            // Check if the item was originally scanned
                            // Create Code object
                            Code code = new Code(barcode);
                            assert codes.getValue() != null;
                            if (!Utils.codeListContains(codes.getValue(), code)) {
                                // Add barcode to the array only if the barcode if never scanned before.
                                codes.getValue().add(code);

                                // Insert the Code into Room database.
                                HistoryDatabase.insertCode(app.getApplicationContext(), code);

                            }

                        }
                        notifyLiveDataChanged(); // Triggers LiveData observer

                    }
                },
                (e) -> {

                }
        );

    }


}
