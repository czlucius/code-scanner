package com.czlucius.scan.objects;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;


// A class to store values of codes(QR/Barcode) scanned in the past, with all values publicly available to the Room persistence library.
// This is to prevent breaking encapsulation in the Code class, as Room needs fields to be public to read them.
// To create a HistoryCode object out of a Code object, use code.toHistoryElement(date)
// To convert a HistoryCode object to a Code object, use Code.fromHistoryElement
@Entity(tableName = "history_code_table")
public class HistoryCode {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "data_type")
    private Type dataType;

    @ColumnInfo(name = "format")
    private int format;

    @ColumnInfo(name = "contents")
    private Contents contents;

    @ColumnInfo(name = "date")
    private Date date;


    public HistoryCode(Type dataType, int format, Contents contents, Date date) {
        this.date = date;
        this.dataType = dataType;
        this.format = format;
        this.contents = contents;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Type getDataType() {
        return dataType;
    }

    public void setDataType(Type dataType) {
        this.dataType = dataType;
    }

    public int getFormat() {
        return format;
    }

    public void setFormat(int format) {
        this.format = format;
    }

    public Contents getContents() {
        return contents;
    }

    public void setContents(Contents contents) {
        this.contents = contents;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
