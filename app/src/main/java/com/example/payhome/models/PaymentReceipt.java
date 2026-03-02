package com.example.payhome.models;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class PaymentReceipt {
    private String receiptId;
    private String transactionId;
    private String paymentType;
    private double amount;
    private String paymentMethod;
    private String phoneNumber;
    private Date timestamp;
    private String status;
    private String qrCodeData;

    public PaymentReceipt(String transactionId, String paymentType, double amount, 
                         String paymentMethod, String phoneNumber, String status) {
        this.receiptId = "REC" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.transactionId = transactionId;
        this.paymentType = paymentType;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.phoneNumber = phoneNumber;
        this.timestamp = new Date();
        this.status = status;
        this.qrCodeData = generateQRCodeData();
    }

    private String generateQRCodeData() {
        return String.format(Locale.getDefault(),
            "PAYHOME|%s|%s|%.2f|%s|%s|%d",
            receiptId, transactionId, amount, paymentMethod, phoneNumber, timestamp.getTime()
        );
    }

    // Getters
    public String getReceiptId() { return receiptId; }
    public String getTransactionId() { return transactionId; }
    public String getPaymentType() { return paymentType; }
    public double getAmount() { return amount; }
    public String getPaymentMethod() { return paymentMethod; }
    public String getPhoneNumber() { return phoneNumber; }
    public Date getTimestamp() { return timestamp; }
    public String getStatus() { return status; }
    public String getQrCodeData() { return qrCodeData; }

    // Helper methods
    public String getFormattedAmount() {
        return String.format("KES %.2f", amount);
    }

    public String getFormattedDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault());
        return sdf.format(timestamp);
    }

    public String getReceiptSummary() {
        return String.format("Payment: %s\nAmount: %s\nMethod: %s\nDate: %s\nReceipt ID: %s",
            paymentType, getFormattedAmount(), paymentMethod, getFormattedDate(), receiptId);
    }
}
