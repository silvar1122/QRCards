package com.example.qrcards.Database;

import android.content.Intent;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.qrcards.Record;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface RecordDAO {

    @Insert
    void insert(Record record);

    @Query("SELECT * FROM Record_Table ORDER BY Date DESC")
    List<Record> getAll();

    @Delete
    void delete(Record record);

    @Query("DELETE FROM Record_Table")
    void deleteAll();

    @Query("SELECT COUNT(Name)  FROM Record_Table WHERE Name=:name AND state=:state")
    Integer get_arrivals(String name, String state);

    @Query("SELECT Name,id,qr_code_value,date,number,state FROM Record_Table WHERE Name=:name ORDER BY date DESC")
    List<Record> get_person_data(String name);


}
