package com.example.skateparkapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity {

    private Button loginBtn, registerBtn;
    private EditText emailEt, passwordEt;

    private FirebaseAuth authFb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Attach java object to XML
        loginBtn = findViewById(R.id.loginPage_login_btn);
        registerBtn = findViewById(R.id.loginPage_register_btn);
        emailEt = findViewById(R.id.loginPage_email_et);
        passwordEt = findViewById(R.id.loginPage_password_et);

        // Initialize firebase instance
        authFb = FirebaseAuth.getInstance();
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logInUser();
            }
        });

        /*if(authFb.getCurrentUser() != null){
            startActivity(new Intent(MainActivity.this, SkateparksActivity.class));
        }*/
    }

    private void logInUser() {
        try {
            if (authFb.getCurrentUser() != null) {
                authFb.signOut();
            }
            if (!emailEt.getText().toString().isEmpty() && !passwordEt.getText().toString().isEmpty()) {
                if (authFb.getCurrentUser() == null) {
                    authFb.signInWithEmailAndPassword(emailEt.getText().toString(), passwordEt.getText().toString())
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    Toast.makeText(MainActivity.this, "Welcome "+ emailEt.getText().toString(), Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(MainActivity.this, SkateparksActivity.class));
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(MainActivity.this, "Failed to login "+e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                }
            }
            else {
                Toast.makeText(MainActivity.this, "Please fill in Email and Password fields", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "Login Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}