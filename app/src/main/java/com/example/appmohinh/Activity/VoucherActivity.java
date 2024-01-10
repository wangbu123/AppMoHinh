package com.example.appmohinh.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.appmohinh.Adapter.VoucherUserAdapter;
import com.example.appmohinh.Model.VoucherUser;
import com.example.appmohinh.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class VoucherActivity extends AppCompatActivity {
    private List<VoucherUser> list;
    private VoucherUserAdapter adapter;
    private RecyclerView recy_voucherUser;
    private String idUser;
    RelativeLayout layout_voucherNull;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voucher);
        list = new ArrayList<>();
        recy_voucherUser = findViewById(R.id.recy_voucherUser);
        layout_voucherNull = findViewById(R.id.layout_voucherNull);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        idUser = user.getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mRef = database.getReference("list_VoucherUser");
        mRef.child(idUser).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    VoucherUser objVoucherUser = dataSnapshot.getValue(VoucherUser.class);
                    if (objVoucherUser != null) {
                        list.add(objVoucherUser);
                    }
                }
                adapter.notifyDataSetChanged();
                if (!(list.isEmpty() || list.size() == 0)) {
                    layout_voucherNull.setVisibility(View.GONE);
                }else {
                    layout_voucherNull.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        adapter = new VoucherUserAdapter(list, this, new VoucherUserAdapter.IclickItem() {
            @Override
            public void useVoucher(VoucherUser voucherUser) {
                onClickUserVocher(voucherUser);
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recy_voucherUser.setAdapter(adapter);
        recy_voucherUser.setLayoutManager(linearLayoutManager);
        recy_voucherUser.addItemDecoration(dividerItemDecoration);
    }

    private void onClickUserVocher(VoucherUser voucherUser) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("disscount", voucherUser.getDiscountVoucher());
        resultIntent.putExtra("idVoucher", "" + voucherUser.getId());
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}