/*
 * Code Scanner. An android app to scan and create codes(barcodes, QR codes, etc)
 * Copyright (C) 2021 Lucius Chee Zihan
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

package com.czlucius.scan.objects;


import static com.czlucius.scan.objects.Type.TypeInt.TYPE_CONTACT;
import static com.czlucius.scan.objects.Type.TypeInt.TYPE_EMAIL;
import static com.czlucius.scan.objects.Type.TypeInt.TYPE_GEOLOCATION;
import static com.czlucius.scan.objects.Type.TypeInt.TYPE_PHONE;
import static com.czlucius.scan.objects.Type.TypeInt.TYPE_SMS;
import static com.czlucius.scan.objects.Type.TypeInt.TYPE_UNKNOWN_OR_TEXT;
import static com.czlucius.scan.objects.Type.TypeInt.TYPE_URL;
import static com.czlucius.scan.objects.Type.TypeInt.TYPE_WIFI;

import androidx.annotation.IntDef;
import androidx.room.Entity;

import com.czlucius.scan.App;
import com.czlucius.scan.R;
import com.czlucius.scan.objects.actions.Action;
import com.czlucius.scan.objects.actions.AddContactAction;
import com.czlucius.scan.objects.actions.AddWiFiAction;
import com.czlucius.scan.objects.actions.CallPhoneAction;
import com.czlucius.scan.objects.actions.CopyAction;
import com.czlucius.scan.objects.actions.CopyPasskeyAction;
import com.czlucius.scan.objects.actions.CopyPhoneAction;
import com.czlucius.scan.objects.actions.CopySMSContentsAction;
import com.czlucius.scan.objects.actions.CopySMSRecipientAction;
import com.czlucius.scan.objects.actions.CopySSIDAction;
import com.czlucius.scan.objects.actions.EmailAction;
import com.czlucius.scan.objects.actions.SMSAction;
import com.czlucius.scan.objects.actions.URLAction;
import com.czlucius.scan.objects.actions.ViewLocationAction;
import com.czlucius.scan.objects.data.Phone;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Entity
public class Type {
    private final List<Action> actions;
    private final String typeName;
    private final @TypeInt int typeInt;

    public static Type EMAIL;
    public static Type URL;
    public static Type CONTACT;
    public static Type UNKNOWN_OR_TEXT;
    public static Type WIFI;
    public static Type SMS;
    public static Type PHONE;
    public static Type GEOLOCATION;

    @IntDef({
            TYPE_EMAIL,
            TYPE_URL,
            TYPE_CONTACT,
            TYPE_WIFI,
            TYPE_SMS,
            TYPE_PHONE,
            TYPE_GEOLOCATION,
            TYPE_UNKNOWN_OR_TEXT
    })
    public static @interface TypeInt {
        int TYPE_EMAIL = 1;
        int TYPE_URL = 2;
        int TYPE_CONTACT = 3;
        int TYPE_WIFI = 4;
        int TYPE_SMS = 5;
        int TYPE_PHONE = 6;
        int TYPE_GEOLOCATION = 7;
        int TYPE_UNKNOWN_OR_TEXT = 0;
    }

    private Type(List<Action> actions, String typeName, int typeInt) {
        actions.add(0, CopyAction.getInstance());
        //actions.add(1, ShareAction.getInstance());
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

    public static Type getTypeFromCode(@TypeInt int barcodeValueType) {

        // TODO if add more types here, also add @ Data.java
        switch (barcodeValueType) {
            case TYPE_EMAIL:
                return Type.EMAIL;
            case TYPE_CONTACT:
                return Type.CONTACT;
            case TYPE_URL:
                return Type.URL;
            case TYPE_WIFI:
                return Type.WIFI;
            case TYPE_SMS:
                return Type.SMS;
            case TYPE_PHONE:
                return Type.PHONE;
            case TYPE_GEOLOCATION:
                return Type.GEOLOCATION;
            case TypeInt.TYPE_UNKNOWN_OR_TEXT: default:
                return Type.UNKNOWN_OR_TEXT;
        }
    }


    // Different predefined types:
    static {

        // Creating actions
        // Email
        ArrayList<Action> emailActions = new ArrayList<>();
        emailActions.add(EmailAction.getInstance());
        EMAIL = new Type(emailActions, App.getStringGlobal(R.string.email, "Email"), TYPE_EMAIL);


        // Contacts
        ArrayList<Action> contactActions = new ArrayList<>();
        contactActions.add(AddContactAction.getInstance());
        CONTACT = new Type(contactActions, App.getStringGlobal(R.string.contact, "Contact"), TYPE_CONTACT);


        // URL
        ArrayList<Action> urlActions = new ArrayList<>();
        urlActions.add(URLAction.getInstance());
        URL = new Type(urlActions, App.getStringGlobal(R.string.url, "URL"), TYPE_URL);

        // WiFi
        ArrayList<Action> wifiActions = new ArrayList<>();
        wifiActions.add(CopySSIDAction.getInstance());
        wifiActions.add(CopyPasskeyAction.getInstance());
        wifiActions.add(AddWiFiAction.getInstance());
        WIFI = new Type(wifiActions, App.getStringGlobal(R.string.wifi, "Wi-Fi"), TYPE_WIFI);


        // SMS
        ArrayList<Action> smsActions = new ArrayList<>();
        smsActions.add(CopySMSRecipientAction.getInstance());
        smsActions.add(CopySMSContentsAction.getInstance());
        smsActions.add(SMSAction.getInstance());
        SMS = new Type(smsActions, App.getStringGlobal(R.string.sms, "SMS"), TYPE_SMS);

        // Phone
        ArrayList<Action> phoneActions = new ArrayList<>();
        phoneActions.add(new CopyPhoneAction(data -> (Phone) data));
        phoneActions.add(new CallPhoneAction(data -> (Phone) data));
        PHONE = new Type(phoneActions, App.getStringGlobal(R.string.phone_number, "Phone number"), TYPE_PHONE);

        // Geolocation
        ArrayList<Action> locationActions = new ArrayList<>();
        locationActions.add(ViewLocationAction.getInstance());
        GEOLOCATION = new Type(locationActions, App.getStringGlobal(R.string.geolocation, "Geolocation"), TYPE_GEOLOCATION);


        //unknown, same as text
        UNKNOWN_OR_TEXT = new Type(new ArrayList<>(), App.getStringGlobal(R.string.text, "Text"), TYPE_UNKNOWN_OR_TEXT);
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
