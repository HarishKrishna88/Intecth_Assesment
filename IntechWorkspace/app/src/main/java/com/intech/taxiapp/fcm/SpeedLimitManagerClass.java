package com.intech.taxiapp.fcm;

import com.google.firebase.firestore.*;
import com.intech.taxiapp.dto.SpeedDetails;

/**
 * This class is for getting the updated Speed limit from the Rental company
 *
 */
public class SpeedLimitManagerClass {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public interface SpeedLimitCallback {
        void onSpeedLimitFetched(SpeedDetails speedLimit);
        void onError(Exception e);
    }

    public void fetchSpeedLimit(String customerId, SpeedLimitCallback callback) {
        db.collection("speed_limits").document(customerId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        double maxSpeed = documentSnapshot.getDouble("maxSpeed");
                        callback.onSpeedLimitFetched(new SpeedDetails(customerId, maxSpeed));
                    } else {
                        callback.onError(new Exception("No speed limit found"));
                    }
                })
                .addOnFailureListener(callback::onError);
    }
}

