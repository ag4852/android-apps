package com.example.skateparkapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    // for XML objects
    private EditText nameEt, emailEt, passwordEt, ageEt;
    private Button registerBtn;
    private RadioGroup genderRg;
    private RadioButton selectedGenderRtn;


    //for Firebase variable
    private FirebaseFirestore dbFb;
    private FirebaseAuth authFb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nameEt = findViewById(R.id.registerPage_name_et);
        emailEt = findViewById(R.id.registerPage_email_et);
        passwordEt = findViewById(R.id.registerPage_password_et);
        ageEt = findViewById(R.id.registerPage_age_et);
        genderRg = findViewById(R.id.registerPage_gender_rg);

        dbFb = FirebaseFirestore.getInstance();
        authFb = FirebaseAuth.getInstance();

        registerBtn = findViewById(R.id.registerPage_register_btn);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        try {
            if (authFb.getCurrentUser() != null) {
                authFb.signOut();
            }
            if( authFb.getCurrentUser() == null
                    && !nameEt.getText().toString().isEmpty()
                    && !emailEt.getText().toString().isEmpty()
                    && !passwordEt.getText().toString().isEmpty()
                    && !ageEt.getText().toString().isEmpty() )
            {
                authFb.createUserWithEmailAndPassword(emailEt.getText().toString(), passwordEt.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                selectedGenderRtn = findViewById(genderRg.getCheckedRadioButtonId());
                                Skater newSkater = new Skater(emailEt.getText().toString(),
                                        nameEt.getText().toString(),
                                        passwordEt.getText().toString(),
                                        ageEt.getText().toString(),
                                        selectedGenderRtn.getText().toString(),
                                        null
                                        );

                                dbFb.collection("skaters").document(emailEt.getText().toString()).set(newSkater)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(RegisterActivity.this, "User is registered ", Toast.LENGTH_SHORT).show();
                                                if (authFb.getCurrentUser() != null) {
                                                    authFb.signOut();
                                                }
                                                // redirect user to login page
                                                Toast.makeText(RegisterActivity.this, "User registered Successfully", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(RegisterActivity.this, "Fails to save user to the database " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(RegisterActivity.this, "Fails to create user "+e.getMessage(), Toast.LENGTH_SHORT ).show();
                            }
                        });
            }
            else {
                Toast.makeText(RegisterActivity.this, "Invalid Input, Please try again", Toast.LENGTH_SHORT ).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Register User:"+e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}