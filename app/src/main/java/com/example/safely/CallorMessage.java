package com.example.safely;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CallorMessage extends AppCompatActivity {

    Button close;
    private FirebaseUser fUser;
    static int PERMISSION_CODE = 100;
    Button btnsendlc;
    private final static int SEND_SMS_PERMISSION_REQ = 1;
    private LocationRequest locationRequest;
//    private String phoneno;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_callor_message);

        Button call;
        call = findViewById(R.id.call);
        btnsendlc = findViewById(R.id.sendlc);
        btnsendlc.setEnabled(false);

       /* if (ContextCompat.checkSelfPermission(CallorMessage.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(CallorMessage.this, new String[]{Manifest.permission.CALL_PHONE}, PERMISSION_CODE);

            btnsendlc = findViewById(R.id.sendlc);
            btnsendlc.setOnClickListener(view -> {
                startActivity(new Intent(CallorMessage.this, MapsActivity.class));
            });
        }*/

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Get current user
                fUser = FirebaseAuth.getInstance().getCurrentUser();
                //retrieve data

                FirebaseDatabase.getInstance().getReference().child(fUser.getUid()).child("GuardianInfo").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        GuardianConstructor Guser = snapshot.getValue(GuardianConstructor.class);
                        if (Guser != null) {
                            //String phoneno = phoneNo.getText().toString();
                            Intent i = new Intent(Intent.ACTION_CALL);
                            i.setData(Uri.parse("tel:" + Guser.getGuardianPhone()));
                            startActivity(i);

                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


                close = findViewById(R.id.close);
                close.setOnClickListener(view -> {
                    startActivity(new Intent(CallorMessage.this, sosActivity.class));
                });

            }
        });

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);
       // sendlc = findViewById(R.id.sendlc);
        if (checkPermission(android.Manifest.permission.SEND_SMS)) {
            btnsendlc.setEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.SEND_SMS}, SEND_SMS_PERMISSION_REQ);
        }
        btnsendlc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getCurrentLocation();

                //latlngConst test = new latlngConst();
                //System.out.println( test.latitude + "test");

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {

                getCurrentLocation();
            }
        }
    }

    private void getCurrentLocation() {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(CallorMessage.this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                if (isGPSEnabled()) {

                    LocationServices.getFusedLocationProviderClient(CallorMessage.this)
                            .requestLocationUpdates(locationRequest, new LocationCallback() {
                                @Override
                                public void onLocationResult(@NonNull LocationResult locationResult) {
                                    super.onLocationResult(locationResult);

                                    LocationServices.getFusedLocationProviderClient(CallorMessage.this)
                                            .removeLocationUpdates(this);

                                    if (locationResult.getLocations().size() > 0) {

                                        int index = locationResult.getLocations().size() - 1;
                                        double latitude = locationResult.getLocations().get(index).getLatitude();
                                        double longitude = locationResult.getLocations().get(index).getLongitude();

                                        //System.out.println("im here!!!" + latitude + " " + longitude);
                                        //new latlngConst(latitude,longitude);
                                        //x.latlngTest(latitude,longitude);
//                                        AddressText.setText("Latitude: "+ latitude + "\n" + "Longitude: "+ longitude);
                                        fUser = FirebaseAuth.getInstance().getCurrentUser();

                                        FirebaseDatabase.getInstance().getReference().child(fUser.getUid()).child("GuardianInfo").addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                GuardianConstructor Guser = snapshot.getValue(GuardianConstructor.class);
                                                if (Guser != null) {
                                                    String phoneno = Guser.getGuardianPhone();
                                                    String s1 = phoneno;
                                                    String s3 = "Hey I am in DANGER!! HELP ME!! \nMy Location : \n";
                                                    String s2 = s3 + "https://www.google.com/maps/search/?api=1&query=" + latitude + "," + longitude;

                                                    if (!TextUtils.isEmpty(s1) && !TextUtils.isEmpty(s2)) {

                                                        if (checkPermission(Manifest.permission.SEND_SMS)) {
                                                            SmsManager smsManager = SmsManager.getDefault();
                                                            smsManager.sendTextMessage(s1, null, s2, null, null);
                                                            Toast.makeText(CallorMessage.this,"Your Location sent",Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            Toast.makeText(CallorMessage.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                                                        }
                                                    } else {
                                                        Toast.makeText(CallorMessage.this, "Permission denied", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }
                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                            }
                                        });


                                    }
                                }
                            }, Looper.getMainLooper());

                } else {
                    turnOnGPS();
                }

            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }


    private void turnOnGPS() {


        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(getApplicationContext())
                .checkLocationSettings(builder.build());

        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {

                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    Toast.makeText(CallorMessage.this, "GPS is already tured on", Toast.LENGTH_SHORT).show();

                } catch (ApiException e) {

                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                                resolvableApiException.startResolutionForResult(CallorMessage.this, 2);
                            } catch (IntentSender.SendIntentException ex) {
                                ex.printStackTrace();
                            }
                            break;

                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            //Device does not have location
                            break;
                    }
                }
            }
        });

    }

    private boolean isGPSEnabled() {
        LocationManager locationManager = null;
        boolean isEnabled = false;

        if (locationManager == null) {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        }

        isEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isEnabled;

    }

    private boolean checkPermission(String sendSms) {

        int checkpermission = ContextCompat.checkSelfPermission(this, sendSms);
        return checkpermission == PackageManager.PERMISSION_GRANTED;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case SEND_SMS_PERMISSION_REQ:
                if (grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    btnsendlc.setEnabled(true);
                }
                break;
        }
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (isGPSEnabled()) {

                    getCurrentLocation();

                } else {

                    turnOnGPS();
                }
            }
        }
    }
}