package com.example.user1.urnextapp;

import android.app.ActionBar;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Chronometer;
import android.widget.TextView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
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

public class Nurse extends AppCompatActivity {

    TabLayout MyTabs;
    ViewPager MyPage;
    private static FirebaseDatabase database = FirebaseDatabase.getInstance();
    static DatabaseReference waiting_table = database.getReference("waiting time and queue number");
    static DatabaseReference external_data = database.getReference("ExternalDB");
    static DatabaseReference patient_table = database.getReference("Patient");
    private FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();
    private FirebaseUser user = firebaseAuth.getCurrentUser();
    static String User_ID=" ";
    List<Accept_List_Information> PatientList;
    long count=0,numberofchild=0;
    static Accept_List_Information patient_1, p1;
    static boolean flag=false;
    String Nurse_Name,Doctor_Name;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nurse);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        MyTabs = (TabLayout) findViewById(R.id.MyTabs);
        MyPage = (ViewPager) findViewById(R.id.MyPage);

        MyTabs.setupWithViewPager(MyPage);
        SetUpViewPager(MyPage);



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
                                numberofchild=dataSnapshot.getChildrenCount();
                                showNotification("","New patient arrived");
                            }
                            @Override
                            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                            }

                            @Override
                            public void onChildRemoved(DataSnapshot dataSnapshot) {

                            }

                            @Override
                            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

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
    }

        public void SetUpViewPager (ViewPager viewpage){
        Nurse.MyViewPageAdapter Adapter = new Nurse.MyViewPageAdapter(getSupportFragmentManager());
        Adapter.AddFragmentPage(new AcceptPatient(), "Accept patient Page");
        Adapter.AddFragmentPage(new NDwaiting(), "Waiting Page");
        Adapter.AddFragmentPage(new cancelOrDelay(), "Cancel or Delay Page");
        viewpage.setAdapter(Adapter);
        }

    public class MyViewPageAdapter extends FragmentPagerAdapter {
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
        getMenuInflater().inflate(R.menu.nurse, menu);
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
    public void showNotification(String title, String message) {
// create the notification with sound and light
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, getString(R.string.default_notification_channel_id))
                .setSmallIcon(R.drawable.logo)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true);
        builder.setLights(Color.BLUE, 500, 500);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(alarmSound);

// to open specific page when click on the notification
        Intent resultIntent = new Intent(this,this.getClass());

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_CANCEL_CURRENT
                );
        builder.setContentIntent(resultPendingIntent);

// to give each notification unique id and notification manager
        int notification_id = (int) System.currentTimeMillis();
        NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        manager.notify(notification_id, builder.build());

    }
}
