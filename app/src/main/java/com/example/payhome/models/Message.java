package com.example.payhome.models;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class Message {
    private String messageId;
    private String senderId;
    private String receiverId;
    private String propertyId;
    private String subject;
    private String content;
    private String messageType; // Text, Image, Document, PaymentRequest, MaintenanceRequest
    private String status; // Sent, Delivered, Read, Replied
    private Date sentDate;
    private Date readDate;
    private String replyToMessageId;
    private boolean hasAttachment;
    private String attachmentUrl;
    private boolean isSystemMessage;
    private String priority; // Low, Normal, High, Urgent

    public Message(String senderId, String receiverId, String propertyId, String subject, String content, String messageType) {
        this.messageId = "MSG" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.propertyId = propertyId;
        this.subject = subject;
        this.content = content;
        this.messageType = messageType;
        this.status = "Sent";
        this.sentDate = new Date();
        this.readDate = null;
        this.replyToMessageId = "";
        this.hasAttachment = false;
        this.attachmentUrl = "";
        this.isSystemMessage = false;
        this.priority = "Normal";
    }

    // Static factory methods for common message types
    public static Message createPaymentRequest(String landlordId, String tenantId, String propertyId, double amount, String dueDate) {
        String subject = "Payment Request - KES " + String.format("%.2f", amount);
        String content = "Dear Tenant,\n\nThis is a reminder that your rent payment of KES " + 
                        String.format("%.2f", amount) + " is due on " + dueDate + 
                        ".\n\nPlease make your payment on time to avoid late fees.\n\nThank you,\nProperty Management";
        
        Message message = new Message(landlordId, tenantId, propertyId, subject, content, "PaymentRequest");
        message.priority = "High";
        message.isSystemMessage = true;
        return message;
    }

    public static Message createMaintenanceNotice(String landlordId, String tenantId, String propertyId, String maintenanceType, Date scheduledDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", java.util.Locale.getDefault());
        String subject = "Scheduled Maintenance - " + maintenanceType;
        String content = "Dear Tenant,\n\nWe will be conducting " + maintenanceType + 
                        " maintenance on " + sdf.format(scheduledDate) + 
                        ".\n\nPlease ensure you are available during this time.\n\nThank you for your cooperation.\n\nProperty Management";
        
        Message message = new Message(landlordId, tenantId, propertyId, subject, content, "MaintenanceRequest");
        message.priority = "Normal";
        message.isSystemMessage = true;
        return message;
    }

    public static Message createPaymentConfirmation(String systemId, String landlordId, String propertyId, String tenantName, double amount) {
        String subject = "Payment Received - KES " + String.format("%.2f", amount);
        String content = "Payment Confirmation\n\nTenant: " + tenantName + 
                        "\nAmount: KES " + String.format("%.2f", amount) + 
                        "\nProperty: " + propertyId + 
                        "\nDate: " + new SimpleDateFormat("dd MMM yyyy", java.util.Locale.getDefault()).format(new Date()) +
                        "\n\nPayment has been successfully processed.";
        
        Message message = new Message(systemId, landlordId, propertyId, subject, content, "Text");
        message.priority = "Normal";
        message.isSystemMessage = true;
        return message;
    }

    // Getters and Setters
    public String getMessageId() { return messageId; }
    public String getSenderId() { return senderId; }
    public String getReceiverId() { return receiverId; }
    public String getPropertyId() { return propertyId; }
    public String getSubject() { return subject; }
    public String getContent() { return content; }
    public String getMessageType() { return messageType; }
    public String getStatus() { return status; }
    public Date getSentDate() { return sentDate; }
    public Date getReadDate() { return readDate; }
    public String getReplyToMessageId() { return replyToMessageId; }
    public boolean hasAttachment() { return hasAttachment; }
    public String getAttachmentUrl() { return attachmentUrl; }
    public boolean isSystemMessage() { return isSystemMessage; }
    public String getPriority() { return priority; }

    public void setStatus(String status) { 
        this.status = status;
        if ("Read".equals(status) && readDate == null) {
            this.readDate = new Date();
        }
    }
    
    public void setReplyToMessageId(String replyToMessageId) { this.replyToMessageId = replyToMessageId; }
    public void setAttachment(String attachmentUrl) { 
        this.attachmentUrl = attachmentUrl;
        this.hasAttachment = true;
    }
    public void setPriority(String priority) { this.priority = priority; }

    // Helper methods
    public String getFormattedSentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, hh:mm a", java.util.Locale.getDefault());
        return sdf.format(sentDate);
    }

    public String getFormattedReadDate() {
        if (readDate == null) return "Not read yet";
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, hh:mm a", java.util.Locale.getDefault());
        return sdf.format(readDate);
    }

    public boolean isRead() {
        return "Read".equals(status);
    }

    public String getMessageTypeIcon() {
        switch (messageType.toLowerCase()) {
            case "text": return "💬";
            case "image": return "🖼️";
            case "document": return "📄";
            case "paymentrequest": return "💳";
            case "maintenancerequest": return "🔧";
            default: return "📩";
        }
    }

    public int getPriorityColor() {
        switch (priority.toLowerCase()) {
            case "urgent": return android.graphics.Color.parseColor("#F44336"); // Red
            case "high": return android.graphics.Color.parseColor("#FF9800"); // Orange
            case "normal": return android.graphics.Color.parseColor("#2196F3"); // Blue
            case "low": return android.graphics.Color.parseColor("#4CAF50"); // Green
            default: return android.graphics.Color.parseColor("#757575"); // Gray
        }
    }

    public String getShortContent(int maxLength) {
        if (content.length() <= maxLength) return content;
        return content.substring(0, maxLength) + "...";
    }

    public boolean isReply() {
        return !replyToMessageId.isEmpty();
    }

    public long getTimeAgoInMillis() {
        return System.currentTimeMillis() - sentDate.getTime();
    }

    public String getTimeAgo() {
        long diff = getTimeAgoInMillis();
        long seconds = diff / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;

        if (days > 0) return days + "d ago";
        if (hours > 0) return hours + "h ago";
        if (minutes > 0) return minutes + "m ago";
        return "Just now";
    }
}
