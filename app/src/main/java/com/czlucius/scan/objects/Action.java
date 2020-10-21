package com.czlucius.scan.objects;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.provider.ContactsContract;
import android.widget.Toast;

import androidx.annotation.DrawableRes;

import com.czlucius.scan.R;
import com.czlucius.scan.Utils;
import com.google.mlkit.vision.barcode.Barcode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.provider.ContactsContract.CommonDataKinds.Phone.NUMBER;
import static android.provider.ContactsContract.CommonDataKinds.Phone.TYPE;
import static android.provider.ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE;
import static android.provider.ContactsContract.CommonDataKinds.Website.TYPE_WORK;
import static android.provider.ContactsContract.Data.MIMETYPE;
import static android.provider.ContactsContract.Intents.Insert.DATA;

public abstract class Action {
    private final String mActionText;
    @DrawableRes
    private final int mActionIcon;

    public abstract void doAction(Context context, Contents contents);

    public Action(String actionText, @DrawableRes Integer icon) {
        mActionText = actionText;
        mActionIcon = icon;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Action actionObj = (Action) o;
        return mActionIcon == actionObj.mActionIcon &&
                mActionText.equals(actionObj.mActionText);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mActionText, mActionIcon);
    }

    public int getActionIcon() {
        return mActionIcon;
    }

    public String getActionText() {
        return mActionText;
    }


    // Static variables/ Action templates
    public static Action copy = new Action("Copy", null) {
        @Override
        public void doAction(Context context, Contents contents) {
            ClipData clipData = ClipData.newPlainText(
                    "Text",
                    contents.getDisplayValue()
            );
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setPrimaryClip(clipData);
        }
    };


    // Email
    public static Action email = new Action("Send in email app", R.drawable.ic_baseline_alternate_email_24) {
        @Override
        public void doAction(Context context, Contents contents) {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            //data is an email object
            Barcode.Email email = (Barcode.Email) contents.getBarcodeData();
            intent.putExtra(Intent.EXTRA_EMAIL, email.getAddress());
            intent.putExtra(Intent.EXTRA_SUBJECT, email.getSubject());
            intent.putExtra(Intent.EXTRA_TEXT, email.getBody());
            intent.setData(Uri.parse("mailto:"));
            if (intent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(intent);
            } else {
                Toast.makeText(context, "No email providers found", Toast.LENGTH_SHORT).show();

            }
        }
    };

    // Open URL
    public static Action url = new Action("Open", R.drawable.ic_baseline_open_in_new_24) {
        @Override
        public void doAction(Context context, Contents contents) {

            //data is a url object
            Barcode.UrlBookmark url = (Barcode.UrlBookmark) contents.getBarcodeData();
            Uri webpage = Uri.parse(url.getUrl());
            Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
            if (intent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(intent);
            } else {
                Toast.makeText(context, "No browsers found", Toast.LENGTH_SHORT).show();

            }
        }
    };


    public static Action addContact = new Action("Add contact", R.drawable.ic_baseline_add_circle_24) {
        @Override
        public void doAction(Context context, Contents contents) {
            Barcode.ContactInfo contactInfo = (Barcode.ContactInfo) contents.getBarcodeData();
            Intent intent = new Intent(Intent.ACTION_INSERT);
            intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
            intent.putExtra(ContactsContract.Intents.Insert.NAME, contactInfo.getName().getFormattedName());
            List<Barcode.Phone> phoneList = contactInfo.getPhones();

            ArrayList<ContentValues> data = new ArrayList<>();
            for (int i = 0; i < phoneList.size(); i++) {
                ContentValues row = new ContentValues();
                row.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE);
                row.put(ContactsContract.CommonDataKinds.Phone.NUMBER, phoneList.get(i).getNumber());
                row.put(ContactsContract.CommonDataKinds.Phone.TYPE, Utils.convertPhoneType(phoneList.get(i).getType()));
                data.add(row);
            }
            intent.putExtra(ContactsContract.Intents.Insert.COMPANY, contactInfo.getOrganization());


            List<Barcode.Email> emailList = contactInfo.getEmails();

            data = new ArrayList<>();
            for (int i = 0; i < emailList.size(); i++) {
                ContentValues row = new ContentValues();
                row.put(MIMETYPE, CONTENT_ITEM_TYPE);
                row.put(NUMBER, emailList.get(i).getAddress());
                row.put(TYPE, TYPE_WORK);
                data.add(row);
            }
            intent.putParcelableArrayListExtra(DATA, data);
            context.startActivity(intent);

        }
    };


    public static Action joinWifi = new Action("Join", R.drawable.ic_baseline_signal_wifi_4_bar_24) {
        @Override
        public void doAction(Context context, Contents contents) {
            Barcode.WiFi wifiDetails = (Barcode.WiFi) contents.getBarcodeData();
            String ssid = wifiDetails.getSsid();
            String password = wifiDetails.getPassword();

            WifiConfiguration configuration = new WifiConfiguration();
            configuration.SSID = "\"" + ssid + "\"";


            switch (wifiDetails.getEncryptionType()) {
                case Barcode.WiFi.TYPE_WEP:
                    configuration.wepKeys[0] = "\"" + password + "\"";
                    configuration.wepTxKeyIndex = 0;
                    configuration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                    configuration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
                    break;
                case Barcode.WiFi.TYPE_WPA:
                    configuration.preSharedKey = "\"" + password + "\"";
                    break;
                case Barcode.WiFi.TYPE_OPEN:
                    configuration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            }
            WifiManager manager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            manager.addNetwork(configuration);

        }
    };

    public static Action copySSID = new Action("Copy SSID", null) {
        @Override
        public void doAction(Context context, Contents contents) {
            Barcode.WiFi wifiDetails = (Barcode.WiFi) contents.getBarcodeData();
            ClipData clipData = ClipData.newPlainText(
                    "Wifi: " + wifiDetails.getSsid() ,
                    wifiDetails.getSsid()
            );
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setPrimaryClip(clipData);

        }
    };


    public static Action copyPassword = new Action("Copy SSID", null) {
        @Override
        public void doAction(Context context, Contents contents) {
            Barcode.WiFi wifiDetails = (Barcode.WiFi) contents.getBarcodeData();
            ClipData clipData = ClipData.newPlainText(
                    "Wifi \"" + wifiDetails.getSsid() + "\" password" ,
                    wifiDetails.getPassword()
            );
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setPrimaryClip(clipData);

        }
    };

    //More actions:
    // Call contact, email contact, share, CALENDAR, SMS, WIFI


}

