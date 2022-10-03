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

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.czlucius.scan.objects.Type;
import com.czlucius.scan.objects.data.Data;

import java.util.Date;


// A class to store values of codes(QR/Barcode) scanned in the past, with all values publicly available to the Room persistence library.
// This is to prevent breaking encapsulation in the Code class, as Room needs fields to be public to read them.
// To create a CodeMemento object out of a Code object, use code.toHistoryElement(date)
// To convert a CodeMemento object to a Code object, use Code.fromHistoryElement
@Entity(tableName = "code_memento_table")
public class CodeMemento {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "data_type")
    private Type dataType;

    @ColumnInfo(name = "format")
    private int format;
//
//    @Embedded
//    private Contents contents;
    @ColumnInfo
    private Data data;

    @ColumnInfo(name = "date")
    private Date date;


    public CodeMemento(Type dataType, int format, Data data, Date date) {
        this.date = date;
        this.dataType = dataType;
        this.format = format;
        this.data = data;
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


    public Date getDate() {
        return date;
    }


    public void setDate(Date date) {
        this.date = date;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getDisplayData() {
        return data.getStringRepresentation();
    }
}
