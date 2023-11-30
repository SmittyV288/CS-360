package com.cs360.myapplication;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener {

    // Declare SensorManager variables.
    private SensorManager sensorManager;
    private Sensor lightSensor;

    // Declare TextView for sensor level
    private TextView lightSensorTextView;

    // Declare ProgressBar for level
    private ProgressBar lightSensorDial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set TextView and ProgressBar from the layout to variables
        lightSensorTextView = findViewById(R.id.lightSensorTextView);
        lightSensorDial = findViewById(R.id.lightSensorDial);

        // Initialize SensorManager and set lightSensor variable.
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        // Check if lightSensor is null, display error and hide progressbar if true.
        if (lightSensor == null) {
            lightSensorTextView.setText("Ambient light sensor not found");
            lightSensorDial.setVisibility(ProgressBar.INVISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // If lightSensor is not null, register sensor listener.
        if (lightSensor != null) {
            sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister listener if paused
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // Check if light levels change.
        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            // Assign sensor data to variable
            float lux = event.values[0];
            // Update TextView with the current light level.
            lightSensorTextView.setText("Light Level: " + lux + " lux");
            // Update the ProgressBar to display the current light level.
            lightSensorDial.setProgress((int) lux);
        }
    }

    // Android Studio told me to implement the following methods.
    // onAccuarcyChanged checks if the accuracy of the sensor changed.
    // onPointerCaptureChanged is called when pointer capture is enabled or disabled in window
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}
