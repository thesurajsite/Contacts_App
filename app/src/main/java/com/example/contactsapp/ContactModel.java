package com.example.contactsapp;

public class ContactModel {
    int img,id;
    String name, number, instagram;

    public ContactModel(int img,int id, String name, String number, String instagram) {
        this.img = img;
        this.id=id;
        this.name = name;
        this.number = number;
        this.instagram=instagram;

    }

    public ContactModel(String name, String number, String instagram) {
        this.name = name;
        this.number = number;
        this.instagram=instagram;



    }
}
