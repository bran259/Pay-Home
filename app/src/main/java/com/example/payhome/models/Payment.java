package com.example.payhome.models;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Payment {
    private String id;
    private String type; // Rent, Electricity, Water
    private double amount;
    private String status; // Success, Failed, Pending
    private Date date;
    private String description;
    private String phoneNumber;

    public Payment(String id, String type, double amount, String status, Date date, String description, String phoneNumber) {
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.status = status;
        this.date = date;
        this.description = description;
        this.phoneNumber = phoneNumber;
    }

    // Getters
    public String getId() { return id; }
    public String getType() { return type; }
    public double getAmount() { return amount; }
    public String getStatus() { return status; }
    public Date getDate() { return date; }
    public String getDescription() { return description; }
    public String getPhoneNumber() { return phoneNumber; }

    // Helper methods
    public String getFormattedAmount() {
        return String.format("KES %.2f", amount);
    }

    public String getFormattedDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        return sdf.format(date);
    }

    public String getFormattedTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        return sdf.format(date);
    }

    public int getStatusColor() {
        switch (status.toLowerCase()) {
            case "success":
                return android.graphics.Color.parseColor("#4CAF50");
            case "failed":
                return android.graphics.Color.parseColor("#F44336");
            case "pending":
                return android.graphics.Color.parseColor("#FF9800");
            default:
                return android.graphics.Color.parseColor("#757575");
        }
    }

    public int getTypeColor() {
        switch (type.toLowerCase()) {
            case "rent":
                return android.graphics.Color.parseColor("#1976D2");
            case "electricity":
                return android.graphics.Color.parseColor("#FF9800");
            case "water":
                return android.graphics.Color.parseColor("#2196F3");
            default:
                return android.graphics.Color.parseColor("#1976D2");
        }
    }

    public String getTypeIcon() {
        switch (type.toLowerCase()) {
            case "rent":
                return "🏠";
            case "electricity":
                return "💡";
            case "water":
                return "💧";
            default:
                return "💳";
        }
    }
}
