/*
 * Code Scanner. An android app to scan and create codes(barcodes, QR codes, etc)
 * Copyright (C) 2020 Lucius Chee Zihan
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

import androidx.annotation.NonNull;

import com.czlucius.scan.exceptions.QRDataNotSupportedException;

public class Text extends Data{
    private final CharSequence data;

    public Text(CharSequence data) {
        this.data = data;
    }

    @Override
    public boolean isEmpty() {
        return data.length() == 0;
    }

    @NonNull
    @Override
    public String getStringRepresentation() {
        return data.toString();
    }


}
