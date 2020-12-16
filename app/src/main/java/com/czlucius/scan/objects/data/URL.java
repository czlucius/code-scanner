package com.czlucius.scan.objects.data;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.mlkit.vision.barcode.Barcode;

public class URL extends Data {
    private final String title;
    private final String urlAddress;



    public URL(Barcode.UrlBookmark urlBookmark) {
        title = urlBookmark.getTitle();
        urlAddress = urlBookmark.getUrl();
    }


    public String getTitle() {
        return title;
    }

    public String getUrlAddress() {
        return urlAddress;
    }

    @NonNull
    @Override
    public String getStringRepresentation() {
        return urlAddress;
    }

    @Override
    public boolean isEmpty() {
        return title.isEmpty() && urlAddress.isEmpty();
    }


}
