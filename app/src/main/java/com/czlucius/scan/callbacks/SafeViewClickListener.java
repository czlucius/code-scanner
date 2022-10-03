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

import android.os.SystemClock;
import android.view.View;

/**
 * A View.OnClickListener replacement that is safe from double clicks.
 */
public abstract class SafeViewClickListener implements View.OnClickListener{
    private long lastTimeClicked = SystemClock.elapsedRealtime();
    private long interval;

    public SafeViewClickListener(long interval) {
        this.interval = interval;
    }



    @Override
    public void onClick(View v) {
        // Set a threshold of 1000ms to prevent double clicks of buttons
        if ((SystemClock.elapsedRealtime() - lastTimeClicked ) < interval) {
            return;
        }
        lastTimeClicked = SystemClock.elapsedRealtime();
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }
}
