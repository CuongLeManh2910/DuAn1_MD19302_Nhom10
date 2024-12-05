package com.example.apptaichinh.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.apptaichinh.Models.Transaction;
import com.example.apptaichinh.R;

import java.util.ArrayList;
import java.util.Locale;

public class TransactionAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Transaction> transactions;

    public TransactionAdapter(Context context, ArrayList<Transaction> transactions) {
        this.context = context;
        this.transactions = transactions;
    }

    @Override
    public int getCount() {
        return transactions.size();
    }

    @Override
    public Object getItem(int position) {
        return transactions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.transaction_item, parent, false);
        }

        Transaction transaction = transactions.get(position);

        TextView transactionDate = convertView.findViewById(R.id.transaction_date);
        TextView transactionNotes = convertView.findViewById(R.id.transaction_notes);
        TextView transactionAmount = convertView.findViewById(R.id.transaction_amount);
        TextView transactionType = convertView.findViewById(R.id.transaction_type);

        transactionDate.setText(transaction.getDate());
        transactionNotes.setText(transaction.getNotes());
        transactionAmount.setText(String.format(Locale.getDefault(), "%,.2f₫", transaction.getAmount()));
        transactionType.setText(transaction.getType().equals("income") ? "Thu" : "Chi");

        return convertView;
    }
}
