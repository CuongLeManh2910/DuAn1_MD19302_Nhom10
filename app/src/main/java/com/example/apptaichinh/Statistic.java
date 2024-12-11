package com.example.apptaichinh;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Map;

public class Statistic extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private LinearLayout incomeBarLayout, expenseBarLayout;
    private TextView totalIncomeTextView, totalExpenseTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);
        ImageButton btn_back;

        // Initialize views
        incomeBarLayout = findViewById(R.id.incomeBarLayout);
        expenseBarLayout = findViewById(R.id.expenseBarLayout);
        totalIncomeTextView = findViewById(R.id.totalIncomeTextView);
        totalExpenseTextView = findViewById(R.id.totalExpenseTextView);
        btn_back = findViewById(R.id.backImageButton);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Statistic.this, CalendarActivity.class);
                startActivity(intent);
            }
        });

        // Initialize Database Helper
        dbHelper = new DatabaseHelper(this);

        // Get the current month and year in the format "MM/yyyy"
        String currentMonthYear = getCurrentMonthYear();

        // Display statistics for the current month
        displayCurrentMonthStatistics(currentMonthYear);

    }

    private String getCurrentMonthYear() {
        // Get current month and year in the format "MM/yyyy"
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM/yyyy");
        return sdf.format(new java.util.Date());
    }

    private void displayCurrentMonthStatistics(String currentMonthYear) {
        // Get total income and expenses for the current month
        double totalIncome = dbHelper.getTotalIncomeByMonth(currentMonthYear);
        double totalExpense = dbHelper.getTotalExpenseByMonth(currentMonthYear);

        // Display total income and expenses
        totalIncomeTextView.setText("Tổng Thu Nhập: " + totalIncome + " VNĐ");
        totalExpenseTextView.setText("Tổng Chi Tiêu: " + totalExpense + " VNĐ");

        // Display income and expenses by category
        displayIncomeByCategory(currentMonthYear);
        displayExpenseByCategory(currentMonthYear);
    }

    private void displayIncomeByCategory(String currentMonthYear) {
        // Get income by category for the current month
        Map<String, Double> incomeByCategory = dbHelper.getIncomeByMonthAndCategory(currentMonthYear);

        if (incomeByCategory != null && !incomeByCategory.isEmpty()) {
            double totalIncomeByCategories = 0;
            for (double income : incomeByCategory.values()) {
                totalIncomeByCategories += income;
            }

            // Clear previous data in the incomeBarLayout
            incomeBarLayout.removeAllViews();

            // Create bar chart for income categories
            for (String category : incomeByCategory.keySet()) {
                double income = incomeByCategory.get(category);

                if (totalIncomeByCategories > 0) {
                    // Calculate the percentage ratio for this category
                    float ratio = (float) (income / totalIncomeByCategories);

                    // Create a bar for this category
                    LinearLayout categoryIncomeBar = new LinearLayout(this);
                    categoryIncomeBar.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, ratio));
                    categoryIncomeBar.setBackgroundColor(getCategoryColor(category));

                    // Add category name inside the bar
                    TextView categoryText = new TextView(this);
                    categoryText.setText(category);
                    categoryText.setTextColor(Color.WHITE);
                    categoryText.setTextSize(14);
                    categoryText.setGravity(Gravity.CENTER_VERTICAL);

                    // Add category name to the bar
                    categoryIncomeBar.addView(categoryText);

                    // Add this category bar to the income bar layout
                    incomeBarLayout.addView(categoryIncomeBar);
                }
            }
        }
    }

    private void displayExpenseByCategory(String currentMonthYear) {
        // Get expense by category for the current month
        Map<String, Double> expenseByCategory = dbHelper.getExpenseByMonthAndCategory(currentMonthYear);

        if (expenseByCategory != null && !expenseByCategory.isEmpty()) {
            double totalExpenseByCategories = 0;
            for (double expense : expenseByCategory.values()) {
                totalExpenseByCategories += expense;
            }

            // Clear previous data in the expenseBarLayout
            expenseBarLayout.removeAllViews();

            // Create bar chart for expense categories
            for (String category : expenseByCategory.keySet()) {
                double expense = expenseByCategory.get(category);

                if (totalExpenseByCategories > 0) {
                    // Calculate the percentage ratio for this category
                    float ratio = (float) (expense / totalExpenseByCategories);

                    // Create a bar for this category
                    LinearLayout categoryExpenseBar = new LinearLayout(this);
                    categoryExpenseBar.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, ratio));
                    categoryExpenseBar.setBackgroundColor(getCategoryColor(category));

                    // Add category name inside the bar
                    TextView categoryText = new TextView(this);
                    categoryText.setText(category);
                    categoryText.setTextColor(Color.WHITE);
                    categoryText.setTextSize(14);
                    categoryText.setGravity(Gravity.CENTER_VERTICAL);

                    // Add category name to the bar
                    categoryExpenseBar.addView(categoryText);

                    // Add this category bar to the expense bar layout
                    expenseBarLayout.addView(categoryExpenseBar);
                }
            }
        }
    }

    private int getCategoryColor(String category) {
        // You can define specific colors for different categories
        switch (category) {
            case "Luong":
                return Color.parseColor("#4caf50"); // Green
            case "Hoc phi":
                return Color.parseColor("#ff9800"); // Orange
            case "Ve sinh":
                return Color.parseColor("#2196f3"); // Blue
            case "An uong":
                return Color.parseColor("#ff5722"); // Deep Orange
            case "Hang ngay":
                return Color.parseColor("#8bc34a"); // Light Green
            case "Quan ao":
                return Color.parseColor("#9c27b0"); // Purple
            case "My pham":
                return Color.parseColor("#e91e63"); // Pink
            case "Di lai":
                return Color.parseColor("#00bcd4"); // Cyan
            case "Giao luu":
                return Color.parseColor("#3f51b5"); // Indigo
            case "Y te":
                return Color.parseColor("#009688"); // Teal
            case "Giao duc":
                return Color.parseColor("#f44336"); // Red
            case "Tien dien":
                return Color.parseColor("#673ab7"); // Deep Purple
            case "Lien lac":
                return Color.parseColor("#ffeb3b"); // Yellow
            case "Tien nha":
                return Color.parseColor("#795548"); // Brown
            case "Thuong":
                return Color.parseColor("#c2185b"); // Deep Pink
            case "Ban hang":
                return Color.parseColor("#607d8b"); // Blue Grey
            case "Dau tu":
                return Color.parseColor("#9e9e9e"); // Grey
            case "Tiet kiem":
                return Color.parseColor("#3e2723"); // Dark Brown
            default:
                return Color.parseColor("#9e9e9e"); // Default Grey
        }

    }
}
