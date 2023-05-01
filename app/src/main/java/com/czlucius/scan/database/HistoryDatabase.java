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

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.czlucius.scan.App;
import com.czlucius.scan.objects.Code;


@Database(entities = {CodeMemento.class}, version = 4, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class HistoryDatabase extends RoomDatabase {


    private static HistoryDatabase INSTANCE;

    public abstract HistoryDao historyDao();

    public static synchronized HistoryDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), HistoryDatabase.class, "code")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }

    public static void insertCode(Context context, Code code) {
        // Open Database and insert item
        HistoryDao dao = HistoryDatabase.getInstance(context).historyDao();
        CodeMemento codeHistoryElement = code.toMemento();
        App.globalExService.submit(() -> dao.add(codeHistoryElement));
    }
}
