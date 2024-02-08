package com.example.contactsapp;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
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

        int []arr=new int[1];
        arr[0]=0;

        //carryVelocity IS THE CARDVIEW
        iconVisibilityControls(holder, position);


        holder.carryVelocity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(arr[0]==0)
                {
                    arr[0]=1;
                    holder.linear.setVisibility(View.VISIBLE);

                }
                else{
                    arr[0]=0;
                    holder.linear.setVisibility(View.GONE);
                }

            }
        });



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
                ImageView deleteButton=dialog.findViewById(R.id.deleteButton);

                addContactTitle.setText("Update contact");
                addName.setText(arrContact.get(position).name);
                addNumber.setText(arrContact.get(position).number);
                addInstagram.setText(arrContact.get(position).instagram);
                long contactID = arrContact.get(position).id;
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
                            //NOTHING
                        }

                        arrContact.set(position, new ContactModel(R.drawable.contact_image,contactID,name,number,instagram,"linkedin","x"));
                        notifyItemChanged(position);

                        contactsDao.updateCon(new Contacts(contactID,name,number,instagram,"linkedIn","x"));

                        dialog.dismiss();

                    }
                });

                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        AlertDialog.Builder builder=new AlertDialog.Builder(context)
                                .setTitle("Delete Contact")
                                .setIcon(R.drawable.baseline_delete_24)
                                .setMessage("Do you want to Delete this contact ?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        try{
                                            contactsDao.deleteCon(arrContact.get(position).id);
                                            arrContact.remove(position);
                                            notifyItemRemoved(position);
                                            dialog.dismiss();
                                        }
                                        catch (Exception e){
                                            Log.w("crash-in-deleting", e);
                                        }
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






                dialog.show();
            }
        });



        holder.callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(arrContact.get(position).number!="") {
                    Toast.makeText(context, "Calling " + arrContact.get(position).name, Toast.LENGTH_SHORT).show();
                    Intent callintent = new Intent(Intent.ACTION_DIAL);
                    callintent.setData(Uri.parse("tel:" + arrContact.get(position).number));
                    context.startActivity(callintent);
                }
            }
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
//                Toast.makeText(context, userNumber_without_spaces, Toast.LENGTH_SHORT).show();

                int length=userNumber_without_spaces.length(); // length of filtered phone number
                if(length>=10) {
                    String whatsappNumber = "91" + userNumber_without_spaces.substring(length - 10, length);
                    if (whatsappNumber.length() == 12) {
                        // concatinating country code 91
                        Toast.makeText(context, "Opening Whatsapp ", Toast.LENGTH_SHORT).show();
                        Intent whatsappintent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=" + whatsappNumber));
                        context.startActivity(whatsappintent);
                    } else {
                        Toast.makeText(context, "Invalid WhatsApp Number", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(context, "Invalid WhatsApp Number", Toast.LENGTH_SHORT).show();
                }
            }

//
        });

        holder.instagramButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String instaID=arrContact.get(position).instagram;
                if(instaID.length()>0) {
                    if (instaID.charAt(0) == '@') {
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
                else{
                    Toast.makeText(context, "Invalid Instagram ID", Toast.LENGTH_SHORT).show();
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
        ImageView editButton,callButton, whatsappbutton, instagramButton;
//        RelativeLayout relativeLayout;
        CardView carryVelocity;
        LinearLayout linear;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName=itemView.findViewById(R.id.txtName);
            txtNumber=itemView.findViewById(R.id.txtNumber);
            imgContact=itemView.findViewById(R.id.imgcontact);
            editButton=itemView.findViewById(R.id.editButton);
            callButton=itemView.findViewById(R.id.callButton);
            whatsappbutton=itemView.findViewById(R.id.whatsappButton);
            instagramButton=itemView.findViewById(R.id.instagramButton);
            carryVelocity= itemView.findViewById(R.id.carryVelocity);
            linear=itemView.findViewById(R.id.linear);

        }
    }

    private void iconVisibilityControls(ViewHolder holder, int position){

        // INSTAGRAM VISIBILITY CONTROLS
        if(arrContact.get(position).instagram.isEmpty())
        {
            holder.instagramButton.setVisibility(View.GONE);
        }

        if(arrContact.get(position).number.isEmpty())
        {
            holder.callButton.setVisibility(View.GONE);
            holder.whatsappbutton.setVisibility(View.GONE);
        }

    }


}