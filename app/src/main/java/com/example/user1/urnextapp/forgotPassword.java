package com.example.user1.urnextapp;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
public class forgotPassword extends AppCompatActivity {
    private EditText inputEmail;
    private Button btnReset;
    private FirebaseAuth auth;
    private TextView signin;
    private TextView signup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        inputEmail = (EditText) findViewById(R.id.editTextresetemail);
        signin = (TextView)findViewById(R.id.signin);
        signup = (TextView)findViewById(R.id.signup);
        btnReset = (Button) findViewById(R.id.btn_reset_password);
        auth = FirebaseAuth.getInstance();
        btnReset.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString().trim();
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                AccessibilityNodeInfo inputPassword = null;
                if (email.isEmpty()) {
                    inputEmail.setError("Please enter your email");}
                else if(!email.matches(emailPattern)){
                    inputEmail.setError("Invalid email address");}
                else if (email.isEmpty()) {
                    inputPassword.setError("Please enter your password");}
                else if (email.length()<5) {
                    inputPassword.setError("Password must to be more than 9 characters");}
                auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                Toast.makeText(forgotPassword.this, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                 } else {
                 Toast.makeText(forgotPassword.this, "Failed to send reset email!", Toast.LENGTH_SHORT).show();}}});
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(forgotPassword.this, PSignUpPage.class);
                startActivity(i);
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ii = new Intent(forgotPassword.this, SignInPage.class);
                startActivity(ii);
            }
        });

    }
}
