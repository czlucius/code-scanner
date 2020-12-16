package com.czlucius.scan.objects.data.created;

import android.net.Uri;
import android.widget.EditText;

import androidx.annotation.NonNull;

public class CreatedURL extends CreatedText{

    public CreatedURL(String text) {
        super(text);
    }


    @NonNull
    @Override
    public String getQRData() {

        Uri uri = Uri.parse(text);
        Uri.Builder builder = uri.buildUpon();
        if (uri.getScheme() == null || uri.getScheme().isEmpty()) {
            builder.scheme("http");
        }
        return builder.build().toString();
    }
}
