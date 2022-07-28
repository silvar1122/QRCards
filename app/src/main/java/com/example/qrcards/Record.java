package com.example.qrcards;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;


@Entity(tableName = "Record_Table")
public class Record implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String Name;

    private String qr_code_value;

    private String date;

    private int number;

    private String state;



    public Record() {
    }

    public Record(String name, String qr_code_value, String date, int number, String state) {
        Name = name;
        this.qr_code_value = qr_code_value;
        this.date = date;
        this.number = number;
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getQr_code_value() {
        return qr_code_value;
    }

    public void setQr_code_value(String qr_code_value) {
        this.qr_code_value = qr_code_value;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
