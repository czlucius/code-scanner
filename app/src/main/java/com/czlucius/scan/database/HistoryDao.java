package com.czlucius.scan.database;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.czlucius.scan.objects.HistoryCode;

import java.util.List;

@Dao
public interface HistoryDao {
    @Query("SELECT * FROM history_code_table")
    List<HistoryCode> getAll();



    @Insert
    void add(HistoryCode code);

    @Query("DELETE FROM history_code_table")
    void clearAll();

    @Query("SELECT * FROM history_code_table where id==:entryId")
    HistoryCode get(int entryId);


}
