package com.intech.taxiapp.locationdetails;


import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.intech.taxiapp.fcm.FCMNotifierToRentalCompany;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * This class is to check the Speed using the location Listner and check the
 * status of speed and notifies the user and Company .
 */
public class SpeedMonitor implements LocationListener {
    private double maxSpeed;
    private Context context;
    private String customerId;
    private FCMNotifierToRentalCompany fcmNotifier;

    public SpeedMonitor(Context context, String customerId, double maxSpeed) {
        this.context = context;
        this.customerId = customerId;
        this.maxSpeed = maxSpeed;
        this.fcmNotifier = new FCMNotifierToRentalCompany(context);
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location.hasSpeed()) {
            float speedInKmh = location.getSpeed() * 3.6f;
            if (speedInKmh > maxSpeed) {
                sendSpeedAlert(speedInKmh);
                showDriverWarning();
            }
        }
    }

    private void sendSpeedAlert(double speed) {
        DatabaseReference alertRef = FirebaseDatabase.getInstance().getReference("alerts");
        String alertId = UUID.randomUUID().toString();

        Map<String, Object> alertData = new HashMap<>();
        alertData.put("customerId", customerId);
        alertData.put("speed", speed);
        alertData.put("timestamp", System.currentTimeMillis());

        alertRef.child(alertId).setValue(alertData)
                .addOnSuccessListener(aVoid -> Log.d("SpeedMonitor", "Alert sent to Firebase Realtime DB"))
                .addOnFailureListener(e -> Log.e("SpeedMonitor", "Failed to send alert", e));

        fcmNotifier.sendNotificationToCompany(customerId, speed);
    }

    private void showDriverWarning() {
        Toast.makeText(context, "Slow down! You're exceeding the speed limit!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onProviderDisabled(String provider) {}
}
