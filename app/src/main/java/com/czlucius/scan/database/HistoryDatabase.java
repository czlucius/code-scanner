package com.czlucius.scan.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.czlucius.scan.objects.Code;
import com.czlucius.scan.objects.HistoryCode;

import java.util.Date;


@Database(entities = {HistoryCode.class}, version = 1, exportSchema = false)
@TypeConverters({HistoryConverters.class})
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
        Date currentDateTime = new Date();
        HistoryCode codeHistoryElement = code.toHistoryElement(currentDateTime);
        new Thread(() -> dao.add(codeHistoryElement)).start();
    }
}
