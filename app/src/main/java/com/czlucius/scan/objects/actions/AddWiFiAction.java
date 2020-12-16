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
    public AddWiFiAction() {
        super(App.getStringGlobal(R.string.connect_wifi, "Connect"), R.drawable.ic_baseline_signal_wifi_4_bar_24);
    }

    private static Action INSTANCE;
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
                    default:
                        e.printStackTrace();
                    case NetworkInvalidException.SIMPLE_ERROR:
                        e.printStackTrace();
                }
            }
        }
    }

    private void errorMsg(Context context, @StringRes int stringRes) {
        Toast.makeText(context, stringRes, Toast.LENGTH_SHORT).show();
    }

}
