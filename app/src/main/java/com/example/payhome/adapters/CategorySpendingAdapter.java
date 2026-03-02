package com.example.payhome.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.payhome.R;
import com.example.payhome.models.SpendingAnalytics;

import java.util.List;

public class CategorySpendingAdapter extends RecyclerView.Adapter<CategorySpendingAdapter.CategoryViewHolder> {

    private List<SpendingAnalytics.CategorySpending> categorySpendingList;

    public CategorySpendingAdapter(List<SpendingAnalytics.CategorySpending> categorySpendingList) {
        this.categorySpendingList = categorySpendingList;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category_spending, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        SpendingAnalytics.CategorySpending categorySpending = categorySpendingList.get(position);
        
        // Set icon and name
        holder.categoryIcon.setText(categorySpending.getCategoryIcon());
        holder.categoryName.setText(categorySpending.getCategory());
        
        // Set amount and percentage
        holder.spendingAmount.setText(categorySpending.getFormattedAmount());
        holder.spendingPercentage.setText(categorySpending.getFormattedPercentage() + " of total spending");
        
        // Set progress bar
        int percentage = (int) Math.round(categorySpending.getPercentage());
        holder.progressBar.setProgress(percentage);
        holder.percentageText.setText(categorySpending.getFormattedPercentage());
    }

    @Override
    public int getItemCount() {
        return categorySpendingList.size();
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView categoryIcon;
        TextView categoryName;
        TextView spendingAmount;
        TextView spendingPercentage;
        ProgressBar progressBar;
        TextView percentageText;

        CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryIcon = itemView.findViewById(R.id.category_icon);
            categoryName = itemView.findViewById(R.id.category_name);
            spendingAmount = itemView.findViewById(R.id.spending_amount);
            spendingPercentage = itemView.findViewById(R.id.spending_percentage);
            progressBar = itemView.findViewById(R.id.progress_bar);
            percentageText = itemView.findViewById(R.id.percentage_text);
        }
    }
}
