package com.example.safely;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUp extends AppCompatActivity {
    FirebaseAuth mAuth;
    Button signupbtn;
    TextView loginin;
    EditText usernameu;
    EditText passwordu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        usernameu=findViewById(R.id.usernameu);
        passwordu=findViewById(R.id.passwordu);
        loginin = findViewById(R.id.loginin);
        signupbtn = findViewById(R.id.signupbtn);
        mAuth = FirebaseAuth.getInstance();

        signupbtn.setOnClickListener(view ->{
            createUser();
        });

        loginin.setOnClickListener(view -> {
            startActivity(new Intent(SignUp.this, MainActivity.class));
        });
    }

    private void createUser(){
        String email = usernameu.getText().toString();
        String password = passwordu.getText().toString();

        if (TextUtils.isEmpty(email)){
            usernameu.setError("Email cannot be empty");
            usernameu.requestFocus();
        }else if (TextUtils.isEmpty(password)){
            passwordu.setError("Password cannot be empty");
            passwordu.requestFocus();
        }else{
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(SignUp.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignUp.this, MainActivity.class));
                    }else{
                        Toast.makeText(SignUp.this, "Registration error:" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}