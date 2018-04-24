package com.example.user1.urnextapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

// class for allow the nurse to cancel or delay the appointment
// and send comment to the all the patients
public class cancelOrDelay extends Fragment {
    //Constructor default
    public cancelOrDelay(){};

    RadioGroup group;
    TextView delay_text;
    View PageOne;
    String Comment;
    EditText textArea,time;
    Button submit;
    RadioButton Cancel,Delay;
    private FirebaseFirestore fire_store; // to store user document
    private static final String TIME24HOURS_PATTERN =
            "([01]?[0-9]|2[0-3]):[0-5][0-9]"; // time patten for delay

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        PageOne = inflater.inflate(R.layout.fragment_cancel_or_delay, container, false);


        textArea = (EditText) PageOne.findViewById(R.id.textArea_information);
        delay_text=(TextView)PageOne.findViewById(R.id.textView4);
        time=  (EditText) PageOne.findViewById(R.id.time);
        group=(RadioGroup)PageOne.findViewById(R.id.groubp);
        submit=(Button) PageOne.findViewById(R.id.button2);
        Cancel=(RadioButton) PageOne.findViewById(R.id.Cancel);
        Delay=(RadioButton) PageOne.findViewById(R.id.Delay);
        fire_store= FirebaseFirestore.getInstance(); // fire store to store user notification inside his document

        // to scroll the text area
        textArea.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                switch (event.getAction() & MotionEvent.ACTION_MASK){
                    case MotionEvent.ACTION_UP:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
                return false;
            }
        });
        time.setVisibility(View.INVISIBLE);
        delay_text.setVisibility(View.INVISIBLE);

        // radio listener and store the radio name
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Comment="The appointment has been canceled: \n \n";
                time.setVisibility(View.INVISIBLE);
                delay_text.setVisibility(View.INVISIBLE);
            }
        });

        Delay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Comment="The appointment has to be delayed: \n \n";
                time.setVisibility(View.VISIBLE);
                delay_text.setVisibility(View.VISIBLE);
            }
        });

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        // loop throw the patient in the waiting page by the id and send to
        // them the notification by store the notification in his document
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Comment != null){
                    if (!textArea.getText().toString().matches("")) {
                        if (Comment.contains("The appointment has to be delayed") && !time.getText().toString().matches("")) {
                            if (time.getText().toString().matches(TIME24HOURS_PATTERN)) {
                                for (int i = 0; i < AcceptPatient.patient_ID.size(); i++) {
                                    // each user have a document of notification
                                    Map<String, Object> notification_message = new HashMap<>(); //map between user and his collection
                                    notification_message.put("Message", Comment + textArea.getText().toString()+"\n \n Delay period will be: "+ time.getText().toString()); //put the message in patient collection
                                    notification_message.put("From", AcceptPatient.User_ID);
                                    fire_store.collection("Usres/" + AcceptPatient.patient_ID.get(i) + "/Notifications").add(notification_message).addOnSuccessListener(new OnSuccessListener() {
                                        @Override
                                        public void onSuccess(Object o) {
                                        }
                                    });
                                }
                                //  notify the nurse when the notification sent
                                Toast.makeText(getActivity(), "Notification has been Sent", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(getActivity(),"Please enter a correct time format e.g. \n 1. 01:00  \n 2. 1:00 \n 3. 23:59", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else if(Comment.contains("The appointment has been canceled")){
                            for (int i = 0; i < AcceptPatient.patient_ID.size(); i++) {
                                // each user have a document of notification
                                Map<String, Object> notification_message = new HashMap<>(); //map between user and his collection
                                notification_message.put("Message", Comment + textArea.getText().toString()); //put the message in patient collection
                                notification_message.put("From", AcceptPatient.User_ID);
                                fire_store.collection("Usres/" + AcceptPatient.patient_ID.get(i) + "/Notifications").add(notification_message).addOnSuccessListener(new OnSuccessListener() {
                                    @Override
                                    public void onSuccess(Object o) {
                                    }
                                });
                                // delete him from waiting data
                                AcceptPatient.waiting_table.child(AcceptPatient.Doctor_Name).child(AcceptPatient.appTime.get(i)).removeValue();
                                // delete him from appointment data
                                AcceptPatient.external_data.child("Appointment").child("Dental clinic").child( AcceptPatient.patient_phone.get(i)).removeValue();

                            }
                            //  notify the nurse when the notification sent
                            Toast.makeText(getActivity(), "Notification has been Sent", Toast.LENGTH_SHORT).show();
                            // delete all appointment
                            AcceptList.Patient_List.clear();
                            AcceptPatient.PatientList.clear();
                            AcceptPatient.appTime.clear();
                            AcceptPatient.patient_phone.clear();
                            AcceptPatient.patient_ID.clear();
                            AcceptPatient.AcceptList.setAdapter(AcceptPatient.adapter);
                            AcceptPatient.adapter.notifyDataSetChanged();



                        }
                        else{
                            Toast.makeText(getActivity(),"Please write the delay time", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(getActivity(),"Please write a comment for the patients", Toast.LENGTH_SHORT).show();

                    }
                }
                else{
                    Toast.makeText(getActivity(),"Please select one of cancel or delay option", Toast.LENGTH_SHORT).show();
                }

            }
        });

        return PageOne;
    }
}
