package com.example.contactsapp;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;


import static androidx.core.app.ActivityCompat.startActivityForResult;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
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

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class RecyclerContactAdapter extends RecyclerView.Adapter<RecyclerContactAdapter.ViewHolder> {
    Context context;
    ArrayList<ContactModel> arrContact;
    ArrayList<Contacts> arrContacts;
    DatabaseHelper databaseHelper;
    ContactsDao contactsDao;
    Bitmap bitmap;
    private ActivityResultLauncher<Intent> imagePickerLauncher;



    //*********** CHANGES HERE *************
    RecyclerContactAdapter(Context context, ArrayList<ContactModel> arrContact, ArrayList<Contacts> arrContacts, DatabaseHelper databaseHelper) {
        this.context = context;
        this.arrContact = arrContact;
        this.arrContacts = arrContacts;
        this.databaseHelper = databaseHelper;
        contactsDao = databaseHelper.contactsDao();
        Log.w("crash-contacts", contactsDao.getAllContacts().size() + "");
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.contact_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }


    //*********** CHANGES HERE *************
    @Override
    @SuppressWarnings("all")  //<<<<< here
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.imgContact.setImageBitmap(arrContact.get(position).img);
        holder.txtName.setText(arrContact.get(position).name);
        holder.txtNumber.setText(arrContact.get(position).number);
        holder.imgContact.setScaleType(ImageView.ScaleType.CENTER_CROP);


        final int currentPosition = position; // Create a final variable

        int[] arr = new int[1];
        arr[0] = 0;

        iconVisibilityControls(holder, position);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (arr[0] == 0) {
                    arr[0] = 1;
                    holder.linear.setVisibility(View.VISIBLE);

                } else {
                    arr[0] = 0;
                    holder.linear.setVisibility(View.GONE);
                }

            }
        });


        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.add_update_layout);

                Bitmap bitmapImage = getImage();

                TextView addContactTitle = dialog.findViewById(R.id.addContactTitle);
                EditText addName = dialog.findViewById(R.id.addName);
                EditText addNumber = dialog.findViewById(R.id.addNumber);
                EditText addInstagram = dialog.findViewById(R.id.addInstagram);
                EditText addX = dialog.findViewById(R.id.addX);
                EditText addLinkedin = dialog.findViewById(R.id.addLinkedin);

                Button saveButton = dialog.findViewById(R.id.saveButton);
                ImageView deleteButton = dialog.findViewById(R.id.deleteButton);
                ImageView profileImage = dialog.findViewById(R.id.profileImage);

                addContactTitle.setText("Update contact");
                addName.setText(arrContact.get(position).name);
                addNumber.setText(arrContact.get(position).number);
                addInstagram.setText(arrContact.get(position).instagram);
                addX.setText(arrContact.get(position).x);
                addLinkedin.setText(arrContact.get(position).linkedin);
                long contactID = arrContact.get(position).id;
                profileImage.setImageBitmap(arrContact.get(position).img);
                profileImage.setScaleType(ImageView.ScaleType.CENTER_CROP);

                profileImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        openImagePicker();
                       // Bitmap bitmapImage123=RecyclerContactAdapter.this.bitmap;
                    }
                });

                saveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        String name = "", number = "", instagram = "", x = "", linkedin = "";

                        name = addName.getText().toString();
                        number = addNumber.getText().toString();
                        instagram = addInstagram.getText().toString();
                        x = addX.getText().toString();
                        linkedin = addLinkedin.getText().toString();

                        //BITMAP TO BYTEARRAY
                        byte[] byteArrayImage = bitmapToByteArray(bitmapImage);

                        if (!name.isEmpty() || !number.isEmpty()) {

                            if (name.isEmpty()) {
                                name = number;
                            }

                            arrContact.set(position, new ContactModel(bitmapImage, contactID, name, number, instagram, x, linkedin));
                            notifyItemChanged(position);

                            contactsDao.updateCon(new Contacts(byteArrayImage, contactID, name, number, instagram, x, linkedin));

                            //SORTING THE ARRAY AGAIN
                            arrContactSorting();
                            notifyDataSetChanged();

                            dialog.dismiss();
                        } else {
                            Toast.makeText(context, "Please Enter Name or Number", Toast.LENGTH_SHORT).show();
                        }


                    }
                });

                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                                .setTitle("Delete Contact")
                                .setIcon(R.drawable.baseline_delete_24)
                                .setMessage("Do you want to Delete this contact ?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        try {
                                            contactsDao.deleteCon(arrContact.get(position).id);
                                            arrContact.remove(position);
                                            notifyItemRemoved(position);

                                            //SORTING THE ARRAY AGAIN
                                            arrContactSorting();
                                            notifyDataSetChanged();


                                            dialog.dismiss();
                                        } catch (Exception e) {
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
                if (arrContact.get(position).number != "") {
                    Toast.makeText(context, "Calling " + arrContact.get(position).name, Toast.LENGTH_SHORT).show();
                    Intent callintent = new Intent(Intent.ACTION_DIAL);
                    callintent.setData(Uri.parse("tel:" + arrContact.get(position).number));
                    context.startActivity(callintent);
                }
            }
        });

        holder.whatsappButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userNumber_with_spaces = arrContact.get(position).number;  // The Phone Number May have spaces
                String userNumber_without_spaces = ""; // To store Number Without Spaces
                for (int i = 0; i < userNumber_with_spaces.length(); i++)  // To Remove spaces and + from the phone number
                {
                    if (userNumber_with_spaces.charAt(i) != (' ') && userNumber_with_spaces.charAt(i) != ('+')) {
                        userNumber_without_spaces = userNumber_without_spaces + userNumber_with_spaces.charAt(i); // to Store number without spaces and + sign

                    }
                }
