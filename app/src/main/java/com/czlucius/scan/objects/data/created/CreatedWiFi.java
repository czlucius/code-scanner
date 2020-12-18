/*
 * Code Scanner. An android app to scan and create codes(barcodes, QR codes, etc)
 * Copyright (C) 2020 Lucius Chee Zihan
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

package com.czlucius.scan.objects.data.created;


import androidx.annotation.NonNull;

public class CreatedWiFi implements ICreatedData {
    private final String ssid;
    private final String password;
    private final EncryptionType encryptionType;

    public CreatedWiFi(String ssid, String password, EncryptionType encryptionType) {
        this.ssid = ssid;
        this.password = password;
        this.encryptionType = encryptionType;
    }

    @NonNull
    @Override
    public String getQRData() {
        return "WIFI:"
                + "T:"
                + encryptionType.value
                + ";S:"
                + ssid
                + ";P:"
                + password
                + ";;";
    }


    public enum EncryptionType {
        Open("nopass"), WEP("WEP"), WPA("WPA");

        private final String value;
        EncryptionType(String value) {
            this.value = value;
        }
    }

    @Override
    public boolean isEmpty() {
        return ssid.isEmpty() && password.isEmpty();
    }
}