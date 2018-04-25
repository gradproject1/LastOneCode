package com.example.user1.urnextapp;
//android
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

//java
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class AcceptList extends ArrayAdapter<Accept_List_Information> {
    // class for custom list view with adapter of Accept_List_Information object
    private Activity context;
    static List<Accept_List_Information> Patient_List;
    static Button accept_button;
    static int index=0;
    private String Message = "Hi, it's your turn!"; // message to send when nurse click accept
    private FirebaseFirestore fire_store; // to store user document
    TextView arrival;
    static View v;
    private static FirebaseDatabase database = FirebaseDatabase.getInstance();
    static DatabaseReference admission_table = database.getReference("admission time");
    AcceptList(Activity context, List<Accept_List_Information> patient) {
        super(context, R.layout.list_layout, patient);
        this.context = context;
        this.Patient_List = patient;
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        @SuppressLint({"InflateParams", "ViewHolder"}) View listViewItem = inflater.inflate(R.layout.list_layout, null, true);
        TextView name = (TextView) listViewItem.findViewById(R.id.pname);
        arrival = (TextView) listViewItem.findViewById(R.id.textView12);
        TextView time = (TextView) listViewItem.findViewById(R.id.textView8);
        TextView number = (TextView) listViewItem.findViewById(R.id.number);
        accept_button = (Button) listViewItem.findViewById(R.id.accept);
        fire_store = FirebaseFirestore.getInstance(); // fire store to store user notification inside his document
        admission_table.child(AcceptPatient.Doctor_Name).setValue("");
        Accept_List_Information patient_Information = Patient_List.get(position);
        name.setText(patient_Information.getname());
        time.setText(patient_Information.gettime());
        arrival.setText(patient_Information.getarrival());
        number.setText(position + 1 + "");
        v = AcceptPatient.AcceptList.getChildAt(index);
        accept_button.setVisibility(View.INVISIBLE);

        if(Patient_List.size()==1){
            accept_button.setVisibility(View.VISIBLE);
            accept_button.setOnClickListener(new View.OnClickListener() {
                // check if the button clicked then remove patient from list view by his position
                // and also remove him from waiting table by appointment time and from appointment table by his phone number
                @Override
                public void onClick(View v) {
                    Patient_List.remove(index);
                    AcceptPatient.AcceptList.setAdapter(AcceptPatient.adapter);
                    AcceptPatient.adapter.notifyDataSetChanged();
                    // delete him from waiting data
                    AcceptPatient.waiting_table.child(AcceptPatient.Doctor_Name).child(AcceptPatient.appTime.get(index)).removeValue();
                    // delete him from appointment data
                    AcceptPatient.external_data.child("Appointment").child("Dental clinic").child(AcceptPatient.patient_phone.get(index)).removeValue();
                    // store admission time when nurse click accept
                    final Time today = new Time(Time.getCurrentTimezone());
                    today.setToNow();
                    String Today = today.format("%k:%M");
                    AcceptPatient.patient_table.child(AcceptPatient.patient_ID.get(index)).child(" admission time").setValue(Today);

                    // each user have a document of notification
                    Map<String, Object> notification_message = new HashMap<>(); //map between user and his collection
                    notification_message.put("Message", Message); //put the message in patient collection
                    notification_message.put("From", AcceptPatient.User_ID);
                    fire_store.collection("Usres/" + AcceptPatient.patient_ID.get(position) + "/Notifications").add(notification_message).addOnSuccessListener(new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {
                        }
                    });
                    AcceptPatient.appTime.remove(index);
                    AcceptPatient.patient_phone.remove(index);
                    AcceptPatient.patient_ID.remove(index);
                }
            });
        }
        if (v != null) {
            accept_button = v.findViewById(R.id.accept);
            accept_button.setVisibility(View.VISIBLE);
        accept_button.setOnClickListener(new View.OnClickListener() {
            // check if the button clicked then remove patient from list view by his position
            // and also remove him from waiting table by appointment time and from appointment table by his phone number
            @Override
            public void onClick(View v) {
                Patient_List.remove(index);
                AcceptPatient.AcceptList.setAdapter(AcceptPatient.adapter);
                AcceptPatient.adapter.notifyDataSetChanged();
                // delete him from waiting data
                AcceptPatient.waiting_table.child(AcceptPatient.Doctor_Name).child(AcceptPatient.appTime.get(index)).removeValue();
                // delete him from appointment data
                AcceptPatient.external_data.child("Appointment").child("Dental clinic").child(AcceptPatient.patient_phone.get(index)).removeValue();
                // store admission time when nurse click accept
                final Time today = new Time(Time.getCurrentTimezone());
                today.setToNow();
                String Today = today.format("%k:%M");
                AcceptPatient.patient_table.child(AcceptPatient.patient_ID.get(index)).child(" admission time").setValue(Today);

                // each user have a document of notification
                Map<String, Object> notification_message = new HashMap<>(); //map between user and his collection
                notification_message.put("Message", Message); //put the message in patient collection
                notification_message.put("From", AcceptPatient.User_ID);
                fire_store.collection("Usres/" + AcceptPatient.patient_ID.get(position) + "/Notifications").add(notification_message).addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                    }
                });
                AcceptPatient.appTime.remove(index);
                AcceptPatient.patient_phone.remove(index);
                AcceptPatient.patient_ID.remove(index);
            }
        });
    }
        return listViewItem;
    }
    }



