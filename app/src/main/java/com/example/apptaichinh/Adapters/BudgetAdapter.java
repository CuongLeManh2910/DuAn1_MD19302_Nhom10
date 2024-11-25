package com.example.apptaichinh.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apptaichinh.Dao.BudgetDao;
import com.example.apptaichinh.Models.Budget;
import com.example.apptaichinh.R;
import com.example.apptaichinh.list_budget.BudgetHolder;

import java.util.ArrayList;

public class BudgetAdapter extends RecyclerView.Adapter<BudgetHolder> {

    ArrayList<Budget> list;
    Context context;
    BudgetDao budgetDao;

    public BudgetAdapter(ArrayList<Budget> list, Context context, BudgetDao budgetDao) {
        this.list = list;
        this.context = context;
        this.budgetDao = budgetDao;
    }

    @NonNull
    @Override
    public BudgetHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BudgetHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_budget,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull BudgetHolder holder, int position) {
        Budget budget = list.get(position);
        holder.txt_ten_budget.setText(budget.getTenDanhMuc());
        holder.txt_ngay_budget.setText("Hạn: " + budget.getNgayKetThuc());
        holder.txt_tong_tien_budget.setText(String.valueOf(budget.getSoTienDeRa()));

        // Thêm sự kiện nhấn giữ để xóa mục
        holder.itemView.setOnLongClickListener(v -> {
            // Xóa item khi nhấn giữ
            deleteItem(position);
            return true;  // Trả về true để sự kiện không bị tiếp tục xử lý
        });
    }

    // Phương thức xóa item
    private void deleteItem(int position) {
        Budget budgetToDelete = list.get(position);

        // Xóa ngân sách từ cơ sở dữ liệu
        if (budgetDao.delete(budgetToDelete)) {
            // Xóa item khỏi danh sách và cập nhật RecyclerView
            list.remove(position);
            notifyItemRemoved(position);
            Toast.makeText(context, "Xóa thành công:  " + budgetToDelete.getTenDanhMuc(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Xóa thất bại", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }
}
