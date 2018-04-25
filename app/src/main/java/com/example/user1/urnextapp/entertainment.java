package com.example.user1.urnextapp;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.estimote.coresdk.common.config.EstimoteSDK.getApplicationContext;
public class entertainment extends Fragment {
    // fire base tables
    private static FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();
    private FirebaseUser user = firebaseAuth.getCurrentUser();
    static DatabaseReference patient_table = database.getReference("Patient");
    static DatabaseReference enter_table = database.getReference("entertainment");
    static String User_ID=" ";
    static TextView article;
    //Constructor default
    public entertainment(){};
    // Creating RecyclerView.
    RecyclerView recyclerView;
    // Creating RecyclerView.Adapter.
    RecyclerView.Adapter adapter;
    // Creating List of ImageUploadInfo class.
    List<entertainment_List_Information> list = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View PageOne = inflater.inflate(R.layout.fragment_entertainment, container, false);
article=(TextView) PageOne.findViewById(R.id.article);
        if (user != null) {
            User_ID = user.getUid();
        }
        // Assign id to RecyclerView.
        recyclerView = (RecyclerView) PageOne.findViewById(R.id.recyclerView);
        // Setting RecyclerView size true.
        recyclerView.setHasFixedSize(true);
        // Setting RecyclerView layout as LinearLayout.
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        patient_table.child(User_ID).addValueEventListener(new ValueEventListener() {
            //get patient name and phone number to search about him in appointment data
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final String health = dataSnapshot.child("health").getValue(String.class);
                final String diy = dataSnapshot.child("diy").getValue(String.class);
                final String fashion = dataSnapshot.child("fashion").getValue(String.class);
                final String sport = dataSnapshot.child("sport").getValue(String.class);
                final String technology = dataSnapshot.child("technology").getValue(String.class);
                final String history = dataSnapshot.child("history").getValue(String.class);
                final String book = dataSnapshot.child("book").getValue(String.class);
                final String travil = dataSnapshot.child("travil").getValue(String.class);
                assert health != null;
                assert diy != null;
                assert fashion != null;
                assert sport != null;
                assert technology != null;
                assert history != null;
                assert book != null;
                assert travil != null;
                if (health.equals("1")) {
                    // Adding Add Value Event Listener to databaseReference.
                    enter_table.child("health").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                                String url = postSnapshot.child("url").getValue(String.class);
                                String articleLink =postSnapshot.child("article").getValue(String.class);
                                entertainment_List_Information imageUploadInfo = new entertainment_List_Information(url,articleLink);
                             list.add(imageUploadInfo);
                            }
                           adapter = new RecyclerViewAdapter(getContext(), list);
                           recyclerView.setAdapter(adapter);
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }
                else if (diy.equals("1"))
                {
                    // Adding Add Value Event Listener to databaseReference.
                    enter_table.child("diy").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                                String url = postSnapshot.child("url").getValue(String.class);
                                String articleLink =postSnapshot.child("article").getValue(String.class);
                                entertainment_List_Information imageUploadInfo = new entertainment_List_Information(url,articleLink);
                               list.add(imageUploadInfo);
                            }
                          adapter = new RecyclerViewAdapter(getContext(), list);
                          recyclerView.setAdapter(adapter);
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }
                else if (fashion.equals("1"))
                {
                    // Adding Add Value Event Listener to databaseReference.
                    enter_table.child("fashion").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                                String url = postSnapshot.child("url").getValue(String.class);
                                String articleLink =postSnapshot.child("article").getValue(String.class);
                                entertainment_List_Information imageUploadInfo = new entertainment_List_Information(url,articleLink);
                             list.add(imageUploadInfo);
                            }
                            adapter = new RecyclerViewAdapter(getContext(), list);
                          recyclerView.setAdapter(adapter);
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }
                else if (sport.equals("1"))
                {
                    // Adding Add Value Event Listener to databaseReference.
                    enter_table.child("sport").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                                String url = postSnapshot.child("url").getValue(String.class);
                                String articleLink =postSnapshot.child("article").getValue(String.class);
                                entertainment_List_Information imageUploadInfo = new entertainment_List_Information(url,articleLink);
                                list.add(imageUploadInfo);
                            }
                          adapter = new RecyclerViewAdapter(getContext(), list);
                         recyclerView.setAdapter(adapter);
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }
                else if (technology.equals("1"))
                {
                    // Adding Add Value Event Listener to databaseReference.
                    enter_table.child("technology").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                                String url = postSnapshot.child("url").getValue(String.class);
                                String articleLink =postSnapshot.child("article").getValue(String.class);
                                entertainment_List_Information imageUploadInfo = new entertainment_List_Information(url,articleLink);
                                list.add(imageUploadInfo);
                            }
                            adapter = new RecyclerViewAdapter(getContext(), list);
                            recyclerView.setAdapter(adapter);
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }
                else if (history.equals("1"))
                {
                    // Adding Add Value Event Listener to databaseReference.
                    enter_table.child("history").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                                String url = postSnapshot.child("url").getValue(String.class);
                                String articleLink =postSnapshot.child("article").getValue(String.class);
                                entertainment_List_Information imageUploadInfo = new entertainment_List_Information(url,articleLink);
                                list.add(imageUploadInfo);
                            }
                            adapter = new RecyclerViewAdapter(getContext(), list);
                            recyclerView.setAdapter(adapter);
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }
                else if (travil.equals("1"))
                {
                    // Adding Add Value Event Listener to databaseReference.
                    enter_table.child("travil").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                                String url = postSnapshot.child("url").getValue(String.class);
                                String articleLink =postSnapshot.child("article").getValue(String.class);
                                entertainment_List_Information imageUploadInfo = new entertainment_List_Information(url,articleLink);
                                list.add(imageUploadInfo);
                            }
                            adapter = new RecyclerViewAdapter(getContext(), list);
                            recyclerView.setAdapter(adapter);
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }
                else if (book.equals("1"))
                {
                    // Adding Add Value Event Listener to databaseReference.
                    enter_table.child("book").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                                String url = postSnapshot.child("url").getValue(String.class);
                                String articleLink =postSnapshot.child("article").getValue(String.class);
                                entertainment_List_Information imageUploadInfo = new entertainment_List_Information(url,articleLink);
                                list.add(imageUploadInfo);
                            }
                            adapter = new RecyclerViewAdapter(getContext(), list);
                            recyclerView.setAdapter(adapter);
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }//app
                });//app
                return PageOne;}}
