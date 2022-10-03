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

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.Color;

import androidx.annotation.IntDef;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.czlucius.scan.objects.QR;
import com.czlucius.scan.objects.data.created.ICreatedData;
import com.google.zxing.WriterException;

import java.util.Objects;


public class CreateViewModel extends AndroidViewModel {


    private final MutableLiveData<Integer> currentState; // since current Java version doesn't allow for annotated type arguments, we just use Integer.
    private final MutableLiveData<QR> qr;


    public CreateViewModel(Application app) {
        super(app);
        // Initialization
        currentState = new MutableLiveData<>();
        currentState.setValue(NONE);

        qr = new MutableLiveData<>();
        qr.setValue(new QR(null, Color.BLACK, Color.WHITE));
        getQrNullSafe().registerObserver(this.qr::setValue);

    }

    public QR getQrNullSafe() {
        return Objects.requireNonNull(qr.getValue());
    }

    public LiveData<QR> getQr() {
        return qr;
    }

    public Bitmap generateImage() throws WriterException {
        return getQrNullSafe().generateBitmap();
    }

    public ICreatedData getContents() {
        return getQrNullSafe().getContents();
    }

    public int getForegroundColor() {
        return getQrNullSafe().getColor();
    }

    public int getBackgroundColor() {
        return getQrNullSafe().getBackground();
    }

    public LiveData<Integer> getCurrentState() {
        return currentState;
    }


    public void setContents(ICreatedData contents) {
        getQrNullSafe().setContents(contents);
    }

    public void setForegroundColor(int foregroundColor) {
        getQrNullSafe().setColor(foregroundColor);
    }

    public void setBackgroundColor(int backgroundColor) {
        getQrNullSafe().setBackground(backgroundColor);
    }


    public void setCurrentState(@EditState int currentState) {
        this.currentState.setValue(currentState);
    }


    public QR getQrValue() {
        return qr.getValue();
    }




    @IntDef({CONTENTS, FOREGROUND, BACKGROUND, NONE})
    public @interface EditState {
        int CONTENTS = 0;
        int FOREGROUND = 1;
        int BACKGROUND = 2;
        int NONE = 3;
    }



}
