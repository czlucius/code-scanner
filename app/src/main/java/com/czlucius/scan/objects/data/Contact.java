package com.czlucius.scan.objects.data;


import androidx.annotation.NonNull;

import com.czlucius.scan.App;
import com.czlucius.scan.R;
import com.google.mlkit.vision.barcode.Barcode;

import java.util.Arrays;
import java.util.Objects;

import ezvcard.VCard;
import ezvcard.property.FormattedName;

public class Contact extends Data {

    private static final String TAG = "Contact";

    private final Address[] addresses;
    private final Email[] emails;
    private final Phone[] phones;

    private final Name name;

    private final String organization;
    private final String title;
    private final String[] urls;


    public Contact(Barcode.ContactInfo contactInfo) {
        Address[] addresses = new Address[contactInfo.getAddresses().size()];
        for (int i = 0; i < addresses.length; i++) {
            addresses[i] = new Address(contactInfo.getAddresses().get(i));
        }
        this.addresses = addresses;
        Email[] emails = new Email[contactInfo.getEmails().size()];
        for (int i = 0; i < emails.length; i++) {
            emails[i] = new Email(contactInfo.getEmails().get(i));
        }
        this.emails = emails;
        Phone[] phones = new Phone[contactInfo.getPhones().size()];
        for (int i = 0; i < phones.length; i++) {
            phones[i] = new Phone(contactInfo.getPhones().get(i));
        }
        this.phones = phones;
        this.name = contactInfo.getName() != null ? new Name(contactInfo.getName()) : Name.NO_NAME_INSTANCE;
        this.organization = contactInfo.getOrganization();
        this.title = contactInfo.getTitle();
        this.urls = contactInfo.getUrls().toArray(new String[0]);
    }


    public Address[] getAddresses() {
        return addresses;
    }

    public Email[] getEmails() {
        return emails;
    }

    public Phone[] getPhones() {
        return phones;
    }

    public Name getName() {
        return name;
    }

    public String getOrganization() {
        return organization;
    }

    public String getTitle() {
        return title;
    }

    public String[] getUrls() {
        return urls;
    }


    private static String getStringFromIDataList(Data[] datas) {
        if (datas == null || datas.length == 0) return "";
        StringBuilder sb = new StringBuilder();
        for (Data data : datas) {
            if (data == null) continue;
            sb.append(data.getStringRepresentation())
                    .append("\n");

        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Contact contact = (Contact) o;
        return Arrays.equals(addresses, contact.addresses) &&
                Arrays.equals(emails, contact.emails) &&
                Arrays.equals(phones, contact.phones) &&
                name.equals(contact.name) &&
                Objects.equals(organization, contact.organization) &&
                Objects.equals(title, contact.title) &&
                Arrays.equals(urls, contact.urls);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(super.hashCode(), name, organization, title);
        result = 31 * result + Arrays.hashCode(addresses);
        result = 31 * result + Arrays.hashCode(emails);
        result = 31 * result + Arrays.hashCode(phones);
        result = 31 * result + Arrays.hashCode(urls);
        return result;
    }

    @NonNull
    @Override
    public String getStringRepresentation() {
        // EXPM!!!!!!!!!!!!!!
        return addresses[0].getFormattedAddresses()[0];//App.getStringGlobal(R.string.contact, "Contact") + "\n" + getDescription();
    }

    @Override
    public String getDescription() {
        StringBuilder contact = new StringBuilder();
        contact.append(!title.equals("") ? App.getStringGlobal(R.string.title, "Title") + ": \"" : "")
                .append(title)
                .append(!title.equals("") ? "\"" : "")
                .append("\n")
                .append(App.getStringGlobal(R.string.name, "Name"))
                .append(": \"")
                .append(name.getStringRepresentation())
                .append(phones.length != 0 ? "\"\n" + App.getStringGlobal(R.string.phones, "Phones") + ":\n" : "\"\n")
                .append(getStringFromIDataList(phones));

        if (!(organization.isEmpty() || organization.trim().isEmpty())) {
            contact.append(App.getStringGlobal(R.string.org, "Organization"))
                    .append(": \"")
                    .append(organization)
                    .append("\"\n");
        }

        contact.append(emails.length != 0 ? App.getStringGlobal(R.string.emails, "Emails") + ": \"" : "");
        for (int i = 0; i < emails.length; i++) {
            // just get sender
            contact.append(emails[i].getAddress());
            if (!(i == emails.length - 1)) {
                contact.append("\", ");
            } else {
                contact.append("\"\n");
            }
        }

        contact.append(addresses.length != 0 ? App.getStringGlobal(R.string.addresses, "Addresses") + ": \n" : "")
                .append(getStringFromIDataList(addresses))
                .append(urls.length != 0 ? App.getStringGlobal(R.string.websites, "Websites") + ": \n" : "");
        for (String website : urls) {
            contact.append("\"")
                    .append(website)
                    .append("\"\n");
        }

        return contact.toString();
    }




    @Override
    public boolean isEmpty() {
        return addresses.length == 0 && emails.length == 0 && phones.length == 0
                && name.isEmpty() && organization.isEmpty() && title.isEmpty()
                && urls.length == 0;
    }


}
