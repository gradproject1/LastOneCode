package com.example.user1.urnextapp;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
public class entertainmentSignUp extends AppCompatActivity {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference Patient = database.getReference("Patient");
    private FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();
    private FirebaseUser user = firebaseAuth.getCurrentUser();
    String id;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entertainment_sign_up);
        if (user != null)
        {
            id = user.getUid();
        }
    }
    public void selectItem (View view)
    {
        boolean checked =((CheckBox)view ).isChecked();

        switch (view.getId())
        {
            case R.id.health:
                if (checked)
               Patient.child(id).child("health").setValue("1");
                else
                    Patient.child(id).child("health").removeValue();
                break;
            case R.id.diy:
                if (checked)
                    Patient.child(id).child("diy").setValue("1");
                else
                    Patient.child(id).child("diy").removeValue();
                break;
            case R.id.fashion:
                if (checked)
                    Patient.child(id).child("fashion").setValue("1");
                else
                    Patient.child(id).child("fashion").removeValue();
                break;
            case R.id.sport:
                if (checked)
                    Patient.child(id).child("sport").setValue("1");
                else
                    Patient.child(id).child("sport").removeValue();
                break;
            case R.id.History:
                if (checked)
                    Patient.child(id).child("history").setValue("1");
                else
                    Patient.child(id).child("history").removeValue();
                break;
            case R.id.Technology:
                if (checked)
                    Patient.child(id).child("technology").setValue("1");
                else
                    Patient.child(id).child("technology").removeValue();
                break;
            case R.id.Book:
                if (checked)
                    Patient.child(id).child("book").setValue("1");
                else
                    Patient.child(id).child("book").removeValue();
                break;
            case R.id.travil:
                if (checked)
                    Patient.child(id).child("travil").setValue("1");
                else
                    Patient.child(id).child("travil").removeValue();
                break;
        }
    }
    public void f (View view)
    { user.sendEmailVerification();
        Toast.makeText(this, "A verification link has been sent to your email account" , Toast.LENGTH_LONG).show();
        startActivity(new Intent(this, SignInPage.class));
        finish();
    }

}
