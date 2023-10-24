package com.example.contactsapp;

import android.content.Context;
import android.provider.ContactsContract;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities=Contacts.class , exportSchema = true, version = 1)
public abstract class DatabaseHelper extends RoomDatabase{
    public static final String DB_NAME="contactsdb";
    public static DatabaseHelper instance;

    public static synchronized DatabaseHelper getDB(Context context) {
        if (instance == null){
            instance= Room.databaseBuilder(context , DatabaseHelper.class, DB_NAME)
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

    public abstract ContactsDao contactsDao();
}
