//package com.example.apptaichinh;
//
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//public class DatabaseHelper extends SQLiteOpenHelper {
//
//    private static final String DATABASE_NAME = "expenses.db";
//    private static final int DATABASE_VERSION = 5;
//
//    public static final String TABLE_EXPENSES = "expenses";
//    public static final String COLUMN_ID = "_id";
//    public static final String COLUMN_DATE = "date";
//    public static final String COLUMN_NOTES = "notes";
//    public static final String COLUMN_AMOUNT = "amount";
//    public static final String COLUMN_CATEGORY = "category";
//
//    public static final String TABLE_EXPENSE_CATEGORIES = "expense_categories";
//    public static final String COLUMN_EXPENSE_CATEGORY_ID = "category_id";
//    public static final String COLUMN_EXPENSE_CATEGORY_NAME = "category_name";
//
//    public static final String TABLE_INCOME = "income";
//    public static final String TABLE_INCOME_CATEGORIES = "income_categories";
//    public static final String COLUMN_INCOME_CATEGORY_ID = "category_id";
//    public static final String COLUMN_INCOME_CATEGORY_NAME = "category_name";
//
//    private static final String TABLE_EXPENSES_CREATE =
//            "CREATE TABLE " + TABLE_EXPENSES + " (" +
//                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                    COLUMN_DATE + " TEXT, " +
//                    COLUMN_NOTES + " TEXT, " +
//                    COLUMN_AMOUNT + " REAL, " +
//                    COLUMN_CATEGORY + " TEXT" +
//                    ");";
//
//    private static final String TABLE_EXPENSE_CATEGORIES_CREATE =
//            "CREATE TABLE " + TABLE_EXPENSE_CATEGORIES + " (" +
//                    COLUMN_EXPENSE_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                    COLUMN_EXPENSE_CATEGORY_NAME + " TEXT UNIQUE NOT NULL" +
//                    ");";
//
//    private static final String TABLE_INCOME_CREATE =
//            "CREATE TABLE " + TABLE_INCOME + " (" +
//                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                    COLUMN_DATE + " TEXT, " +
//                    COLUMN_NOTES + " TEXT, " +
//                    COLUMN_AMOUNT + " REAL, " +
//                    COLUMN_CATEGORY + " TEXT" +
//                    ");";
//
//    private static final String TABLE_INCOME_CATEGORIES_CREATE =
//            "CREATE TABLE " + TABLE_INCOME_CATEGORIES + " (" +
//                    COLUMN_INCOME_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                    COLUMN_INCOME_CATEGORY_NAME + " TEXT UNIQUE NOT NULL" +
//                    ");";
//
//    public DatabaseHelper(Context context) {
//        super(context, DATABASE_NAME, null, DATABASE_VERSION);
//    }
//
//    @Override
//    public void onCreate(SQLiteDatabase db) {
//        db.execSQL(TABLE_EXPENSES_CREATE);
//        db.execSQL(TABLE_EXPENSE_CATEGORIES_CREATE);
//        db.execSQL(TABLE_INCOME_CREATE);
//        db.execSQL(TABLE_INCOME_CATEGORIES_CREATE);
//        addDefaultCategories(db);
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        if (oldVersion < 2) {
//            db.execSQL(TABLE_EXPENSE_CATEGORIES_CREATE);
//        }
//        if (oldVersion < 3) {
//            db.execSQL(TABLE_INCOME_CREATE);
//        }
//        if (oldVersion < 4) {
//            db.execSQL(TABLE_INCOME_CATEGORIES_CREATE);
//        }
//    }
//
//    private void addDefaultCategories(SQLiteDatabase db) {
//        String[] expenseCategories = {
//                "Ăn uống", "Hàng ngày", "Quần áo",
//                "Mỹ phẩm", "Giao lưu", "Phí y tế",
//                "Giáo dục", "Tiền điện", "Đi lại",
//                "Liên lạc", "Tiền nhà"
//        };
//
//        String[] incomeCategories = {
//                "Lương", "Thưởng", "Bán hàng",
//                "Đầu tư", "Tiết kiệm", "Khác"
//        };
//
//        for (String category : expenseCategories) {
//            ContentValues values = new ContentValues();
//            values.put(COLUMN_EXPENSE_CATEGORY_NAME, category);
//            try {
//                db.insertOrThrow(TABLE_EXPENSE_CATEGORIES, null, values);
//            } catch (Exception e) {
//                // Ignore duplicate entry
//            }
//        }
//
//        for (String category : incomeCategories) {
//            ContentValues values = new ContentValues();
//            values.put(COLUMN_INCOME_CATEGORY_NAME, category);
//            try {
//                db.insertOrThrow(TABLE_INCOME_CATEGORIES, null, values);
//            } catch (Exception e) {
//                // Ignore duplicate entry
//            }
//        }
//    }
//
//    // Phương thức để thêm thu nhập
//    public long addIncome(String date, String notes, double amount, String category) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put(COLUMN_DATE, date);
//        values.put(COLUMN_NOTES, notes);
//        values.put(COLUMN_AMOUNT, amount);
//        values.put(COLUMN_CATEGORY, category);
//        return db.insert(TABLE_INCOME, null, values);
//    }
//
//    // Phương thức để thêm chi tiêu
//    public long addExpense(String date, String notes, double amount, String category) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put(COLUMN_DATE, date);
//        values.put(COLUMN_NOTES, notes);
//        values.put(COLUMN_AMOUNT, amount);
//        values.put(COLUMN_CATEGORY, category);
//        return db.insert(TABLE_EXPENSES, null, values);
//    }
//
//    // Phương thức để lấy tổng thu nhập
//    public double getTotalIncome() {
//        SQLiteDatabase db = this.getReadableDatabase();
//        double totalIncome = 0;
//        String query = "SELECT SUM(amount) as total_income FROM income";
//        Cursor cursor = db.rawQuery(query, null);
//        if (cursor.moveToFirst()) {
//            int index = cursor.getColumnIndex("total_income");
//            if (index != -1) {
//                totalIncome = cursor.getDouble(index);
//            }
//        }
//        cursor.close();
//        return totalIncome;
//    }
//
//    // Phương thức để lấy tổng chi tiêu
//    public double getTotalExpense() {
//        SQLiteDatabase db = this.getReadableDatabase();
//        double totalExpense = 0;
//        String query = "SELECT SUM(amount) as total_expense FROM expenses";
//        Cursor cursor = db.rawQuery(query, null);
//        if (cursor.moveToFirst()) {
//            int index = cursor.getColumnIndex("total_expense");
//            if (index != -1) {
//                totalExpense = cursor.getDouble(index);
//            }
//        }
//        cursor.close();
//        return totalExpense;
//    }
////
////    // Phương thức để lấy tổng thu nhập theo ngày
////    public double getTotalIncomeByDay(String date) {
////        SQLiteDatabase db = this.getReadableDatabase();
////        double totalIncome = 0;
////        String query = "SELECT SUM(amount) as total_income FROM income WHERE date = ?";
////        Cursor cursor = db.rawQuery(query, new String[]{date});
////        if (cursor.moveToFirst()) {
////            int index = cursor.getColumnIndex("total_income");
////            if (index != -1) {
////                totalIncome = cursor.getDouble(index);
////            }
////        }
////        cursor.close();
////        return totalIncome;
////    }
////
////    // Phương thức để lấy tổng chi tiêu theo ngày
////    public double getTotalExpenseByDay(String date) {
////        SQLiteDatabase db = this.getReadableDatabase();
////        double totalExpense = 0;
////        String query = "SELECT SUM(amount) as total_expense FROM expenses WHERE date = ?";
////        Cursor cursor = db.rawQuery(query, new String[]{date});
////        if (cursor.moveToFirst()) {
////            int index = cursor.getColumnIndex("total_expense");
////            if (index != -1) {
////                totalExpense = cursor.getDouble(index);
////            }
////        }
////        cursor.close();
////        return totalExpense;
////    }
//
////    // Phương thức tính tổng thu nhập theo ngày
////    public double getTotalIncomeByDate(String date) {
////        SQLiteDatabase db = this.getReadableDatabase();
////        double totalIncome = 0;
////
////        // Câu truy vấn SQL để lấy tổng thu nhập trong ngày
////        String query = "SELECT SUM(amount) AS total_income FROM " + TABLE_INCOME + " WHERE " + COLUMN_DATE + " = ?";
////
////        Cursor cursor = db.rawQuery(query, new String[]{date});
////        if (cursor.moveToFirst()) {
////            int index = cursor.getColumnIndex("total_income");
////            if (index != -1) {
////                totalIncome = cursor.getDouble(index);
////            }
////        }
////        cursor.close();
////        return totalIncome;
////    }
////
////    // Phương thức tính tổng chi tiêu theo ngày
////    public double getTotalExpenseByDate(String date) {
////        SQLiteDatabase db = this.getReadableDatabase();
////        double totalExpense = 0;
////
////        // Câu truy vấn SQL để lấy tổng chi tiêu trong ngày
////        String query = "SELECT SUM(amount) AS total_expense FROM " + TABLE_EXPENSES + " WHERE " + COLUMN_DATE + " = ?";
////
////        Cursor cursor = db.rawQuery(query, new String[]{date});
////        if (cursor.moveToFirst()) {
////            int index = cursor.getColumnIndex("total_expense");
////            if (index != -1) {
////                totalExpense = cursor.getDouble(index);
////            }
////        }
////        cursor.close();
////        return totalExpense;
////    }
//
//    // Phương thức để lấy tổng chi tiêu theo ngày
//    public double getTotalExpenseByDate(String date) {
//        SQLiteDatabase db = this.getReadableDatabase();
//        double totalExpense = 0;
//
//        // Câu truy vấn SQL để lấy tổng chi tiêu theo ngày
//        String query = "SELECT SUM(amount) AS total_expense FROM " + TABLE_EXPENSES + " WHERE " + COLUMN_DATE + " = ?";
//
//        // Thực thi truy vấn với tham số ngày
//        Cursor cursor = db.rawQuery(query, new String[]{date});
//        if (cursor.moveToFirst()) {
//            int index = cursor.getColumnIndex("total_expense");
//            if (index != -1) {
//                totalExpense = cursor.getDouble(index);
//            }
//        }
//        cursor.close();
//        return totalExpense;
//    }
//
//    // Phương thức để lấy tổng thu nhập theo ngày
//    public double getTotalIncomeByDate(String date) {
//        SQLiteDatabase db = this.getReadableDatabase();
//        double totalIncome = 0;
//
//        // Câu truy vấn SQL để lấy tổng thu nhập theo ngày
//        String query = "SELECT SUM(amount) AS total_income FROM " + TABLE_INCOME + " WHERE " + COLUMN_DATE + " = ?";
//
//        // Thực thi truy vấn với tham số ngày
//        Cursor cursor = db.rawQuery(query, new String[]{date});
//        if (cursor.moveToFirst()) {
//            int index = cursor.getColumnIndex("total_income");
//            if (index != -1) {
//                totalIncome = cursor.getDouble(index);
//            }
//        }
//        cursor.close();
//        return totalIncome;
//    }
//
//
//
//    // Phương thức để lấy tổng thu nhập theo tháng
//    public double getTotalIncomeByMonth(String monthYear) {
//        SQLiteDatabase db = this.getReadableDatabase();
//        double totalIncome = 0;
//        String query = "SELECT SUM(amount) as total_income FROM income WHERE strftime('%m/%Y', date) = ?";
//        Cursor cursor = db.rawQuery(query, new String[]{monthYear});
//        if (cursor.moveToFirst()) {
//            int index = cursor.getColumnIndex("total_income");
//            if (index != -1) {
//                totalIncome = cursor.getDouble(index);
//            }
//        }
//        cursor.close();
//        return totalIncome;
//    }
//
//    // Phương thức để lấy tổng chi tiêu theo tháng
//    public double getTotalExpenseByMonth(String monthYear) {
//        SQLiteDatabase db = this.getReadableDatabase();
//        double totalExpense = 0;
//        String query = "SELECT SUM(amount) as total_expense FROM expenses WHERE strftime('%m/%Y', date) = ?";
//        Cursor cursor = db.rawQuery(query, new String[]{monthYear});
//        if (cursor.moveToFirst()) {
//            int index = cursor.getColumnIndex("total_expense");
//            if (index != -1) {
//                totalExpense = cursor.getDouble(index);
//            }
//        }
//        cursor.close();
//        return totalExpense;
//    }
//
//    // Phương thức để lấy các giao dịch gần đây
//    public Cursor getRecentTransactions() {
//        SQLiteDatabase db = this.getReadableDatabase();
//        String query = "SELECT * FROM expenses ORDER BY date DESC LIMIT 10";
//        return db.rawQuery(query, null);
//    }
//
//
//}
//


package com.example.apptaichinh;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "expenses.db";
    private static final int DATABASE_VERSION = 5;

    // Các bảng và cột
    public static final String TABLE_EXPENSES = "expenses";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_NOTES = "notes";
    public static final String COLUMN_AMOUNT = "amount";
    public static final String COLUMN_CATEGORY = "category";

    public static final String TABLE_EXPENSE_CATEGORIES = "expense_categories";
    public static final String COLUMN_EXPENSE_CATEGORY_ID = "category_id";
    public static final String COLUMN_EXPENSE_CATEGORY_NAME = "category_name";

    public static final String TABLE_INCOME = "income";
    public static final String TABLE_INCOME_CATEGORIES = "income_categories";
    public static final String COLUMN_INCOME_CATEGORY_ID = "category_id";
    public static final String COLUMN_INCOME_CATEGORY_NAME = "category_name";

    // Các câu lệnh tạo bảng
    private static final String TABLE_EXPENSES_CREATE =
            "CREATE TABLE " + TABLE_EXPENSES + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_DATE + " TEXT, " +
                    COLUMN_NOTES + " TEXT, " +
                    COLUMN_AMOUNT + " REAL, " +
                    COLUMN_CATEGORY + " TEXT" +
                    ");";

    private static final String TABLE_EXPENSE_CATEGORIES_CREATE =
            "CREATE TABLE " + TABLE_EXPENSE_CATEGORIES + " (" +
                    COLUMN_EXPENSE_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_EXPENSE_CATEGORY_NAME + " TEXT UNIQUE NOT NULL" +
                    ");";

    private static final String TABLE_INCOME_CREATE =
            "CREATE TABLE " + TABLE_INCOME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_DATE + " TEXT, " +
                    COLUMN_NOTES + " TEXT, " +
                    COLUMN_AMOUNT + " REAL, " +
                    COLUMN_CATEGORY + " TEXT" +
                    ");";

    private static final String TABLE_INCOME_CATEGORIES_CREATE =
            "CREATE TABLE " + TABLE_INCOME_CATEGORIES + " (" +
                    COLUMN_INCOME_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_INCOME_CATEGORY_NAME + " TEXT UNIQUE NOT NULL" +
                    ");";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_EXPENSES_CREATE);
        db.execSQL(TABLE_EXPENSE_CATEGORIES_CREATE);
        db.execSQL(TABLE_INCOME_CREATE);
        db.execSQL(TABLE_INCOME_CATEGORIES_CREATE);
        addDefaultCategories(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL(TABLE_EXPENSE_CATEGORIES_CREATE);
        }
        if (oldVersion < 3) {
            db.execSQL(TABLE_INCOME_CREATE);
        }
        if (oldVersion < 4) {
            db.execSQL(TABLE_INCOME_CATEGORIES_CREATE);
        }
    }

    // Thêm danh mục mặc định
    private void addDefaultCategories(SQLiteDatabase db) {
        String[] expenseCategories = {
                "Ăn uống", "Hàng ngày", "Quần áo",
                "Mỹ phẩm", "Giao lưu", "Phí y tế",
                "Giáo dục", "Tiền điện", "Đi lại",
                "Liên lạc", "Tiền nhà"
        };

        String[] incomeCategories = {
                "Lương", "Thưởng", "Bán hàng",
                "Đầu tư", "Tiết kiệm", "Khác"
        };

        for (String category : expenseCategories) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_EXPENSE_CATEGORY_NAME, category);
            try {
                db.insertOrThrow(TABLE_EXPENSE_CATEGORIES, null, values);
            } catch (Exception e) {
                // Ignore duplicate entry
            }
        }

        for (String category : incomeCategories) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_INCOME_CATEGORY_NAME, category);
            try {
                db.insertOrThrow(TABLE_INCOME_CATEGORIES, null, values);
            } catch (Exception e) {
                // Ignore duplicate entry
            }
        }
    }

    // Phương thức thêm thu nhập
    public long addIncome(String date, String notes, double amount, String category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DATE, date);  // Lưu theo định dạng yyyy-MM-dd
        values.put(COLUMN_NOTES, notes);
        values.put(COLUMN_AMOUNT, amount);
        values.put(COLUMN_CATEGORY, category);
        return db.insert(TABLE_INCOME, null, values);
    }

    // Phương thức thêm chi tiêu
    public long addExpense(String date, String notes, double amount, String category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DATE, date);  // Lưu theo định dạng yyyy-MM-dd
        values.put(COLUMN_NOTES, notes);
        values.put(COLUMN_AMOUNT, amount);
        values.put(COLUMN_CATEGORY, category);
        return db.insert(TABLE_EXPENSES, null, values);
    }


    // Phương thức để lấy tổng thu nhập
    public double getTotalIncome() {
        SQLiteDatabase db = this.getReadableDatabase();
        double totalIncome = 0;
        String query = "SELECT SUM(amount) as total_income FROM income";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            int index = cursor.getColumnIndex("total_income");
            if (index != -1) {
                totalIncome = cursor.getDouble(index);
            }
        }
        cursor.close();
        return totalIncome;
    }

    // Phương thức để lấy tổng chi tiêu
    public double getTotalExpense() {
        SQLiteDatabase db = this.getReadableDatabase();
        double totalExpense = 0;
        String query = "SELECT SUM(amount) as total_expense FROM expenses";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            int index = cursor.getColumnIndex("total_expense");
            if (index != -1) {
                totalExpense = cursor.getDouble(index);
            }
        }
        cursor.close();
        return totalExpense;
    }
