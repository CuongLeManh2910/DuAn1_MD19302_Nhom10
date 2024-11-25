package com.example.apptaichinh;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "AppTaiChinh";
    private static final int VERSION = 2;

    public DbHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql_ds_budget = "CREATE TABLE DS_BUDGET (\n" +
                "    id_budget     INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    ten_budget    TEXT,\n" +
                "    tien_budget   INTEGER,\n" +
                "    ngaykt_budget DATE\n" +
                ");";
        db.execSQL(sql_ds_budget);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS DS_BUDGET");
            onCreate(db);
    }
}
