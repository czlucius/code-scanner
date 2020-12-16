package com.czlucius.scan.objects.actions;

import android.content.Context;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;

import com.czlucius.scan.objects.data.Data;

import java.util.Objects;

public abstract class Action {
    private final String mActionText;

    @DrawableRes
    @Nullable
    private final Integer mActionIcon;

    public abstract void performAction(Context context, Data data);


    public Action(String actionText, @DrawableRes @Nullable Integer icon) {
        mActionText = actionText;
        mActionIcon = icon;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Action actionObj = (Action) o;
        return Objects.equals(mActionIcon, actionObj.mActionIcon) &&
                mActionText.equals(actionObj.mActionText);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mActionText, mActionIcon);
    }

    public Integer getActionIcon() {
        return mActionIcon;
    }

    public String getActionText() {
        return mActionText;
    }



}

