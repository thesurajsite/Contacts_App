package com.example.contactsapp;

public class ContactModel {
    int img;
    long id;
    String name, number, instagram, x, linkedin;

    public ContactModel(int img, long id, String name, String number, String instagram, String x, String linkedin) {
        this.img = img;
        this.id = id;
        this.name = name;
        this.number = number;
        this.instagram = instagram;
        this.x = x;
        this.linkedin = linkedin;
    }

    public ContactModel(String name, String number, String instagram, String x, String linkedin) {
        this.name = name;
        this.number = number;
        this.instagram = instagram;
        this.x = x;
        this.linkedin = linkedin;
    }
}
