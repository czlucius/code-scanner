package com.czlucius.scan.objects.data;

import androidx.annotation.NonNull;

import com.czlucius.scan.App;
import com.czlucius.scan.R;
import com.czlucius.scan.exceptions.InvalidTypeException;
import com.google.mlkit.vision.barcode.Barcode;

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
