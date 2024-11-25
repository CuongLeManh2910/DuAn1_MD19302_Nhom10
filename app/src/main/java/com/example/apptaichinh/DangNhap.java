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

public class DangNhap extends AppCompatActivity {

    EditText et_sdt, et_mk;
    Button btn_dangnhap, btn_dangky;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dang_nhap);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        et_sdt = findViewById(R.id.etPhoneNumber);
        et_mk = findViewById(R.id.etPassword);
        btn_dangnhap = findViewById(R.id.btnLogin);
        btn_dangky = findViewById(R.id.btnRegister);

        btn_dangky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DangNhap.this, DangKy.class);
                startActivity(intent);
            }
        });

        btn_dangnhap.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {



                    // Lấy giá trị từ EditText và xóa khoảng trắng thừa
                    String sdt = et_sdt.getText().toString().trim();
                    String password = et_mk.getText().toString().trim();

                    // Kiểm tra nếu trường số điện thoại bị bỏ trống
                    if (TextUtils.isEmpty(sdt)) {
                        Toast.makeText(DangNhap.this, "Vui lòng nhập số điện thoại!", Toast.LENGTH_SHORT).show();
                    }
                    // Kiểm tra nếu trường mật khẩu bị bỏ trống
                    else if (TextUtils.isEmpty(password)) {
                        Toast.makeText(DangNhap.this, "Vui lòng nhập mật khẩu!", Toast.LENGTH_SHORT).show();
                    }
                    // Nếu không có lỗi, thực hiện hành động tiếp theo
                    else {
                        Toast.makeText(DangNhap.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                        // Thêm logic xử lý đăng nhập tại đây
                        Intent intent = new Intent(DangNhap.this, MainActivity.class);
                        startActivity(intent);
                    }
                }



        });

    }
}