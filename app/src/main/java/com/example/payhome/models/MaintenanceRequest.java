package com.example.payhome.models;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

public class MaintenanceRequest {
    private String requestId;
    private String propertyId;
    private String tenantId;
    private String landlordId;
    private String title;
    private String description;
    private String category; // Plumbing, Electrical, Structural, Other
    private String priority; // Low, Medium, High, Urgent
    private String status; // Pending, In Progress, Completed, Rejected
    private Date createdDate;
    private Date dueDate;
    private Date completedDate;
    private String assignedTo;
    private double estimatedCost;
    private double actualCost;
    private List<String> images;

    public MaintenanceRequest(String propertyId, String tenantId, String landlordId, 
                            String title, String description, String category, String priority) {
        this.requestId = "MAINT" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.propertyId = propertyId;
        this.tenantId = tenantId;
        this.landlordId = landlordId;
        this.title = title;
        this.description = description;
        this.category = category;
        this.priority = priority;
        this.status = "Pending";
        this.createdDate = new Date();
        this.dueDate = calculateDueDate(priority);
        this.completedDate = null;
        this.assignedTo = "";
        this.estimatedCost = 0.0;
        this.actualCost = 0.0;
        this.images = new java.util.ArrayList<>();
    }

    private Date calculateDueDate(String priority) {
        Date dueDate = new Date();
        long daysToAdd = 0;
        
        switch (priority.toLowerCase()) {
            case "urgent":
                daysToAdd = 1; // 1 day
                break;
            case "high":
                daysToAdd = 3; // 3 days
                break;
            case "medium":
                daysToAdd = 7; // 1 week
                break;
            case "low":
                daysToAdd = 14; // 2 weeks
                break;
        }
        
        dueDate.setTime(dueDate.getTime() + (daysToAdd * 24 * 60 * 60 * 1000));
        return dueDate;
    }

    // Getters and Setters
    public String getRequestId() { return requestId; }
    public String getPropertyId() { return propertyId; }
    public String getTenantId() { return tenantId; }
    public String getLandlordId() { return landlordId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getCategory() { return category; }
    public String getPriority() { return priority; }
    public String getStatus() { return status; }
    public Date getCreatedDate() { return createdDate; }
    public Date getDueDate() { return dueDate; }
    public Date getCompletedDate() { return completedDate; }
    public String getAssignedTo() { return assignedTo; }
    public double getEstimatedCost() { return estimatedCost; }
    public double getActualCost() { return actualCost; }
    public java.util.List<String> getImages() { return images; }

    public void setStatus(String status) { 
        this.status = status;
        if ("Completed".equals(status) && completedDate == null) {
            this.completedDate = new Date();
        }
    }
    
    public void setAssignedTo(String assignedTo) { this.assignedTo = assignedTo; }
    public void setEstimatedCost(double estimatedCost) { this.estimatedCost = estimatedCost; }
    public void setActualCost(double actualCost) { this.actualCost = actualCost; }

    // Helper methods
    public String getFormattedCreatedDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", java.util.Locale.getDefault());
        return sdf.format(createdDate);
    }

    public String getFormattedDueDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", java.util.Locale.getDefault());
        return sdf.format(dueDate);
    }

    public String getFormattedEstimatedCost() {
        return String.format("KES %.2f", estimatedCost);
    }

    public String getFormattedActualCost() {
        return String.format("KES %.2f", actualCost);
    }

    public int getDaysUntilDue() {
        long diffInMillis = dueDate.getTime() - new Date().getTime();
        return (int) (diffInMillis / (1000 * 60 * 60 * 24));
    }

    public boolean isOverdue() {
        return new Date().after(dueDate) && !"Completed".equals(status);
    }

    public String getCategoryIcon() {
        switch (category.toLowerCase()) {
            case "plumbing": return "🔧";
            case "electrical": return "⚡";
            case "structural": return "🏗️";
            default: return "🔨";
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

    public int getStatusColor() {
        switch (status.toLowerCase()) {
            case "pending": return android.graphics.Color.parseColor("#FF9800"); // Orange
            case "in progress": return android.graphics.Color.parseColor("#2196F3"); // Blue
            case "completed": return android.graphics.Color.parseColor("#4CAF50"); // Green
            case "rejected": return android.graphics.Color.parseColor("#F44336"); // Red
            default: return android.graphics.Color.parseColor("#757575"); // Gray
        }
    }

    public void addImage(String imagePath) {
        images.add(imagePath);
    }
}
