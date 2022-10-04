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

import com.google.mlkit.vision.barcode.common.Barcode;

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
