package com.example.qrcards;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "Record_Table")
public class RecordModel implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String full_name;
    private String qr_value;
    private String date;


    public RecordModel(String full_name, String qr_value, String date) {
        this.full_name = full_name;
        this.qr_value = qr_value;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getQr_value() {
        return qr_value;
    }

    public void setQr_value(String qr_value) {
        this.qr_value = qr_value;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
