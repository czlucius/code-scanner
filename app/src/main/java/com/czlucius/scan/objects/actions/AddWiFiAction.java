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

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.StringRes;

import com.czlucius.scan.App;
import com.czlucius.scan.R;
import com.czlucius.scan.exceptions.NetworkInvalidException;
import com.czlucius.scan.objects.data.Data;
import com.czlucius.scan.objects.data.WiFi;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

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
                        // errorMsg(context, R.string.connection_app_disallowed);

                        new MaterialAlertDialogBuilder(context)
                                //.setTitle("")
                                .setMessage(R.string.wifi_control_error)
                                .setPositiveButton(R.string.open_settings, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(Settings.ACTION_SETTINGS);
                                        context.startActivity(intent); } })
                                .setNegativeButton(R.string.add_manually, new  DialogInterface.OnClickListener(){
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                                        context.startActivity(intent);
                                        Toast.makeText(context,"Password copied\n\n"+"Select  "+wifi.getSsid()+"  & Paste password" , Toast.LENGTH_SHORT).show();
                                        ClipData clipData = ClipData.newPlainText(context.getString(R.string.password), ((WiFi)data).getPassword());
                                        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                                        clipboard.setPrimaryClip(clipData);
                                    }})//      .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();

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
