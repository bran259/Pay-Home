package com.example.payhome.models;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class SmartReminder {
    private String reminderId;
    private String userId;
    private String propertyId;
    private String billId;
    private String title;
    private String description;
    private String reminderType; // PaymentDue, Maintenance, LeaseExpiry, DocumentExpiry, Custom
    private String priority; // Low, Medium, High, Urgent
    private Date triggerDate;
    private Date scheduledDate;
    private boolean isRecurring;
    private String recurrencePattern; // Daily, Weekly, Monthly, Yearly
    private String status; // Scheduled, Triggered, Acknowledged, Dismissed, Expired
    private String actionRequired; // Pay, Contact, Review, Upload, Custom
    private Map<String, String> actionData;
    private Date createdDate;
    private Date lastTriggeredDate;
    private int triggerCount;
    private boolean isActive;

    public SmartReminder(String userId, String title, String description, String reminderType, 
                       String priority, Date triggerDate, String actionRequired) {
        this.reminderId = "REM" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.reminderType = reminderType;
        this.priority = priority;
        this.triggerDate = triggerDate;
        this.scheduledDate = triggerDate;
        this.isRecurring = false;
        this.recurrencePattern = "";
        this.status = "Scheduled";
        this.actionRequired = actionRequired;
        this.actionData = new HashMap<>();
        this.createdDate = new Date();
        this.lastTriggeredDate = null;
        this.triggerCount = 0;
        this.isActive = true;
    }

    // Static factory methods for common reminders
    public static SmartReminder createPaymentDueReminder(String userId, String billId, String propertyId, 
                                                       String billType, Date dueDate, double amount) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", java.util.Locale.getDefault());
        String title = billType + " Payment Due";
        String description = "Your " + billType + " payment of KES " + String.format("%.2f", amount) + 
                            " is due on " + sdf.format(dueDate);
        
        // Schedule reminder 3 days before due date
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dueDate);
        calendar.add(Calendar.DAY_OF_YEAR, -3);
        Date reminderDate = calendar.getTime();
        
        SmartReminder reminder = new SmartReminder(userId, title, description, "PaymentDue", 
                                                  "High", reminderDate, "Pay");
        reminder.billId = billId;
        reminder.propertyId = propertyId;
        reminder.actionData.put("amount", String.valueOf(amount));
        reminder.actionData.put("billType", billType);
        reminder.actionData.put("dueDate", sdf.format(dueDate));
        
        return reminder;
    }

    public static SmartReminder createMaintenanceReminder(String userId, String propertyId, 
                                                        String maintenanceType, Date scheduledDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", java.util.Locale.getDefault());
        String title = "Scheduled Maintenance - " + maintenanceType;
        String description = maintenanceType + " maintenance scheduled for " + sdf.format(scheduledDate);
        
        // Schedule reminder 1 day before maintenance
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(scheduledDate);
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        Date reminderDate = calendar.getTime();
        
        SmartReminder reminder = new SmartReminder(userId, title, description, "Maintenance", 
                                                  "Medium", reminderDate, "Review");
        reminder.propertyId = propertyId;
        reminder.actionData.put("maintenanceType", maintenanceType);
        reminder.actionData.put("scheduledDate", sdf.format(scheduledDate));
        
        return reminder;
    }

    public static SmartReminder createLeaseExpiryReminder(String userId, String propertyId, 
                                                         Date expiryDate, int daysBefore) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", java.util.Locale.getDefault());
        String title = "Lease Expiry Reminder";
        String description = "Your lease expires on " + sdf.format(expiryDate) + 
                            " (" + daysBefore + " days from now)";
        
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(expiryDate);
        calendar.add(Calendar.DAY_OF_YEAR, -daysBefore);
        Date reminderDate = calendar.getTime();
        
        SmartReminder reminder = new SmartReminder(userId, title, description, "LeaseExpiry", 
                                                  "High", reminderDate, "Contact");
        reminder.propertyId = propertyId;
        reminder.actionData.put("expiryDate", sdf.format(expiryDate));
        reminder.actionData.put("daysBefore", String.valueOf(daysBefore));
        
        return reminder;
    }

    public static SmartReminder createLowBalanceAlert(String userId, double currentBalance, 
                                                   double threshold, String accountType) {
        String title = "Low Balance Alert - " + accountType;
        String description = "Your " + accountType + " balance is KES " + String.format("%.2f", currentBalance) + 
                            " (below threshold of KES " + String.format("%.2f", threshold) + ")";
        
        SmartReminder reminder = new SmartReminder(userId, title, description, "LowBalance", 
                                                  "Urgent", new Date(), "Review");
        reminder.actionData.put("currentBalance", String.valueOf(currentBalance));
        reminder.actionData.put("threshold", String.valueOf(threshold));
        reminder.actionData.put("accountType", accountType);
        
        return reminder;
    }

    // Getters and Setters
    public String getReminderId() { return reminderId; }
    public String getUserId() { return userId; }
    public String getPropertyId() { return propertyId; }
    public String getBillId() { return billId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getReminderType() { return reminderType; }
    public String getPriority() { return priority; }
    public Date getTriggerDate() { return triggerDate; }
    public Date getScheduledDate() { return scheduledDate; }
    public boolean isRecurring() { return isRecurring; }
    public String getRecurrencePattern() { return recurrencePattern; }
    public String getStatus() { return status; }
    public String getActionRequired() { return actionRequired; }
    public Map<String, String> getActionData() { return actionData; }
    public Date getCreatedDate() { return createdDate; }
    public Date getLastTriggeredDate() { return lastTriggeredDate; }
    public int getTriggerCount() { return triggerCount; }
    public boolean isActive() { return isActive; }

    public void setStatus(String status) { 
        this.status = status;
        if ("Triggered".equals(status)) {
            this.lastTriggeredDate = new Date();
            this.triggerCount++;
            if (isRecurring) {
                scheduleNextTrigger();
            }
        }
    }
    
    public void setActive(boolean active) { isActive = active; }

    // Helper methods
    public String getFormattedTriggerDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, hh:mm a", java.util.Locale.getDefault());
        return sdf.format(triggerDate);
    }

    public String getFormattedCreatedDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", java.util.Locale.getDefault());
        return sdf.format(createdDate);
    }

    public boolean isOverdue() {
        return new Date().after(triggerDate) && !"Triggered".equals(status) && !"Acknowledged".equals(status);
    }

    public long getDaysUntilTrigger() {
        long diffInMillis = triggerDate.getTime() - new Date().getTime();
        return TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
    }

    public String getTriggerStatus() {
        if ("Triggered".equals(status) || "Acknowledged".equals(status)) return "Completed";
        if (isOverdue()) return "Overdue";
        
        long daysUntil = getDaysUntilTrigger();
        if (daysUntil <= 0) return "Due Today";
        if (daysUntil == 1) return "Due Tomorrow";
        if (daysUntil <= 7) return "Due This Week";
        if (daysUntil <= 30) return "Due This Month";
        return "Upcoming";
    }

    public int getTriggerStatusColor() {
        String triggerStatus = getTriggerStatus();
        switch (triggerStatus) {
            case "Completed": return android.graphics.Color.parseColor("#4CAF50"); // Green
            case "Overdue": return android.graphics.Color.parseColor("#F44336"); // Red
            case "Due Today": return android.graphics.Color.parseColor("#FF9800"); // Orange
            case "Due Tomorrow": return android.graphics.Color.parseColor("#FF9800"); // Orange
            case "Due This Week": return android.graphics.Color.parseColor("#2196F3"); // Blue
            default: return android.graphics.Color.parseColor("#757575"); // Gray
        }
    }

    public String getReminderTypeIcon() {
        switch (reminderType.toLowerCase()) {
            case "paymentdue": return "💳";
            case "maintenance": return "🔧";
            case "leaseexpiry": return "📄";
            case "documentexpiry": return "📋";
            case "lowbalance": return "⚠️";
            default: return "🔔";
        }
    }

    public int getPriorityColor() {
        switch (priority.toLowerCase()) {
            case "urgent": return android.graphics.Color.parseColor("#F44336"); // Red
            case "high": return android.graphics.Color.parseColor("#FF9800"); // Orange
            case "medium": return android.graphics.Color.parseColor("#2196F3"); // Blue
            case "low": return android.graphics.Color.parseColor("#4CAF50"); // Green
            default: return android.graphics.Color.parseColor("#757575"); // Gray
        }
    }

    private void scheduleNextTrigger() {
        if (!isRecurring || recurrencePattern.isEmpty()) return;
        
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(triggerDate);
        
        switch (recurrencePattern.toLowerCase()) {
            case "daily":
                calendar.add(Calendar.DAY_OF_YEAR, 1);
                break;
            case "weekly":
                calendar.add(Calendar.WEEK_OF_YEAR, 1);
                break;
            case "monthly":
                calendar.add(Calendar.MONTH, 1);
                break;
            case "yearly":
                calendar.add(Calendar.YEAR, 1);
                break;
        }
        
        this.triggerDate = calendar.getTime();
        this.status = "Scheduled";
    }

    public void setRecurring(String recurrencePattern) {
        this.isRecurring = true;
        this.recurrencePattern = recurrencePattern;
    }
}
