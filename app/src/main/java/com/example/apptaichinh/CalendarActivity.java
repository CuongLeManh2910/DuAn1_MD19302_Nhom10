package com.example.apptaichinh;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.GridLayout;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        calendarGrid = findViewById(R.id.calendarGrid);
        incomeTextView = findViewById(R.id.incomeTextView);
        expenseTextView = findViewById(R.id.expenseTextView);
        totalTextView = findViewById(R.id.totalTextView);
        monthYearTextView = findViewById(R.id.monthYearTextView);

        databaseHelper = new DatabaseHelper(this);

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

        Cursor incomeCursor = db.rawQuery("SELECT date, amount FROM income WHERE date LIKE ?", new String[]{"%/" + currentMonth});
        while (incomeCursor.moveToNext()) {
            double amount = incomeCursor.getDouble(incomeCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_AMOUNT));
            totalIncome += amount;
        }
        incomeCursor.close();

        Cursor expenseCursor = db.rawQuery("SELECT date, amount FROM expenses WHERE date LIKE ?", new String[]{"%/" + currentMonth});
        while (expenseCursor.moveToNext()) {
            double amount = expenseCursor.getDouble(expenseCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_AMOUNT));
            totalExpense += amount;
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
        Cursor cursor = db.rawQuery("SELECT amount FROM income WHERE date = ?", new String[]{date});
        if (cursor.moveToFirst()) {
            income = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_AMOUNT));
        }
        cursor.close();
        return income;
    }

    private double getExpenseForDay(String date) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        double expense = 0;
        Cursor cursor = db.rawQuery("SELECT amount FROM expenses WHERE date = ?", new String[]{date});
        if (cursor.moveToFirst()) {
            expense = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_AMOUNT));
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

        Cursor incomeCursor = db.rawQuery("SELECT amount FROM income WHERE date = ?", new String[]{date});
        if (incomeCursor.moveToFirst()) {
            income = incomeCursor.getDouble(incomeCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_AMOUNT));
        }
        incomeCursor.close();

        Cursor expenseCursor = db.rawQuery("SELECT amount FROM expenses WHERE date = ?", new String[]{date});
        if (expenseCursor.moveToFirst()) {
            expense = expenseCursor.getDouble(expenseCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_AMOUNT));
        }
        expenseCursor.close();

        Toast.makeText(this, "Thu nhập: " + income + "₫\nChi tiêu: " + expense + "₫", Toast.LENGTH_LONG).show();
    }
}
