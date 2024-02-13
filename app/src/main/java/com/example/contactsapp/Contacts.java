package com.example.contactsapp;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "contacts")
public class Contacts {

    @ColumnInfo(name = "image")
    private byte[] image;
    @PrimaryKey(autoGenerate = true)
    private long id;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "number")
    private String number;

    @ColumnInfo(name = "instagram")
    private String instagram;

    @ColumnInfo(name = "linkedin")
    private String linkedin;

    @ColumnInfo(name = "x")
    private String x;

    Contacts(byte[] image, long id, String name, String number, String instagram, String x, String linkedin){
        this.image = image;
        this.id = id;
        this.name = name;
        this.number = number;
        this.instagram = instagram;
        this.x = x;
        this.linkedin = linkedin;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public String getLinkedin() {
        return linkedin;
    }

    public void setLinkedin(String linkedin) {
        this.linkedin = linkedin;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }
}
