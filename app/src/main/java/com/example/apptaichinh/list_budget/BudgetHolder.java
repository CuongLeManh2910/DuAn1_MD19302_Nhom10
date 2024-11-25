package com.example.apptaichinh.list_budget;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apptaichinh.R;

public class BudgetHolder extends RecyclerView.ViewHolder {
    public final TextView txt_ten_budget;
    public final TextView txt_ngay_budget;
    final TextView txt_tien_tieu;
    public final TextView txt_tong_tien_budget;


    public BudgetHolder(@NonNull View itemView) {
        super(itemView);
        txt_ten_budget = itemView.findViewById(R.id.txt_ten_budget);
        txt_ngay_budget = itemView.findViewById(R.id.txt_ngay_budget);
        txt_tien_tieu = itemView.findViewById(R.id.txt_tien_tieu);
        txt_tong_tien_budget = itemView.findViewById(R.id.txt_tong_tien_budget);
    }
}
