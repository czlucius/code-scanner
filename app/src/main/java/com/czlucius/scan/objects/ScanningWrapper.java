package com.czlucius.scan.objects;

import android.app.Dialog;
import android.content.Context;

import com.czlucius.scan.App;
import com.czlucius.scan.ui.ResultDisplayDialog;
import com.google.mlkit.vision.barcode.Barcode;

import java.util.Objects;

/**
 * An UI wrapper around <code>Code</code> to manage UI interactions.
 */
public class ScanningWrapper {
    private static final String TAG = "ScanningWrapper";
    private Dialog dialog;
    private final Code code;
    private boolean scanned;
    private DismissListener dismissListener;

    public ScanningWrapper(Code code) {
        this.code = code;
    }

    public ScanningWrapper(Barcode barcode, DismissListener dismissListener) {
        this.code = new Code(barcode);
        this.dismissListener = dismissListener;
    }

    public Code getCode() {
        return code;
    }

    public void release() {
        App.globalExService.submit(() -> {
            // Wait for a while 1st
            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            scanned = false;

            dismissListener.destroy(this);
        });


    }

    public void display(Context ctx) {
        if (!scanned) {
            dialog = new ResultDisplayDialog(ctx, code);
            dialog.setOnDismissListener(__ -> release());
            dialog.show();
            scanned = true;
        }
    }

    public void dismissDialog() {
        dialog.dismiss();
        dialog = null; // Prevent references to old dialog.
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScanningWrapper that = (ScanningWrapper) o;
        return code.equals(that.code);
        // Cannot compare scanned variable, because in this case, a new object is set by default to scanned, and comparison by HashSet will always return false.
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    public interface DismissListener {
        void destroy(ScanningWrapper instance);
    }

}
