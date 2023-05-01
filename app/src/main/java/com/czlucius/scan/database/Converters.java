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

package com.czlucius.scan.database;

import android.util.Log;

import androidx.annotation.Keep;
import androidx.room.TypeConverter;

import com.czlucius.scan.Utils;
import com.czlucius.scan.objects.data.Data;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonDataException;
import com.squareup.moshi.Moshi;

import java.io.IOException;
import java.util.Date;
import java.util.Locale;

@Keep
public class Converters {
    private static final String TAG = "Converters";

    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }


    @TypeConverter
    public static int typeToInt(com.czlucius.scan.objects.Type type) {
        return type.getTypeInt();
    }

    @TypeConverter
    public static com.czlucius.scan.objects.Type intToType(int typeInt) {
        return com.czlucius.scan.objects.Type.getTypeFromCode(typeInt);
    }


    @TypeConverter
    public static String serializeData(Data data) {
        Moshi moshi = new Moshi.Builder().build();
        String serialized = "";
        // Since we need to know type, get type from object.
        Class cl = data.getClass();
        String type = data.getClass().getName();
        try {
            Class.forName(type, false, cl.getClassLoader());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw Utils.suppressExceptionRuntime(e); // Runtime exception as Room cannot deal with checked exceptions.
        }

        // Add the length of the type. The first 3 letters will always be the length of the type.
        String typeLength = String.format(Locale.getDefault(), "%03d", type.length());
        serialized += typeLength;

        // Append the object type to the string to help in deserialization.
        serialized += type;
        Log.e(TAG, type);
        JsonAdapter dataAdapter = moshi.adapter(cl);

        serialized += dataAdapter.toJson(data);

        return serialized;
    }


    @TypeConverter
    public static Data deserializeData(String str) {
        Moshi moshi = new Moshi.Builder().build();
        // Get the type length from 1st 3 characters.
        int length = Integer.parseInt(str.substring(0, 3));

        // Extract the type
        String className = str.substring(3, 3 + length);
        Class cl;
        try {
            cl = Class.forName(className, false, Barcode.UrlBookmark.class.getClassLoader());
        } catch (ClassNotFoundException exc) {
            cl = Object.class;
            exc.printStackTrace();
            Log.e(TAG, "deserializeContents: Class Not Found!", exc.getCause());
        }

        JsonAdapter dataAdapter = moshi.adapter(cl);
        Data data;
        try {
            data = (Data) dataAdapter.fromJson(str.substring(3 + length));

        } catch (IOException e) {
            throw new RuntimeException("JSON Serialization cannot be read: IOException occured.", e.getCause());
        } catch (JsonDataException e) {
            throw new RuntimeException("JSON is in invalid format.", e.getCause());
        }
        return data;
    }



}