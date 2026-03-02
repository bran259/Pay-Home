package com.example.payhome.models;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class Bill {
    private String billId;
    private String billType; // Rent, Electricity, Water
    private String provider;
    private double amount;
    private Date dueDate;
    private Date issueDate;
    private String status; // Paid, Unpaid, Overdue, Partial
    private String description;
    private String billImagePath;
    private double amountPaid;
    private String accountNumber;

    public Bill(String billType, String provider, double amount, Date dueDate, String description) {
        this.billId = "BILL" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.billType = billType;
        this.provider = provider;
        this.amount = amount;
        this.dueDate = dueDate;
        this.issueDate = new Date();
        this.status = "Unpaid";
        this.description = description;
        this.amountPaid = 0.0;
        this.accountNumber = "";
    }

    // Getters and Setters
    public String getBillId() { return billId; }
    public String getBillType() { return billType; }
    public String getProvider() { return provider; }
    public double getAmount() { return amount; }
    public Date getDueDate() { return dueDate; }
    public Date getIssueDate() { return issueDate; }
    public String getStatus() { return status; }
    public String getDescription() { return description; }
    public String getBillImagePath() { return billImagePath; }
    public double getAmountPaid() { return amountPaid; }
    public String getAccountNumber() { return accountNumber; }

    public void setBillImagePath(String billImagePath) { this.billImagePath = billImagePath; }
    public void setAmountPaid(double amountPaid) { this.amountPaid = amountPaid; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    public void setStatus(String status) { this.status = status; }

    // Helper methods
    public String getFormattedAmount() {
        return String.format("KES %.2f", amount);
    }

    public String getFormattedDueDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        return sdf.format(dueDate);
    }

    public String getFormattedIssueDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        return sdf.format(issueDate);
    }

    public double getRemainingAmount() {
        return amount - amountPaid;
    }

    public String getFormattedRemainingAmount() {
        return String.format("KES %.2f", getRemainingAmount());
    }

    public int getDaysUntilDue() {
        long diffInMillis = dueDate.getTime() - new Date().getTime();
        return (int) (diffInMillis / (1000 * 60 * 60 * 24));
    }

    public boolean isOverdue() {
        return new Date().after(dueDate) && !status.equals("Paid");
    }

    public String getDueStatus() {
        if (status.equals("Paid")) return "Paid";
        if (isOverdue()) return "Overdue";
        
        int daysUntilDue = getDaysUntilDue();
        if (daysUntilDue <= 3) return "Due Soon";
        if (daysUntilDue <= 7) return "Due This Week";
        return "Upcoming";
    }

    public int getDueStatusColor() {
        if (status.equals("Paid")) return android.graphics.Color.parseColor("#4CAF50");
        if (isOverdue()) return android.graphics.Color.parseColor("#F44336");
        if (getDaysUntilDue() <= 3) return android.graphics.Color.parseColor("#FF9800");
        return android.graphics.Color.parseColor("#1976D2");
    }

    public String getBillIcon() {
        switch (billType.toLowerCase()) {
            case "rent": return "🏠";
            case "electricity": return "💡";
            case "water": return "💧";
            default: return "📄";
        }
    }
}
