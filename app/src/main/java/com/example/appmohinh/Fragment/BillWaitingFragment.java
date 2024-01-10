package com.example.appmohinh.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.appmohinh.Adapter.BillAdminAdapter;
import com.example.appmohinh.Model.Bill;
import com.example.appmohinh.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BillWaitingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BillWaitingFragment extends Fragment {

    private RecyclerView recy_bill;
    private List<Bill> listBill, listBillAdmin;
    private BillAdminAdapter adapterAdmin, adapter;
    private String idUser;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    private boolean isNewActivity = true;

    public BillWaitingFragment() {
        // Required empty public constructor
    }

    public static BillWaitingFragment newInstance() {
        BillWaitingFragment fragment = new BillWaitingFragment();

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
        return inflater.inflate(R.layout.fragment_bill_waiting, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isNewActivity = false;
        recy_bill = view.findViewById(R.id.recy_billWaiting);
        listBill = new ArrayList<>();
        listBillAdmin = new ArrayList<>();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        idUser = user.getUid();
        DatabaseReference mRef = database.getReference("list_bill");


        SharedPreferences preferences = getActivity().getSharedPreferences("user_login", getActivity().MODE_PRIVATE);
        String email = preferences.getString("email", "");

        if (email.equals("admin@gmail.com")) {
            mRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    Bill bill = snapshot.getValue(Bill.class);

                    if (bill != null && bill.getStatus() == 1) {
                        listBillAdmin.add(bill);
                    }
                    adapterAdmin.notifyDataSetChanged();
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    Bill objBill = snapshot.getValue(Bill.class);

                    for (int i = 0; i < listBillAdmin.size(); i++) {
                        if (objBill.getId() == listBillAdmin.get(i).getId()) {
                            listBillAdmin.set(i, objBill);
                        }
                    }
                    adapterAdmin.notifyDataSetChanged();

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                    Bill objBill = snapshot.getValue(Bill.class);


                    for (int i = 0; i < listBillAdmin.size(); i++) {
                        if (objBill.getId() == listBillAdmin.get(i).getId()) {
                            listBillAdmin.remove(i);
                        }
                    }
                    adapterAdmin.notifyDataSetChanged();
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            adapterAdmin = new BillAdminAdapter(getActivity(), listBillAdmin);

            recy_bill.setAdapter(adapterAdmin);
        } else {
            mRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    Bill objBill = snapshot.getValue(Bill.class);
                    if (objBill != null && objBill.getStatus() == 1 && objBill.getIdUser().equals(idUser)) {
                        listBill.add(objBill);
                    }
                    adapter.notifyDataSetChanged();

                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    Bill objBill = snapshot.getValue(Bill.class);

                    if (objBill == null || listBill == null || listBill.isEmpty()) {
                        return;
                    }
                    for (int i = 0; i < listBill.size(); i++) {
                        if (objBill.getId() == listBill.get(i).getId()) {
                            listBill.set(i, objBill);
                        }
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                    Bill objBill = snapshot.getValue(Bill.class);

                    if (objBill == null || listBill == null || listBill.isEmpty()) {
                        return;
                    }
                    for (int i = 0; i < listBill.size(); i++) {
                        if (objBill.getId() == listBill.get(i).getId()) {
                            listBill.remove(i);
                        }
                    }
                    adapter.notifyDataSetChanged();

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            adapter = new BillAdminAdapter(getActivity(), listBill);
            recy_bill.setAdapter(adapter);
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        recy_bill.addItemDecoration(dividerItemDecoration);
        recy_bill.setLayoutManager(linearLayoutManager);
    }


    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences preferences = getActivity().getSharedPreferences("user_login", getActivity().MODE_PRIVATE);
        String email = preferences.getString("email", "");
        if (isNewActivity) {
            DatabaseReference ref1 = database.getReference("list_bill");
            if (email.equals("admin@gmail.com")) {
                if (listBillAdmin != null) {
                    listBillAdmin.clear();
                }
                ref1.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        Bill bill = snapshot.getValue(Bill.class);
                        if (bill != null && bill.getStatus() == 1) {
                            listBillAdmin.add(bill);
                        }
                        adapterAdmin.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        Bill objBill = snapshot.getValue(Bill.class);

                        for (int i = 0; i < listBillAdmin.size(); i++) {
                            if (objBill.getId() == listBillAdmin.get(i).getId()) {
                                listBillAdmin.set(i, objBill);
                            }
                        }
                        adapterAdmin.notifyDataSetChanged();

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                        Bill objBill = snapshot.getValue(Bill.class);


                        for (int i = 0; i < listBillAdmin.size(); i++) {
                            if (objBill.getId() == listBillAdmin.get(i).getId()) {
                                listBillAdmin.remove(i);
                            }
                        }
                        adapterAdmin.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                adapterAdmin = new BillAdminAdapter(getActivity(), listBillAdmin);
                recy_bill.setAdapter(adapterAdmin);

            } else {
                if (listBill != null) {
                    listBill.clear();
                }
                ref1.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        Bill objBill = snapshot.getValue(Bill.class);

                        if (objBill != null && objBill.getStatus() == 1 && objBill.getIdUser().equals(idUser)) {
                            listBill.add(objBill);
                        }
                        adapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        Bill objBill = snapshot.getValue(Bill.class);

                        if (objBill == null || listBill == null || listBill.isEmpty()) {
                            return;
                        }
                        for (int i = 0; i < listBill.size(); i++) {
                            if (objBill.getId() == listBill.get(i).getId()) {
                                listBill.set(i, objBill);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                        Bill objBill = snapshot.getValue(Bill.class);

                        if (objBill == null || listBill == null || listBill.isEmpty()) {
                            return;
                        }
                        for (int i = 0; i < listBill.size(); i++) {
                            if (objBill.getId() == listBill.get(i).getId()) {
                                listBill.remove(i);
                            }
                        }
                        adapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                adapter = new BillAdminAdapter(getActivity(), listBill);
                recy_bill.setAdapter(adapter);
            }
        }
        isNewActivity = true;


    }
}

