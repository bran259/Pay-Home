package com.example.payhome.utils;

import com.example.payhome.models.Payment;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class PaymentData {
    
    public static List<Payment> getMockPayments() {
        List<Payment> payments = new ArrayList<>();
        Random random = new Random();
        
        // Generate mock payment data
        String[] types = {"Rent", "Electricity", "Water"};
        String[] statuses = {"Success", "Failed", "Pending"};
        
        for (int i = 0; i < 10; i++) {
            String type = types[random.nextInt(types.length)];
            String status = statuses[random.nextInt(statuses.length)];
            double amount = generateAmount(type);
            Date date = generateRandomDate(i);
            
            Payment payment = new Payment(
                "PAY" + String.format("%03d", i + 1),
                type,
                amount,
                status,
                date,
                generateDescription(type),
                "0798088797"
            );
            
            payments.add(payment);
        }
        
        return payments;
    }
    
    private static double generateAmount(String type) {
        switch (type) {
            case "Rent":
                return 15000 + Math.random() * 10000; // 15K-25K
            case "Electricity":
                return 2000 + Math.random() * 3000; // 2K-5K
            case "Water":
                return 1000 + Math.random() * 2000; // 1K-3K
            default:
                return 1000 + Math.random() * 5000; // 1K-6K
        }
    }
    
    private static Date generateRandomDate(int daysAgo) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -daysAgo);
        calendar.add(Calendar.HOUR_OF_DAY, new Random().nextInt(24));
        calendar.add(Calendar.MINUTE, new Random().nextInt(60));
        return calendar.getTime();
    }
    
    private static String generateDescription(String type) {
        switch (type) {
            case "Rent":
                return "Monthly rent payment for House No. 12";
            case "Electricity":
                return "Electricity bill for " + getCurrentMonth();
            case "Water":
                return "Water bill for " + getCurrentMonth();
            default:
                return "Payment for " + type.toLowerCase();
        }
    }
    
    private static String getCurrentMonth() {
        String[] months = {"January", "February", "March", "April", "May", "June",
                          "July", "August", "September", "October", "November", "December"};
        return months[Calendar.getInstance().get(Calendar.MONTH)];
    }
}
