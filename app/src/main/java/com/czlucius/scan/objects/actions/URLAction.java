package com.czlucius.scan.objects.actions;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.czlucius.scan.App;
import com.czlucius.scan.R;
import com.czlucius.scan.objects.data.Data;
import com.czlucius.scan.objects.data.URL;

public class URLAction extends Action {
    private static Action INSTANCE;

    private URLAction() {
        super(App.getStringGlobal(R.string.open_url, "Open URL"), R.drawable.ic_baseline_open_in_new_24);
    }

    public static Action getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new URLAction();
        }
        return INSTANCE;
    }

    @Override
    public void performAction(Context context, Data data) {
        //data is a url object
        URL url = (URL) data;
        Uri webpage = Uri.parse(url.getUrlAddress());
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        } else {
            Toast.makeText(context, R.string.no_browsers, Toast.LENGTH_SHORT).show();

        }
    }
}
