package com.czlucius.scan.objects;

import androidx.room.Entity;

import com.czlucius.scan.App;
import com.czlucius.scan.R;
import com.czlucius.scan.objects.actions.Action;
import com.czlucius.scan.objects.actions.AddContactAction;
import com.czlucius.scan.objects.actions.AddWiFiAction;
import com.czlucius.scan.objects.actions.CopyAction;
import com.czlucius.scan.objects.actions.CopyPasskeyAction;
import com.czlucius.scan.objects.actions.CopySSIDAction;
import com.czlucius.scan.objects.actions.EmailAction;
import com.czlucius.scan.objects.actions.URLAction;
import com.google.mlkit.vision.barcode.Barcode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Entity
public class Type {
    private final List<Action> actions;
    private final String typeName;
    private final int typeInt;

    public static Type EMAIL;
    public static Type URL;
    public static Type CONTACT;
    public static Type UNKNOWN_OR_TEXT;
    public static Type WIFI;

    private Type(List<Action> actions, String typeName, int typeInt) {
        actions.add(0, CopyAction.getInstance());
        this.actions = actions;
        this.typeName = typeName;
        this.typeInt = typeInt;
    }

    public List<Action> getActions() {
        return actions;
    }

    public String getTypeName() {
        return typeName;
    }

    public int getTypeInt() {
        return typeInt;
    }

    public static Type getTypeFromCode(int barcodeValueType) {

        // TODO if add more types here, also add @ IData.java
        switch (barcodeValueType) {
            case Barcode.TYPE_EMAIL:
                return Type.EMAIL;
            case Barcode.TYPE_CONTACT_INFO:
                return Type.CONTACT;
            case Barcode.TYPE_URL:
                return Type.URL;
            case Barcode.TYPE_WIFI:
                return Type.WIFI;
            default:
                return Type.UNKNOWN_OR_TEXT;
        }
    }


    // Different predefined types:
    static {

        // Creating actions
        // Email
        ArrayList<Action> emailActions = new ArrayList<>();
        emailActions.add(EmailAction.getInstance());
        EMAIL = new Type(emailActions, App.getStringGlobal(R.string.email, "Email"), Barcode.TYPE_EMAIL);


        // Contacts
        ArrayList<Action> contactActions = new ArrayList<>();
        contactActions.add(AddContactAction.getInstance());
        CONTACT = new Type(contactActions, App.getStringGlobal(R.string.contact, "Contact"), Barcode.TYPE_CONTACT_INFO);


        // URL
        ArrayList<Action> urlActions = new ArrayList<>();
        urlActions.add(URLAction.getInstance());
        URL = new Type(urlActions, App.getStringGlobal(R.string.url, "URL"), Barcode.TYPE_URL);

        // WiFi
        ArrayList<Action> wifiActions = new ArrayList<>();
        wifiActions.add(CopySSIDAction.getInstance());
        wifiActions.add(CopyPasskeyAction.getInstance());
        wifiActions.add(AddWiFiAction.getInstance());
        WIFI = new Type(wifiActions, "WiFi", Barcode.TYPE_WIFI);

        //unknown, same as text
        UNKNOWN_OR_TEXT = new Type(new ArrayList<>(), App.getStringGlobal(R.string.text, "Text"), Barcode.TYPE_TEXT);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Type type = (Type) o;
        return typeInt == type.typeInt &&
                typeName.equals(type.typeName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(typeName, typeInt);
    }
}
