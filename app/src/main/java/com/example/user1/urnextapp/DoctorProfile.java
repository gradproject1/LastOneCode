package com.example.user1.urnextapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.annotation.NonNull;

import android.widget.TextView;

import java.lang.*;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;


public class DoctorProfile extends Fragment {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = firebaseAuth.getCurrentUser();
    DatabaseReference external = database.getReference("ExternalDB");
    String id = " ";

    //Constructor default
    public DoctorProfile() {
    }

    ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View PageOne = inflater.inflate(R.layout.fragment_doctor_profile, container, false);

        final TextView name = (TextView) PageOne.findViewById(R.id.doctorName1);
        final TextView did = (TextView) PageOne.findViewById(R.id.did);

        if (user != null) {
            id = user.getUid();

        }
        external.child("Doctors").child(id).addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                final String dname = dataSnapshot.child("Name").getValue(String.class);
                final String id = dataSnapshot.child("Employee ID").getValue(String.class);
                name.setText(dname);
                did.setText(id);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        return PageOne;
    }
}