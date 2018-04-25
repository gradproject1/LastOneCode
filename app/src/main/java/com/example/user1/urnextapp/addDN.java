package com.example.user1.urnextapp;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static android.app.Activity.RESULT_OK;
import static com.estimote.coresdk.common.config.EstimoteSDK.getApplicationContext;
public class addDN extends Fragment {
   private Button selectDIY;
   private Button selectHealth;
   private Button mSelectFashion;
   private Button mSelectBook;
   private Button mSelectSport;
   private Button mSelectTravil;
   private Button mSelectTechnology;
   private Button mSelectHistory;
   private EditText article;
   private FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();
   private StorageReference mStorage;
   private static final int DIY_INTENT = 1;
   private static final int Health_INTENT = 2;
   private static final int FASHION_INTENT = 3;
   private static final int BOOK_INTENT = 4;
   private static final int Travil_INTENT = 5;
   private static final int Technology_INTENT = 6;
   private static final int History_INTENT = 7;
   private static final int Sport_INTENT = 8;
   private ProgressDialog mprogressDialog;
   private Button logout;
   // Root Database Name for Firebase Database.
   String Database_Path = "All_Image_Uploads_Database";
   // Creating URI.
   Uri FilePathUri;
   // Creating StorageReference and DatabaseReference object.
   private FirebaseDatabase database = FirebaseDatabase.getInstance();
   DatabaseReference databaseReference=database.getReference("entertainment");;
   // Image request code for onActivityResult() .
   int Image_Request_Code = 7;
   //Constructor default
   public addDN(){};
   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      View PageOne = inflater.inflate(R.layout.fragment_add_dn, container, false);
      mStorage = FirebaseStorage.getInstance().getReference();
      selectDIY = (Button) PageOne.findViewById(R.id.selectDIY);
      selectHealth = (Button) PageOne.findViewById(R.id.selectHealth);
      mSelectFashion = (Button) PageOne.findViewById(R.id.selectFashion);
      mSelectBook = (Button) PageOne.findViewById(R.id.selectBook);
      mSelectHistory = (Button) PageOne.findViewById(R.id.selectHistory);
      mSelectSport = (Button) PageOne.findViewById(R.id.selectSport);
      mSelectTechnology = (Button) PageOne.findViewById(R.id.selectTechnology);
      mSelectTravil = (Button) PageOne.findViewById(R.id.selectTravil);
      article = (EditText) PageOne.findViewById(R.id.articleLink);
      logout = (Button) PageOne.findViewById(R.id.logout3);
      mprogressDialog = new ProgressDialog(getActivity());
      selectDIY.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            Intent intent = new Intent();
            //Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select DIY and crafts"),DIY_INTENT);
         }
      });
////////////////////////////////////////////////////////////////
      selectHealth.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            Intent intent = new Intent();
            //Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Health and fitness"),Health_INTENT);
         }
      });
/////////////////////////////////////////////////////////////////
      mSelectFashion.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            Intent intent = new Intent();
            //Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select fashion"),FASHION_INTENT);
         }
      });
///////////////////////////////////////////////////////////////////
      mSelectBook.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            Intent intent = new Intent();
            //Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select book"),BOOK_INTENT);
         }
      });
//////////////////////////////////////////////////////////////////////
      mSelectTravil.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            Intent intent = new Intent();
            //Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select travil"),Travil_INTENT);
         }
      });
//////////////////////////////////////////////////////////////////////
      mSelectTechnology.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            Intent intent = new Intent();
            //Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select technology"),Technology_INTENT);
         }
      });
//////////////////////////////////////////////////////////////////////
      mSelectHistory.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            Intent intent = new Intent();
            //Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select History"),History_INTENT);
         }
      });
//////////////////////////////////////////////////////////////////////
      mSelectSport.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            Intent intent = new Intent();
            //Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select sport"),Sport_INTENT);
         }
      });
//////////////////////////////////////////////////////////////////////
      logout.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            new AlertDialog.Builder(getContext())
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Log out")
                    .setMessage("Are you sure you want to logging out?")
                    .setPositiveButton("Confirm", new DialogInterface.OnClickListener()
                    {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {
                          if(firebaseAuth != null)
                          {
                             firebaseAuth.signOut();
                             Toast.makeText(getContext(),"You successfully logged out!", Toast.LENGTH_SHORT).show();
                             Intent h= new Intent(getContext(), WelcomePage.class);
                             startActivity(h); }
                       }

                    })
                    .setNegativeButton("Cancel", null)
                    .show();
         }

      });
      return PageOne;
   }
