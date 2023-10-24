package com.example.contactsapp;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ContactsDao {
    @Query("select * from contacts")
    List<Contacts> getAllContacts();

    @Insert
    void addCon(Contacts contacts);

    @Update
    void updateCon(Contacts contacts);

    @Delete
    void deleteCon(Contacts contacts);






}

