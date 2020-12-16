package com.czlucius.scan.objects.data.created;

import androidx.annotation.NonNull;

import ezvcard.VCard;
import ezvcard.property.Address;
import ezvcard.property.Organization;
import ezvcard.property.StructuredName;

public class CreatedContact implements ICreatedData {
    private final String firstName;
    private final String lastName;
    private final String prefix;
    private final String suffix;
    private final String company;
    private final String job;
    private final String phoneNo;
    private final String email;
    private final String street;
    private final String zipCode;
    private final String region;
    private final String country;
    private final String url;
    private final String additionalNotes;

    public CreatedContact(String firstName, String lastName, String prefix, String suffix, String company, String job, String phoneNo, String email, String street, String zipCode, String region, String country, String url, String additionalNotes) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.prefix = prefix;
        this.suffix = suffix;
        this.company = company;
        this.job = job;
        this.phoneNo = phoneNo;
        this.email = email;
        this.street = street;
        this.zipCode = zipCode;
        this.region = region;
        this.country = country;
        this.url = url;
        this.additionalNotes = additionalNotes;
    }

    @NonNull
    @Override
    public String getQRData() {

        VCard vCard = new VCard();
        StructuredName name = new StructuredName();
        name.setGiven(firstName);
        name.setFamily(lastName);
        name.getPrefixes().add(prefix);
        name.getPrefixes().add(suffix);
        vCard.setStructuredName(name);


        Organization org = new Organization();
        org.getValues().add(job);
        org.getValues().add(company);
        vCard.addOrganization(org);

        vCard.addTelephoneNumber(phoneNo);
        vCard.addEmail(email);

        Address address = new Address();
        address.setStreetAddress(street);
        address.setRegion(region);
        address.setCountry(country);
        address.setPostalCode(zipCode);
        vCard.addAddress(address);

        vCard.addUrl(url);
        vCard.addNote(additionalNotes);
        return vCard.toString();
    }

    @Override
    public boolean isEmpty() {
        return
                firstName.isEmpty() && lastName.isEmpty() && prefix.isEmpty()
                        && suffix.isEmpty() && company.isEmpty() && job.isEmpty()
                        && phoneNo.isEmpty() && email.isEmpty() && street.isEmpty()
                        && zipCode.isEmpty() && region.isEmpty() && country.isEmpty()
                        && url.isEmpty() && additionalNotes.isEmpty();
    }
}
