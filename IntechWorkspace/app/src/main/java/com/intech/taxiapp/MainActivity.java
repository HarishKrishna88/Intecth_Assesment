package com.intech.taxiapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.location.LocationManager;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.intech.taxiapp.dto.SpeedDetails;
import com.intech.taxiapp.fcm.SpeedLimitManagerClass;
import com.intech.taxiapp.locationdetails.SpeedMonitor;

import javax.annotation.Nullable;

/**
 * This class is for the main laucher class to the application that deals with all
 * the main tasks
 */
public class MainActivity extends AppCompatActivity {
    private SpeedMonitor speedMonitor;
    private SpeedLimitManagerClass speedLimitManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String customerId = "customer123"; // This should be dynamically set
        speedLimitManager = new SpeedLimitManagerClass();

        speedLimitManager.fetchSpeedLimit(customerId, new SpeedLimitManagerClass.SpeedLimitCallback() {
            @Override
            public void onSpeedLimitFetched(SpeedDetails speedLimit) {
                LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                speedMonitor = new SpeedMonitor(MainActivity.this, customerId, speedLimit.getMaxSpeed());

                try {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 2, speedMonitor);
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
    }
}