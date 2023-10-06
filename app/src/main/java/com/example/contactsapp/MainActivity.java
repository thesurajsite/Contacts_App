package com.example.contactsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<ContactModel> arrContact=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerview=findViewById(R.id.recyclercontact);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));

        arrContact.add(new ContactModel(R.drawable.contactimage , "Suraj Verma", "7667484399"));
        arrContact.add(new ContactModel(R.drawable.contactimage,"Ravi Shankar", "9835168731"));
        arrContact.add(new ContactModel(R.drawable.contactimage, "Sunny Kumar","9142499693"));
        arrContact.add(new ContactModel(R.drawable.contactimage,"Harshit FOCUS", "9334844533"));
        arrContact.add(new ContactModel(R.drawable.contactimage , "Saurabh", "9835180230"));
        arrContact.add(new ContactModel(R.drawable.contactimage, "Anupam","6295809737"));
        arrContact.add(new ContactModel(R.drawable.contactimage , "Yash Alok", "6204976853"));
        arrContact.add(new ContactModel(R.drawable.contactimage,"Prince Mech NITA", "9508683892"));
        arrContact.add(new ContactModel(R.drawable.contactimage, "Dipanjan Sinha","8798707290"));
        arrContact.add(new ContactModel(R.drawable.contactimage , "Pratik Vats", "9508195677"));
        arrContact.add(new ContactModel(R.drawable.contactimage,"Yuvaraj Mech", "8132989628"));
        arrContact.add(new ContactModel(R.drawable.contactimage, "Rajdeep Malakar","6009177781"));
        arrContact.add(new ContactModel(R.drawable.contactimage , "Vedant Vaidya", "7843055348"));
        arrContact.add(new ContactModel(R.drawable.contactimage,"Ankesh IIIT CSE", "7849968913"));
        arrContact.add(new ContactModel(R.drawable.contactimage, "Dheeraj Civil","9829393515"));

        RecyclerContactAdapter adapter=new RecyclerContactAdapter(this, arrContact);
        recyclerview.setAdapter(adapter);

    }
}