///////////////////////////////////////////////////////////
   // Creating Method to get the selected image file Extension from File Path URI.
   public String GetFileExtension(Uri uri) {
      ContentResolver contentResolver = getContext().getContentResolver();
      MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
      // Returning the file Extension.
      return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;
   }
   @Override
   public void onActivityResult(int requestCode, int resultCode, Intent Data){
      super.onActivityResult(requestCode,resultCode,Data);
      if(resultCode == RESULT_OK ){
         if(requestCode == DIY_INTENT){
            if ( !article.getText().toString().isEmpty()){
         mprogressDialog.setMessage(" Uploading... ");
         mprogressDialog.show();
         Uri uri = Data.getData();
         StorageReference filepath = mStorage.child("DIY").child(uri.getLastPathSegment());
         filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
               Toast.makeText(getActivity(),"Upload done",Toast.LENGTH_LONG).show();
               mprogressDialog.dismiss();

            }
         });
            // Creating second StorageReference.
            StorageReference storageReference2nd = mStorage.child("DIY/" + System.currentTimeMillis() + "." + GetFileExtension(uri));
            // Adding addOnSuccessListener to second StorageReference.
            storageReference2nd.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                       @Override
                       public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                          // Showing toast message after done uploading.
                          Toast.makeText(getContext(), "Image Uploaded Successfully ", Toast.LENGTH_LONG).show();
                          @SuppressWarnings("VisibleForTests")
                          entertainment_List_Information imageUploadInfo = new entertainment_List_Information( taskSnapshot.getDownloadUrl().toString(),article.getText().toString());
                          // Getting image upload ID.
                          String ImageUploadId = databaseReference.push().getKey();
                          // Adding image upload id s child element into databaseReference.
                          databaseReference.child("diy").child(ImageUploadId).child("url").setValue(imageUploadInfo.getImageURL());
                          databaseReference.child("diy").child(ImageUploadId).child("article").setValue(imageUploadInfo.getImageArticle());
                       }
                    })
                    // If something goes wrong .
                    .addOnFailureListener(new OnFailureListener() {
                       @Override
                       public void onFailure(@NonNull Exception exception) {
                       }
                    })
                    // On progress change upload time.
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                       @Override
                       public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                          // Setting progressDialog Title.
                          mprogressDialog.setTitle("Image is Uploading...");
                       }
                    });}
            else {    Toast.makeText(getContext(), "Please enter the article link ", Toast.LENGTH_LONG).show();}
      }
      ///////////////////////////////////////////////////
      else if(requestCode == Health_INTENT){
            if ( !article.getText().toString().isEmpty()){
            mprogressDialog.setMessage(" Uploading... ");
            mprogressDialog.show();
            Uri uri = Data.getData();
            StorageReference filepath = mStorage.child("Health").child(uri.getLastPathSegment());
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
               @Override
               public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                  Toast.makeText(getActivity(),"Upload done",Toast.LENGTH_LONG).show();
                  mprogressDialog.dismiss();
               }
            });
            // Creating second StorageReference.
            StorageReference storageReference2nd = mStorage.child("Health/" + System.currentTimeMillis() + "." + GetFileExtension(uri));
            // Adding addOnSuccessListener to second StorageReference.
            storageReference2nd.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                       @Override
                       public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                          // Showing toast message after done uploading.
                          Toast.makeText(getContext(), "Image Uploaded Successfully ", Toast.LENGTH_LONG).show();
                          @SuppressWarnings("VisibleForTests")
                          entertainment_List_Information imageUploadInfo = new entertainment_List_Information( taskSnapshot.getDownloadUrl().toString(), article.getText().toString());
                          // Getting image upload ID.
                          String ImageUploadId = databaseReference.push().getKey();
                          // Adding image upload id s child element into databaseReference.
                          databaseReference.child("health").child(ImageUploadId).child("url").setValue(imageUploadInfo.getImageURL());
                          databaseReference.child("health").child(ImageUploadId).child("article").setValue(imageUploadInfo.getImageArticle());
                       }
                    })
                    // If something goes wrong .
                    .addOnFailureListener(new OnFailureListener() {
                       @Override
                       public void onFailure(@NonNull Exception exception) {
                       }
                    })
                    // On progress change upload time.
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                       @Override
                       public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                          // Setting progressDialog Title.
                          mprogressDialog.setTitle("Image is Uploading...");
                       }
                    });}
            else {Toast.makeText(getContext(), "Please enter the article link ", Toast.LENGTH_LONG).show();}
         }
         //////////////////////////////////////////////////////
         else if(requestCode == FASHION_INTENT){
            if (!article.getText().toString().isEmpty()){
            mprogressDialog.setMessage(" Uploading... ");
            mprogressDialog.show();
            Uri uri = Data.getData();
            StorageReference filepath = mStorage.child("Fashion").child(uri.getLastPathSegment());
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
               @Override
               public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                  Toast.makeText(getActivity(),"Upload done",Toast.LENGTH_LONG).show();
                  mprogressDialog.dismiss();
               }
            });
            // Creating second StorageReference.
            StorageReference storageReference2nd = mStorage.child("Fashion/" + System.currentTimeMillis() + "." + GetFileExtension(uri));
            // Adding addOnSuccessListener to second StorageReference.
            storageReference2nd.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                       @Override
                       public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                          // Showing toast message after done uploading.
                          Toast.makeText(getContext(), "Image Uploaded Successfully ", Toast.LENGTH_LONG).show();
                          @SuppressWarnings("VisibleForTests")
                          entertainment_List_Information imageUploadInfo = new entertainment_List_Information( taskSnapshot.getDownloadUrl().toString(),article.getText().toString());
                          // Getting image upload ID.
                          String ImageUploadId = databaseReference.push().getKey();
                       }})
                    // If something goes wrong .
                    .addOnFailureListener(new OnFailureListener() {
                       @Override
                       public void onFailure(@NonNull Exception exception) {}})
                    // On progress change upload time.
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                       @Override
                       public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                          // Setting progressDialog Title.
                          mprogressDialog.setTitle("Image is Uploading...");}});}
            else {Toast.makeText(getContext(), "Please enter the article link ", Toast.LENGTH_LONG).show();}}
