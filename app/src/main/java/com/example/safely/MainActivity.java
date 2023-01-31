package com.example.safely;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class   MainActivity extends AppCompatActivity {
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    ImageView googleBtn;
    Button logsignup;
    Button loginbtn;
    EditText username;
    EditText passwordi;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        googleBtn = findViewById(R.id.google_btn);
        logsignup=findViewById(R.id.logsignup);
        mAuth = FirebaseAuth.getInstance();
        loginbtn = findViewById(R.id.loginbtn);
        username = findViewById(R.id.username);
        passwordi = findViewById(R.id.passwordi);

        loginbtn.setOnClickListener(view -> {
            loginUser();
    });

        logsignup.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this,SignUp.class));
        });

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this,gso);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if(acct!=null){
            navigateToSecondActivity();
        }


        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });



    }

    private void loginUser(){
        String email = username.getText().toString();
        String password = passwordi.getText().toString();

        if (TextUtils.isEmpty(email)){
            username.setError("Email cannot be empty");
            username.requestFocus();
        }else if (TextUtils.isEmpty(password)){
            passwordi.setError("Password cannot be empty");
            passwordi.requestFocus();
        }else{
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(MainActivity.this, "User logged in successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this, SecondActivity.class));
                    }else{
                        Toast.makeText(MainActivity.this, "Log in error", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    void signIn(){
        Intent signInIntent = gsc.getSignInIntent();
        startActivityForResult(signInIntent,1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                task.getResult(ApiException.class);
                navigateToSecondActivity();
            } catch (ApiException e) {
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }

    }
    void navigateToSecondActivity(){
        finish();
        Intent intent = new Intent(MainActivity.this,SecondActivity.class);
        startActivity(intent);
    }

}