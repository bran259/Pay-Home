package com.example.payhome.models;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class ScheduledPayment {
    private String scheduleId;
    private String paymentType;
    private double amount;
    private String paymentMethod;
    private String frequency; // Weekly, Monthly, Yearly
    private Date startDate;
    private Date nextPaymentDate;
    private Date endDate;
    private boolean isActive;
    private String description;
    private String phoneNumber;
    private int executionCount;
    private int totalExecutions;

    public ScheduledPayment(String paymentType, double amount, String paymentMethod, 
                           String frequency, Date startDate, String description, String phoneNumber) {
        this.scheduleId = "SCH" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.paymentType = paymentType;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.frequency = frequency;
        this.startDate = startDate;
        this.nextPaymentDate = calculateNextPaymentDate(startDate);
        this.endDate = null; // No end date by default
        this.isActive = true;
        this.description = description;
        this.phoneNumber = phoneNumber;
        this.executionCount = 0;
        this.totalExecutions = 0;
    }

    private Date calculateNextPaymentDate(Date fromDate) {
        // Simple calculation - in a real app, this would be more sophisticated
        Date nextDate = new Date(fromDate.getTime());
        
        switch (frequency.toLowerCase()) {
            case "weekly":
                nextDate.setTime(nextDate.getTime() + (7L * 24 * 60 * 60 * 1000));
                break;
            case "monthly":
                nextDate.setTime(nextDate.getTime() + (30L * 24 * 60 * 60 * 1000));
                break;
            case "yearly":
                nextDate.setTime(nextDate.getTime() + (365L * 24 * 60 * 60 * 1000));
                break;
            default:
                nextDate.setTime(nextDate.getTime() + (30L * 24 * 60 * 60 * 1000));
                break;
        }
        
        return nextDate;
    }

    // Getters and Setters
    public String getScheduleId() { return scheduleId; }
    public String getPaymentType() { return paymentType; }
    public double getAmount() { return amount; }
    public String getPaymentMethod() { return paymentMethod; }
    public String getFrequency() { return frequency; }
    public Date getStartDate() { return startDate; }
    public Date getNextPaymentDate() { return nextPaymentDate; }
    public Date getEndDate() { return endDate; }
    public boolean isActive() { return isActive; }
    public String getDescription() { return description; }
    public String getPhoneNumber() { return phoneNumber; }
    public int getExecutionCount() { return executionCount; }
    public int getTotalExecutions() { return totalExecutions; }

    public void setActive(boolean active) { isActive = active; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }

    // Helper methods
    public String getFormattedAmount() {
        return String.format("KES %.2f", amount);
    }

    public String getFormattedNextPaymentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        return sdf.format(nextPaymentDate);
    }

    public String getFormattedStartDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        return sdf.format(startDate);
    }

    public int getDaysUntilNextPayment() {
        long diffInMillis = nextPaymentDate.getTime() - new Date().getTime();
        return (int) (diffInMillis / (1000 * 60 * 60 * 24));
    }

    public void executePayment() {
        executionCount++;
        totalExecutions++;
        nextPaymentDate = calculateNextPaymentDate(nextPaymentDate);
    }

    public String getFrequencyDisplay() {
        switch (frequency.toLowerCase()) {
            case "weekly": return "Every Week";
            case "monthly": return "Every Month";
            case "yearly": return "Every Year";
            default: return frequency;
        }
    }

    public String getPaymentIcon() {
        switch (paymentType.toLowerCase()) {
            case "rent": return "🏠";
            case "electricity": return "💡";
            case "water": return "💧";
            default: return "💳";
        }
    }
}
