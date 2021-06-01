/*
 * Code Scanner. An android app to scan and create codes(barcodes, QR codes, etc)
 * Copyright (C) 2020 Lucius Chee Zihan
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

package com.czlucius.scan.objects.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.mlkit.vision.barcode.Barcode;

import java.util.Objects;

public abstract class Data {
    @NonNull
    public abstract String getStringRepresentation();

    public abstract boolean isEmpty();




    @Deprecated
    public String getDescription() {
        return getStringRepresentation();
    }

    public String getSummary() {
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

    @NonNull
    @Override
    public String toString() {
        return getStringRepresentation();
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
                case Barcode.TYPE_SMS:
                    return new SMS(Objects.requireNonNull(barcode.getSms()));
                default:
                    return new Text(barcode.getDisplayValue());
            }
        }
    }



}
