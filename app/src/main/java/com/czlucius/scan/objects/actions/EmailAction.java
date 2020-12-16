package com.czlucius.scan.objects.actions;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.czlucius.scan.App;
import com.czlucius.scan.R;
import com.czlucius.scan.objects.data.Data;
import com.czlucius.scan.objects.data.Email;

public class EmailAction extends Action {


    private EmailAction() {
        super(App.getStringGlobal(R.string.send_email, "Send email"), R.drawable.ic_baseline_alternate_email_24);
    }
    private static EmailAction INSTANCE;
    public static EmailAction getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new EmailAction();
        }
        return INSTANCE;
    }

    @Override
    public void performAction(Context context, Data data) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        //data is an email object
        Email email;
        try {
            email = (Email) data;
        } catch (ClassCastException exc) {
            Toast.makeText(context, R.string.data_invalid, Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        intent.putExtra(Intent.EXTRA_EMAIL, email.getAddress());
        intent.putExtra(Intent.EXTRA_SUBJECT, email.getSubject());
        intent.putExtra(Intent.EXTRA_TEXT, email.getBody());
        intent.setData(Uri.parse("mailto:"));
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        } else {
            Toast.makeText(context, R.string.no_email_providers, Toast.LENGTH_SHORT).show();
        }
    }
}