///////////////////////////////////////////////
         else if(requestCode == BOOK_INTENT){
            if (!article.getText().toString().isEmpty()){
            mprogressDialog.setMessage(" Uploading... ");
            mprogressDialog.show();
            Uri uri = Data.getData();
            StorageReference filepath = mStorage.child("Book").child(uri.getLastPathSegment());
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
               @Override
               public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                  Toast.makeText(getActivity(),"Upload done",Toast.LENGTH_LONG).show();
                  mprogressDialog.dismiss();}
            });
            // Creating second StorageReference.
            StorageReference storageReference2nd = mStorage.child("Book/" + System.currentTimeMillis() + "." + GetFileExtension(uri));
            // Adding addOnSuccessListener to second StorageReference.
            storageReference2nd.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                       @Override
                       public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                          // Showing toast message after done uploading.
                          Toast.makeText(getContext(), "Image Uploaded Successfully ", Toast.LENGTH_LONG).show();
                          @SuppressWarnings("VisibleForTests")
                          entertainment_List_Information imageUploadInfo = new entertainment_List_Information( taskSnapshot.getDownloadUrl().toString(),article.getText().toString());
                          // Getting image upload ID.
                          String ImageUploadId = databaseReference.push().getKey();
                          // Adding image upload id s child element into databaseReference.
                          databaseReference.child("book").child(ImageUploadId).child("url").setValue(imageUploadInfo.getImageURL());
                          databaseReference.child("book").child(ImageUploadId).child("article").setValue(imageUploadInfo.getImageArticle());}
                    })
                    // If something goes wrong .
                    .addOnFailureListener(new OnFailureListener() {
                       @Override
                       public void onFailure(@NonNull Exception exception) {}})
                    // On progress change upload time.
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                       @Override
                       public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                          // Setting progressDialog Title.
                          mprogressDialog.setTitle("Image is Uploading...");
                       }
                    });}
            else {Toast.makeText(getContext(), "Please enter the article link ", Toast.LENGTH_LONG).show();}}
