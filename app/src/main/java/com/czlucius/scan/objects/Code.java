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

package com.czlucius.scan.objects;


import static com.google.mlkit.vision.barcode.common.Barcode.FORMAT_AZTEC;
import static com.google.mlkit.vision.barcode.common.Barcode.FORMAT_CODABAR;
import static com.google.mlkit.vision.barcode.common.Barcode.FORMAT_CODE_128;
import static com.google.mlkit.vision.barcode.common.Barcode.FORMAT_CODE_39;
import static com.google.mlkit.vision.barcode.common.Barcode.FORMAT_CODE_93;
import static com.google.mlkit.vision.barcode.common.Barcode.FORMAT_DATA_MATRIX;
import static com.google.mlkit.vision.barcode.common.Barcode.FORMAT_EAN_13;
import static com.google.mlkit.vision.barcode.common.Barcode.FORMAT_EAN_8;
import static com.google.mlkit.vision.barcode.common.Barcode.FORMAT_ITF;
import static com.google.mlkit.vision.barcode.common.Barcode.FORMAT_PDF417;
import static com.google.mlkit.vision.barcode.common.Barcode.FORMAT_QR_CODE;
import static com.google.mlkit.vision.barcode.common.Barcode.FORMAT_UPC_A;
import static com.google.mlkit.vision.barcode.common.Barcode.FORMAT_UPC_E;

import android.content.Context;

import com.czlucius.scan.R;
import com.czlucius.scan.database.CodeMemento;
import com.czlucius.scan.objects.data.Data;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.barcode.common.Barcode.BarcodeFormat;

import java.util.Date;
import java.util.Objects;


public class Code {

    private final Type dataType;
    @BarcodeFormat
    private final int format;
    private final Data data;
    private final Date timeScanned;
    // For comparison purposes only


    public Code(Barcode barcode) {
        dataType = Type.getTypeFromCode(barcode.getValueType());
        format = barcode.getFormat();

        Data.Factory factory = Data.Factory.getInstance();
        data = factory.create(barcode);
        timeScanned = new Date();
    }

    private Code(Type dataType, int format, Data data) {
        this.dataType = dataType;
        this.format = format;
        this.data = data;
        this.timeScanned = new Date();
    }



    public Type getDataType() {
        return dataType;
    }

    public String getFormatName(Context ctx) {

        switch (format) {
            case FORMAT_CODABAR:
                return "Codabar";
            case FORMAT_CODE_39:
                return "Code 39";
            case FORMAT_CODE_93:
                return "Code 93";
            case FORMAT_CODE_128:
                return "Code 128";
            case FORMAT_EAN_8:
                return "EAN-8";
            case FORMAT_EAN_13:
                return "EAN-13";
            case FORMAT_ITF:
                return "ITF";
            case FORMAT_UPC_A:
                return "UPC-A";
            case FORMAT_UPC_E:
                return "UPC-E";
            case FORMAT_AZTEC:
                return "Aztec";
            case FORMAT_DATA_MATRIX:
                return "Data Matrix";
            case FORMAT_PDF417:
                return "PDF417";
            case FORMAT_QR_CODE:
                return ctx.getString(R.string.qr);
            default:
                return ctx.getString(R.string.unknown);
        }

    }


    public Data getData() {
        return data;
    }

    public Date getTimeScanned() {
        return timeScanned;
    }

    public CodeMemento toMemento() {
        return new CodeMemento(dataType, format, data, timeScanned);
    }

    public static Code fromHistoryElement(CodeMemento code) {
        return new Code(code.getDataType(), code.getFormat(), code.getData());
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Code code = (Code) o;
        return format == code.format &&
                Objects.equals(dataType, code.dataType) &&
                Objects.equals(data, code.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dataType, format, data);
    }



}
