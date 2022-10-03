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

import androidx.arch.core.util.Function;

import com.czlucius.scan.App;
import com.czlucius.scan.R;
import com.czlucius.scan.objects.data.Data;
import com.czlucius.scan.objects.data.Phone;

public class CallPhoneAction extends PhoneAction {
    public CallPhoneAction(Function<Data, Phone> phoneRetrievalMethod) {
        this(phoneRetrievalMethod, App.getStringGlobal(R.string.call_number, "Call number"));
    }

    public CallPhoneAction(Function<Data, Phone> phoneRetrievalMethod, String chipText) {
        super(chipText, R.drawable.ic_baseline_call_24, phoneRetrievalMethod);
    }




    @Override
    public void performAction(Context context, Data data) {
        // May be type Contact or type Phone, so we use the callback function to get our phone.
        Phone phone = getPhone(data);
        Intent intent = new Intent(Intent.ACTION_DIAL);
        String telProtocol = "tel:" + phone.getNumber();
        intent.setData(Uri.parse(telProtocol));
        context.startActivity(intent);
    }
}
