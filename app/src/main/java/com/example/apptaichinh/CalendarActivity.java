package com.example.apptaichinh;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CalendarActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private GridLayout calendarGrid;
    private TextView incomeTextView, expenseTextView, totalTextView, monthYearTextView;
    ImageView btn_home, btn_expenses, btn_stats, btn_profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        calendarGrid = findViewById(R.id.calendarGrid);
        incomeTextView = findViewById(R.id.incomeTextView);
        expenseTextView = findViewById(R.id.expenseTextView);
        totalTextView = findViewById(R.id.totalTextView);
        monthYearTextView = findViewById(R.id.monthYearTextView);
        btn_home = findViewById(R.id.nav_home);
        btn_expenses = findViewById(R.id.nav_expenses);
        btn_stats = findViewById(R.id.nav_stats);
        btn_profile = findViewById(R.id.nav_profile);

        databaseHelper = new DatabaseHelper(this);

        btn_home.setOnClickListener(v -> {
            Intent intent = new Intent(CalendarActivity.this, MainActivity.class);
            startActivity(intent);
        });

        btn_stats.setOnClickListener(v -> {
            Intent intent = new Intent(CalendarActivity.this, Card_TK.class);
            startActivity(intent);
        });

        btn_profile.setOnClickListener(v -> {
            Intent intent = new Intent(CalendarActivity.this, Profile.class);
            startActivity(intent);
        });

        loadCalendar();
        loadIncomeExpenseData();
    }

    private void loadCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        String monthYear = new SimpleDateFormat("MM/yyyy", Locale.getDefault()).format(calendar.getTime());
        monthYearTextView.setText(monthYear);

        calendarGrid.removeAllViews();
        for (int i = 0; i < firstDayOfWeek; i++) {
            TextView emptyView = new TextView(this);
            calendarGrid.addView(emptyView);
        }

        for (int day = 1; day <= daysInMonth; day++) {
            final int finalDay = day;
            TextView dayView = new TextView(this);
            dayView.setText(String.valueOf(day));
            dayView.setPadding(8, 8, 8, 8);
            calendarGrid.addView(dayView);

            // Hiển thị dữ liệu thu nhập và chi tiêu trực tiếp từ cơ sở dữ liệu
            String date = String.format(Locale.getDefault(), "%02d/%02d/%04d", day, calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR));
            double income = getIncomeForDay(date);
            double expense = getExpenseForDay(date);

            if (income > 0 || expense > 0) {
                String text = String.format(Locale.getDefault(), "%d\nThu: %.0f\nChi: %.0f", day, income, expense);
                dayView.setText(text);
            }

            dayView.setOnClickListener(v -> showIncomeExpenseForDay(finalDay));
        }
    }

    private void loadIncomeExpenseData() {
        Calendar calendar = Calendar.getInstance();
        String currentMonth = new SimpleDateFormat("MM/yyyy", Locale.getDefault()).format(calendar.getTime());

        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        double totalIncome = 0;
        double totalExpense = 0;

        String incomeQuery = "SELECT SUM(amount) as total_income FROM income WHERE strftime('%m/%Y', date) = ?";
        Cursor incomeCursor = db.rawQuery(incomeQuery, new String[]{currentMonth});
        if (incomeCursor.moveToFirst()) {
            totalIncome = incomeCursor.getDouble(incomeCursor.getColumnIndexOrThrow("total_income"));
        }
        incomeCursor.close();

        String expenseQuery = "SELECT SUM(amount) as total_expense FROM expenses WHERE strftime('%m/%Y', date) = ?";
        Cursor expenseCursor = db.rawQuery(expenseQuery, new String[]{currentMonth});
        if (expenseCursor.moveToFirst()) {
            totalExpense = expenseCursor.getDouble(expenseCursor.getColumnIndexOrThrow("total_expense"));
        }
        expenseCursor.close();

        double total = totalIncome - totalExpense;

        incomeTextView.setText("Thu nhập: " + totalIncome + "₫");
        expenseTextView.setText("Chi tiêu: " + totalExpense + "₫");
        totalTextView.setText("Tổng: " + total + "₫");
    }

    private double getIncomeForDay(String date) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        double income = 0;
        String query = "SELECT SUM(amount) as total_income FROM income WHERE date = ?";
        Cursor cursor = db.rawQuery(query, new String[]{date});
        if (cursor.moveToFirst()) {
            income = cursor.getDouble(cursor.getColumnIndexOrThrow("total_income"));
        }
        cursor.close();
        return income;
    }

    private double getExpenseForDay(String date) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        double expense = 0;
        String query = "SELECT SUM(amount) as total_expense FROM expenses WHERE date = ?";
        Cursor cursor = db.rawQuery(query, new String[]{date});
        if (cursor.moveToFirst()) {
            expense = cursor.getDouble(cursor.getColumnIndexOrThrow("total_expense"));
        }
        cursor.close();
        return expense;
    }

    private void showIncomeExpenseForDay(int day) {
        String dayString = String.format(Locale.getDefault(), "%02d", day);
        Calendar calendar = Calendar.getInstance();
        String currentMonth = new SimpleDateFormat("MM/yyyy", Locale.getDefault()).format(calendar.getTime());
        String date = dayString + "/" + currentMonth;

        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        double income = 0;
        double expense = 0;

        String incomeQuery = "SELECT SUM(amount) as total_income FROM income WHERE date = ?";
        Cursor incomeCursor = db.rawQuery(incomeQuery, new String[]{date});
        if (incomeCursor.moveToFirst()) {
            income = incomeCursor.getDouble(incomeCursor.getColumnIndexOrThrow("total_income"));
        }
        incomeCursor.close();

        String expenseQuery = "SELECT SUM(amount) as total_expense FROM expenses WHERE date = ?";
        Cursor expenseCursor = db.rawQuery(expenseQuery, new String[]{date});
        if (expenseCursor.moveToFirst()) {
            expense = expenseCursor.getDouble(expenseCursor.getColumnIndexOrThrow("total_expense"));
        }
        expenseCursor.close();

        Toast.makeText(this, "Thu nhập: " + income + "₫\nChi tiêu: " + expense + "₫", Toast.LENGTH_LONG).show();
    }
}
