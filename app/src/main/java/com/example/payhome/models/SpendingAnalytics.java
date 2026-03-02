package com.example.payhome.models;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class SpendingAnalytics {
    private String userId;
    private Date startDate;
    private Date endDate;
    private List<Payment> payments;
    private List<Bill> bills;
    private Map<String, Double> spendingByCategory;
    private Map<String, Double> spendingByMonth;
    private double totalSpent;
    private double averageMonthlySpending;
    private double budgetLimit;
    private double budgetUtilization;

    public SpendingAnalytics(String userId, List<Payment> payments, List<Bill> bills, double budgetLimit) {
        this.userId = userId;
        this.payments = payments != null ? payments : new ArrayList<>();
        this.bills = bills != null ? bills : new ArrayList<>();
        this.budgetLimit = budgetLimit;
        this.startDate = calculateStartDate();
        this.endDate = new Date();
        this.spendingByCategory = new HashMap<>();
        this.spendingByMonth = new HashMap<>();
        calculateAnalytics();
    }

    private Date calculateStartDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -1); // Last 12 months
        return calendar.getTime();
    }

    private void calculateAnalytics() {
        // Calculate spending by category
        for (Payment payment : payments) {
            if (payment.getDate().after(startDate) && payment.getDate().before(endDate)) {
                String category = payment.getType();
                spendingByCategory.put(category, spendingByCategory.getOrDefault(category, 0.0) + payment.getAmount());
            }
        }

        // Calculate spending by month
        SimpleDateFormat monthFormat = new SimpleDateFormat("yyyy-MM", Locale.getDefault());
        for (Payment payment : payments) {
            if (payment.getDate().after(startDate) && payment.getDate().before(endDate)) {
                String month = monthFormat.format(payment.getDate());
                spendingByMonth.put(month, spendingByMonth.getOrDefault(month, 0.0) + payment.getAmount());
            }
        }

        // Calculate total spent
        totalSpent = spendingByCategory.values().stream().mapToDouble(Double::doubleValue).sum();

        // Calculate average monthly spending
        if (!spendingByMonth.isEmpty()) {
            averageMonthlySpending = totalSpent / spendingByMonth.size();
        } else {
            averageMonthlySpending = 0.0;
        }

        // Calculate budget utilization
        budgetUtilization = budgetLimit > 0 ? (totalSpent / budgetLimit) * 100 : 0.0;
    }

    // Getters
    public String getUserId() { return userId; }
    public Date getStartDate() { return startDate; }
    public Date getEndDate() { return endDate; }
    public List<Payment> getPayments() { return payments; }
    public List<Bill> getBills() { return bills; }
    public Map<String, Double> getSpendingByCategory() { return spendingByCategory; }
    public Map<String, Double> getSpendingByMonth() { return spendingByMonth; }
    public double getTotalSpent() { return totalSpent; }
    public double getAverageMonthlySpending() { return averageMonthlySpending; }
    public double getBudgetLimit() { return budgetLimit; }
    public double getBudgetUtilization() { return budgetUtilization; }

    // Helper methods
    public String getFormattedTotalSpent() {
        return String.format("KES %.2f", totalSpent);
    }

    public String getFormattedAverageMonthlySpending() {
        return String.format("KES %.2f", averageMonthlySpending);
    }

    public String getFormattedBudgetLimit() {
        return String.format("KES %.2f", budgetLimit);
    }

    public String getBudgetStatus() {
        if (budgetUtilization >= 100) return "Over Budget";
        if (budgetUtilization >= 90) return "Near Limit";
        if (budgetUtilization >= 75) return "On Track";
        return "Under Budget";
    }

    public int getBudgetStatusColor() {
        if (budgetUtilization >= 100) return android.graphics.Color.parseColor("#F44336"); // Red
        if (budgetUtilization >= 90) return android.graphics.Color.parseColor("#FF9800"); // Orange
        if (budgetUtilization >= 75) return android.graphics.Color.parseColor("#2196F3"); // Blue
        return android.graphics.Color.parseColor("#4CAF50"); // Green
    }

    public List<SpendingTrend> getSpendingTrends() {
        List<SpendingTrend> trends = new ArrayList<>();
        SimpleDateFormat monthFormat = new SimpleDateFormat("MMM yyyy", Locale.getDefault());
        
        // Sort months chronologically
        List<String> sortedMonths = spendingByMonth.keySet().stream()
                .sorted()
                .collect(Collectors.toList());
        
        for (String month : sortedMonths) {
            trends.add(new SpendingTrend(month, spendingByMonth.get(month)));
        }
        
        return trends;
    }

    public List<CategorySpending> getCategoryBreakdown() {
        List<CategorySpending> breakdown = new ArrayList<>();
        
        for (Map.Entry<String, Double> entry : spendingByCategory.entrySet()) {
            double percentage = totalSpent > 0 ? (entry.getValue() / totalSpent) * 100 : 0.0;
            breakdown.add(new CategorySpending(entry.getKey(), entry.getValue(), percentage));
        }
        
        // Sort by amount descending
        breakdown.sort((a, b) -> Double.compare(b.getAmount(), a.getAmount()));
        
        return breakdown;
    }

    public double getSpendingForCurrentMonth() {
        SimpleDateFormat monthFormat = new SimpleDateFormat("yyyy-MM", Locale.getDefault());
        String currentMonth = monthFormat.format(new Date());
        return spendingByMonth.getOrDefault(currentMonth, 0.0);
    }

    public double getSpendingForPreviousMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        SimpleDateFormat monthFormat = new SimpleDateFormat("yyyy-MM", Locale.getDefault());
        String previousMonth = monthFormat.format(calendar.getTime());
        return spendingByMonth.getOrDefault(previousMonth, 0.0);
    }

    public double getMonthOverMonthChange() {
        double currentMonth = getSpendingForCurrentMonth();
        double previousMonth = getSpendingForPreviousMonth();
        
        if (previousMonth == 0) return 0.0;
        return ((currentMonth - previousMonth) / previousMonth) * 100;
    }

    // Inner classes for data structures
    public static class SpendingTrend {
        private String month;
        private double amount;

        public SpendingTrend(String month, double amount) {
            this.month = month;
            this.amount = amount;
        }

        public String getMonth() { return month; }
        public double getAmount() { return amount; }
        public String getFormattedAmount() {
            return String.format("KES %.2f", amount);
        }
    }

    public static class CategorySpending {
        private String category;
        private double amount;
        private double percentage;

        public CategorySpending(String category, double amount, double percentage) {
            this.category = category;
            this.amount = amount;
            this.percentage = percentage;
        }

        public String getCategory() { return category; }
        public double getAmount() { return amount; }
        public double getPercentage() { return percentage; }
        public String getFormattedAmount() {
            return String.format("KES %.2f", amount);
        }
        public String getFormattedPercentage() {
            return String.format("%.1f%%", percentage);
        }

        public String getCategoryIcon() {
            switch (category.toLowerCase()) {
                case "rent": return "🏠";
                case "electricity": return "💡";
                case "water": return "💧";
                default: return "📄";
            }
        }
    }
}
