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

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.arch.core.util.Function;

import com.czlucius.scan.objects.data.Data;
import com.czlucius.scan.objects.data.Phone;

public abstract class PhoneAction extends Action {
    private final Function<Data, Phone> phoneRetrievalMethod;
    public PhoneAction(String actionText, @DrawableRes @Nullable Integer icon, Function<Data, Phone> phoneRetrievalMethod) {
        super(actionText, icon);
        this.phoneRetrievalMethod = phoneRetrievalMethod;
    }

    protected Phone getPhone(Data data) {
        return phoneRetrievalMethod.apply(data);
    }
}
