package com.example.payhome;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.payhome.R;
import com.example.payhome.adapters.CategorySpendingAdapter;
import com.example.payhome.models.Payment;
import com.example.payhome.models.SpendingAnalytics;
import com.example.payhome.utils.PaymentData;

import java.util.List;

public class AnalyticsActivity extends Activity {
    
    private static final String TAG = "AnalyticsActivity";
    private TextView totalSpentText;
    private TextView budgetStatusText;
    private RecyclerView categoryRecycler;
    private SpendingAnalytics analytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics);
        
        Log.i(TAG, "AnalyticsActivity onCreate started");
        
        initViews();
        setupAnalytics();
        loadAnalyticsData();
        
        Log.i(TAG, "AnalyticsActivity setup completed");
    }
    
    private void initViews() {
        totalSpentText = findViewById(R.id.total_spent);
        budgetStatusText = findViewById(R.id.budget_status);
        categoryRecycler = findViewById(R.id.category_recycler);
    }
    
    private void setupAnalytics() {
        categoryRecycler.setLayoutManager(new LinearLayoutManager(this));
    }
    
    private void loadAnalyticsData() {
        Log.d(TAG, "Loading analytics data...");
        
        // Get mock payment data
        List<Payment> payments = PaymentData.getMockPayments();
        
        // Create analytics with budget limit
        analytics = new SpendingAnalytics("USER001", payments, null, 50000.0);
        
        // Update UI
        updateSummaryCards();
        updateCategoryBreakdown();
        
        Log.d(TAG, "Analytics data loaded successfully");
    }
    
    private void updateSummaryCards() {
        // Update total spent
        totalSpentText.setText(analytics.getFormattedTotalSpent());
        
        // Update budget status
        budgetStatusText.setText(analytics.getBudgetStatus());
        budgetStatusText.setTextColor(analytics.getBudgetStatusColor());
        
        Log.d(TAG, "Summary cards updated");
    }
    
    private void updateCategoryBreakdown() {
        List<SpendingAnalytics.CategorySpending> categorySpending = analytics.getCategoryBreakdown();
        
        CategorySpendingAdapter adapter = new CategorySpendingAdapter(categorySpending);
        categoryRecycler.setAdapter(adapter);
        
        Log.d(TAG, "Category breakdown updated with " + categorySpending.size() + " categories");
    }
}
