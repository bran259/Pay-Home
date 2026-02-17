package com.example.payhome.MpesaApi;

import android.util.Base64;
import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MpesaApi {

    private static final String BASE_URL = "https://sandbox.safaricom.co.ke"; // Use production URL for live
    private static final String SHORTCODE = "601234"; // Replace with your actual shortcode
    private static final String LIPA_NA_M_PESA_URL = BASE_URL + "/mpesa/stkpush/v1/processrequest";
    private static final String CONSUMER_KEY = "URgRBWsQEAfdRGENLQjkKZFWNQwzM5lpANv3vwfe6mwmu7A6"; // Replace with your actual consumer key
    private static final String CONSUMER_SECRET = "TOcB0RRm9yhNy4xK4S0KJ2IoVBjJ5QuE7ifkLeYbdtDL6p168F7UXRrdeCkl2Psj"; // Replace with your actual consumer secret
    private static final String PHONE_NUMBER = "0798088797"; // Replace with your actual phone number
    private static final String CALLBACK_URL = "http://localhost:3000/mpesa/callback"; // Replace with your actual callback URL

    private OkHttpClient client = new OkHttpClient();

    // Method to get the access token
    private String getAccessToken() throws IOException {
        String credentials = CONSUMER_KEY + ":" + CONSUMER_SECRET;
        String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

        Request request = new Request.Builder()
                .url(BASE_URL + "/oauth/v1/generate?grant_type=client_credentials")
                .addHeader("Authorization", auth) // Set the Authorization header correctly
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                // Extract access token from response
                JSONObject json = new JSONObject(responseBody);
                return json.getString("access_token"); // Return the actual access token
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    // Method to make payment
    public boolean makePayment(String utilityType, String password, String amount) {
        try {
            int paymentAmount = Integer.parseInt(amount); // Assuming amount is passed as a string

            String accessToken = getAccessToken();
            if (accessToken == null) {
                System.out.println("Failed to retrieve access token.");
                return false; // Handle error getting access token
            }

            // Set up the payment request JSON
            String jsonBody = "{"
                    + "\"BusinessShortCode\": \"" + SHORTCODE + "\","
                    + "\"Password\": \"" + password + "\","
                    + "\"Timestamp\": \"" + getTimestamp() + "\","
                    + "\"TransactionType\": \"CustomerPayBillOnline\","
                    + "\"Amount\": " + paymentAmount + ","
                    + "\"PartyA\": \"" + PHONE_NUMBER + "\","
                    + "\"PartyB\": \"" + SHORTCODE + "\","
                    + "\"PhoneNumber\": \"" + PHONE_NUMBER + "\","
                    + "\"CallBackURL\": \"" + CALLBACK_URL + "\","
                    + "\"AccountReference\": \"" + utilityType + "\","
                    + "\"TransactionDesc\": \"" + utilityType + "\""
                    + "}";

            System.out.println("Attempting to make payment:");
            System.out.println("Amount: " + paymentAmount);
            System.out.println("Shortcode: " + SHORTCODE);
            System.out.println("Request Body: " + jsonBody);

            Request request = new Request.Builder()
                    .url(LIPA_NA_M_PESA_URL)
                    .post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonBody))
                    .addHeader("Authorization", "Bearer " + accessToken)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    // Log the response body for debugging
                    String responseBody = response.body().string(); // Capture the response body
                    System.out.println("Payment failed: " + responseBody);
                    return false; // Handle error appropriately
                }
                return true; // Payment successful
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    // Helper method to generate timestamp
    private String getTimestamp() {
        // Generate a timestamp in the required format (e.g., "YYYYMMDDHHMMSS")
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyyMMddHHmmss", java.util.Locale.getDefault());
        return sdf.format(new java.util.Date());
    }
}
