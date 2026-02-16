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

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.myhome.R;

public class MainActivity extends AppCompatActivity {

    private static final int SMS_PERMISSION_CODE = 100;
    private static final int INTERNET_PERMISSION_CODE = 101;
    private static final String USER_PHONE_NUMBER = "0798088797"; // Replace with dynamic phone number if needed

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get reference to the payment button
        Button payButton = findViewById(R.id.PayButton); // Ensure you have a button with this ID

        // Check for SMS and Internet permissions
        checkForPermissions();

        // Set up the onClickListener for the payment button
        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPhoneNumberDialog(); // Prompt for phone number first
            }
        });
    }

    // Function to display a phone number input dialog
    private void showPhoneNumberDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Enter your phone number");

        final EditText phoneInput = new EditText(MainActivity.this);
        phoneInput.setInputType(InputType.TYPE_CLASS_PHONE);
        builder.setView(phoneInput);

        // Set up the OK button
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String phoneNumber = phoneInput.getText().toString();
                showPasswordDialog(phoneNumber); // Prompt for password next
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
    private void showPasswordDialog(final String phoneNumber) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Enter your password");

        final EditText passwordInput = new EditText(MainActivity.this);
        passwordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(passwordInput);

        // Set up the OK button
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String password = passwordInput.getText().toString();
                processPayment(phoneNumber, password); // Process payment
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
    private void processPayment(String phoneNumber, String password) {
        if (password.isEmpty()) {
            Toast.makeText(MainActivity.this, "Password cannot be empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate against stored phone number
        if (phoneNumber.equals(USER_PHONE_NUMBER)) {
            // Call MpesaApi to process the payment
            com.example.roleselectionactivity.MpesaApi mpesaApi = new com.example.roleselectionactivity.MpesaApi();
            String amount = "1000"; // Set the amount for the payment
            boolean paymentSuccess = mpesaApi.makePayment("Rent", password, amount);

            if (paymentSuccess) {
                Toast.makeText(MainActivity.this, "Payment successful!", Toast.LENGTH_LONG).show();
                sendConfirmationSMS("John Doe", "House No. 12", "Rent");
            } else {
                Toast.makeText(MainActivity.this, "Payment failed. Please try again.", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(MainActivity.this, "Incorrect phone number, please try again.", Toast.LENGTH_SHORT).show();
        }
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
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, SMS_PERMISSION_CODE);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, INTERNET_PERMISSION_CODE);
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
