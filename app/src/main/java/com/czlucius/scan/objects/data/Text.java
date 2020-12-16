package com.czlucius.scan.objects.data;

import androidx.annotation.NonNull;

import com.czlucius.scan.exceptions.QRDataNotSupportedException;

public class Text extends Data{
    private final CharSequence data;

    public Text(CharSequence data) {
        this.data = data;
    }

    @Override
    public boolean isEmpty() {
        return data.length() == 0;
    }

    @NonNull
    @Override
    public String getStringRepresentation() {
        return data.toString();
    }


}
