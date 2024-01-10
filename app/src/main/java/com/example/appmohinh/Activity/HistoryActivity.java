package com.example.appmohinh.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

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

public class HistoryActivity extends AppCompatActivity {
    private boolean isResum = true;
    private RecyclerView recy_history;
    private List<Bill> listBill;
    BillAdminAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        isResum = false;
        recy_history = findViewById(R.id.recy_history);
        listBill = new ArrayList<>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref1 = database.getReference("list_bill");
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        String idUser = mUser.getUid();
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
        adapter = new BillAdminAdapter(this, listBill);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recy_history.addItemDecoration(dividerItemDecoration);
        recy_history.setLayoutManager(linearLayoutManager);
        recy_history.setAdapter(adapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref1 = database.getReference("list_bill");
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        String idUser = mUser.getUid();
        if (isResum) {
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
            adapter = new BillAdminAdapter(this, listBill);
            recy_history.setAdapter(adapter);
        }
        isResum = true;
    }
}