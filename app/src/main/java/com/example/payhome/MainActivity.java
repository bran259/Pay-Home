package com.example.payhome;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import android.app.Activity;
import android.os.Bundle;

import android.util.Log;

import com.example.payhome.R;

public class MainActivity extends Activity {

    private static final int SMS_PERMISSION_CODE = 100;
    private static final int INTERNET_PERMISSION_CODE = 101;
    private static final String USER_PHONE_NUMBER = "0798088797"; // Replace with dynamic phone number if needed
    private static final String TAG = "PayHomeMainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "MainActivity onCreate started");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "Layout set successfully");

        // Get reference to the payment buttons
        Button payButton = findViewById(R.id.PayButton); // Pay Rent button
        Button payStimaButton = findViewById(R.id.PayStima); // Pay Electricity button
        Button payWaterButton = findViewById(R.id.PayWater); // Pay Water button

        // Check for SMS and Internet permissions
        checkForPermissions();

        // Set up the onClickListener for the Pay Rent button
        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Pay Rent button clicked");
                showPhoneNumberDialog("Rent"); // Prompt for phone number first
            }
        });

        // Set up the onClickListener for the Pay Electricity button
        payStimaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Pay Electricity button clicked");
                showPhoneNumberDialog("Electricity"); // Prompt for phone number first
            }
        });

        // Set up the onClickListener for the Pay Water button
        payWaterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Pay Water button clicked");
                showPhoneNumberDialog("Water"); // Prompt for phone number first
            }
        });
        Log.i(TAG, "MainActivity setup completed");
    }

    // Function to display a phone number input dialog
    private void showPhoneNumberDialog(final String paymentType) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Enter your phone number for " + paymentType + " payment");

        final EditText phoneInput = new EditText(MainActivity.this);
        phoneInput.setInputType(InputType.TYPE_CLASS_PHONE);
        builder.setView(phoneInput);

        // Set up the OK button
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String phoneNumber = phoneInput.getText().toString();
                showPasswordDialog(phoneNumber, paymentType); // Prompt for password next
            }
        });

        // Set up the Cancel button
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    // Function to display a password input dialog
    private void showPasswordDialog(final String phoneNumber, final String paymentType) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Enter your password for " + paymentType + " payment");

        final EditText passwordInput = new EditText(MainActivity.this);
        passwordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(passwordInput);

        // Set up the OK button
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String password = passwordInput.getText().toString();
                processPayment(phoneNumber, password, paymentType); // Process payment
            }
        });

        // Set up the Cancel button
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    // Process payment logic
    private void processPayment(String phoneNumber, String password, String paymentType) {
        Log.i(TAG, "Processing " + paymentType + " payment for phone: " + phoneNumber);
        
        if (password.isEmpty()) {
            Log.e(TAG, "Password is empty - payment failed");
            Toast.makeText(MainActivity.this, "Password cannot be empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate against stored phone number
        if (phoneNumber.equals(USER_PHONE_NUMBER)) {
            Log.d(TAG, "Phone number validated successfully");
            // Call MpesaApi to process the payment
            com.example.payhome.MpesaApi.MpesaApi mpesaApi = new com.example.payhome.MpesaApi.MpesaApi();
            String amount = "1000"; // Set the amount for the payment
            Log.d(TAG, "Initiating M-Pesa payment of amount: " + amount + " for " + paymentType);
            boolean paymentSuccess = mpesaApi.makePayment(paymentType, password, amount);

            if (paymentSuccess) {
                Log.i(TAG, paymentType + " payment successful - sending confirmation SMS");
                Toast.makeText(MainActivity.this, paymentType + " payment successful!", Toast.LENGTH_LONG).show();
                sendConfirmationSMS("John Doe", "House No. 12", paymentType);
            } else {
                Log.e(TAG, paymentType + " payment failed - M-Pesa transaction unsuccessful");
                Toast.makeText(MainActivity.this, paymentType + " payment failed. Please try again.", Toast.LENGTH_LONG).show();
            }
        } else {
            Log.w(TAG, "Phone number validation failed - expected: " + USER_PHONE_NUMBER + ", got: " + phoneNumber);
            Toast.makeText(MainActivity.this, "Incorrect phone number, please try again.", Toast.LENGTH_SHORT).show();
        }
        Log.i(TAG, paymentType + " payment processing completed");
    }

    // Function to send confirmation SMS
    private void sendConfirmationSMS(String tenantName, String houseNumber, String utilityType) {
        SmsManager smsManager = SmsManager.getDefault();
        String message = "Thank you " + tenantName + ". We have received your payment for " + utilityType + " for " + houseNumber;

        try {
            smsManager.sendTextMessage(USER_PHONE_NUMBER, null, message, null, null);
            Toast.makeText(MainActivity.this, "Confirmation SMS sent.", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "Failed to send SMS.", Toast.LENGTH_SHORT).show();
        }
    }

    // Check for SMS and Internet permissions
    private void checkForPermissions() {
        if (checkSelfPermission(Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.SEND_SMS}, SMS_PERMISSION_CODE);
        }

        if (checkSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.INTERNET}, INTERNET_PERMISSION_CODE);
        }
    }

    // Handle the user's response to the permission request
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == SMS_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
            } else {
                Toast.makeText(this, "SMS permission is required to send messages", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == INTERNET_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Internet permission granted
            } else {
                Toast.makeText(this, "Internet permission is required for app functionality", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
