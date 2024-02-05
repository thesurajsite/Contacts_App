package com.example.contactsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<ContactModel> arrContact=new ArrayList<>();
    FloatingActionButton floatingActionButton;
    RecyclerContactAdapter adapter;
    RecyclerView recyclerview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DatabaseHelper databaseHelper=DatabaseHelper.getDB(this);
        ContactsDao contactsDao = databaseHelper.contactsDao();
        ArrayList<Contacts> arrContacts =(ArrayList<Contacts>) databaseHelper.contactsDao().getAllContacts();

        recyclerview=findViewById(R.id.recyclercontact);
        adapter=new RecyclerContactAdapter(this, arrContact, arrContacts, databaseHelper);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.setAdapter(adapter);


        for (Contacts contact : arrContacts) {
            arrContact.add(new ContactModel(R.drawable.contactimage, contact.getName(), contact.getNumber(), contact.getInstagram()));
        }
        adapter.notifyDataSetChanged();


        floatingActionButton=findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Toast.makeText(MainActivity.this, "Clicked On add", Toast.LENGTH_SHORT).show();
                Dialog dialog=new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.add_update_layout);

                EditText addName=dialog.findViewById(R.id.addName);
                EditText addNumber=dialog.findViewById(R.id.addNumber);
                EditText addInstagram=dialog.findViewById(R.id.addInstagram);

                Button saveButton=dialog.findViewById(R.id.saveButton);

                saveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String name="",number="",instagram="";
                        if(!addName.getText().toString().equals("") || !addNumber.getText().toString().equals("")) {

                            name = addName.getText().toString();
                            number = addNumber.getText().toString();
                            instagram=addInstagram.getText().toString();

                            arrContact.add(new ContactModel(R.drawable.contactimage,name, number,instagram));
                            adapter.notifyItemInserted(arrContact.size()-1);
                            recyclerview.scrollToPosition(arrContact.size()-1);

                            // Add the contact to the database
                            databaseHelper.contactsDao().addCon(new Contacts(name, number, instagram));
                        }
                        else{
                            Toast.makeText(MainActivity.this, "Please Enter Name", Toast.LENGTH_SHORT).show();
                        }

                        dialog.dismiss();
                    }
                });
                dialog.show();

            }
        });











//        arrContact.add(new ContactModel(R.drawable.contactimage , "Suraj Verma", "7667484399"));
//        arrContact.add(new ContactModel(R.drawable.contactimage,"Ravi Shankar", "9835168731"));
//        arrContact.add(new ContactModel(R.drawable.contactimage, "Sunny Kumar","9142499693"));
//        arrContact.add(new ContactModel(R.drawable.contactimage,"Harshit FOCUS", "9334844533"));
//        arrContact.add(new ContactModel(R.drawable.contactimage , "Saurabh", "9835180230"));
//        arrContact.add(new ContactModel(R.drawable.contactimage, "Anupam","6295809737"));
//        arrContact.add(new ContactModel(R.drawable.contactimage , "Yash Alok", "6204976853"));
//        arrContact.add(new ContactModel(R.drawable.contactimage,"Prince Mech NITA", "9508683892"));
//        arrContact.add(new ContactModel(R.drawable.contactimage, "Dipanjan Sinha","8798707290"));
//        arrContact.add(new ContactModel(R.drawable.contactimage , "Pratik Vats", "9508195677"));
//        arrContact.add(new ContactModel(R.drawable.contactimage,"Yuvaraj Mech", "8132989628"));
//        arrContact.add(new ContactModel(R.drawable.contactimage, "Rajdeep Malakar","6009177781"));
//        arrContact.add(new ContactModel(R.drawable.contactimage , "Vedant Vaidya", "7843055348"));
//        arrContact.add(new ContactModel(R.drawable.contactimage,"Ankesh IIIT CSE", "7849968913"));
//        arrContact.add(new ContactModel(R.drawable.contactimage, "Dheeraj Civil","9829393515"));



    }
}