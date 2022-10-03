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
import android.net.Uri;

import com.czlucius.scan.App;
import com.czlucius.scan.R;
import com.czlucius.scan.Utils;
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
        Uri rawWebAddress = Uri.parse(url.getUrlAddress());

        // Prepend a http schema in front if url comes without schema.
        // See https://github.com/czlucius/code-scanner/issues/20
        Uri.Builder builder = rawWebAddress.buildUpon();
        if (rawWebAddress.getScheme() == null || rawWebAddress.getScheme().isEmpty()) {
            builder.scheme("http");
        }
        Uri webpage = builder.build();

        Utils.launchWebPageExternally(context, webpage);
    }


}
