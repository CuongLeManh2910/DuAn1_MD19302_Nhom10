package com.example.apptaichinh;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.apptaichinh.Adapters.TransactionAdapter;
import com.example.apptaichinh.Models.Transaction;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private TextView totalBalance, expenses, incomes;
    private ListView transactionListView;
    private ImageButton alarmButton;
    private ImageView btn_home, btn_expenses, btn_stats, btn_profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Liên kết các đối tượng giao diện với các thành phần trong XML
        totalBalance = findViewById(R.id.total_balance);
        expenses = findViewById(R.id.expenses);
        incomes = findViewById(R.id.incomes);
        transactionListView = findViewById(R.id.transactionListView);
        alarmButton = findViewById(R.id.alarm);
        btn_expenses = findViewById(R.id.nav_expenses);
        btn_stats = findViewById(R.id.nav_stats);
        btn_profile = findViewById(R.id.nav_profile);

        databaseHelper = new DatabaseHelper(this);

        // Tải dữ liệu số dư, thu nhập, chi tiêu và các giao dịch gần đây
        loadBalanceData();
        loadTransactionData();

        // Đặt sự kiện cho nút báo thức
        alarmButton.setOnClickListener(v -> showTimePickerDialog());

        // Xử lý các sự kiện nút điều hướng
        btn_expenses.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CalendarActivity.class);
            startActivity(intent);
        });

        btn_stats.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Card_TK.class);
            startActivity(intent);
        });

        btn_profile.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Profile.class);
            startActivity(intent);
        });
    }

    private void loadBalanceData() {
        // Truy vấn tổng thu nhập và chi tiêu mà không cần lọc theo tháng
        double totalIncome = databaseHelper.getTotalIncome();
        double totalExpense = databaseHelper.getTotalExpense();

        // Tính toán số dư hiện tại
        double totalBalanceValue = totalIncome - totalExpense;

        // Cập nhật giao diện với các giá trị đã tính toán
        totalBalance.setText(String.format(Locale.getDefault(), "%,.2f₫", totalBalanceValue));
        incomes.setText(String.format(Locale.getDefault(), "%,.2f₫", totalIncome));
        expenses.setText(String.format(Locale.getDefault(), "%,.2f₫", totalExpense));
    }

    private void loadTransactionData() {
        ArrayList<Transaction> transactions = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        // Truy vấn các giao dịch gần đây từ cả thu nhập và chi tiêu
        Cursor cursor = db.rawQuery(
                "SELECT date, notes, amount, 'income' as type FROM income " +
                        "UNION ALL " +
                        "SELECT date, notes, amount, 'expenses' as type FROM expenses " +
                        "ORDER BY date DESC LIMIT 10", null);

        while (cursor.moveToNext()) {
            String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
            String notes = cursor.getString(cursor.getColumnIndexOrThrow("notes"));
            double amount = cursor.getDouble(cursor.getColumnIndexOrThrow("amount"));
            String type = cursor.getString(cursor.getColumnIndexOrThrow("type"));
            transactions.add(new Transaction(date, notes, amount, type));
        }
        cursor.close();

        // Tạo adapter để hiển thị danh sách giao dịch
        TransactionAdapter adapter = new TransactionAdapter(this, transactions);
        transactionListView.setAdapter(adapter);
    }

    private void showTimePickerDialog() {
        // Lấy thời gian hiện tại
        Calendar currentTime = Calendar.getInstance();
        int hour = currentTime.get(Calendar.HOUR_OF_DAY);
        int minute = currentTime.get(Calendar.MINUTE);

        // Tạo TimePickerDialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minute1) -> {
            // Khi người dùng chọn thời gian, thiết lập báo thức
            setAlarm(hourOfDay, minute1);
        }, hour, minute, true);

        timePickerDialog.show();
    }

    private void setAlarm(int hourOfDay, int minute) {
        // Lấy đối tượng AlarmManager
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        // Tạo Intent để BroadcastReceiver nhận sự kiện báo thức
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent,
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ? PendingIntent.FLAG_IMMUTABLE : 0);

        // Đặt thời gian báo thức
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        // Đặt báo thức lặp lại hàng ngày
        if (alarmManager != null) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pendingIntent);
            Toast.makeText(this, "Đã đặt thông báo vào " + hourOfDay + ":" + minute, Toast.LENGTH_SHORT).show();
        }
    }
}
