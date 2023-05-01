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

package com.czlucius.scan;

import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiNetworkSuggestion;
import android.os.Build;
import android.provider.ContactsContract;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.Toast;

import com.czlucius.scan.database.CodeMemento;
import com.czlucius.scan.exceptions.NetworkInvalidException;
import com.czlucius.scan.objects.Availability;
import com.czlucius.scan.objects.Code;
import com.czlucius.scan.objects.data.WiFi;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.mlkit.vision.barcode.common.Barcode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Utils {
    public static boolean codeListContains(Iterable<Code> codes, Code code) {
        for (Code c : codes) {
            if (code.equals(c)) return true;
        }
        return false;

    }

    public static void copyToClipboard(Context context, String textToCopy) {
        ClipData clipData = ClipData.newPlainText(
                context.getString(R.string.text),
                textToCopy
        );
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        clipboard.setPrimaryClip(clipData);
    }

    public static int convertPhoneType(@Barcode.Phone.FormatType int barcodePhoneType) {
        switch (barcodePhoneType) {
            case Barcode.Phone.TYPE_HOME:
                return ContactsContract.CommonDataKinds.Phone.TYPE_HOME;
            case Barcode.Phone.TYPE_WORK:
                return ContactsContract.CommonDataKinds.Phone.TYPE_WORK;
            case Barcode.Phone.TYPE_FAX:
                return ContactsContract.CommonDataKinds.Phone.TYPE_FAX_HOME;
            default:
                return ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE;
        }
    }


    public static int convertEmailType(@Barcode.Email.FormatType int barcodeEmailType) {
        switch (barcodeEmailType) {
            case Barcode.Email.TYPE_HOME:
                return ContactsContract.CommonDataKinds.Email.TYPE_HOME;
            case Barcode.Email.TYPE_WORK:
                return ContactsContract.CommonDataKinds.Email.TYPE_WORK;
            default:
                return ContactsContract.CommonDataKinds.Email.TYPE_OTHER;
        }
    }

    public static List<CodeMemento> getAllSortLatest(List<CodeMemento> codeList) {
        List<CodeMemento> codeListCopy = new ArrayList<>(codeList);
        Collections.reverse(codeListCopy);
        return codeListCopy;
    }


    public float dp(float px, DisplayMetrics metrics) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, px, metrics);
    }

    public static void addWiFiCompat(WiFi wifi, Context context) throws NetworkInvalidException {
        if (wifi.isEmpty()) return;
        Context appCtx = context.getApplicationContext();
        WifiManager manager = (WifiManager) appCtx.getSystemService(Context.WIFI_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            WifiNetworkSuggestion.Builder suggestionBuilder = new WifiNetworkSuggestion.Builder()
                    .setSsid(wifi.getSsid());
            if (wifi.getEncryptionType() != Barcode.WiFi.TYPE_OPEN || wifi.getEncryptionType() != Barcode.WiFi.TYPE_WEP) {
                suggestionBuilder.setWpa2Passphrase(wifi.getPassword());
            }
            suggestionBuilder.setIsAppInteractionRequired(false);
            ArrayList<WifiNetworkSuggestion> suggestions = new ArrayList<>(1);
            suggestions.add(suggestionBuilder.build());

            int status = manager.addNetworkSuggestions(suggestions);
            if (status != WifiManager.STATUS_NETWORK_SUGGESTIONS_SUCCESS) {
                throw new NetworkInvalidException(status);
            }

        } else {
            WifiConfiguration config = new WifiConfiguration();
            config.SSID = "\"" + wifi.getSsid() + "\"";
            switch (wifi.getEncryptionType()) {
                case Barcode.WiFi.TYPE_WEP:
                    config.wepKeys[0] = "\"" + wifi.getPassword() + "\"";
                    config.wepTxKeyIndex = 0;
                    config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                    config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
                    break;
                case Barcode.WiFi.TYPE_WPA:
                    config.preSharedKey = "\"" + wifi.getPassword() + "\"";
                    break;
                case Barcode.WiFi.TYPE_OPEN:
                    config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                    break;
            }
            if (manager.addNetwork(config) == -1) {
                throw new NetworkInvalidException(NetworkInvalidException.SIMPLE_ERROR);
            }
        }
    }


    public static RuntimeException suppressExceptionRuntime(Throwable exception) {
        if (exception instanceof RuntimeException) throw (RuntimeException) exception;

        RuntimeException runtimeException = new RuntimeException(exception.getCause());
        runtimeException.addSuppressed(exception);
        return runtimeException;

    }

    public static int getToggleGroupIndex(MaterialButtonToggleGroup toggleGroup) {
        return toggleGroup.indexOfChild(toggleGroup.findViewById(toggleGroup.getCheckedButtonId()));
    }

    /**
     *
     * @param intent Intent to launch.
     * @param context Context to use startActivity() method
     * @return Whether an app to launch the intent exists on the device
     */
    public static boolean launchIntentCheckAvailable(Intent intent, Context context) {

        try {
            context.startActivity(intent);
            return true;
        } catch (ActivityNotFoundException e) {
            // There are no apps that support this intent
            return false;
        }
    }

    public static void launchWebPageExternally(Context context, Uri webpage) {
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (!Utils.launchIntentCheckAvailable(intent, context)) {
            // Browser unavailable.
            Toast.makeText(context, R.string.no_browsers, Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean availabilityToBoolean(@Availability int availability) {
        return availability == Availability.ON;
    }
}
