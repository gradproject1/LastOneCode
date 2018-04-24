package com.example.user1.urnextapp;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.content.Intent;
import android.support.annotation.NonNull;

import android.widget.TextView;
import android.widget.Toast;

import java.lang.*;
import java.util.HashMap;
import java.util.Map;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;


public class Pprofile extends Fragment {

    private TextView name,arrival,docName,appTime;
    private Button logout,cancel;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();
    private FirebaseUser user = firebaseAuth.getCurrentUser();
    DatabaseReference Patient = database.getReference("Patient");
    DatabaseReference external = database.getReference("ExternalDB");
    DatabaseReference waiting = database.getReference("waiting time and queue number");
     String id=" ";
    private FirebaseFirestore fire_store; //to store token id in his document

    //Constructor default
    public Pprofile(){};

    @Override
    public View onCreateView(  @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) throws NullPointerException {
        View Page1 = inflater.inflate(R.layout.fragment_pprofile, container, false);

        name=    (TextView) Page1.findViewById(R.id.patientName);
        arrival = (TextView) Page1.findViewById(R.id.arrivelTime);
        docName =(TextView) Page1.findViewById(R.id.doctorName);
        appTime =(TextView) Page1.findViewById(R.id.appTime);
        logout = (Button) Page1.findViewById(R.id.logout);
        cancel =(Button) Page1.findViewById(R.id.cancel);
        fire_store= FirebaseFirestore.getInstance(); //fire store instance


        if(user != null)
        {
            id=user.getUid();

        }

        Patient.child(id).addValueEventListener(new ValueEventListener(){
            public void onDataChange(DataSnapshot dataSnapshot) {
              final String  pname= dataSnapshot.child("Name").getValue(String.class);
              final String  pphone= dataSnapshot.child("Phone").getValue(String.class);
                final String arrival2 = dataSnapshot.child("arrival").getValue(String.class);
                arrival.setText(arrival2);
              name.setText(pname);

                external.child("Appointment").child("Dental clinic").child(pphone).addValueEventListener(new ValueEventListener(){
                    public void onDataChange(DataSnapshot dataSnapshot) {
                      final String  dname= dataSnapshot.child("Doctor Name").getValue(String.class);
                      final String papp= dataSnapshot.child("appTime").getValue(String.class);

                        docName.setText(dname);
                        appTime.setText(papp);

                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                new AlertDialog.Builder(getContext())
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .setTitle("Canceling appointment")
                                        .setMessage("Are you sure you want to cancel this appointment?")
                                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener()
                                        {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (dname != null)
                                                waiting.child(dname).child(papp).removeValue();
                                                Toast.makeText(getContext(),"You successfully canceled this appointment!", Toast.LENGTH_SHORT).show();
                                                Intent h= new Intent(getContext(), WelcomePage.class);
                                                startActivity(h);


                                            }

                                        })
                                        .setNegativeButton("cancel", null)
                                        .show();
                         }
                        });

}
                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });

             }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

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
                Map<String,Object> token_remove= new HashMap<>();
                token_remove.put("Token_ID", FieldValue.delete());
                //because when field in firebase all become null the key will deleted so we use this method
                fire_store.collection("Usres").document(id).update(token_remove).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                });
            }

        });
        return Page1;
    }
}
