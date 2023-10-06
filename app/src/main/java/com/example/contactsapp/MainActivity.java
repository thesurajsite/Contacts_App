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
        arrContact.add(new ContactModel(R.drawable.contactimage , "Suraj Verma", "7667484399"));
        arrContact.add(new ContactModel(R.drawable.contactimage,"Ravi Shankar", "9835168731"));
        arrContact.add(new ContactModel(R.drawable.contactimage, "Sunny Kumar","9142499693"));
        arrContact.add(new ContactModel(R.drawable.contactimage , "Suraj Verma", "7667484399"));
        arrContact.add(new ContactModel(R.drawable.contactimage,"Ravi Shankar", "9835168731"));
        arrContact.add(new ContactModel(R.drawable.contactimage, "Sunny Kumar","9142499693"));
        arrContact.add(new ContactModel(R.drawable.contactimage , "Suraj Verma", "7667484399"));
        arrContact.add(new ContactModel(R.drawable.contactimage,"Ravi Shankar", "9835168731"));
        arrContact.add(new ContactModel(R.drawable.contactimage, "Sunny Kumar","9142499693"));
        arrContact.add(new ContactModel(R.drawable.contactimage , "Suraj Verma", "7667484399"));
        arrContact.add(new ContactModel(R.drawable.contactimage,"Ravi Shankar", "9835168731"));
        arrContact.add(new ContactModel(R.drawable.contactimage, "Sunny Kumar","9142499693"));

        RecyclerContactAdapter adapter=new RecyclerContactAdapter(this, arrContact);
        recyclerview.setAdapter(adapter);

    }
}