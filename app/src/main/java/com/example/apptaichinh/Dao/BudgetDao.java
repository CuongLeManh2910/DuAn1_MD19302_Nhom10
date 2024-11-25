package com.example.apptaichinh.Dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.apptaichinh.DbHelper;
import com.example.apptaichinh.Models.Budget;

import java.util.ArrayList;

public class BudgetDao {
    DbHelper dbHelper;
    SQLiteDatabase db;

    public BudgetDao(Context context) {
        dbHelper = new DbHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    // Phương thức chèn ngân sách vào cơ sở dữ liệu
    public int insert(Budget budget) {
        ContentValues values = new ContentValues();
        values.put("ten_budget", budget.getTenDanhMuc());
        values.put("ngaykt_budget", budget.getNgayKetThuc());
        values.put("tien_budget", budget.getSoTienDeRa());
        long result = db.insert("DS_BUDGET", null, values); // insert() trả về long, không phải int
        return (result == -1) ? 0 : 1;  // Trả về 0 nếu lỗi, 1 nếu thành công
    }

    // Phương thức lấy tất cả ngân sách
    public ArrayList<Budget> getAll() {
        ArrayList<Budget> list = new ArrayList<>();
        String sql = "SELECT * FROM DS_BUDGET";
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Budget budget = new Budget();
                budget.setId(cursor.getInt(0));
                budget.setTenDanhMuc(cursor.getString(1));
                budget.setSoTienDeRa(cursor.getDouble(2));
                budget.setNgayKetThuc(cursor.getString(3));
                list.add(budget);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    // Phương thức xóa ngân sách dựa trên ID
    public boolean delete(Budget budget) {
        // Kiểm tra xem ngân sách có ID hay không (Giả sử 'id' là khóa chính trong bảng)
        String whereClause = "id_budget = ?";
        String[] whereArgs = new String[]{String.valueOf(budget.getId())}; // Giả sử Budget có id()

        int rowsAffected = db.delete("DS_BUDGET", whereClause, whereArgs);

        return rowsAffected > 0; // Nếu xóa thành công, trả về true, nếu không trả về false
    }
}

