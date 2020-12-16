package com.czlucius.scan.objects.data;

import androidx.annotation.NonNull;

import com.google.mlkit.vision.barcode.Barcode;

import java.util.Arrays;
import java.util.Objects;

public class Address extends Data {
    private final String[] formattedAddresses;

    @Barcode.Address.AddressType
    private final int type;

    public Address(Barcode.Address address) {
        this.formattedAddresses = address.getAddressLines();
        this.type = address.getType();
    }


    public String[] getFormattedAddresses() {
        return formattedAddresses;
    }

    public int getType() {
        return type;
    }


    @NonNull
    @Override
    public String getStringRepresentation() {
        StringBuilder sb = new StringBuilder();
        for (String formattedAddress : formattedAddresses) {
            sb.append("\"")
                    .append(formattedAddress)
                    .append("\"\n");
        }
        return sb.toString();
    }

    @Override
    public boolean isEmpty() {
        return formattedAddresses.length == 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Address address = (Address) o;
        return type == address.type &&
                Arrays.equals(formattedAddresses, address.formattedAddresses);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(super.hashCode(), type);
        result = 31 * result + Arrays.hashCode(formattedAddresses);
        return result;
    }
}
