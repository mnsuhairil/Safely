package com.example.safely;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class DetectVibration extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detect_vibration);

        textView=findViewById(R.id.shaking);

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

                    if(floatSum > 14) {

                        textView.setText("Shaking");

                        startActivity(new Intent(DetectVibration.this, CallorMessage.class));
                    }
                    else {
                        textView.setText("Not shaking");
                    }
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };

        sensorManager.registerListener(sensorEventListener,sensorShake,SensorManager.SENSOR_DELAY_NORMAL);
    }
}