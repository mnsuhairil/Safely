package com.example.safely;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SecondActivity extends AppCompatActivity {
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    TextView name,email;
    Button signOutBtn;
    FirebaseAuth mAuth;
    Button guardian;
    Button test;
    Button aboutus;
    Button emergency;
    Button profile;
    Button location;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);


        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        signOutBtn = findViewById(R.id.signout);
        mAuth = FirebaseAuth.getInstance();
        guardian=findViewById(R.id.guardian);
        guardian.setOnClickListener(view -> {
            startActivity(new Intent(SecondActivity.this,Guardian.class));
        });
        test=findViewById(R.id.acctest);
        test.setOnClickListener(view -> {
            startActivity(new Intent(SecondActivity.this,DetectVibration.class));
        });
        aboutus=findViewById(R.id.aboutus);
        aboutus.setOnClickListener(view -> {
            startActivity(new Intent(SecondActivity.this,AboutUs.class));
        });
        emergency=findViewById(R.id.emergency);
        emergency.setOnClickListener(view -> {
            startActivity(new Intent(SecondActivity.this,sosActivity.class));
        });
        profile=findViewById(R.id.profile);
        profile.setOnClickListener(view -> {
            startActivity(new Intent(SecondActivity.this,Profile.class));
        });
        location=findViewById(R.id.location);
        location.setOnClickListener(view -> {
            startActivity(new Intent(SecondActivity.this,MapsActivity.class));
        });


        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this,gso);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if(acct!=null){
            String personName = acct.getDisplayName();
            String personEmail = acct.getEmail();
            name.setText(personName);
            email.setText(personEmail);
        }

        signOutBtn.setOnClickListener(view -> {
            mAuth.signOut();
            startActivity(new Intent(SecondActivity.this, MainActivity.class));
        });

        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch simpleSwitch = (Switch) findViewById(R.id.switch1);
        // on below line we are adding check change listener for our switch.
        simpleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // on below line we are checking
                // if switch is checked or not.
                if (isChecked) {
                    // on below line we are setting text
                    // if switch is checked.
                    simpleSwitch.setText("ON");
                    SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

                    Sensor sensorShake = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

                    SensorEventListener sensorEventListener = new SensorEventListener() {
                        @Override
                        public void onSensorChanged(SensorEvent sensorEvent) {
                            if (sensorEvent != null) {
                                float x_accl = sensorEvent.values[0];
                                float y_accl = sensorEvent.values[1];
                                float z_accl = sensorEvent.values[2];

                                float floatSum = Math.abs(x_accl) + Math.abs(y_accl) + Math.abs(z_accl);

                                //if (x_accl > 2 ||
                                //  x_accl < -2 ||
                                //  y_accl > 12 ||
                                //  y_accl < -12 ||
                                //  z_accl > 2 ||
                                //  z_accl < -2) {

                                if(floatSum > 24) {

                                    //textView.setText("Shaking");

                                    startActivity(new Intent(SecondActivity.this, CallorMessage.class));
                                }
                                else {
                                    //textView.setText("Not shaking");
                                }
                            }
                        }

                        @Override
                        public void onAccuracyChanged(Sensor sensor, int i) {

                        }
                    };

                    sensorManager.registerListener(sensorEventListener,sensorShake,SensorManager.SENSOR_DELAY_NORMAL);
                } else {
                    // on below line we are setting text
                    // if switch is unchecked.
                    simpleSwitch.setText("OFF");
                }
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.chat_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user == null){
            startActivity(new Intent(SecondActivity.this,MainActivity.class));
        }
    }

    void signOut(){
        gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Task<Void> task) {
                finish();
                startActivity(new Intent(SecondActivity.this,MainActivity.class));
            }
        });
    }


}