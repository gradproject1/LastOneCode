package com.example.user1.urnextapp;
//android

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

//fire base
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
//java
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import static android.content.Context.NOTIFICATION_SERVICE;


public class AcceptPatient extends Fragment {

    // fire base tables
    private static FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();
    private FirebaseUser user = firebaseAuth.getCurrentUser();
    static DatabaseReference patient_table = database.getReference("Patient");
    static DatabaseReference external_data = database.getReference("ExternalDB");
    static DatabaseReference waiting_table = database.getReference("waiting time and queue number");

    static String User_ID=" ";
    static ListView AcceptList;
    static ArrayList<Accept_List_Information> PatientList;
    static ArrayList<String> appTime = new ArrayList<>();
    static ArrayList<String> patient_phone = new ArrayList<>();
    static ArrayList<String> patient_ID = new ArrayList<>();
    static AcceptList adapter;
    static String Doctor_Name;
    static String Nurse_Name;

    //Constructor default
    public AcceptPatient(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View PageOne = inflater.inflate(R.layout.fragment_accept_patient, container, false);

        AcceptList = (ListView) PageOne.findViewById(R.id.Acceptlist);
        PatientList = new ArrayList<>();


        FirebaseMessaging.getInstance().subscribeToTopic("pushNotifications");
        FirebaseMessaging.getInstance().unsubscribeFromTopic("pushNotifications");

        if (user != null) {
            User_ID = user.getUid();
        }

        external_data.child("Nurse").child(User_ID).addValueEventListener(new ValueEventListener() {
            // get doctor id from nurse data
            public void onDataChange(DataSnapshot dataSnapshot) {
                Nurse_Name= dataSnapshot.child("Name").getValue(String.class);
                String Doctor_ID = dataSnapshot.child("Doctor ID").getValue(String.class);

                assert Doctor_ID != null;
                external_data.child("Doctors").child(Doctor_ID).addValueEventListener(new ValueEventListener() {
                    // get doctor name from doctor data
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Doctor_Name = dataSnapshot.child("Name").getValue(String.class);

                        assert Doctor_Name != null;
                        waiting_table.child(Doctor_Name).addChildEventListener(new ChildEventListener() {
                            //search by doctor name in waiting table to get patient id
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                                final String user_ID = dataSnapshot.getValue(String.class);
                                assert user_ID != null;

                                patient_table.child(user_ID).addValueEventListener(new ValueEventListener() {
                                    //get patient name and phone number to search about him in appointment data
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        final String phone_number = dataSnapshot.child("Phone").getValue(String.class);
                                        final String arrival = dataSnapshot.child("arrival").getValue(String.class);

                                        assert phone_number != null;
                                        assert arrival != null;
                                        external_data.child("Appointment").child("Dental clinic").addValueEventListener(new ValueEventListener() {
                                            //search about patient in appointment data to get his appointment time
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                String name = dataSnapshot.child(phone_number).child("Name").getValue(String.class);
                                                String app_time = dataSnapshot.child(phone_number).child("appTime").getValue(String.class);
                                                if (name != null && appTime != null) {
                                                    Accept_List_Information Patient_information = new Accept_List_Information(name, app_time, arrival);
                                                    if (PatientList.isEmpty() == true) {
                                                        //store user phone number for delete him from appointment table when the nurse accept him
                                                        patient_phone.add(phone_number);
                                                        //store patient id to send him a notification when his turn come
                                                        patient_ID.add(user_ID);
                                                        // store user appointment time for delete him from waiting table when the nurse accept him
                                                        appTime.add(app_time);
                                                        // store patient information in object of patients
                                                        PatientList.add(Patient_information);
                                                        // create adapter of patient
                                                        adapter = new AcceptList(getActivity(), PatientList);
                                                        AcceptList.setAdapter(adapter);
                                                        adapter.notifyDataSetChanged();
                                                    } else {
                                                        int count = 0;
                                                        for (int i = 0; i < PatientList.size(); i++) {
                                                            if (PatientList.get(i).gettime().equals(Patient_information.gettime())) {
                                                                count++;
                                                            }
                                                        }
                                                        if (count == 0) {
                                                            //store user phone number for delete him from appointment table when the nurse accept him
                                                            patient_phone.add(phone_number);
                                                            //store patient id to send him a notification when his turn come
                                                            patient_ID.add(user_ID);
                                                            // store user appointment time for delete him from waiting table when the nurse accept him
                                                            appTime.add(app_time);
                                                            // store patient information in object of patients
                                                            PatientList.add(Patient_information);
                                                            // create adapter of patient
                                                            if (getActivity()!=null){
                                                                adapter = new AcceptList(getActivity(), PatientList);
                                                                AcceptList.setAdapter(adapter);
                                                                adapter.notifyDataSetChanged();
                                                            }
                                                        }
                                                    }
                                                }
                                                adapter.notifyDataSetChanged();

                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                            }//app
                                        });//app

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                    }
                                });
                            }

                            @Override
                            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onChildRemoved(DataSnapshot dataSnapshot) {
                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        }); // end of nurse table search
        return PageOne;
    }// end create
}// end class
