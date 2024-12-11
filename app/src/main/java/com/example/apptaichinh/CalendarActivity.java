package com.example.apptaichinh;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class CalendarActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private GridLayout calendarGrid;
    private TextView incomeTextView, expenseTextView, totalTextView, monthYearTextView;
    private TextView dailyIncomeTextView, dailyExpenseTextView;
    ImageView btn_home, btn_expenses, btn_stats, btn_profile;
    ImageButton btn_show;
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", new Locale("vi", "VN"));

    // Biến để lưu trữ TextView của ngày đã chọn
    private TextView selectedDayView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        // Khởi tạo các thành phần giao diện người dùng
        calendarGrid = findViewById(R.id.calendarGrid);
        incomeTextView = findViewById(R.id.incomeTextView);
        expenseTextView = findViewById(R.id.expenseTextView);
        totalTextView = findViewById(R.id.totalTextView);
        monthYearTextView = findViewById(R.id.monthYearTextView);
        dailyIncomeTextView = findViewById(R.id.dailyIncomeTextView);
        dailyExpenseTextView = findViewById(R.id.dailyExpenseTextView);
        btn_home = findViewById(R.id.nav_home);
        btn_expenses = findViewById(R.id.nav_expenses);
        btn_stats = findViewById(R.id.nav_stats);
        btn_profile = findViewById(R.id.nav_profile);
        btn_show = findViewById(R.id.btn_show);



        // Khởi tạo DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Tải lịch và dữ liệu thu nhập/chi tiêu
        loadCalendar();
        loadIncomeExpenseData();

        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CalendarActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btn_stats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CalendarActivity.this, Card_TK.class);
                startActivity(intent);
            }
        });

        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CalendarActivity.this, Profile.class);
                startActivity(intent);
            }
        });

        btn_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CalendarActivity.this, Statistic.class);
                startActivity(intent);
            }
        });
    }



    private void loadCalendar() {
        Calendar calendar = Calendar.getInstance();
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        int currentMonth = calendar.get(Calendar.MONTH); // tháng từ 0 - 11
        int currentYear = calendar.get(Calendar.YEAR);

        // Đặt ngày đầu tháng cho tháng hiện tại
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;  // Ngày trong tuần của ngày đầu tháng (0 = Chủ nhật)
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);  // Số ngày trong tháng


        // Hiển thị tháng và năm hiện tại
        String monthYear = String.format(Locale.getDefault(), "%02d/%d", currentMonth + 1, currentYear);
        monthYearTextView.setText(monthYear);  // Hiển thị tháng và năm lên TextView

        calendarGrid.removeAllViews();  // Xóa hết các ô hiện tại trong lưới

        // Thêm các ô trống cho các ngày trước ngày đầu tháng
        for (int i = 0; i < firstDayOfWeek; i++) {
            TextView emptyView = new TextView(this);
            emptyView.setLayoutParams(new GridLayout.LayoutParams());  // Đảm bảo ô trống có kích thước hợp lý
            calendarGrid.addView(emptyView);  // Thêm ô trống
        }

        // Thêm các ô cho các ngày trong tháng
        for (int day = 1; day <= daysInMonth; day++) {
            final int finalDay = day;
            TextView dayView = new TextView(this);
            dayView.setText(String.valueOf(day));  // Hiển thị ngày
            dayView.setPadding(8, 8, 8, 8);  // Thêm padding cho các ngày
            dayView.setGravity(Gravity.CENTER);  // Căn giữa nội dung

            // Thiết lập màu sắc hoặc hình thức cho các ngày (tùy chọn)
            if (finalDay == currentDay) {
                // Nếu là ngày hiện tại, thay đổi màu nền
                dayView.setBackgroundColor(ContextCompat.getColor(this, R.color.selected_day_color));  // Màu cho ngày hiện tại
            } else {
                dayView.setBackgroundColor(ContextCompat.getColor(this, R.color.normal_day_color));  // Màu cho các ngày khác
            }

            // Đảm bảo rằng các ô ngày có kích thước phù hợp với các ô trống
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 0;
            params.height = GridLayout.LayoutParams.WRAP_CONTENT;
            params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            dayView.setLayoutParams(params);

            // Thêm vào lưới
            calendarGrid.addView(dayView);

            // Xử lý sự kiện click cho mỗi ngày
            dayView.setOnClickListener(v -> {
                // Tạo chuỗi ngày theo định dạng yyyy-MM-dd
                String date = String.format(Locale.getDefault(), "%04d-%02d-%02d", currentYear, currentMonth + 1, finalDay);
                showIncomeExpenseForDay(date);

                // Nếu có ngày đã chọn trước đó, đặt lại màu nền
                if (selectedDayView != null) {
                    selectedDayView.setBackgroundColor(ContextCompat.getColor(CalendarActivity.this, R.color.normal_day_color)); // Đặt lại màu nền cho ngày cũ
                }

                // Đổi màu nền của ngày được chọn
                dayView.setBackgroundColor(ContextCompat.getColor(CalendarActivity.this, R.color.selected_day_color)); // Màu nền của ngày được chọn

                // Lưu lại TextView của ngày hiện tại đã chọn
                selectedDayView = dayView;
            });
        }
    }

    String currentMonthYear = getCurrentMonthYear();

    private String getCurrentMonthYear() {
        // Get current month and year in the format "MM/yyyy"
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM/yyyy");
        return sdf.format(new java.util.Date());
    }

    private void loadIncomeExpenseData() {

        // Truy vấn tổng thu nhập và chi tiêu mà không cần lọc theo tháng
        double totalIncome = databaseHelper.getTotalIncomeByMonth(currentMonthYear);
        double totalExpense = databaseHelper.getTotalExpenseByMonth(currentMonthYear);

        // Tính toán số dư hiện tại
        double totalBalanceValue = totalIncome - totalExpense;

        // Cập nhật giao diện với các giá trị đã tính toán
        incomeTextView.setText(totalIncome + "₫");
        expenseTextView.setText(totalExpense + "₫");
        totalTextView.setText(totalBalanceValue + "₫");
    }

    private void showIncomeExpenseForDay(String date) {
        Log.d("SelectedDate", "Ngày chọn: " + date);  // Kiểm tra xem ngày có đúng không

        // Lấy tổng thu nhập và chi tiêu từ cơ sở dữ liệu
        double income = databaseHelper.getTotalIncomeByDate(date);
        double expense = databaseHelper.getTotalExpenseByDate(date);

        // Log kết quả thu nhập và chi tiêu
        Log.d("IncomeExpenseData", "Thu nhập: " + income + "₫, Chi tiêu: " + expense + "₫");

        // Cập nhật giao diện
        dailyIncomeTextView.setText("Thu nhập trong ngày: " + income + "₫");
        dailyExpenseTextView.setText("Chi tiêu trong ngày: " + expense + "₫");

        // Hiển thị thông báo
        String message = "Ngày " + date;
        Toast.makeText(CalendarActivity.this, message, Toast.LENGTH_LONG).show();
    }
}
