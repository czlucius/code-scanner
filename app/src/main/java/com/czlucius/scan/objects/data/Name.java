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

import java.util.Objects;

import ezvcard.property.StructuredName;

public class Name extends Data {
    private final String formattedName;
    private final String firstName;
    private final String lastName;
    private final String middleName;
    private final String prefix;
    private final String suffix;


    public Name(String formattedName, String firstName, String lastName, String middleName, String prefix, String suffix) {
        this.formattedName = formattedName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.prefix = prefix;
        this.suffix = suffix;
    }


    public Name(Barcode.PersonName name) {
        this(name.getFirst(), name.getLast(), name.getMiddle(), name.getPrefix(), name.getSuffix(), name.getFormattedName());
    }

    private Name() {
        // No name constructor
        this.formattedName = "";
        this.firstName = "";
        this.lastName =  "";
        this.middleName = "";
        this.prefix = "";
        this.suffix = "";
    }

    public static Name NO_NAME_INSTANCE = new Name();


    public String getFormattedName() {
        return formattedName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getSuffix() {
        return suffix;
    }


    public StructuredName toStructuredName() {
        StructuredName name = new StructuredName();
        name.setGiven(firstName);
        name.getAdditionalNames().add(middleName);
        name.setFamily(lastName);
        name.getPrefixes().add(prefix);
        name.getSuffixes().add(suffix);

        return name;
    }

    @NonNull
    @Override
    public String getStringRepresentation() {
        return formattedName;
    }

    @Override
    public boolean isEmpty() {
        return formattedName.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Name name = (Name) o;
        return formattedName.equals(name.formattedName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), formattedName);
    }
}
