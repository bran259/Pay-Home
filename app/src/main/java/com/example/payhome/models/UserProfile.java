package com.example.payhome.models;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

public class UserProfile {
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String userType; // Tenant, Landlord, PropertyManager
    private String profileImageUrl;
    private Date createdDate;
    private Date lastLoginDate;
    private boolean isActive;
    private boolean emailNotifications;
    private boolean smsNotifications;
    private boolean pushNotifications;
    private String preferredPaymentMethod;
    private List<String> propertyIds;
    private List<String> documentUrls;
    private UserSettings settings;

    public UserProfile(String firstName, String lastName, String email, String phoneNumber, String userType) {
        this.userId = "USER" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.userType = userType;
        this.profileImageUrl = "";
        this.createdDate = new Date();
        this.lastLoginDate = new Date();
        this.isActive = true;
        this.emailNotifications = true;
        this.smsNotifications = true;
        this.pushNotifications = true;
        this.preferredPaymentMethod = "M-Pesa";
        this.propertyIds = new ArrayList<>();
        this.documentUrls = new ArrayList<>();
        this.settings = new UserSettings();
    }

    // Getters and Setters
    public String getUserId() { return userId; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getUserType() { return userType; }
    public String getProfileImageUrl() { return profileImageUrl; }
    public Date getCreatedDate() { return createdDate; }
    public Date getLastLoginDate() { return lastLoginDate; }
    public boolean isActive() { return isActive; }
    public boolean isEmailNotifications() { return emailNotifications; }
    public boolean isSmsNotifications() { return smsNotifications; }
    public boolean isPushNotifications() { return pushNotifications; }
    public String getPreferredPaymentMethod() { return preferredPaymentMethod; }
    public List<String> getPropertyIds() { return propertyIds; }
    public List<String> getDocumentUrls() { return documentUrls; }
    public UserSettings getSettings() { return settings; }

    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setEmail(String email) { this.email = email; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setProfileImageUrl(String profileImageUrl) { this.profileImageUrl = profileImageUrl; }
    public void setActive(boolean active) { isActive = active; }
    public void setEmailNotifications(boolean emailNotifications) { this.emailNotifications = emailNotifications; }
    public void setSmsNotifications(boolean smsNotifications) { this.smsNotifications = smsNotifications; }
    public void setPushNotifications(boolean pushNotifications) { this.pushNotifications = pushNotifications; }
    public void setPreferredPaymentMethod(String preferredPaymentMethod) { this.preferredPaymentMethod = preferredPaymentMethod; }

    // Helper methods
    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getFormattedCreatedDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", java.util.Locale.getDefault());
        return sdf.format(createdDate);
    }

    public String getFormattedLastLoginDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, hh:mm a", java.util.Locale.getDefault());
        return sdf.format(lastLoginDate);
    }

    public void updateLastLoginDate() {
        this.lastLoginDate = new Date();
    }

    public void addProperty(String propertyId) {
        if (!propertyIds.contains(propertyId)) {
            propertyIds.add(propertyId);
        }
    }

    public void removeProperty(String propertyId) {
        propertyIds.remove(propertyId);
    }

    public void addDocument(String documentUrl) {
        documentUrls.add(documentUrl);
    }

    public String getUserTypeIcon() {
        switch (userType.toLowerCase()) {
            case "tenant": return "👤";
            case "landlord": return "👔";
            case "propertymanager": return "🏢";
            default: return "👥";
        }
    }

    public int getPropertyCount() {
        return propertyIds.size();
    }

    // Inner class for user settings
    public static class UserSettings {
        private String language;
        private String currency;
        private String timezone;
        private boolean darkMode;
        private boolean autoPay;
        private double monthlyBudgetLimit;
        private String paymentFrequency;

        public UserSettings() {
            this.language = "English";
            this.currency = "KES";
            this.timezone = "Africa/Nairobi";
            this.darkMode = false;
            this.autoPay = false;
            this.monthlyBudgetLimit = 50000.0;
            this.paymentFrequency = "Monthly";
        }

        // Getters and Setters
        public String getLanguage() { return language; }
        public String getCurrency() { return currency; }
        public String getTimezone() { return timezone; }
        public boolean isDarkMode() { return darkMode; }
        public boolean isAutoPay() { return autoPay; }
        public double getMonthlyBudgetLimit() { return monthlyBudgetLimit; }
        public String getPaymentFrequency() { return paymentFrequency; }

        public void setLanguage(String language) { this.language = language; }
        public void setCurrency(String currency) { this.currency = currency; }
        public void setTimezone(String timezone) { this.timezone = timezone; }
        public void setDarkMode(boolean darkMode) { this.darkMode = darkMode; }
        public void setAutoPay(boolean autoPay) { this.autoPay = autoPay; }
        public void setMonthlyBudgetLimit(double monthlyBudgetLimit) { this.monthlyBudgetLimit = monthlyBudgetLimit; }
        public void setPaymentFrequency(String paymentFrequency) { this.paymentFrequency = paymentFrequency; }

        public String getFormattedBudgetLimit() {
            return String.format("KES %.2f", monthlyBudgetLimit);
        }
    }
}
