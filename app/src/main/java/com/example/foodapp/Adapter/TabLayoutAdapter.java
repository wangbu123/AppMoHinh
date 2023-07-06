package com.example.foodapp.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.foodapp.Fragment.BillAccomplishedFragment;
import com.example.foodapp.Fragment.BillCancelledFragment;
import com.example.foodapp.Fragment.BillConfirmedFragment;
import com.example.foodapp.Fragment.BillDeliveryFragment;
import com.example.foodapp.Fragment.BillWaitingFragment;

public class TabLayoutAdapter extends FragmentStateAdapter {
    public TabLayoutAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment =null;
        switch (position) {
            case 0:
                fragment = BillWaitingFragment.newInstance();
                break;
            case 1:
                fragment = BillConfirmedFragment.newInstance();
                break;
            case 2:
                fragment = BillDeliveryFragment.newInstance();
                break;
            case 3:
                fragment = BillAccomplishedFragment.newInstance();
                break;
            case 4:
                fragment = BillCancelledFragment.newInstance();
                break;
        }
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
