package com.example.apptaichinh.tabs;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ThongKePagerAdapter extends FragmentStateAdapter {
    public ThongKePagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = null;
        switch (position){
            case 0:
                fragment = new ExpenseFragment();
                break;
            case 1:
                fragment = new IncomeFragment();
                break;
            default:
        }
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    public String getPageItem(int position) {
        switch (position){
            case 0:
                return "Khoản chi";
            case 1:
                return "Khoản thu";
        }
        return "";
    }
}
