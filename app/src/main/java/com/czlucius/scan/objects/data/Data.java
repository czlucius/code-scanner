package com.czlucius.scan.objects.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.mlkit.vision.barcode.Barcode;

import java.util.Objects;

public abstract class Data {
    @NonNull
    public abstract String getStringRepresentation();

    public abstract boolean isEmpty();




    public String getDescription() {
        return getStringRepresentation();
    }




    /**
     * Override to provide a more efficient equals method.
     * Regular method requires generating human-readable description, which may be expensive.
     */
    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof Data)) return false;
        Data that = (Data) obj;
        return getStringRepresentation().equals(that.getStringRepresentation())
                && getDescription().equals(that.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStringRepresentation(), getDescription());
    }

    public static class Factory {
        private static final Factory INSTANCE = new Factory();

        private Factory() {
        }

        public static Factory getInstance() {
            return INSTANCE;
        }

        // TODO
        public Data create(Barcode barcode) {
            switch (barcode.getValueType()) {
                case Barcode.TYPE_EMAIL:
                    assert barcode.getEmail() != null;
                    return new Email(barcode.getEmail());
                case Barcode.TYPE_CONTACT_INFO:
                    assert barcode.getContactInfo() != null;
                    return new Contact(barcode.getContactInfo());
                case Barcode.TYPE_URL:
                    assert barcode.getUrl() != null;
                    return new URL(barcode.getUrl());
                case Barcode.TYPE_WIFI:
                    assert barcode.getWifi() != null;
                    return new WiFi(barcode.getWifi());
                default:
                    return new Text(barcode.getDisplayValue());
            }
        }
    }



}
