package com.example.appmohinh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.appmohinh.Adapter.ViewPager2Adapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private ViewPager2 id_pager2;
    private BottomNavigationView bottom_Nav;
    private ViewPager2Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        id_pager2 = findViewById(R.id.id_viewPager2);
        bottom_Nav = findViewById(R.id.id_botNav);
        adapter = new ViewPager2Adapter(this);
        id_pager2.setAdapter(adapter);
        id_pager2.setUserInputEnabled(false);
        SharedPreferences preferences = getSharedPreferences("user_login", MODE_PRIVATE);
        String email = preferences.getString("email", "");
        if(email.equals("admin@gmail.com")){
            bottom_Nav.getMenu().findItem(R.id.menu_cart).setVisible(false);
        }
        bottom_Nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_home:
                        id_pager2.setCurrentItem(0);
                        break;
                    case R.id.menu_voucher:
                        id_pager2.setCurrentItem(1);
                        break;
                    case R.id.menu_oder:
                        id_pager2.setCurrentItem(2);
                        break;

                    case R.id.menu_cart:
                        id_pager2.setCurrentItem(3);
                        break;

                    case R.id.menu_acount:
                        id_pager2.setCurrentItem(4);
                        break;
                }
                return true;
            }
        });
        id_pager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        bottom_Nav.getMenu().findItem(R.id.menu_home).setChecked(true);
                        break;
                    case 1:
                        bottom_Nav.getMenu().findItem(R.id.menu_voucher).setChecked(true);
                        break;
                    case 2:
                        bottom_Nav.getMenu().findItem(R.id.menu_oder).setChecked(true);
                        break;
                    case 3:
                        bottom_Nav.getMenu().findItem(R.id.menu_cart).setChecked(true);
                        break;

                    case 4:
                        bottom_Nav.getMenu().findItem(R.id.menu_acount).setChecked(true);
                        break;
                }
            }
        });
    }
}