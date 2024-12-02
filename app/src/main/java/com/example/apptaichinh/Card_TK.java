package com.example.apptaichinh;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.apptaichinh.tabs.ThongKePagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class Card_TK extends AppCompatActivity {

    ImageView btn_home,btn_expenses,btn_stats,btn_profile;
    ThongKePagerAdapter adapter;
    TabLayout tabs;
    ViewPager2 pager2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_tk);
        initView();


        btn_home = findViewById(R.id.nav_home);
        btn_expenses = findViewById(R.id.nav_expenses);
        btn_stats = findViewById(R.id.nav_stats);
        btn_profile = findViewById(R.id.nav_profile);





        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Card_TK.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btn_expenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Card_TK.this, CalendarActivity.class);
                startActivity(intent);
            }
        });

//        btn_stats.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, ThongKe.class);
//                startActivity(intent);
//            }
//        });

        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Card_TK.this, Profile.class);
                startActivity(intent);
            }
        });
    }
    private void initView() {
        tabs = findViewById(R.id.tabs);
        pager2 = findViewById(R.id.pager);
        adapter = new ThongKePagerAdapter(this);
        pager2.setAdapter(adapter);
        new TabLayoutMediator(tabs,pager2,
                (tab, position) -> tab.setText(adapter.getPageItem(position))).attach();
    }

}

