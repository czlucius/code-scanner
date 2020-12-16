package com.czlucius.scan.objects.data;

import androidx.annotation.NonNull;

import com.google.mlkit.vision.barcode.Barcode;

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