//

    // Phương thức lấy tổng thu nhập theo ngày
    public double getTotalIncomeByDate(String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        double totalIncome = 0;
        String query = "SELECT SUM(amount) AS total_income FROM " + TABLE_INCOME + " WHERE " + COLUMN_DATE + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{date});
        if (cursor.moveToFirst()) {
            totalIncome = cursor.getDouble(cursor.getColumnIndex("total_income"));
        }
        cursor.close();
        return totalIncome;
    }

    // Phương thức lấy tổng chi tiêu theo ngày
    public double getTotalExpenseByDate(String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        double totalExpense = 0;
        String query = "SELECT SUM(amount) AS total_expense FROM " + TABLE_EXPENSES + " WHERE " + COLUMN_DATE + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{date});
        if (cursor.moveToFirst()) {
            totalExpense = cursor.getDouble(cursor.getColumnIndex("total_expense"));
        }
        cursor.close();
        return totalExpense;
    }

    // Phương thức lấy tổng thu nhập theo tháng (dạng mm/yyyy)
    public double getTotalIncomeByMonth(String monthYear) {
        SQLiteDatabase db = this.getReadableDatabase();
        double totalIncome = 0;
        String query = "SELECT SUM(amount) AS total_income FROM " + TABLE_INCOME + " WHERE strftime('%m/%Y', date) = ?";
        Cursor cursor = db.rawQuery(query, new String[]{monthYear});
        if (cursor.moveToFirst()) {
            totalIncome = cursor.getDouble(cursor.getColumnIndex("total_income"));
        }
        cursor.close();
        return totalIncome;
    }

    // Phương thức lấy tổng chi tiêu theo tháng (dạng mm/yyyy)
    public double getTotalExpenseByMonth(String monthYear) {
        SQLiteDatabase db = this.getReadableDatabase();
        double totalExpense = 0;
        String query = "SELECT SUM(amount) AS total_expense FROM " + TABLE_EXPENSES + " WHERE strftime('%m/%Y', date) = ?";
        Cursor cursor = db.rawQuery(query, new String[]{monthYear});
        if (cursor.moveToFirst()) {
            totalExpense = cursor.getDouble(cursor.getColumnIndex("total_expense"));
        }
        cursor.close();
        return totalExpense;
    }

    // Phương thức lấy các giao dịch gần đây
    public Cursor getRecentTransactions() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_EXPENSES + " ORDER BY " + COLUMN_DATE + " DESC LIMIT 10";
        return db.rawQuery(query, null);
    }
}

