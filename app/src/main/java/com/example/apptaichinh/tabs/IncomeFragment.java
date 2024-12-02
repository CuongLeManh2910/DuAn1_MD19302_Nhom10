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
import java.util.Locale;

public class IncomeFragment extends Fragment {

    private EditText dateEditText, notesEditText, amountEditText, newCategoryEditText;
    private TextView categoryTextView;
    private MaterialButton submitButton, addCategoryButton;
    private DatabaseHelper databaseHelper;
    private String selectedCategory = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_income, container, false);

        dateEditText = view.findViewById(R.id.dateEditTextIncome);
        notesEditText = view.findViewById(R.id.notesEditTextIncome);
        amountEditText = view.findViewById(R.id.amountEditTextIncome);
        newCategoryEditText = view.findViewById(R.id.newCategoryEditTextIncome);
        categoryTextView = view.findViewById(R.id.categoryTextViewIncome);
        submitButton = view.findViewById(R.id.submitButtonIncome);
        addCategoryButton = view.findViewById(R.id.addCategoryButtonIncome);
        GridLayout categoryGrid = view.findViewById(R.id.categoryGridIncome);

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
                // Xử lý lưu khoản thu vào SQLite
                saveIncome(date, notes, amount, selectedCategory);
            }
        });

        return view;
    }

    private void setCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy (EEE)", new Locale("vi", "VN"));
        String currentDate = dateFormat.format(calendar.getTime());
        dateEditText.setText(currentDate);
    }

    private void addCategory(String category) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_INCOME_CATEGORY_NAME, category);
        try {
            db.insertOrThrow(DatabaseHelper.TABLE_INCOME_CATEGORIES, null, values);
            Toast.makeText(getContext(), "Danh mục mới đã được thêm", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Snackbar.make(getView(), "Danh mục đã tồn tại", Snackbar.LENGTH_LONG).show();
        }
        db.close();
    }

    private void loadCategories(GridLayout categoryGrid) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        Cursor cursor = db.query(DatabaseHelper.TABLE_INCOME_CATEGORIES,
                new String[]{DatabaseHelper.COLUMN_INCOME_CATEGORY_NAME},
                null, null, null, null, null);

        categoryGrid.removeAllViews();
        while (cursor.moveToNext()) {
            String category = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_INCOME_CATEGORY_NAME));
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

    private void saveIncome(String date, String notes, String amount, String category) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_DATE, date);
        values.put(DatabaseHelper.COLUMN_NOTES, notes);
        values.put(DatabaseHelper.COLUMN_AMOUNT, Double.parseDouble(amount));
        values.put(DatabaseHelper.COLUMN_CATEGORY, category);

        long newRowId = db.insert(DatabaseHelper.TABLE_INCOME, null, values);  // Lưu vào bảng income
        if (newRowId != -1) {
            Toast.makeText(getContext(), "Khoản thu đã được lưu", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Lỗi khi lưu khoản thu", Toast.LENGTH_SHORT).show();
        }

        db.close();
    }
}
