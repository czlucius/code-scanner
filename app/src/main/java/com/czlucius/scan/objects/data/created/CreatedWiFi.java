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