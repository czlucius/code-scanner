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

package com.czlucius.scan.exceptions;

import android.annotation.SuppressLint;
import android.os.Build;

import androidx.annotation.IntDef;
import androidx.annotation.RequiresApi;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class NetworkInvalidException extends Exception {

    @RequiresApi(Build.VERSION_CODES.Q)
    public static final int INTERNAL_ERR = 1,
            APP_DISALLOWED = 2,
            DUPLICATE = 3,
            EXCEED_MAX_LIMIT = 4,
            NETWORK_NOT_ALLOWED = 6,
            NETWORK_INVALID = 7;

    public static final int SIMPLE_ERROR = -1;

    @ErrorCode
    private final int type;

    public NetworkInvalidException(int type) {
        super();
        this.type = type;

    }

    @ErrorCode
    public int getType() {
        return type;
    }



    @SuppressLint("NewApi")
    @IntDef(value =  {
            INTERNAL_ERR,
            APP_DISALLOWED,
            DUPLICATE,
            EXCEED_MAX_LIMIT,
            NETWORK_NOT_ALLOWED,
            NETWORK_INVALID,
            SIMPLE_ERROR
    } )
    @Retention(RetentionPolicy.SOURCE)
    public @interface ErrorCode {}
}


