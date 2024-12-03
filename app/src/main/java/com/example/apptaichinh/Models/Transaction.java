package com.example.apptaichinh.Models;

public class Transaction {
    private String date;
    private String notes;
    private double amount;

    public Transaction(String date, String notes, double amount) {
        this.date = date;
        this.notes = notes;
        this.amount = amount;
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
}

