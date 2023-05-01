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

import androidx.annotation.NonNull;

import ezvcard.VCard;
import ezvcard.property.Address;
import ezvcard.property.Organization;
import ezvcard.property.StructuredName;

public class CreatedContact implements ICreatedData {
    public final String firstName;
    public final String lastName;
    public final String prefix;
    public final String suffix;
    public final String company;
    public final String job;
    public final String phoneNo;
    public final String email;
    public final String street;
    public final String zipCode;
    public final String region;
    public final String country;
    public final String url;
    public final String additionalNotes;

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
        return vCard.write();
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

    public static CreatedContact EMPTY = new CreatedContact("", "", "", "", "", "", "", "", "", "", "", "", "", "");

}
