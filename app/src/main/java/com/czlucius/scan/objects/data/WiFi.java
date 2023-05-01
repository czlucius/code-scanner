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

import android.content.Context;

import androidx.annotation.NonNull;

import com.czlucius.scan.App;
import com.czlucius.scan.R;
import com.czlucius.scan.Utils;
import com.czlucius.scan.exceptions.NetworkInvalidException;
import com.google.mlkit.vision.barcode.common.Barcode;

import java.util.Objects;

public class WiFi extends Data {
    private final String ssid;
    private final String password;
    @Barcode.WiFi.EncryptionType
    private final int encryptionType;


    public WiFi(String ssid, String password, int encryptionType) {
        this.ssid = ssid;
        this.password = password;
        this.encryptionType = encryptionType;
    }

    public WiFi(Barcode.WiFi otherWiFi) {
        this(otherWiFi.getSsid(), otherWiFi.getPassword(), otherWiFi.getEncryptionType());
    }

    public String getSsid() {
        return ssid;
    }

    public String getPassword() {
        return password;
    }

    @Barcode.WiFi.EncryptionType
    public int getEncryptionType() {
        return encryptionType;
    }

    public void connect(Context context) throws NetworkInvalidException {
        Utils.addWiFiCompat(this, context);
    }


    @NonNull
    @Override
    public String getStringRepresentation() {
        StringBuilder text = new StringBuilder().append(App.getStringGlobal(R.string.wifi, "Wi-Fi"))
                .append("\n")
                .append(App.getStringGlobal(R.string.ssid, "Wi-Fi SSID"))
                .append(": ")
                .append(ssid)
                .append("\n");
        if (encryptionType != Barcode.WiFi.TYPE_OPEN) {
            text.append(App.getStringGlobal(R.string.password, "Wi-Fi password"))
                    .append(": ")
                    .append(password)
                    .append("\n");
        }
        text.append(App.getStringGlobal(R.string.encryption_type, "Encryption type"))
                .append(": ");
        switch (encryptionType) {
            case Barcode.WiFi.TYPE_OPEN:
                text.append(App.getStringGlobal(R.string.open_network, "Open"));
                break;
            case Barcode.WiFi.TYPE_WPA:
                text.append("WPA");
                break;
            case Barcode.WiFi.TYPE_WEP:
                text.append("WEP");
                break;
        }
        return text.toString();
    }

    @Override
    public String getSummary() {
        return ssid;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WiFi wiFi = (WiFi) o;
        return encryptionType == wiFi.encryptionType &&
                ssid.equals(wiFi.ssid) &&
                password.equals(wiFi.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ssid, password, encryptionType);
    }

    @Override
    public boolean isEmpty() {
        return ssid.isEmpty() && password.isEmpty();
    }
}
