package com.example.user1.urnextapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.estimote.coresdk.common.requirements.SystemRequirementsChecker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Patient extends AppCompatActivity {
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference waiting = database.getReference("waiting time and queue number");
    private FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();
    private FirebaseUser user = firebaseAuth.getCurrentUser();
    DatabaseReference Patient = database.getReference("Patient");
    DatabaseReference external = database.getReference("ExternalDB");
    TabLayout MyTabs;
    ViewPager MyPage;
    private   String id=user.getUid();
private  Date date = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        MyTabs = (TabLayout)findViewById(R.id.MyTabs);
        MyPage = (ViewPager)findViewById(R.id.MyPage);

        MyTabs.setupWithViewPager(MyPage);
        SetUpViewPager(MyPage);

        // store the notification from nurse in string
        String data_message = this.getIntent().getStringExtra("Message");
        // check if not null
        if (data_message != null) {
            // if message contains delay word display specific alert with two button for delay
            if (data_message.contains("The appointment has to be delayed")) {
                // setup the alert builder
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("UrNext");
                builder.setMessage(data_message);
                // add the buttons
                builder.setPositiveButton("Accept", null);
                builder.setNegativeButton("Cancel", null);
                // create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();
                builder.setPositiveButton("Accept",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                             String msg  = dialog.toString();
                             String time =msg.substring(msg.length()-3);
                                DateFormat df = new java.text.SimpleDateFormat("hh:mm");

                                long diff;
                                try {
                                    date = df.parse(time);

                                    Patient.child(id).addValueEventListener(new ValueEventListener() {
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            final String pphone = dataSnapshot.child("Phone").getValue(String.class);
                                            external.child("Appointment").child("Dental clinic").child(pphone).addValueEventListener(new ValueEventListener() {
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    final String dname = dataSnapshot.child("Doctor Name").getValue(String.class);
                                                    String papp = dataSnapshot.child("appTime").getValue(String.class);
                                                    if (dname != null && papp != null) {
                                                        waiting.child(dname).addValueEventListener(new ValueEventListener() {
                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                for (DataSnapshot dSnapshot : dataSnapshot.getChildren()) {
                                                                        String t = dSnapshot.getKey();
                                                                        int t2=Integer.parseInt(t);
                                                                        String total = date.getTime()+t2+"";
                                                                        waiting.child(dname).child(total).setValue(id);

                                                                }}

                                                            public void onCancelled(DatabaseError databaseError) {
                                                            }});}}
                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {
                                                }});}
                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                        }
                                    });
                                }
                                catch (ParseException e) {}
                            }
                        });
                builder.setPositiveButton("Cancel",
                      new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                              /*  Toast.makeText(Patient.this ,"You successfully canceled this appointment!", Toast.LENGTH_SHORT).show();
                                Intent h= new Intent(Patient.this , WelcomePage.class);
                                startActivity(h);*/
                            }
                        });

            }

            else if(data_message.contains("Hi, it's your turn!")){ // alert with one button for your turn
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
                dlgAlert.setMessage(data_message);
                dlgAlert.setTitle("UrNext");
                dlgAlert.setPositiveButton("OK", null);
                dlgAlert.setCancelable(true);
                dlgAlert.create().show();
                dlgAlert.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
            }
            // alert with one button for cancel
            else {
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
                dlgAlert.setMessage(data_message);
                dlgAlert.setTitle("UrNext");
                dlgAlert.setPositiveButton("OK", null);
                dlgAlert.setCancelable(true);
                dlgAlert.create().show();
                dlgAlert.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                               /* Toast.makeText(Patient.this ,"The appointment is cancelled by doctor !", Toast.LENGTH_SHORT).show();
                                Intent h= new Intent(Patient.this , WelcomePage.class);
                                startActivity(h);*/
                            }
                        });
            }

        }
    }
    public void SetUpViewPager (ViewPager viewpage){
        MyViewPageAdapter Adapter = new MyViewPageAdapter(getSupportFragmentManager());
        Adapter.AddFragmentPage(new pwaiting(), "Waiting Page");
        Adapter.AddFragmentPage(new entertainment(), "Entertainment");
        Adapter.AddFragmentPage(new Pprofile(), "Profile");
        //We Need Fragment class now
        viewpage.setAdapter(Adapter);
    }

    public class MyViewPageAdapter extends FragmentPagerAdapter{
        private List<Fragment> MyFragment = new ArrayList<>();
        private List<String> MyPageTittle = new ArrayList<>();

        public MyViewPageAdapter(FragmentManager manager){
            super(manager);
        }

        public void AddFragmentPage(Fragment Frag, String Title){
            MyFragment.add(Frag);
            MyPageTittle.add(Title);
        }

        @Override
        public Fragment getItem(int position) {
            return MyFragment.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return MyPageTittle.get(position);
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_patient, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
