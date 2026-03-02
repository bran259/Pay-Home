package com.example.payhome.models;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

public class Property {
    private String propertyId;
    private String propertyName;
    private String propertyType; // Apartment, House, Commercial
    private String address;
    private int numberOfUnits;
    private double totalRent;
    private String landlordId;
    private List<String> tenantIds;
    private List<String> propertyImages;
    private Date createdDate;
    private boolean isActive;
    private String description;
    private List<MaintenanceRequest> maintenanceRequests;

    public Property(String propertyName, String propertyType, String address, 
                   int numberOfUnits, double totalRent, String landlordId) {
        this.propertyId = "PROP" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.propertyName = propertyName;
        this.propertyType = propertyType;
        this.address = address;
        this.numberOfUnits = numberOfUnits;
        this.totalRent = totalRent;
        this.landlordId = landlordId;
        this.tenantIds = new ArrayList<>();
        this.propertyImages = new ArrayList<>();
        this.createdDate = new Date();
        this.isActive = true;
        this.description = "";
        this.maintenanceRequests = new ArrayList<>();
    }

    // Getters and Setters
    public String getPropertyId() { return propertyId; }
    public String getPropertyName() { return propertyName; }
    public String getPropertyType() { return propertyType; }
    public String getAddress() { return address; }
    public int getNumberOfUnits() { return numberOfUnits; }
    public double getTotalRent() { return totalRent; }
    public String getLandlordId() { return landlordId; }
    public List<String> getTenantIds() { return tenantIds; }
    public List<String> getPropertyImages() { return propertyImages; }
    public Date getCreatedDate() { return createdDate; }
    public boolean isActive() { return isActive; }
    public String getDescription() { return description; }
    public List<MaintenanceRequest> getMaintenanceRequests() { return maintenanceRequests; }

    public void setPropertyName(String propertyName) { this.propertyName = propertyName; }
    public void setDescription(String description) { this.description = description; }
    public void setActive(boolean active) { isActive = active; }
    public void setTotalRent(double totalRent) { this.totalRent = totalRent; }

    // Helper methods
    public String getFormattedTotalRent() {
        return String.format("KES %.2f", totalRent);
    }

    public String getFormattedCreatedDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", java.util.Locale.getDefault());
        return sdf.format(createdDate);
    }

    public int getOccupiedUnits() {
        return tenantIds.size();
    }

    public int getAvailableUnits() {
        return numberOfUnits - tenantIds.size();
    }

    public double getOccupancyRate() {
        if (numberOfUnits == 0) return 0.0;
        return (double) tenantIds.size() / numberOfUnits * 100;
    }

    public String getOccupancyStatus() {
        double rate = getOccupancyRate();
        if (rate == 100) return "Fully Occupied";
        if (rate >= 75) return "High Occupancy";
        if (rate >= 50) return "Moderate Occupancy";
        if (rate > 0) return "Low Occupancy";
        return "Vacant";
    }

    public int getOccupancyStatusColor() {
        double rate = getOccupancyRate();
        if (rate == 100) return android.graphics.Color.parseColor("#4CAF50"); // Green
        if (rate >= 75) return android.graphics.Color.parseColor("#2196F3"); // Blue
        if (rate >= 50) return android.graphics.Color.parseColor("#FF9800"); // Orange
        if (rate > 0) return android.graphics.Color.parseColor("#FF5722"); // Deep Orange
        return android.graphics.Color.parseColor("#9E9E9E"); // Gray
    }

    public String getPropertyIcon() {
        switch (propertyType.toLowerCase()) {
            case "apartment": return "🏢";
            case "house": return "🏠";
            case "commercial": return "🏬";
            default: return "🏘️";
        }
    }

    public void addTenant(String tenantId) {
        if (!tenantIds.contains(tenantId) && tenantIds.size() < numberOfUnits) {
            tenantIds.add(tenantId);
        }
    }

    public void removeTenant(String tenantId) {
        tenantIds.remove(tenantId);
    }

    public void addPropertyImage(String imagePath) {
        propertyImages.add(imagePath);
    }

    public void addMaintenanceRequest(MaintenanceRequest request) {
        maintenanceRequests.add(request);
    }

    public int getPendingMaintenanceCount() {
        int count = 0;
        for (MaintenanceRequest request : maintenanceRequests) {
            if ("Pending".equals(request.getStatus())) {
                count++;
            }
        }
        return count;
    }
}
