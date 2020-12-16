package com.czlucius.scan.objects.actions;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;

import com.czlucius.scan.App;
import com.czlucius.scan.R;
import com.czlucius.scan.Utils;
import com.czlucius.scan.objects.data.Address;
import com.czlucius.scan.objects.data.Contact;
import com.czlucius.scan.objects.data.Data;
import com.czlucius.scan.objects.data.Email;
import com.czlucius.scan.objects.data.Phone;

import java.util.ArrayList;

import static android.provider.ContactsContract.Intents.Insert.DATA;

public class AddContactAction extends Action {
    private AddContactAction() {
        super(App.getStringGlobal(R.string.add_contact, "Add Contact"), R.drawable.ic_baseline_add_circle_24);
    }

    private static AddContactAction INSTANCE;
    public static AddContactAction getInstance() {

        if (INSTANCE == null) {
            INSTANCE = new AddContactAction();
        }
        return INSTANCE;
    }


    @Override
    public void performAction(Context context, Data data) {
        Contact contactInfo = (Contact) data;


        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
        intent.putExtra(ContactsContract.Intents.Insert.NAME, contactInfo.getName().getStringRepresentation());
        Phone[] phoneList = contactInfo.getPhones();

        ArrayList<ContentValues> dataList = new ArrayList<>();
        for (Phone phone : phoneList) {
            ContentValues row = new ContentValues();
            row.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
            row.put(ContactsContract.CommonDataKinds.Phone.NUMBER, phone.getNumber());
            row.put(ContactsContract.CommonDataKinds.Phone.TYPE, Utils.convertPhoneType(phone.getType()));
            dataList.add(row);
        }
        intent.putParcelableArrayListExtra(DATA, dataList);

        intent.putExtra(ContactsContract.Intents.Insert.COMPANY, contactInfo.getOrganization());


        Email[] emailList = contactInfo.getEmails();

        dataList = new ArrayList<>();
        for (Email email : emailList) {
            ContentValues row = new ContentValues();
            row.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE);
            row.put(ContactsContract.CommonDataKinds.Email.ADDRESS, email.getAddress());
            row.put(ContactsContract.CommonDataKinds.Email.TYPE, Utils.convertEmailType(email.getType()));
            dataList.add(row);
        }
        intent.putParcelableArrayListExtra(DATA, dataList);


        String[] urlList = contactInfo.getUrls();
        dataList = new ArrayList<>();

        for (String url: urlList) {
            ContentValues row = new ContentValues();
            row.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE);
            row.put(ContactsContract.CommonDataKinds.Website.URL, url);
            dataList.add(row);

        }

        intent.putParcelableArrayListExtra(DATA, dataList);

        Address[] addresses = contactInfo.getAddresses();
        dataList = new ArrayList<>();

        for (Address address: addresses) {
            ContentValues row = new ContentValues();
            row.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE);
            row.put(ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS, address.getStringRepresentation());
            dataList.add(row);

        }

        intent.putParcelableArrayListExtra(DATA, dataList);
        context.startActivity(intent);

    }
}
