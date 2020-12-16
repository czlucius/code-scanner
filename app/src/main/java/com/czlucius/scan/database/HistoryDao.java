package com.czlucius.scan.database;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface HistoryDao {
    @Query("SELECT * FROM code_memento_table")
    List<CodeMemento> getAll();



    @Insert
    void add(CodeMemento code);

    @Query("DELETE FROM code_memento_table")
    void clearAll();

    @Query("SELECT * FROM code_memento_table where id==:entryId")
    CodeMemento get(int entryId);


}
