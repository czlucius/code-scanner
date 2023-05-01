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

package com.czlucius.scan.objects.data;

import androidx.annotation.NonNull;

import com.czlucius.scan.App;
import com.czlucius.scan.R;
import com.google.mlkit.vision.barcode.common.Barcode;

import java.util.Objects;

public class Email extends Data {
    private final String address;
    private final String body;
    private final String subject;
    private final int type;

    @NonNull
    @Override
    public String getStringRepresentation() {
        return App.getStringGlobal(R.string.email, "Email")
                + ": \n"
                + App.getStringGlobal(R.string.to, "To")
                + " <"
                + address + ">\n"
                + App.getStringGlobal(R.string.subject, "Subject")
                + ": \""
                + subject
                + "\"\n"
                + App.getStringGlobal(R.string.contents, "Contents")
                + ":\n"
                + body;
    }


    @Override
    public boolean isEmpty() {
        return address.isEmpty() && body.isEmpty() && subject.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Email email = (Email) o;
        return type == email.type &&
                Objects.equals(address, email.address) &&
                Objects.equals(body, email.body) &&
                Objects.equals(subject, email.subject);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), address, body, subject, type);
    }

    public String getAddress() {
        return address;
    }

    public String getBody() {
        return body;
    }

    public String getSubject() {
        return subject;
    }

    public int getType() {
        return type;
    }


    public Email(Barcode.Email email) {
        address = email.getAddress();
        body = email.getBody();
        subject = email.getSubject();
        type = email.getType();

    }


}
