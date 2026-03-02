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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.payhome.R;
import com.example.payhome.adapters.PaymentHistoryAdapter;
import com.example.payhome.models.Payment;
import com.example.payhome.utils.PaymentData;
import androidx.cardview.widget.CardView;

import java.util.List;

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

        // Check for SMS and Internet permissions
        checkForPermissions();

        // Set up payment cards click listeners
        setupPaymentCards();

        // Set up payment history
        setupPaymentHistory();

        Log.i(TAG, "MainActivity setup completed");
    }

    private void setupPaymentCards() {
        // Find payment cards container
        View paymentCards = findViewById(R.id.payment_cards);
        
        // Get individual card views from the LinearLayout
        if (paymentCards instanceof android.widget.LinearLayout) {
            android.widget.LinearLayout cardsLayout = (android.widget.LinearLayout) paymentCards;
            
            // Rent card (first child)
            CardView rentCardView = (CardView) cardsLayout.getChildAt(0);
            rentCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Pay Rent card clicked");
                    showPhoneNumberDialog("Rent");
                }
            });

            // Electricity card (second child)
            CardView electricityCardView = (CardView) cardsLayout.getChildAt(1);
            electricityCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Pay Electricity card clicked");
                    showPhoneNumberDialog("Electricity");
                }
            });

            // Water card (third child)
            CardView waterCardView = (CardView) cardsLayout.getChildAt(2);
            waterCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Pay Water card clicked");
                    showPhoneNumberDialog("Water");
                }
            });
        }
    }

    private void setupPaymentHistory() {
        RecyclerView paymentHistoryRecycler = findViewById(R.id.payment_history_recycler);
        
        // Get mock payment data
        List<Payment> payments = PaymentData.getMockPayments();
        
        // Create and set adapter
        PaymentHistoryAdapter adapter = new PaymentHistoryAdapter(payments);
        paymentHistoryRecycler.setAdapter(adapter);
        paymentHistoryRecycler.setLayoutManager(new LinearLayoutManager(this));
        
        Log.d(TAG, "Payment history setup completed with " + payments.size() + " items");
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
