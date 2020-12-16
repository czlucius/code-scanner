package com.czlucius.scan.objects.data.created;

import androidx.annotation.NonNull;

public interface ICreatedData {
    @NonNull
    String getQRData();


    default boolean isEmpty() {
        return getQRData().isEmpty();
    }
}
