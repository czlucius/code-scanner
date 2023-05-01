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

