package com.intech.taxiapp.fcm;



import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * This class will send the notification to the Rental company when the Speed exceeds the limit.
 */
public class FCMNotifierToRentalCompany {
    private static final String FCM_SERVER_URL = "https://fcm.googleapis.com/fcm/send";
    private static final String SERVER_KEY = "SERVER_KEY"; // Replace with  Firebase server key

    private Context context;

    public FCMNotifierToRentalCompany(Context context) {
        this.context = context;
    }

    public void sendNotificationToCompany(String customerId, double speed) {
        JSONObject notification = new JSONObject();
        try {
            notification.put("title", "Speed Limit Exceeded");
            notification.put("body", "Customer " + customerId + " is driving at " + speed + " km/h");

            JSONObject message = new JSONObject();
            message.put("to", "/topics/alerts");
            message.put("notification", notification);

            sendFCMMessage(message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void sendFCMMessage(JSONObject message) {
        RequestQueue queue = Volley.newRequestQueue(context);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, FCM_SERVER_URL, message,
                response -> Log.d("FCM", "Message sent successfully"),
                error -> Log.e("FCM", "Error sending message", error)) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "key=" + SERVER_KEY);
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        queue.add(request);
    }
}
