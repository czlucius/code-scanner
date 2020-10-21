package com.czlucius.scan.objects;

import androidx.room.Entity;

import com.google.mlkit.vision.barcode.Barcode;

import java.util.ArrayList;
import java.util.List;


//TODO enable formatting of text with SpannedString(and also click to open)
@Entity
public class Type {
    private List<Action> actions;
    private String typeName;
    private int typeInt;

    public static Type EMAIL;
    public static Type URL;
    public static Type CONTACT;
    public static Type UNKNOWN_OR_TEXT;
    public static Type WIFI;

    private Type(List<Action> actions, String typeName, int typeInt) {
        actions.add(0, Action.copy);
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

        // TODO if add more types here, also add @ Contents.java
        switch (barcodeValueType) {
            case Barcode.TYPE_EMAIL:
                return Type.EMAIL;
            case Barcode.TYPE_CONTACT_INFO:
                return Type.CONTACT;
            case Barcode.TYPE_URL:
                return Type.URL;
                //todo add more types
            default:
                return Type.UNKNOWN_OR_TEXT;
        }
    }



    // Different predefined types:
    static {
        // Creating actions
        // Email
        ArrayList<Action> emailActions = new ArrayList<>();
        emailActions.add(Action.email);
        EMAIL = new Type(emailActions, "E-mail", Barcode.TYPE_EMAIL);


        // Contacts
        ArrayList<Action> contactActions = new ArrayList<>();
        contactActions.add(Action.addContact);
        CONTACT = new Type(contactActions, "Contact", Barcode.TYPE_CONTACT_INFO);


        // URL
        ArrayList<Action> urlActions = new ArrayList<>();
        urlActions.add(Action.url);
        URL = new Type(urlActions, "URL", Barcode.TYPE_URL);

        // WiFi
        ArrayList<Action> wifiActions = new ArrayList<>();
        wifiActions.add(Action.joinWifi);
        wifiActions.add(Action.copySSID);
        wifiActions.add(Action.copyPassword);
        WIFI = new Type(wifiActions, "WiFi", Barcode.TYPE_WIFI);

        //unknown, same as text
        UNKNOWN_OR_TEXT = new Type(new ArrayList<>(), "Text", Barcode.TYPE_TEXT);
    }



}
