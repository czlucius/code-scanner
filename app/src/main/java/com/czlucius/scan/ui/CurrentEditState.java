package com.czlucius.scan.ui;


import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.czlucius.scan.R;
import com.czlucius.scan.objects.data.created.CreatedText;
import com.czlucius.scan.objects.data.created.CreatedURL;
import com.czlucius.scan.objects.data.created.CreatedWiFi;
import com.czlucius.scan.objects.data.created.ICreatedData;

public enum CurrentEditState {
    TEXT(0) {
        @Override
        public ICreatedData createData(View v) {
            return new CreatedText(((EditText) v).getText().toString());
        }
    }, URL(1) {
        @Override
        public ICreatedData createData(View v) {
            return new CreatedURL(((EditText) v).getText().toString());
        }
    }, WIFI(2) {
        @Override
        public ICreatedData createData(View v) {
            ConstraintLayout layout = (ConstraintLayout) v;
            String ssid = ((EditText) layout.findViewById(R.id.enterWifiSsidCreate))
                    .getText().toString();
            String pw = ((EditText) layout.findViewById(R.id.enterWifiPwCreate))
                    .getText().toString();
            CreatedWiFi.EncryptionType encryptionType = (CreatedWiFi.EncryptionType) ((Spinner) layout.findViewById(R.id.enterWifiEncModeCreate)).getSelectedItem();

            return new CreatedWiFi(ssid, pw, encryptionType);

        }
    };

    public int index;

    CurrentEditState(int index) {
        this.index = index;
    }


    public abstract ICreatedData createData(View v);
}