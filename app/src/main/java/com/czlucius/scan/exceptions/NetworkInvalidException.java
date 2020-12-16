package com.czlucius.scan.exceptions;

import android.annotation.SuppressLint;
import android.net.wifi.WifiManager;
import android.os.Build;

import androidx.annotation.IntDef;
import androidx.annotation.RequiresApi;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static android.net.wifi.WifiManager.STATUS_NETWORK_SUGGESTIONS_ERROR_ADD_DUPLICATE;
import static android.net.wifi.WifiManager.STATUS_NETWORK_SUGGESTIONS_ERROR_ADD_EXCEEDS_MAX_PER_APP;
import static android.net.wifi.WifiManager.STATUS_NETWORK_SUGGESTIONS_ERROR_ADD_INVALID;
import static android.net.wifi.WifiManager.STATUS_NETWORK_SUGGESTIONS_ERROR_ADD_NOT_ALLOWED;
import static android.net.wifi.WifiManager.STATUS_NETWORK_SUGGESTIONS_ERROR_APP_DISALLOWED;
import static android.net.wifi.WifiManager.STATUS_NETWORK_SUGGESTIONS_ERROR_INTERNAL;
import static android.net.wifi.WifiManager.STATUS_NETWORK_SUGGESTIONS_ERROR_REMOVE_INVALID;
import static android.net.wifi.WifiManager.STATUS_NETWORK_SUGGESTIONS_SUCCESS;

public class NetworkInvalidException extends Exception {
    @ErrorCode
    private final int type;

    public NetworkInvalidException(@ErrorCode int type) {
        super();
        this.type = type;

    }

    @ErrorCode
    public int getType() {
        return type;
    }


    @RequiresApi(Build.VERSION_CODES.Q)
    public static final int INTERNAL_ERR = 1, APP_DISALLOWED = 2, DUPLICATE = 3, EXCEED_MAX_LIMIT = 4, NETWORK_NOT_ALLOWED = 6, NETWORK_INVALID = 7;

    public static final int SIMPLE_ERROR = -1;


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


