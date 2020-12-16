package com.czlucius.scan.objects.actions;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import com.czlucius.scan.App;
import com.czlucius.scan.R;
import com.czlucius.scan.objects.data.Data;
import com.czlucius.scan.objects.data.WiFi;

public class CopySSIDAction extends Action {
    private CopySSIDAction() {
        super(App.getStringGlobal(R.string.copy_ssid, "Copy SSID"), null);
    }

    private static Action INSTANCE;
    public static Action getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CopySSIDAction();
        }
        return INSTANCE;
    }

    @Override
    public void performAction(Context context, Data data) {
        ClipData clipData = ClipData.newPlainText(
                context.getString(R.string.ssid),
                ((WiFi)data).getSsid()
        );
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        clipboard.setPrimaryClip(clipData);
    }
}
