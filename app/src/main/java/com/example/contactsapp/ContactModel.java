package com.example.contactsapp;

public class ContactModel {
    int img;
    long id;
    String name, number, instagram, linkedin, x;

    public ContactModel(int img, long id, String name, String number, String instagram, String linkedin, String x) {
        this.img = img;
        this.id = id;
        this.name = name;
        this.number = number;
        this.instagram = instagram;
        this.linkedin = linkedin;
        this.x = x;
    }

    public ContactModel(String name, String number, String instagram, String linkedin, String x) {
        this.name = name;
        this.number = number;
        this.instagram = instagram;
        this.linkedin = linkedin;
        this.x = x;
    }
}
