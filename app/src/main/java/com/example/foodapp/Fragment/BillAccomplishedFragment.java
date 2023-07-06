package com.example.foodapp.Fragment;

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

import com.example.foodapp.Adapter.BillAdminAdapter;
import com.example.foodapp.Model.Bill;
import com.example.foodapp.R;
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
 * Use the {@link BillAccomplishedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BillAccomplishedFragment extends Fragment {
    private RecyclerView recy_billAccomplished;
    private List<Bill> listBill, listBillAdmin;
    private BillAdminAdapter adapter, adapterAdmin;
    private String idUser;


    public BillAccomplishedFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static BillAccomplishedFragment newInstance() {
        BillAccomplishedFragment fragment = new BillAccomplishedFragment();

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
        return inflater.inflate(R.layout.fragment_bill_accomplished, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recy_billAccomplished = view.findViewById(R.id.recy_billAccomplished);
        listBill = new ArrayList<>();
        listBillAdmin = new ArrayList<>();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        idUser = user.getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mRef = database.getReference("list_bill");
        mRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Bill bill = snapshot.getValue(Bill.class);
                if (bill != null && bill.getStatus() == 4) {
                    listBillAdmin.add(bill);
                }

                adapterAdmin.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Bill objBill = snapshot.getValue(Bill.class);

                if (objBill == null || listBillAdmin == null || listBillAdmin.isEmpty()) {
                    return;
                }
                for (int i = 0; i < listBillAdmin.size(); i++) {
                    if (objBill.getId() == listBillAdmin.get(i).getId()) {
                        listBillAdmin.set(i, objBill);
                    }
                }
                adapterAdmin.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        mRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Bill objBill = snapshot.getValue(Bill.class);
                if (objBill != null && objBill.getStatus() == 4 && objBill.getIdUser().equals(idUser)) {
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
        adapterAdmin = new BillAdminAdapter(getActivity(), listBillAdmin);
        SharedPreferences preferences = getActivity().getSharedPreferences("user_login", getActivity().MODE_PRIVATE);
        String email = preferences.getString("email", "");
        if (email.equals("admin@gmail.com")) {
            recy_billAccomplished.setAdapter(adapterAdmin);
        } else {
            recy_billAccomplished.setAdapter(adapter);
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        recy_billAccomplished.addItemDecoration(dividerItemDecoration);
        recy_billAccomplished.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences preferences = getActivity().getSharedPreferences("user_login", getActivity().MODE_PRIVATE);
        String email = preferences.getString("email", "");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref1 = database.getReference("list_bill");
        if (email.equals("admin@gmail.com")) {
            if (listBillAdmin != null) {
                listBillAdmin.clear();
            }
            ref1.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    Bill bill = snapshot.getValue(Bill.class);


                    if (bill != null && bill.getStatus() == 4) {
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
            recy_billAccomplished.setAdapter(adapterAdmin);

        } else {
            if (listBill != null) {
                listBill.clear();
            }
            ref1.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    Bill objBill = snapshot.getValue(Bill.class);

                    if (objBill != null && objBill.getStatus() == 4 && objBill.getIdUser().equals(idUser)) {
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
            recy_billAccomplished.setAdapter(adapter);
        }
    }
}