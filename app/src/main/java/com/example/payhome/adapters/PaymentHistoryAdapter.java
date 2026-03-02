package com.example.payhome.adapters;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.payhome.R;
import com.example.payhome.models.Payment;

import java.util.List;

public class PaymentHistoryAdapter extends RecyclerView.Adapter<PaymentHistoryAdapter.PaymentViewHolder> {

    private List<Payment> payments;

    public PaymentHistoryAdapter(List<Payment> payments) {
        this.payments = payments;
    }

    @NonNull
    @Override
    public PaymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_payment_history, parent, false);
        return new PaymentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentViewHolder holder, int position) {
        Payment payment = payments.get(position);
        
        // Set icon and type
        holder.paymentIcon.setText(payment.getTypeIcon());
        holder.paymentType.setText(payment.getType());
        
        // Set description and date
        holder.paymentDescription.setText(payment.getDescription());
        holder.paymentDate.setText(payment.getFormattedDate() + ", " + payment.getFormattedTime());
        
        // Set amount
        holder.paymentAmount.setText(payment.getFormattedAmount());
        
        // Set status with color
        holder.paymentStatus.setText(payment.getStatus());
        holder.paymentStatus.setTextColor(Color.WHITE);
        
        // Set status background color
        GradientDrawable background = (GradientDrawable) holder.paymentStatus.getBackground();
        background.setColor(payment.getStatusColor());
    }

    @Override
    public int getItemCount() {
        return payments.size();
    }

    static class PaymentViewHolder extends RecyclerView.ViewHolder {
        TextView paymentIcon;
        TextView paymentType;
        TextView paymentDescription;
        TextView paymentDate;
        TextView paymentAmount;
        TextView paymentStatus;

        PaymentViewHolder(@NonNull View itemView) {
            super(itemView);
            paymentIcon = itemView.findViewById(R.id.payment_icon);
            paymentType = itemView.findViewById(R.id.payment_type);
            paymentDescription = itemView.findViewById(R.id.payment_description);
            paymentDate = itemView.findViewById(R.id.payment_date);
            paymentAmount = itemView.findViewById(R.id.payment_amount);
            paymentStatus = itemView.findViewById(R.id.payment_status);
        }
    }
}
