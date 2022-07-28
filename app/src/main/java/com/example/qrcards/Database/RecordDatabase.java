package com.example.qrcards.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.qrcards.Record;

@Database(entities = Record.class,version =4,exportSchema = false)
public abstract class RecordDatabase extends RoomDatabase {
    private static RecordDatabase recordDatabase;
    private static String database_name="qr_app";

    public synchronized static RecordDatabase getInstance(Context context){
        if(recordDatabase==null){

            recordDatabase= Room.databaseBuilder(context.getApplicationContext(),
                    RecordDatabase.class,database_name)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();


        }
        return recordDatabase;
    }

    public abstract RecordDAO recordDAO();


}
