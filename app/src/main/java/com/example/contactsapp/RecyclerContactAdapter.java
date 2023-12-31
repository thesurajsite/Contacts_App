package com.example.contactsapp;

import static androidx.core.content.ContextCompat.startActivity;

import static kotlinx.coroutines.CoroutineScopeKt.CoroutineScope;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.sax.StartElementListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;




public class RecyclerContactAdapter extends RecyclerView.Adapter<RecyclerContactAdapter.ViewHolder> {
    Context context;
    ArrayList<ContactModel> arrContact;
    ArrayList<Contacts> arrContacts;
    DatabaseHelper databaseHelper;
    ContactsDao contactsDao;



    //*********** CHANGES HERE *************
    RecyclerContactAdapter(Context context, ArrayList<ContactModel> arrContact, ArrayList<Contacts> arrContacts, DatabaseHelper databaseHelper){
        this.context=context;
        this.arrContact=arrContact;
        this.arrContacts = arrContacts;
        this.databaseHelper = databaseHelper;
        contactsDao = databaseHelper.contactsDao();
        Log.w("crash-contacts", contactsDao.getAllContacts().size()+"");
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.contact_row, parent, false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }



    //*********** CHANGES HERE *************
    @Override
    @SuppressWarnings("all")  //<<<<< here
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.imgContact.setImageResource(arrContact.get(position).img);
        holder.txtName.setText(arrContact.get(position).name);
        holder.txtNumber.setText(arrContact.get(position).number);

        final int currentPosition = position; // Create a final variable



        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog=new Dialog(context);
                dialog.setContentView(R.layout.add_update_layout);

                TextView addContactTitle=dialog.findViewById(R.id.addContactTitle);
                EditText addName=dialog.findViewById(R.id.addName);
                EditText addNumber=dialog.findViewById(R.id.addNumber);
                EditText addInstagram=dialog.findViewById(R.id.addInstagram);
                Button saveButton=dialog.findViewById(R.id.saveButton);

                addContactTitle.setText("Update contact");
                addName.setText(arrContact.get(position).name);
                addNumber.setText(arrContact.get(position).number);
                addInstagram.setText(arrContact.get(position).instagram);
                saveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String name="",number="",instagram="";
                        if(!addName.getText().toString().equals("")) {
                            name = addName.getText().toString();
                        }
                        else{
                            Toast.makeText(context, "Please Enter Name", Toast.LENGTH_SHORT).show();
                        }
                        if(!addNumber.getText().toString().equals("")){
                            number = addNumber.getText().toString();
                        }
                        else{
                            Toast.makeText(context, "Please Enter Number", Toast.LENGTH_SHORT).show();
                        }
                        if(!addInstagram.getText().toString().equals("")){
                            instagram = addInstagram.getText().toString();
                        }
                        else{
                            Toast.makeText(context, "Please Enter Instagram ID", Toast.LENGTH_SHORT).show();
                        }

                        arrContact.set(position, new ContactModel(R.drawable.contactimage,name,number,instagram));
                        notifyItemChanged(position);

                        Contacts contactt = arrContacts.get(currentPosition);
                        contactt.setName(name);
                        contactt.setNumber(number);
                        contactt.setInstagram(instagram);
                        contactsDao.updateCon(contactt);

                        dialog.dismiss();

                    }
                });
                dialog.show();
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(context)
                        .setTitle("Delete Contact")
                        .setIcon(R.drawable.baseline_delete_24)
                        .setMessage("Do you want to Delete this contact ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                try{
                                    arrContact.remove(currentPosition);
                                    notifyItemRemoved(currentPosition);
                                }
                                catch (Exception e){
                                    Log.w("crash-contacts", e);
                                }

                                // Remove the contact from the Room database
                                Contacts contactt = arrContacts.get(currentPosition);
                                contactsDao.deleteCon(contactt);



                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                builder.show();
            }
        });

        holder.callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Calling "+arrContact.get(position).name, Toast.LENGTH_SHORT).show();
                Intent callintent=new Intent(Intent.ACTION_DIAL);
                callintent.setData(Uri.parse("tel:"+arrContact.get(position).number));
                context.startActivity(callintent);
            }

//
        });

        holder.whatsappbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userNumber_with_spaces=arrContact.get(position).number;  // The Phone Number May have spaces
                String userNumber_without_spaces=""; // To store Number Without Spaces
                for(int i=0;i<userNumber_with_spaces.length();i++)  // To Remove spaces and + from the phone number
                {
                    if(userNumber_with_spaces.charAt(i)!=(' ') && userNumber_with_spaces.charAt(i)!=('+') )
                    {
                        userNumber_without_spaces = userNumber_without_spaces + userNumber_with_spaces.charAt(i); // to Store number without spaces and + sign

                    }
                }

                int length=userNumber_without_spaces.length(); // length of filtered phone number
                String whatsappNumber = "91" + userNumber_without_spaces.substring(length - 10, length);
                if(whatsappNumber.length()==12) {
                     // concatinating country code 91
                    Toast.makeText(context, "Opening Whatsapp ", Toast.LENGTH_SHORT).show();
                    Intent whatsappintent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=" + whatsappNumber));
                    context.startActivity(whatsappintent);
                }
                else{
                    Toast.makeText(context, "Invalid WhatsApp Number", Toast.LENGTH_SHORT).show();
                }
            }

//
        });

        holder.instagramButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String instaID=arrContact.get(position).instagram;
                if(instaID.charAt(0)=='@') {
                    instaID = instaID.substring(1, instaID.length());

                }

                Toast.makeText(context, "Opening "+instaID, Toast.LENGTH_SHORT).show();

                Uri uri=Uri.parse("https://www.instagram.com/"+instaID);
                Intent instagram=new Intent(Intent.ACTION_VIEW, uri);
                Intent webInstagram=new Intent(Intent.ACTION_VIEW, uri);

                instagram.setPackage("com.instagram.android");

                try{
                    context.startActivity(instagram);
                }
                catch (ActivityNotFoundException e){
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/"+instaID)));

                }
            }
        });



    }

    @Override
    public int getItemCount() {
        return arrContact.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName,txtNumber;
        ImageView imgContact;
        ImageView editButton, deleteButton, callButton, whatsappbutton, instagramButton;
        // DatabaseHelper databaseHelper=DatabaseHelper.getDB( context);



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName=itemView.findViewById(R.id.txtName);
            txtNumber=itemView.findViewById(R.id.txtNumber);
            imgContact=itemView.findViewById(R.id.imgcontact);
            editButton=itemView.findViewById(R.id.editButton);
            deleteButton=itemView.findViewById(R.id.deleteButton);
            callButton=itemView.findViewById(R.id.callButton);
            whatsappbutton=itemView.findViewById(R.id.whatsappButton);
            instagramButton=itemView.findViewById(R.id.instagramButton);



        }
    }
}