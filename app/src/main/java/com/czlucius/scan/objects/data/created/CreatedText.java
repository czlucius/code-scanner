package com.czlucius.scan.objects.data.created;

import android.widget.EditText;

import androidx.annotation.NonNull;

public class CreatedText implements ICreatedData {
    protected final String text;

    public CreatedText(String text) {
        this.text = text;
    }

    @NonNull
    @Override
    public String getQRData() {
        return text;
    }
}