//////////////////////////////////////////
         else if(requestCode == Travil_INTENT){
            if ( !article.getText().toString().isEmpty()){
               mprogressDialog.setMessage(" Uploading... ");
               mprogressDialog.show();
               Uri uri = Data.getData();
               StorageReference filepath = mStorage.child("Travil").child(uri.getLastPathSegment());
               filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                  @Override
                  public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                     Toast.makeText(getActivity(),"Upload done",Toast.LENGTH_LONG).show();
                     mprogressDialog.dismiss();}});
               // Creating second StorageReference.
               StorageReference storageReference2nd = mStorage.child("Travil/" + System.currentTimeMillis() + "." + GetFileExtension(uri));
               // Adding addOnSuccessListener to second StorageReference.
               storageReference2nd.putFile(uri)
                       .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                          @Override
                          public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                             // Showing toast message after done uploading.
                             Toast.makeText(getContext(), "Image Uploaded Successfully ", Toast.LENGTH_LONG).show();
                             @SuppressWarnings("VisibleForTests")
                             entertainment_List_Information imageUploadInfo = new entertainment_List_Information( taskSnapshot.getDownloadUrl().toString(),article.getText().toString());
                             // Getting image upload ID.
                             String ImageUploadId = databaseReference.push().getKey();
                             // Adding image upload id s child element into databaseReference.
                             databaseReference.child("travil").child(ImageUploadId).child("url").setValue(imageUploadInfo.getImageURL());
                             databaseReference.child("travil").child(ImageUploadId).child("article").setValue(imageUploadInfo.getImageArticle());
                          }
                       })
                       // If something goes wrong .
                       .addOnFailureListener(new OnFailureListener() {
                          @Override
                          public void onFailure(@NonNull Exception exception) {}})
                       // On progress change upload time.
                       .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                          @Override
                          public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                             // Setting progressDialog Title.
                             mprogressDialog.setTitle("Image is Uploading...");
                          }
                       });}
            else {Toast.makeText(getContext(), "Please enter the article link ", Toast.LENGTH_LONG).show();}
         }
         /////////////////////////////////////////////
         else if(requestCode == Technology_INTENT){
            if ( !article.getText().toString().isEmpty()){
               mprogressDialog.setMessage(" Uploading... ");
               mprogressDialog.show();
               Uri uri = Data.getData();
               StorageReference filepath = mStorage.child("Technology").child(uri.getLastPathSegment());
               filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                  @Override
                  public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                     Toast.makeText(getActivity(),"Upload done",Toast.LENGTH_LONG).show();
                     mprogressDialog.dismiss();
                  }
               });
               // Creating second StorageReference.
               StorageReference storageReference2nd = mStorage.child("Technology/" + System.currentTimeMillis() + "." + GetFileExtension(uri));
               // Adding addOnSuccessListener to second StorageReference.
               storageReference2nd.putFile(uri)
                       .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                          @Override
                          public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                             // Showing toast message after done uploading.
                             Toast.makeText(getContext(), "Image Uploaded Successfully ", Toast.LENGTH_LONG).show();
                             @SuppressWarnings("VisibleForTests")
                             entertainment_List_Information imageUploadInfo = new entertainment_List_Information( taskSnapshot.getDownloadUrl().toString(),article.getText().toString());
                             // Getting image upload ID.
                             String ImageUploadId = databaseReference.push().getKey();
                             // Adding image upload id s child element into databaseReference.
                             databaseReference.child("technology").child(ImageUploadId).child("url").setValue(imageUploadInfo.getImageURL());
                             databaseReference.child("technology").child(ImageUploadId).child("article").setValue(imageUploadInfo.getImageArticle());
                          }
                       })
                       // If something goes wrong .
                       .addOnFailureListener(new OnFailureListener() {
                          @Override
                          public void onFailure(@NonNull Exception exception) {
                          }
                       })
                       // On progress change upload time.
                       .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                          @Override
                          public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                             // Setting progressDialog Title.
                             mprogressDialog.setTitle("Image is Uploading...");
                          }
                       });}
            else {Toast.makeText(getContext(), "Please enter the article link ", Toast.LENGTH_LONG).show();}
         }
         /////////////////////////////////////
         else if(requestCode == History_INTENT){
            if ( !article.getText().toString().isEmpty()){
               mprogressDialog.setMessage(" Uploading... ");
               mprogressDialog.show();
               Uri uri = Data.getData();
               StorageReference filepath = mStorage.child("History").child(uri.getLastPathSegment());
               filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                  @Override
                  public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                     Toast.makeText(getActivity(),"Upload done",Toast.LENGTH_LONG).show();
                     mprogressDialog.dismiss();
                  }
               });
               // Creating second StorageReference.
               StorageReference storageReference2nd = mStorage.child("History/" + System.currentTimeMillis() + "." + GetFileExtension(uri));
               // Adding addOnSuccessListener to second StorageReference.
               storageReference2nd.putFile(uri)
                       .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                          @Override
                          public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                             // Showing toast message after done uploading.
                             Toast.makeText(getContext(), "Image Uploaded Successfully ", Toast.LENGTH_LONG).show();
                             @SuppressWarnings("VisibleForTests")
                             entertainment_List_Information imageUploadInfo = new entertainment_List_Information( taskSnapshot.getDownloadUrl().toString(),article.getText().toString());
                             // Getting image upload ID.
                             String ImageUploadId = databaseReference.push().getKey();
                             // Adding image upload id s child element into databaseReference.
                             databaseReference.child("history").child(ImageUploadId).child("url").setValue(imageUploadInfo.getImageURL());
                             databaseReference.child("history").child(ImageUploadId).child("article").setValue(imageUploadInfo.getImageArticle());
                          }
                       })
                       // If something goes wrong .
                       .addOnFailureListener(new OnFailureListener() {
                          @Override
                          public void onFailure(@NonNull Exception exception) {
                          }})
                       // On progress change upload time.
                       .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                          @Override
                          public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                             // Setting progressDialog Title.
                             mprogressDialog.setTitle("Image is Uploading...");
                          }
                       });}
            else {Toast.makeText(getContext(), "Please enter the article link ", Toast.LENGTH_LONG).show();}}
         ///////////////////////////////////
         else if(requestCode == Sport_INTENT){
            if ( !article.getText().toString().isEmpty()){
               mprogressDialog.setMessage(" Uploading... ");
               mprogressDialog.show();
               Uri uri = Data.getData();
               StorageReference filepath = mStorage.child("Sport").child(uri.getLastPathSegment());
               filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                  @Override
                  public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                     Toast.makeText(getActivity(),"Upload done",Toast.LENGTH_LONG).show();
                     mprogressDialog.dismiss();
                  }
               });
               // Creating second StorageReference.
               StorageReference storageReference2nd = mStorage.child("Sport/" + System.currentTimeMillis() + "." + GetFileExtension(uri));
               // Adding addOnSuccessListener to second StorageReference.
               storageReference2nd.putFile(uri)
                       .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                          @Override
                          public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                             // Showing toast message after done uploading.
                             Toast.makeText(getContext(), "Image Uploaded Successfully ", Toast.LENGTH_LONG).show();
                             @SuppressWarnings("VisibleForTests")
                             entertainment_List_Information imageUploadInfo = new entertainment_List_Information( taskSnapshot.getDownloadUrl().toString(),article.getText().toString());
                             // Getting image upload ID.
                             String ImageUploadId = databaseReference.push().getKey();
                             // Adding image upload id s child element into databaseReference.
                             databaseReference.child("sport").child(ImageUploadId).child("url").setValue(imageUploadInfo.getImageURL());
                             databaseReference.child("sport").child(ImageUploadId).child("article").setValue(imageUploadInfo.getImageArticle());
                          }
                       })
                       // If something goes wrong .
                       .addOnFailureListener(new OnFailureListener() {
                          @Override
                          public void onFailure(@NonNull Exception exception) {
                          }})
                       // On progress change upload time.
                       .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                          @Override
                          public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                             // Setting progressDialog Title.
                             mprogressDialog.setTitle("Image is Uploading...");
                          }
                       });}
            else {Toast.makeText(getContext(), "Please enter the article link ", Toast.LENGTH_LONG).show();}
         }}}}