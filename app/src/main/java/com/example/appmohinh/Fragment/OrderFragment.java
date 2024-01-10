package com.example.appmohinh.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.appmohinh.Adapter.TabLayoutAdapter;
import com.example.appmohinh.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderFragment extends Fragment {

    TabLayout tabLayout;
    ViewPager2 viewPager2;
    TabLayoutAdapter adapter;




    public OrderFragment() {
        // Required empty public constructor
    }

    public static OrderFragment newInstance(String param1, String param2) {
        OrderFragment fragment = new OrderFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tabLayout = view.findViewById(R.id.tabLayoutBill);
        viewPager2 = view.findViewById(R.id.viewPagerTabLayout);
        adapter = new TabLayoutAdapter(getActivity());
        viewPager2.setAdapter(adapter);
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0:
                        tab.setText("Đơn đang chờ");
                        break;
                    case 1:
                        tab.setText("Đơn Đã Xác Nhận");
                        break;
                    case 2:
                        tab.setText("Đơn Đang giao");
                        break;
                    case 3:
                        tab.setText("Đơn Đã nhận  ");
                        break;
                    case 4:
                        tab.setText("Đơn Đã Hủy ");
                        break;
                }
            }
        });
        tabLayoutMediator.attach();
    }

}