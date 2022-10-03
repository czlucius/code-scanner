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

package com.czlucius.scan.objects.actions;

import android.content.Context;
import android.content.Intent;


import com.czlucius.scan.App;
import com.czlucius.scan.R;
import com.czlucius.scan.objects.data.Data;
import com.czlucius.scan.objects.data.GeoLocation;



public class ViewLocationAction extends Action {
    private static Action INSTANCE;
    private ViewLocationAction() {
        super(App.getStringGlobal(R.string.view_location, "View location in maps application"), R.drawable.ic_baseline_location_drop_24);
    }
    public static Action getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ViewLocationAction();
        }
        return INSTANCE;
    }


    @Override
    public void performAction(Context context, Data data) {
        GeoLocation geo = (GeoLocation) data;
        Intent intent = new Intent(Intent.ACTION_VIEW, geo.getGeoUri());
        context.startActivity(intent);
    }
}
