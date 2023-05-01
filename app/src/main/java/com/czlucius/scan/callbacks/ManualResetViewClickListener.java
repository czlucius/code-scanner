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

package com.czlucius.scan.callbacks;

import android.view.View;

public abstract class ManualResetViewClickListener implements View.OnClickListener {
    private boolean enabled = true;

    @Override
    public final void onClick(View v) {
        // Manual enabled switch to prevent double clicks until reset
        if (!enabled) {
            return;
        }
        enabled = false;

        onSingleClick(v);
    }

    public abstract void onSingleClick(View v);

    public final void resetListener() {
        enabled = true;
    }

    public final Callback getResetCallback() {
        return this::resetListener;
    }
}
