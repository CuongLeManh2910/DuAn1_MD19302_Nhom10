package com.example.apptaichinh;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DangKy extends AppCompatActivity {

    Button btn_dangky, btn_huy;
    EditText et_user, et_sdt, et_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dang_ky);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        et_user = findViewById(R.id.etUsername);
        et_sdt = findViewById(R.id.etPhoneNumberRegister);
        et_password = findViewById(R.id.etPasswordRegister);
        btn_huy = findViewById(R.id.btnHuy);
        btn_dangky = findViewById(R.id.btnRegisterSubmit);

        btn_dangky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = et_user.getText().toString().trim();
                String sdt = et_sdt.getText().toString().trim();
                String password = et_password.getText().toString().trim();

                if (TextUtils.isEmpty(user)) {
                    Toast.makeText(DangKy.this, "Vui lòng nhập tên người dùng!", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(sdt)) {
                    Toast.makeText(DangKy.this, "Vui lòng nhập số điện thoại!", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(password)) {
                    Toast.makeText(DangKy.this, "Vui lòng nhập mật khẩu!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DangKy.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(DangKy.this, DangNhap.class);
                    startActivity(intent);

                }
            }


        });
        btn_huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DangKy.this, DangNhap.class);
                startActivity(intent);

            }
        });
    }
}