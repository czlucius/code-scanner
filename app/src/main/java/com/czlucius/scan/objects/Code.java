package com.czlucius.scan.objects;


import com.google.mlkit.vision.barcode.Barcode;
import com.google.mlkit.vision.barcode.Barcode.BarcodeFormat;

import java.util.Date;

import static com.google.mlkit.vision.barcode.Barcode.FORMAT_AZTEC;
import static com.google.mlkit.vision.barcode.Barcode.FORMAT_CODABAR;
import static com.google.mlkit.vision.barcode.Barcode.FORMAT_CODE_128;
import static com.google.mlkit.vision.barcode.Barcode.FORMAT_CODE_39;
import static com.google.mlkit.vision.barcode.Barcode.FORMAT_CODE_93;
import static com.google.mlkit.vision.barcode.Barcode.FORMAT_DATA_MATRIX;
import static com.google.mlkit.vision.barcode.Barcode.FORMAT_EAN_13;
import static com.google.mlkit.vision.barcode.Barcode.FORMAT_EAN_8;
import static com.google.mlkit.vision.barcode.Barcode.FORMAT_ITF;
import static com.google.mlkit.vision.barcode.Barcode.FORMAT_PDF417;
import static com.google.mlkit.vision.barcode.Barcode.FORMAT_QR_CODE;
import static com.google.mlkit.vision.barcode.Barcode.FORMAT_UPC_A;
import static com.google.mlkit.vision.barcode.Barcode.FORMAT_UPC_E;


public class Code {
    private int codeId;


    private Type dataType;
    @BarcodeFormat
    private int format;
    private Contents contents;
    private Date timeScanned;
    // For comparison purposes only


    public Code(Barcode barcode) {
        dataType = Type.getTypeFromCode(barcode.getValueType());
        format = barcode.getFormat();
        contents = Contents.getInstanceFromBarcode(barcode);
        timeScanned = new Date();
    }

    private Code(Type dataType, int format, Contents contents) {
        this.dataType = dataType;
        this.format = format;
        this.contents = contents;
        this.timeScanned = new Date();
    }


    public Type getDataType() {
        return dataType;
    }

    public String getFormatName() {
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
                return "QR Code";
            default:
                return "Unknown";
        }

    }

    public Contents getContents() {
        return contents;
    }

    public Date getTimeScanned() {
        return timeScanned;
    }

    public HistoryCode toHistoryElement(Date date) {
        return new HistoryCode(dataType, format, contents, date);
    }

    public static Code fromHistoryElement(HistoryCode code) {
        return new Code(code.getDataType(), code.getFormat(), code.getContents());
    }


    public boolean equals(Code code) {
        // Using 3 parameters to confirm equality
        boolean b1 = dataType.getTypeName().equals(code.dataType.getTypeName());
        boolean b2 = dataType.getActions().equals(code.dataType.getActions());
        boolean b3 = contents.getDisplayValue().equals(code.contents.getDisplayValue());
        return (b1 && b2) && b3;
    }


}