//                Toast.makeText(context, userNumber_without_spaces, Toast.LENGTH_SHORT).show();

                int length = userNumber_without_spaces.length(); // length of filtered phone number
                if (length >= 10) {
                    String whatsappNumber = "91" + userNumber_without_spaces.substring(length - 10, length);
                    if (whatsappNumber.length() == 12) {
                        // concatinating country code 91
                        Toast.makeText(context, "Opening Whatsapp ", Toast.LENGTH_SHORT).show();
                        Intent whatsappintent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=" + whatsappNumber));
                        context.startActivity(whatsappintent);
                    } else {
                        Toast.makeText(context, "Invalid WhatsApp Number", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Invalid WhatsApp Number", Toast.LENGTH_SHORT).show();
                }
            }

//
        });

        holder.instagramButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String instaID = arrContact.get(position).instagram;
                if (instaID.length() > 0) {
                    if (instaID.charAt(0) == '@') {
                        instaID = instaID.substring(1, instaID.length());
                    }

                    Toast.makeText(context, "Opening " + instaID, Toast.LENGTH_SHORT).show();

                    Uri uri = Uri.parse("https://www.instagram.com/" + instaID);
                    Intent webInstagram = new Intent(Intent.ACTION_VIEW, uri);

                    Intent appInstagram = new Intent(Intent.ACTION_VIEW, uri);
                    appInstagram.setPackage("com.instagram.android");

                    try {
                        context.startActivity(appInstagram);
                    } catch (ActivityNotFoundException e) {
                        context.startActivity(webInstagram);
                    }


                } else {
                    Toast.makeText(context, "Invalid Instagram ID", Toast.LENGTH_SHORT).show();
                }


            }
        });

        holder.xButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String xID = arrContact.get(position).x;
                if (xID.length() > 0) {
                    if (xID.charAt(0) == '@') {
                        xID = xID.substring(1, xID.length());
                    }

                    Toast.makeText(context, "Opening " + xID, Toast.LENGTH_SHORT).show();

                    Uri uri = Uri.parse("https://www.twitter.com/" + xID);
                    Intent webX = new Intent(Intent.ACTION_VIEW, uri);

                    Intent appX = new Intent(Intent.ACTION_VIEW, uri);
                    appX.setPackage("com.twitter.android");

                    try {
                        context.startActivity(appX);
                    } catch (ActivityNotFoundException e) {
                        context.startActivity(webX);
                    }

                } else {
                    Toast.makeText(context, "Invalid X ID", Toast.LENGTH_SHORT).show();

                }


            }
        });

        holder.linkedinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String linkedinID = arrContact.get(position).linkedin;
                if (linkedinID.length() > 0) {
                    if (linkedinID.charAt(0) == '@') {
                        linkedinID = linkedinID + linkedinID.substring(1, linkedinID.length());
                    }

                    Toast.makeText(context, "Opening " + linkedinID, Toast.LENGTH_SHORT).show();

                    Uri uri = Uri.parse("https://www.linkedin.com/in/" + linkedinID);
                    Intent webLinkedin = new Intent(Intent.ACTION_VIEW, uri);

                    Intent appLinkedin = new Intent(Intent.ACTION_VIEW, uri);
                    appLinkedin.setPackage("com.linkedin.android.home");


                    try {
                        context.startActivity(appLinkedin);

                    } catch (ActivityNotFoundException e) {
                        context.startActivity(webLinkedin);
                    }


                } else {

                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrContact.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtNumber;
        ImageView imgContact;
        ImageView editButton, callButton, whatsappButton, instagramButton, xButton, linkedinButton, profileImage;
        //        RelativeLayout relativeLayout;
        CardView cardView;
        LinearLayout linear;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtNumber = itemView.findViewById(R.id.txtNumber);
            imgContact = itemView.findViewById(R.id.imgcontact);
            editButton = itemView.findViewById(R.id.editButton);
            callButton = itemView.findViewById(R.id.callButton);
            whatsappButton = itemView.findViewById(R.id.whatsappButton);
            instagramButton = itemView.findViewById(R.id.instagramButton);
            xButton = itemView.findViewById(R.id.XButton);
            linkedinButton = itemView.findViewById(R.id.linkedinButton);

            cardView = itemView.findViewById(R.id.cardView);
            linear = itemView.findViewById(R.id.linear);

        }
    }

    private void iconVisibilityControls(ViewHolder holder, int position) {

        // CALL AND WHATSAPP VISIBILITY CONTROLS
        if (!arrContact.get(position).number.isEmpty()) {
            holder.callButton.setVisibility(View.VISIBLE);
            holder.whatsappButton.setVisibility(View.VISIBLE);
        } else {
            holder.callButton.setVisibility(View.GONE);
            holder.whatsappButton.setVisibility(View.GONE);
        }

        // INSTAGRAM VISIBILITY CONTROLS
        if (!arrContact.get(position).instagram.isEmpty()) {
            holder.instagramButton.setVisibility(View.VISIBLE);
        } else {
            holder.instagramButton.setVisibility(View.GONE);
        }

        // X VISIBILITY CONTROLS
        if (!arrContact.get(position).x.isEmpty()) {
            holder.xButton.setVisibility(View.VISIBLE);
        } else {
            holder.xButton.setVisibility(View.GONE);
        }

        // LINKEDIN VISIBILITY CONTROLS
        if (!arrContact.get(position).linkedin.isEmpty()) {
            holder.linkedinButton.setVisibility(View.VISIBLE);
        } else {
            holder.linkedinButton.setVisibility(View.GONE);
        }
    }

    private void arrContactSorting() {
        Collections.sort(arrContact, new Comparator<ContactModel>() {
            @Override
            public int compare(ContactModel o1, ContactModel o2) {
                return o1.name.compareToIgnoreCase(o2.name);
            }
        });

    }

    private Bitmap getImage() {
        Drawable drawable = context.getResources().getDrawable(R.drawable.contact_image);
        Bitmap bitmapImage = ((BitmapDrawable) drawable).getBitmap();
        return bitmapImage;

    }

    private byte[] bitmapToByteArray(Bitmap bitmapImage) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

//    private void openImagePicker() {
//        Intent iGallery = new Intent(Intent.ACTION_PICK);
//        iGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        ((Activity) context).startActivityForResult(iGallery, 456);
//    }



//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        if (requestCode == 456 && resultCode == RESULT_OK && data != null) {
//            // Get the image URI from the data Intent
//            Uri imageUri = data.getData();
//
//            // Convert URI to bitmap if needed
//            bitmap = null;
//            try {
//                bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageUri);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        }
//    }




    private Bitmap byteArrayToBitmap(byte[] byteArray) {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }
}
