package com.czlucius.scan.database;

import androidx.annotation.Keep;
import androidx.room.TypeConverter;

import com.czlucius.scan.objects.Contents;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Date;

@Keep
public class HistoryConverters {

    static Gson gson = new Gson();
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
    public static String serializeContents(Contents contents) {
        return gson.toJson(contents);
    }

    @TypeConverter
    public static Contents deserializeContents(String str) {
        Type contentsType = new TypeToken<Contents>() {}.getType();
        return gson.fromJson(str, contentsType);
    }
}