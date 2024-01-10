package com.example.appmohinh.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.appmohinh.Fragment.AcountFragment;
import com.example.appmohinh.Fragment.CartFragment;
import com.example.appmohinh.Fragment.HomeFragment;
import com.example.appmohinh.Fragment.OrderFragment;
import com.example.appmohinh.Fragment.VoucherFragment;

public class ViewPager2Adapter extends FragmentStateAdapter {


    public ViewPager2Adapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new HomeFragment();
            case 1:
                return new VoucherFragment();
            case 2:
                return new OrderFragment();
            case 3:
                return new CartFragment();

            case 4:
                return new AcountFragment();
            default:
                return new HomeFragment();
        }

    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
