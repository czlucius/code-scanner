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

package com.czlucius.scan.objects.data.created;

//import android.net.Uri;
//
//import androidx.annotation.NonNull;
//import androidx.constraintlayout.widget.ConstraintLayout;
//
//import com.czlucius.scan.Utils;
//
//import java.io.UnsupportedEncodingException;
//import java.net.URLEncoder;
//import java.util.Arrays;


//
//public class CreatedEmail implements ICreatedData {
//
//    private final String[] to;
//    private final String[] cc;
//    private final String[] bcc;
//    private final String subject;
//    private final String body;
//
//    public CreatedEmail(String[] to, String[] cc, String[] bcc, String subject, String body) {
//        this.to = to;
//        this.cc = cc;
//        this.bcc = bcc;
//        this.subject = subject;
//        this.body = body;
//    }
//
//
//
//    @NonNull
//    @Override
//    public String getQRData() {
//
//        //mailto:someone@yoursite.com?cc=someoneelse@theirsite.com,another@thatsite.com,me@mysite.com&bcc=lastperson@theirsite.com&subject=Big%20News&body=Body%20goes%20here.
//
//        try {
//            return "mailto:"
//                    + emailArrayToEncodedString(to)
//                    + "?cc="
//                    + emailArrayToEncodedString(cc)
//                    + "&bcc="
//                    + emailArrayToEncodedString(bcc)
//                    + "&subject="
//                    + URLEncoder.encode(subject, "utf-8")
//                    + "&body="
//                    + URLEncoder.encode(body, "utf-8");
//        } catch (UnsupportedEncodingException e) {
//            throw Utils.suppressExceptionRuntime(e);
//        }
//    }
//
//    @NonNull
//    private String emailArrayToEncodedString(String[] emails) throws UnsupportedEncodingException {
//        return URLEncoder.encode(Arrays.toString(emails)
//                .replaceAll("]$", "")
//                .replaceAll("^\\[", "")
//                .replaceAll(", ", ","), "utf-8");
//    }
//
//
//    @Override
//    public boolean isEmpty() {
//        return to.length == 0 && cc.length == 0 && bcc.length == 0 && subject.isEmpty() && body.isEmpty();
//    }
//}
