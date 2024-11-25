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
                fragment = new CardFragment();
                break;
            case 1:
                fragment = new BudgetFragment();
                break;
            case 2:
                fragment = new PayFragment();
                break;
            default:
        }
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public String getPageItem(int position) {
        switch (position){
            case 0:
                return "Cards";
            case 1:
                return "Moneys";
            case 2:
                return "Pays";
        }
        return "";
    }
}