/*if(index<=Patient_List.size()){
        p1 = Patient_List.get(0);
        patient_1 = Patient_List.get(0);
        }
        DateFormat df = new java.text.SimpleDateFormat("hh:mm");
        try {
            if(flag){
                if(index<=Patient_List.size()){
                p1 = Patient_List.get(0);
                patient_1 = Patient_List.get(0);
                flag=false;}
            }
            Date date1 = df.parse(p1.gettime());
            Date date2 = df.parse(p1.getarrival());
            if (date2.before(date1)) {
               if(elapsedMillis!=0){
                   diff = (date1.getTime() - date2.getTime())+elapsedMillis;
                   reverseTimer((int)(diff));
                    elapsedMillis=0;
                  }
                else{
                   diff = (date1.getTime() - date2.getTime());
                    reverseTimer((int)(diff));
                }
            }
            if (date2.after(date1)) {
                 time.setText("00:00:00");
                //   queueNumber.setText("0");
            }
            //  estimate.setText(t);
        } catch (ParseException e) {
            e.printStackTrace();
        }*/
/*******************************************/
  /* public void reverseTimer(int Seconds) {

        new CountDownTimer(Seconds, 1000) {

            public void onTick(long millisUntilFinished) {
                int seconds = (int) (millisUntilFinished / 1000);

                int hours = seconds / (60 * 60);
                int tempMint = (seconds - (hours * 60 * 60));
                int minutes = tempMint / 60;
                seconds = tempMint - (minutes * 60);
                 t = String.format("%02d", hours)
                        + ":" + String.format("%02d", minutes)
                        + ":" + String.format("%02d", seconds);
                View v = AcceptPatient.AcceptList.getChildAt(0 - AcceptPatient.AcceptList.getFirstVisiblePosition());
                if (v != null) {
                    text = v.findViewById(R.id.time);
                    text.setText(t);
                }
            }

            public void onFinish() {
                if (p1.equals(patient_1)) {
                    View v = AcceptPatient.AcceptList.getChildAt(0 - AcceptPatient.AcceptList.getFirstVisiblePosition());
                    if (v != null) {
                        text = v.findViewById(R.id.time);
                        text.setText("");
                            final Chronometer chronometer2 = v.findViewById(R.id.chronometer2);
                            chronometer2.setVisibility(View.VISIBLE);
                            chronometer2.setBase(SystemClock.elapsedRealtime());
                            chronometer2.start(); // start a chronometer
                            chronometer2.setFormat("Lateness time:\n  %s"); // set the format for a chronometer
                        chronometer2.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                            @Override
                            public void onChronometerTick(Chronometer chronometer) {
                                admission_table.child(AcceptPatient.Doctor_Name).setValue(SystemClock.elapsedRealtime() - chronometer2.getBase());
                                    elapsedMillis = SystemClock.elapsedRealtime() - chronometer2.getBase();

                            }
                        });
                    }
                }
            }
        }.start();
    }*/