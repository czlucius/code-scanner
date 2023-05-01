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

package com.czlucius.scan.objects;

import static com.czlucius.scan.objects.Availability.OFF;
import static com.czlucius.scan.objects.Availability.ON;
import static com.czlucius.scan.objects.Availability.UNAVAILABLE;

import androidx.annotation.IntDef;

@IntDef({ON, OFF, UNAVAILABLE})
public @interface Availability {
    int ON = 1;
    int OFF = 0;
    int UNAVAILABLE = -1;
}
