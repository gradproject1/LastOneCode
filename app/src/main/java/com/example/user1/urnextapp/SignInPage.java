package com.example.user1.urnextapp;
//android
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
//fire base
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;
public class SignInPage extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference ;
    FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseFirestore fire_store; //to store token id in his document
    EditText inputEmail;
    EditText inputPassword;
    Button sign_in;
    TextView sign_up;
    TextView forgotPassword;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference Patient = database.getReference("Patient");
    DatabaseReference external = database.getReference("ExternalDB");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_page);
        inputEmail = (EditText)findViewById(R.id.editTextEmailAddress);
        inputPassword = (EditText)findViewById(R.id.editText_Password);
        sign_in = (Button)findViewById(R.id.button3);
        sign_up = (TextView)findViewById(R.id.textView3);
        forgotPassword = (TextView)findViewById(R.id.textView);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        fire_store= FirebaseFirestore.getInstance(); //fire store instance
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    // User is signed in
                } else {
                    // User is signed out
                }
            }
        };
        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String e = inputEmail.getText().toString();
                final String p = inputPassword.getText().toString();
                // pattern for validate the email format
                final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                final String emailAdminPattern = "[HAE]+[0-9]+@[a-z]+\\.+[a-z]+"; //admin email format
                final String emailDoctorPattern = "[HDE]+[0-9]+@[a-z]+\\.+[a-z]+"; //doctor email format
                final String emailNursePattern = "[HNE]+[0-9]+@[a-z]+\\.+[a-z]+"; //nurse email format
// check if email not empty and check email format
                if (e.isEmpty()) {
                    inputEmail.setError("Please enter your email");
                }
                else if(!e.matches(emailPattern)){
                    inputEmail.setError("Invalid email address");
                }
                else if (p.isEmpty()) {
                    inputPassword.setError("Please enter your password");
                }
                else if (p.length()<6) {
                    inputPassword.setError("Password must to be more than 6 characters");
                }
                else { //start sign in
                    firebaseAuth.signInWithEmailAndPassword(e, p).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                // get user token id so, we can send to him a notification
                                String Token_ID= FirebaseInstanceId.getInstance().getToken();
                                Map<String,Object> token_map= new HashMap<>();
                                token_map.put("Token_ID", Token_ID);
                                fire_store.collection("Usres").document(uid).set(token_map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                    }
                                });
                                    // if user verify his email allow him to login else send email for verification
                                    //depend on email format open specific page
                                    if(e.matches(emailAdminPattern)){
                                        Intent i = new Intent(SignInPage.this, Admin.class);
                                        startActivity(i);
                                    }
                                    else if(e.matches(emailDoctorPattern)){
                                        Intent i = new Intent(SignInPage.this, Doctor.class);
                                        startActivity(i);
                                    }
                                    else if(e.matches(emailNursePattern)){
                                        Intent i = new Intent(SignInPage.this, Nurse.class);
                                        startActivity(i);
                                    }
                                    else {
                                        if(user.isEmailVerified() ){
                                            Patient.child(uid).addValueEventListener(new ValueEventListener() {
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    final String pname = dataSnapshot.child("Name").getValue(String.class);
                                                    final String pphone = dataSnapshot.child("Phone").getValue(String.class);

                                                    external.child("Appointment").child("Dental clinic").child(pphone).addValueEventListener(new ValueEventListener() {
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            String name = dataSnapshot.child ("Name").getValue(String.class);
                                                            String dname = dataSnapshot.child("Doctor Name").getValue(String.class);
                                                           String  papp = dataSnapshot.child("appTime").getValue(String.class);
                                                            if (dname != null && papp != null && pname.equals(name)) {
                                                               if(MyBeacon.flag==1){
                                                                Intent i = new Intent(SignInPage.this, Patient.class);
                                                                startActivity(i);}
                                                               else{
                                                                    Toast.makeText(SignInPage.this,"You are not in the range" , Toast.LENGTH_LONG).show();

                                                                }
                                                           }else
                                                            {
                                                                Toast.makeText(SignInPage.this,"You don't have an appointment" , Toast.LENGTH_LONG).show();
                                                                startActivity(new Intent(SignInPage.this, WelcomePage.class));
                                                                finish();
                                                            }
                                                        }
                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {
                                                        }
                                                    });}
                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {
                                                }
                                            });}
                                         else if (!user.isEmailVerified()) {
                                                Toast.makeText(SignInPage.this, "Please confirm your email", Toast.LENGTH_SHORT).show();
                                                user.sendEmailVerification();
                                    }}}
                            if (!task.isSuccessful()) {
                            }}
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) { // exception of login
                            if (e instanceof FirebaseAuthInvalidUserException) {
                                Toast.makeText(SignInPage.this, "This user not found, create a new account", Toast.LENGTH_SHORT).show();
                            }
                            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(SignInPage.this, "The password is invalid, please try valid password", Toast.LENGTH_SHORT).show();
                            }
                            if (e instanceof FirebaseNetworkException) {
                                Toast.makeText(SignInPage.this, "Please check your connection", Toast.LENGTH_SHORT).show();
                            }}});}
            }
        });
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //open sign up if user dos'nt have an account
                Intent i = new Intent(SignInPage.this, PSignUpPage.class);
                startActivity(i);
            }
        });
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //allow the user t change his password
                Intent ii = new Intent(SignInPage.this, forgotPassword.class);
                startActivity(ii);
            }
        });
    }
}
