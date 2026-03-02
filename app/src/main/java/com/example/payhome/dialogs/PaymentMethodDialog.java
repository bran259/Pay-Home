package com.example.payhome.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.payhome.R;

public class PaymentMethodDialog {
    
    public interface PaymentMethodListener {
        void onPaymentMethodSelected(String method, String paymentType, String phoneNumber, String password);
    }
    
    public static void showPaymentMethodDialog(Context context, String paymentType, String phoneNumber, 
                                            String password, PaymentMethodListener listener) {
        
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_payment_method, null);
        
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView);
        builder.setTitle("Select Payment Method");
        
        AlertDialog dialog = builder.create();
        
        RadioGroup paymentMethodsGroup = dialogView.findViewById(R.id.payment_methods_group);
        Button confirmButton = dialogView.findViewById(R.id.btn_confirm_payment_method);
        Button cancelButton = dialogView.findViewById(R.id.btn_cancel_payment_method);
        
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = paymentMethodsGroup.getCheckedRadioButtonId();
                String selectedMethod = "";
                
                if (selectedId == R.id.rb_mpesa) {
                    selectedMethod = "M-Pesa";
                } else if (selectedId == R.id.rb_bank_card) {
                    selectedMethod = "Bank Card";
                } else if (selectedId == R.id.rb_mobile_banking) {
                    selectedMethod = "Mobile Banking";
                }
                
                if (!selectedMethod.isEmpty()) {
                    listener.onPaymentMethodSelected(selectedMethod, paymentType, phoneNumber, password);
                    dialog.dismiss();
                } else {
                    Toast.makeText(context, "Please select a payment method", Toast.LENGTH_SHORT).show();
                }
            }
        });
        
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        
        dialog.show();
    }
}
