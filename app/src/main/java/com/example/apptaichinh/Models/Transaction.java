package com.example.apptaichinh.Models;


public class Transaction {
    private String date;
    private String notes;
    private double amount;
    private String type;

    // Constructor cập nhật để bao gồm trường type
    public Transaction(String date, String notes, double amount, String type) {
        this.date = date;
        this.notes = notes;
        this.amount = amount;
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public String getNotes() {
        return notes;
    }

    public double getAmount() {
        return amount;
    }

    public String getType() {
        return type;
    }
}
