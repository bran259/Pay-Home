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
import android.widget.TextView;
import android.widget.Toast;

import android.app.Activity;
import android.os.Bundle;

import android.util.Log;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.payhome.R;
import com.example.payhome.adapters.PaymentHistoryAdapter;
import com.example.payhome.models.Payment;
import com.example.payhome.models.PaymentReceipt;
import com.example.payhome.utils.PaymentData;
import com.example.payhome.dialogs.PaymentMethodDialog;
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

        // Set up analytics button directly in onCreate
        Log.d(TAG, "Setting up analytics button directly...");
        TextView analyticsButton = findViewById(R.id.btn_analytics_simple);
        if (analyticsButton != null) {
            Log.d(TAG, "Analytics button found! Setting click listener");
            analyticsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Analytics button clicked!");
                    android.content.Intent intent = new android.content.Intent(MainActivity.this, AnalyticsActivity.class);
                    startActivity(intent);
                }
            });
        } else {
            Log.e(TAG, "Analytics button NOT found!");
        }

        // Set up payment cards click listeners
        setupPaymentCards();

        // Set up payment history
        setupPaymentHistory();

        Log.i(TAG, "MainActivity setup completed");
    }

    private void setupPaymentCards() {
        try {
            Log.d(TAG, "Setting up payment cards...");
            // Find payment cards container
            View paymentCards = findViewById(R.id.payment_cards);
            
            // Get individual card views from the LinearLayout
            if (paymentCards instanceof android.widget.LinearLayout) {
                android.widget.LinearLayout cardsLayout = (android.widget.LinearLayout) paymentCards;
                
                // Rent card (first child) - Now shows analytics
                CardView rentCardView = (CardView) cardsLayout.getChildAt(0);
                rentCardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, "Rent card clicked - showing analytics");
                        // Show analytics instead of payment dialog
                        android.content.Intent intent = new android.content.Intent(MainActivity.this, AnalyticsActivity.class);
                        startActivity(intent);
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
            Log.d(TAG, "Payment cards setup completed");
        } catch (Exception e) {
            Log.e(TAG, "Error setting up payment cards", e);
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
                showPaymentMethodDialog(phoneNumber, password, paymentType); // Show payment method selection
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

    // Function to show payment method selection dialog
    private void showPaymentMethodDialog(final String phoneNumber, final String password, final String paymentType) {
        PaymentMethodDialog.showPaymentMethodDialog(this, paymentType, phoneNumber, password, 
            new PaymentMethodDialog.PaymentMethodListener() {
                @Override
                public void onPaymentMethodSelected(String method, String paymentType, String phoneNumber, String password) {
                    processPayment(phoneNumber, password, paymentType, method);
                }
            });
    }

    // Process payment logic
    private void processPayment(String phoneNumber, String password, String paymentType, String paymentMethod) {
        Log.i(TAG, "Processing " + paymentType + " payment for phone: " + phoneNumber + " via " + paymentMethod);
        
        if (password.isEmpty()) {
            Log.e(TAG, "Password is empty - payment failed");
            Toast.makeText(MainActivity.this, "Password cannot be empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate against stored phone number
        if (phoneNumber.equals(USER_PHONE_NUMBER)) {
            Log.d(TAG, "Phone number validated successfully");
            
            // Process payment based on method
            boolean paymentSuccess = false;
            String amount = "1000"; // Set the amount for the payment
            
            switch (paymentMethod) {
                case "M-Pesa":
                    // Call MpesaApi to process the payment
                    com.example.payhome.MpesaApi.MpesaApi mpesaApi = new com.example.payhome.MpesaApi.MpesaApi();
                    paymentSuccess = mpesaApi.makePayment(paymentType, password, amount);
                    break;
                case "Bank Card":
                    // Simulate bank card payment
                    paymentSuccess = simulateBankCardPayment(paymentType, amount);
                    break;
                case "Mobile Banking":
                    // Simulate mobile banking payment
                    paymentSuccess = simulateMobileBankingPayment(paymentType, amount);
                    break;
            }

            if (paymentSuccess) {
                Log.i(TAG, paymentType + " payment successful via " + paymentMethod + " - generating receipt");
                
                // Generate payment receipt
                PaymentReceipt receipt = new PaymentReceipt(
                    "TXN" + System.currentTimeMillis(),
                    paymentType,
                    Double.parseDouble(amount),
                    paymentMethod,
                    phoneNumber,
                    "Success"
                );
                
                Toast.makeText(MainActivity.this, paymentType + " payment successful via " + paymentMethod + "!", Toast.LENGTH_LONG).show();
                sendConfirmationSMS("John Doe", "House No. 12", paymentType);
                showPaymentReceipt(receipt);
                
            } else {
                Log.e(TAG, paymentType + " payment failed via " + paymentMethod);
                Toast.makeText(MainActivity.this, paymentType + " payment failed via " + paymentMethod + ". Please try again.", Toast.LENGTH_LONG).show();
            }
        } else {
            Log.w(TAG, "Phone number validation failed - expected: " + USER_PHONE_NUMBER + ", got: " + phoneNumber);
            Toast.makeText(MainActivity.this, "Incorrect phone number, please try again.", Toast.LENGTH_SHORT).show();
        }
        Log.i(TAG, paymentType + " payment processing completed");
    }

    // Simulate bank card payment
    private boolean simulateBankCardPayment(String paymentType, String amount) {
        // Simulate processing delay
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Math.random() > 0.1; // 90% success rate
    }

    // Simulate mobile banking payment
    private boolean simulateMobileBankingPayment(String paymentType, String amount) {
        // Simulate processing delay
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Math.random() > 0.05; // 95% success rate
    }

    // Show payment receipt dialog
    private void showPaymentReceipt(PaymentReceipt receipt) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Payment Receipt");
        
        String receiptText = receipt.getReceiptSummary();
        builder.setMessage(receiptText);
        
        builder.setPositiveButton("Save Receipt", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "Receipt saved successfully!", Toast.LENGTH_SHORT).show();
            }
        });
        
        builder.setNegativeButton("Share Receipt", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                shareReceipt(receipt);
            }
        });
        
        builder.setNeutralButton("Close", null);
        builder.show();
    }

    // Share receipt functionality
    private void shareReceipt(PaymentReceipt receipt) {
        String shareText = receipt.getReceiptSummary();
        
        android.content.Intent shareIntent = new android.content.Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareText);
        shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Payment Receipt - " + receipt.getReceiptId());
        
        startActivity(android.content.Intent.createChooser(shareIntent, "Share Payment Receipt"));
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
