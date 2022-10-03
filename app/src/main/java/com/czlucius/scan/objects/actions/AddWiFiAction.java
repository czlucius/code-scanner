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

package com.czlucius.scan.objects.actions;

import android.content.Context;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.StringRes;

import com.czlucius.scan.App;
import com.czlucius.scan.R;
import com.czlucius.scan.exceptions.NetworkInvalidException;
import com.czlucius.scan.objects.data.Data;
import com.czlucius.scan.objects.data.WiFi;

public class AddWiFiAction extends Action {
    private static Action INSTANCE;
    private AddWiFiAction() {
        super(App.getStringGlobal(R.string.connect_wifi, "Connect"), R.drawable.ic_baseline_signal_wifi_4_bar_24);
    }

    public static Action getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AddWiFiAction();
        }
        return INSTANCE;
    }

    @Override
    public void performAction(Context context, Data data) {

        WiFi wifi = (WiFi) data;
        try {
            wifi.connect(context);
        } catch (NetworkInvalidException e) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                errorMsg(context, R.string.connection_error_simple);
            } else {
                switch (e.getType()) {
                    case NetworkInvalidException.INTERNAL_ERR:
                        errorMsg(context, R.string.connection_internal_error);
                    case NetworkInvalidException.APP_DISALLOWED:
                        errorMsg(context, R.string.connection_app_disallowed);
                        break;
                    case NetworkInvalidException.DUPLICATE:
                        errorMsg(context, R.string.connection_duplicate);
                        break;
                    case NetworkInvalidException.EXCEED_MAX_LIMIT:
                        errorMsg(context, R.string.connection_app_exceed_limit);
                        break;
                    case NetworkInvalidException.NETWORK_INVALID:
                        errorMsg(context, R.string.connection_ntwk_invalid);
                        break;
                    case NetworkInvalidException.NETWORK_NOT_ALLOWED:
                        errorMsg(context, R.string.connection_ntwk_disallowed);
                        break;
                    case NetworkInvalidException.SIMPLE_ERROR:
                        e.printStackTrace();
                        break;
                    default:
                        e.printStackTrace();
                        break;

                }
            }
        }
    }

    private void errorMsg(Context context, @StringRes int stringRes) {
        Toast.makeText(context, stringRes, Toast.LENGTH_SHORT).show();
    }

}
