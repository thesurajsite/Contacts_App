package com.example.contactsapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.contactsapp.R;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {
    ArrayList<ContactModel> arrContact=new ArrayList<>();
    FloatingActionButton floatingActionButton;
    RecyclerContactAdapter adapter;
    RecyclerView recyclerview;
    ImageView profileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DatabaseHelper databaseHelper=DatabaseHelper.getDB(this);
        ArrayList<Contacts> arrContacts =(ArrayList<Contacts>) databaseHelper.contactsDao().getAllContacts();

        recyclerview=findViewById(R.id.recyclercontact);
        if(recyclerview==null) Log.w("error111", "true");
        adapter=new RecyclerContactAdapter(this, arrContact, arrContacts, databaseHelper);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.setAdapter(adapter);

        Bitmap bitmapImage=getImage();

        for (Contacts contact : arrContacts) {
            arrContact.add(new ContactModel(bitmapImage,contact.getId(), contact.getName(), contact.getNumber(), contact.getInstagram(), contact.getX(), contact.getLinkedin()));
        }

        arrContactSorting();
        adapter.notifyDataSetChanged();


        floatingActionButton=findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Toast.makeText(MainActivity.this, "Clicked On add", Toast.LENGTH_SHORT).show();
                Dialog dialog=new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.add_update_layout);

                profileImage=dialog.findViewById(R.id.profileImage); //SET AS A CLASS VARIABLE
                EditText addName=dialog.findViewById(R.id.addName);
                EditText addNumber=dialog.findViewById(R.id.addNumber);
                EditText addInstagram=dialog.findViewById(R.id.addInstagram);
                EditText addX=dialog.findViewById(R.id.addX);
                EditText addLinkedin=dialog.findViewById(R.id.addLinkedin);

                Button saveButton=dialog.findViewById(R.id.saveButton);
                ImageView deleteButton=dialog.findViewById(R.id.deleteButton);

                profileImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        imagePicker();

                    }
                });

                saveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String name="",number="",instagram="",x="",linkedin="";

                        //Get ContactID from SharedPreferences
                        long contactID = getContactID();

                        name = addName.getText().toString();
                        number = addNumber.getText().toString();
                        instagram=addInstagram.getText().toString();
                        x=addX.getText().toString();
                        linkedin=addLinkedin.getText().toString();

                        if(!name.isEmpty() || !number.isEmpty()) {

                            if(name.isEmpty()){
                                name=number;
                            }

                            arrContact.add(new ContactModel(bitmapImage,contactID,name, number,instagram,x,linkedin));
                            adapter.notifyItemInserted(arrContact.size()-1);
//                            recyclerview.scrollToPosition(arrContact.size()-1);

                            // Add the contact to the database
                            databaseHelper.contactsDao().addCon(new Contacts(contactID,name, number, instagram,x,linkedin));

                            //UPDATE SharedPreferences ContactID
                            long updatedID=contactID+1;
                            updateContactID(updatedID);

                            //ARRAY SORTING
                            arrContactSorting();

                            //SORTING THE ARRAY AGAIN
                            arrContactSorting();
                            adapter.notifyDataSetChanged();

                        }
                        else{
                            Toast.makeText(MainActivity.this, "Please Enter Name", Toast.LENGTH_SHORT).show();
                        }

                        dialog.dismiss();
                    }
                });

                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this, "Task Cancelled", Toast.LENGTH_SHORT).show();
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

    private long getContactID() {
        SharedPreferences pref=getSharedPreferences("CONTACTS",MODE_PRIVATE);
        long contactID=pref.getLong("contactId",1);
        return contactID;
    }

    private void updateContactID(long updatedID){
        SharedPreferences pref=getSharedPreferences("CONTACTS",MODE_PRIVATE);
        SharedPreferences.Editor editor=pref.edit();
        editor.putLong("contactId",updatedID);
        editor.apply();
    }

    private void arrContactSorting(){
        Collections.sort(arrContact, new Comparator<ContactModel>() {
            @Override
            public int compare(ContactModel o1, ContactModel o2) {
                return o1.name.compareToIgnoreCase(o2.name);
            }
        });

    }

    private void imagePicker(){
//        Toast.makeText(this, "image picker", Toast.LENGTH_SHORT).show();

        Intent iGallery=new Intent(Intent.ACTION_PICK);
        iGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(iGallery, 123);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==123 && resultCode == RESULT_OK && data != null ) {
//            profileImage.setImageURI(data.getData());
//            profileImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Uri imageUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                profileImage.setImageBitmap(bitmap);
                profileImage.setScaleType(ImageView.ScaleType.CENTER_CROP);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Bitmap getImage(){
        Drawable drawable = getResources().getDrawable(R.drawable.contact_image);
        Bitmap bitmapImage = ((BitmapDrawable) drawable).getBitmap();
        return bitmapImage;

    }
}