package com.czlucius.scan.objects.actions;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import androidx.annotation.Nullable;

import com.czlucius.scan.App;
import com.czlucius.scan.R;
import com.czlucius.scan.objects.data.Data;
import com.czlucius.scan.objects.data.WiFi;

public class CopyPasskeyAction extends Action {
    private CopyPasskeyAction() {
        super(App.getStringGlobal(R.string.copy_password, "Copy password"), null);
    }

    private static Action INSTANCE;
    public static Action getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CopyPasskeyAction();
        }
        return INSTANCE;
    }

    @Override
    public void performAction(Context context, Data data) {
        ClipData clipData = ClipData.newPlainText(
                context.getString(R.string.password),
                ((WiFi)data).getPassword()
        );
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        clipboard.setPrimaryClip(clipData);
    }
}
