package com.example.apptaichinh.tabs;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apptaichinh.Adapters.BudgetAdapter;
import com.example.apptaichinh.Dao.BudgetDao;
import com.example.apptaichinh.Models.Budget;
import com.example.apptaichinh.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class BudgetFragment extends Fragment {

    ArrayList<Budget> list;
    BudgetAdapter adapter;
    BudgetDao budgetDao;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.money_item,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView rcv = view.findViewById(R.id.rcv);
        FloatingActionButton btn_add = view.findViewById(R.id.btn_add);
        budgetDao = new BudgetDao(getContext());
        list = budgetDao.getAll();

        adapter = new BudgetAdapter(list,getContext(),budgetDao);
        rcv.setLayoutManager(new LinearLayoutManager(getContext()));
        rcv.setAdapter(adapter);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                LayoutInflater inflater = getLayoutInflater();
                View view = inflater.inflate(R.layout.dialog_add_budget,null);
                builder.setView(view);
                AlertDialog dialog = builder.create();
                dialog.show();
                TextView txt_ten = view.findViewById(R.id.edt_ten_budget);
                TextView txt_tongtien = view.findViewById(R.id.edt_tien_budget);
                TextView txt_ngaykt = view.findViewById(R.id.ngay_budget);
                Button btn_luu = view.findViewById(R.id.btn_luu);

                btn_luu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String ten = txt_ten.getText().toString().trim();
                        String tongtien = txt_tongtien.getText().toString().trim();
                        String ngaykt = txt_ngaykt.getText().toString().trim();

                        if(ten.isEmpty() | tongtien.isEmpty() | ngaykt.isEmpty()){
                            Toast.makeText(getContext(), "Vui long nhap day du thong tin", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(tongtien.length() <= 0){
                            Toast.makeText(getContext(), "So tirn phai lon hon 0", Toast.LENGTH_SHORT).show();
                        }

                        Budget budget = new Budget();
                        budget.setTenDanhMuc(ten);
                        budget.setSoTienDeRa(Double.valueOf(tongtien));
                        budget.setNgayKetThuc(ngaykt);
                        int check = budgetDao.insert(budget);
                        if(check>0){
                            list.add(budget);
                            dialog.dismiss();
                            Toast.makeText(getContext(), "Them thanh cong", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(getContext(), "Them that bai", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
