package com.czlucius.scan.ui;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.Color;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.czlucius.scan.objects.QR;
import com.czlucius.scan.objects.data.Data;
import com.czlucius.scan.objects.data.Text;
import com.czlucius.scan.objects.data.created.ICreatedData;
import com.google.zxing.WriterException;

import java.util.Objects;


public class CreateViewModel extends AndroidViewModel {
    private static final String TAG = "CreateViewModel";

    private final MutableLiveData<EditState> currentState;
    private final MutableLiveData<QR> qr;


    public CreateViewModel(Application app) {
        super(app);
        // Initialization
        currentState = new MutableLiveData<>();
        currentState.setValue(EditState.NONE);

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

    public LiveData<EditState> getCurrentState() {
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


    public void setCurrentState(EditState currentState) {
        this.currentState.setValue(currentState);
    }


    public QR getQrValue() {
        return qr.getValue();
    }



    public enum EditState {
        CONTENTS, FOREGROUND, BACKGROUND, NONE
    }


}
