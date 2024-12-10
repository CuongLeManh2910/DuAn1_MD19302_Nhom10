package com.example.apptaichinh.tabs;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.apptaichinh.DatabaseHelper;
import com.example.apptaichinh.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ExpenseFragment extends Fragment {

    private EditText dateEditText, notesEditText, amountEditText, newCategoryEditText;
    private TextView categoryTextView;
    private MaterialButton submitButton, addCategoryButton;
    private DatabaseHelper databaseHelper;
    private String selectedCategory = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expense, container, false);

        dateEditText = view.findViewById(R.id.dateEditText);
        notesEditText = view.findViewById(R.id.notesEditText);
        amountEditText = view.findViewById(R.id.amountEditText);
        newCategoryEditText = view.findViewById(R.id.newCategoryEditText);
        categoryTextView = view.findViewById(R.id.categoryTextView);
        submitButton = view.findViewById(R.id.submitButton);
        addCategoryButton = view.findViewById(R.id.addCategoryButton);
        GridLayout categoryGrid = view.findViewById(R.id.categoryGrid);

        databaseHelper = new DatabaseHelper(getContext());

        // Đặt ngày hiện tại
        setCurrentDate();

        // Hiển thị các danh mục lên lưới
        loadCategories(categoryGrid);

        // Đặt trình xử lý sự kiện cho nút thêm danh mục
        addCategoryButton.setOnClickListener(v -> {
            String newCategory = newCategoryEditText.getText().toString().trim();
            if (!TextUtils.isEmpty(newCategory)) {
                addCategory(newCategory);
                loadCategories(categoryGrid);
                newCategoryEditText.setText("");
            } else {
                Snackbar.make(view, "Vui lòng nhập tên danh mục mới", Snackbar.LENGTH_LONG).show();
            }
        });

        // Đặt trình xử lý sự kiện cho nút nhập
        submitButton.setOnClickListener(v -> {
            String date = dateEditText.getText().toString();
            String notes = notesEditText.getText().toString();
            String amount = amountEditText.getText().toString();

            if (TextUtils.isEmpty(date) || TextUtils.isEmpty(amount) || TextUtils.isEmpty(selectedCategory)) {
                Snackbar.make(view, "Vui lòng điền đầy đủ thông tin", Snackbar.LENGTH_LONG).show();
            } else {
                // Xử lý lưu khoản chi tiêu vào SQLite
                saveExpense(date, notes, amount, selectedCategory);
            }
        });

        return view;
    }

    private void setCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", new Locale("vi", "VN"));
        String currentDate = dateFormat.format(calendar.getTime());
        dateEditText.setText(currentDate);
    }

    private void addCategory(String category) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_EXPENSE_CATEGORY_NAME, category);
        try {
            db.insertOrThrow(DatabaseHelper.TABLE_EXPENSE_CATEGORIES, null, values);
            Toast.makeText(getContext(), "Danh mục mới đã được thêm", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Snackbar.make(getView(), "Danh mục đã tồn tại", Snackbar.LENGTH_LONG).show();
        }
        db.close();
    }

    private void loadCategories(GridLayout categoryGrid) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        Cursor cursor = db.query(DatabaseHelper.TABLE_EXPENSE_CATEGORIES,
                new String[]{DatabaseHelper.COLUMN_EXPENSE_CATEGORY_NAME},
                null, null, null, null, null);

        categoryGrid.removeAllViews();
        while (cursor.moveToNext()) {
            String category = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EXPENSE_CATEGORY_NAME));
            Button categoryButton = new Button(getContext());
            categoryButton.setText(category);
            categoryButton.setOnClickListener(v -> {
                selectedCategory = category;
                categoryTextView.setText(category);
            });
            categoryGrid.addView(categoryButton);
        }
        cursor.close();
        db.close();
    }

    private void saveExpense(String date, String notes, String amount, String category) {
        // Chuyển đổi ngày từ định dạng dd/MM/yyyy sang yyyy-MM-dd
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy", new Locale("vi", "VN"));
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = null;
        try {
            Date parsedDate = inputFormat.parse(date);
            if (parsedDate != null) {
                formattedDate = outputFormat.format(parsedDate);  // Chuyển sang định dạng yyyy-MM-dd
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Lỗi định dạng ngày", Toast.LENGTH_SHORT).show();
            return;
        }

        // Lưu dữ liệu vào cơ sở dữ liệu
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_DATE, formattedDate);  // Lưu ngày đã chuyển đổi
        values.put(DatabaseHelper.COLUMN_NOTES, notes);
        values.put(DatabaseHelper.COLUMN_AMOUNT, Double.parseDouble(amount));
        values.put(DatabaseHelper.COLUMN_CATEGORY, category);

        long newRowId = db.insert(DatabaseHelper.TABLE_EXPENSES, null, values);
        if (newRowId != -1) {
            Toast.makeText(getContext(), "Khoản chi đã được lưu", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Lỗi khi lưu khoản chi", Toast.LENGTH_SHORT).show();
        }

        db.close();
    }
}

