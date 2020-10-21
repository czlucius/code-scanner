package com.czlucius.scan.objects;

import com.google.mlkit.vision.barcode.Barcode;

public class Contents {
    private Object barcodeData; // Barcode.* objects
    private String displayContents;

    public static Contents getInstanceFromBarcode(Barcode barcode) {
        switch (barcode.getValueType()) {
            case Barcode.TYPE_EMAIL:
                return new Contents(barcode.getEmail(), barcode.getDisplayValue());
            case Barcode.TYPE_CONTACT_INFO:
                return new Contents(barcode.getContactInfo(), barcode.getDisplayValue());
            case Barcode.TYPE_URL:
                return new Contents(barcode.getUrl(), barcode.getDisplayValue());

            default:
                return new Contents(barcode.getRawValue(), barcode.getDisplayValue());
        }
    }

    private Contents(Object contents, String displayContents) {
        this.barcodeData = contents;
        this.displayContents = displayContents;
    }

    public static Contents test(Object c, String d) {
        return new Contents(c,d);
    }


    /**
     *
     * @return Object containing information about the contents read. Check barcode data type before accessing, and cast the <code>Object</code> to the relevant classes.
     * returned Object will be String if there is no specialised objects to store its data in <code>Barcode.*</code>
     */
    public Object getBarcodeData() {
        return barcodeData;
    }


    public String getDisplayValue() {
        return displayContents;
    }


}
