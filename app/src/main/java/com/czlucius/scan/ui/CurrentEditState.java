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

package com.czlucius.scan.ui;


import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.czlucius.scan.R;
import com.czlucius.scan.databinding.ContentsDialogBinding;
import com.czlucius.scan.objects.data.Text;
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

        @Override
        public boolean populateIndividualFields(ContentsDialogBinding rootBinding, ICreatedData data) {
            CreatedText createdText;
            if (data.getClass() == CreatedText.class) {
                createdText = (CreatedText) data;
            } else {
                return false;
            }
            rootBinding.textFieldCreate.setText(createdText.getText());
            return true;
        }
    }, URL(1) {
        @Override
        public ICreatedData createData(View v) {
            return new CreatedURL(((EditText) v).getText().toString());
        }

        @Override
        public boolean populateIndividualFields(ContentsDialogBinding rootBinding, ICreatedData data) {
            
            CreatedURL createdURL;
            if (data.getClass() == CreatedURL.class) {
                createdURL = (CreatedURL) data;
            } else {
                return false;
            }
            rootBinding.urlFieldCreate.setText(createdURL.getText());
            return true;
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

        @Override
        public boolean populateIndividualFields(ContentsDialogBinding rootBinding, ICreatedData data) {
            CreatedWiFi createdWiFi;
            if (data.getClass() == CreatedWiFi.class) {
                createdWiFi = (CreatedWiFi) data;
            } else {
                return false;
            }

            rootBinding.enterWifiSsidCreate.setText(createdWiFi.getSsid());
            rootBinding.enterWifiPwCreate.setText(createdWiFi.getPassword());
            rootBinding.enterWifiEncModeCreate.setSelection(createdWiFi.getEncryptionType().index);
            Log.i("werofyw87", rootBinding.enterWifiEncModeCreate.getItemAtPosition(2).toString());
            return true;
        }
    };

    public int index;

    CurrentEditState(int index) {
        this.index = index;
    }


    public abstract ICreatedData createData(View v);

    /**
     *
     * @return Returns true when cast to specific type succeeds, false if the provided data is not of this CurrentEditState type, so that the next type is tried(template method pattern)
     */
    protected abstract boolean populateIndividualFields(ContentsDialogBinding rootBinding, ICreatedData data);

    public static void populateFields(ContentsDialogBinding binding, @Nullable ICreatedData data) {
        if (data == null) {
            return;
        }
        for (CurrentEditState editState: CurrentEditState.values()) {
            // Correct type is found
            if (editState.populateIndividualFields(binding, data)) {
                break;
            }
        }
    }
}