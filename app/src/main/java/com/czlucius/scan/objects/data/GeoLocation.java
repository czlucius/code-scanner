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

import android.net.Uri;

import androidx.annotation.NonNull;

import com.czlucius.scan.App;
import com.czlucius.scan.R;

public class GeoLocation extends Data {


    /*
    Since lat and lng are simple values, no need for using accessors, can get directly.
     */
    public final double lat;
    public final double lng;

    public GeoLocation(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    @NonNull
    @Override
    public String getStringRepresentation() {
        return App.getStringGlobalWithArgs(R.string.geo_template, lat, lng);
    }

    @Override
    public boolean isEmpty() {
        // double cannot be null/empty.
        return false;
    }

    public Uri getGeoUri() {
        return Uri.parse("geo:" + lat + "," + lng);
    }
}
