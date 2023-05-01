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

package com.czlucius.scan.objects;

import android.app.Dialog;
import android.content.Context;

import com.czlucius.scan.App;
import com.czlucius.scan.database.HistoryDatabase;
import com.czlucius.scan.ui.ResultDisplayDialog;
import com.google.mlkit.vision.barcode.common.Barcode;

import java.util.Objects;

/**
 * An UI wrapper around {@link Code} to manage UI interactions.
 */
public class ScanningWrapper {

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
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            scanned = false;

            dismissListener.destroy(this);
        });


    }

    public void display(Context ctx, boolean showDialog) {
        if (!scanned) {
            if (showDialog) {
                dialog = new ResultDisplayDialog(ctx, code);
                dialog.setOnDismissListener(__ -> release());
                dialog.show();
            }
            scanned = true;

            // Add to history
            HistoryDatabase.insertCode(ctx, code);
        }
    }

    public void dismissDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
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
