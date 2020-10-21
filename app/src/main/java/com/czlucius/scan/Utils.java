package com.czlucius.scan;

import android.provider.ContactsContract;

import com.czlucius.scan.objects.Code;
import com.czlucius.scan.objects.HistoryCode;
import com.google.mlkit.vision.barcode.Barcode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Utils {
    public static boolean codeListContains(ArrayList<Code> codes, Code code) {
        for(Code c: codes) {
            if (code.equals(c)) return true;
        }
        return false;

    }

    public static int convertPhoneType(@Barcode.Phone.FormatType int barcodePhoneType) {
        switch (barcodePhoneType) {
            case Barcode.Phone.TYPE_HOME: return ContactsContract.CommonDataKinds.Phone.TYPE_HOME;
            case Barcode.Phone.TYPE_WORK: return ContactsContract.CommonDataKinds.Phone.TYPE_WORK;
            case Barcode.Phone.TYPE_FAX: return ContactsContract.CommonDataKinds.Phone.TYPE_FAX_HOME;
            default:
                return ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE;
        }
    }

    public static List<HistoryCode> getAllSortLatest(List<HistoryCode> codeList) {
        List<HistoryCode> codeListCopy = new ArrayList<>(codeList);
        Collections.reverse(codeListCopy);
        return codeListCopy;
    }
}
