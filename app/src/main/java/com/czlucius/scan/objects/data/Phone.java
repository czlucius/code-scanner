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

package com.czlucius.scan.objects.data;

import androidx.annotation.NonNull;

import com.czlucius.scan.App;
import com.czlucius.scan.R;
import com.czlucius.scan.exceptions.InvalidTypeException;
import com.google.mlkit.vision.barcode.common.Barcode;

import java.util.Objects;

public class Phone extends Data {
    private final String number;
    @Barcode.Phone.FormatType
    private final int type;

    public String getNumber() {
        return number;
    }

    public int getType() {
        return type;
    }

    private String getTypeString() {
        switch (type) {
            case Barcode.Phone.TYPE_HOME: return App.getStringGlobal(R.string.home, "Home");
            case Barcode.Phone.TYPE_MOBILE: return App.getStringGlobal(R.string.mobile, "Mobile");
            case Barcode.Phone.TYPE_FAX: return App.getStringGlobal(R.string.fax, "Fax");
            case Barcode.Phone.TYPE_WORK: return App.getStringGlobal(R.string.work, "Work");
            case Barcode.Phone.TYPE_UNKNOWN: return App.getStringGlobal(R.string.unknown, "Unknown");
            default:
                throw new InvalidTypeException("No type int: " + type);
        }
    }

    private Phone(String number, int type) {
        this.number = number;
        this.type = type;
    }

    public Phone(Barcode.Phone phone) {
        this(phone.getNumber(), phone.getType());
    }

    @NonNull
    @Override
    public String getStringRepresentation() {

        return number + " (" + getTypeString() + ")";
    }

    @Override
    public boolean isEmpty() {
        return number.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Phone phone = (Phone) o;
        return type == phone.type &&
                number.equals(phone.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), number, type);
    }
}
