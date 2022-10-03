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
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.czlucius.scan.App;
import com.czlucius.scan.R;
import com.czlucius.scan.Utils;
import com.czlucius.scan.objects.data.Data;
import com.czlucius.scan.objects.data.Email;

public class EmailAction extends Action {

    private static EmailAction INSTANCE;
    private EmailAction() {
        super(App.getStringGlobal(R.string.send_email, "Send email"), R.drawable.ic_baseline_alternate_email_24);
    }

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
        if (Utils.launchIntentCheckAvailable(intent, context)) {
            context.startActivity(intent);
        } else {
            Toast.makeText(context, R.string.no_email_providers, Toast.LENGTH_SHORT).show();
        }
    }
}
