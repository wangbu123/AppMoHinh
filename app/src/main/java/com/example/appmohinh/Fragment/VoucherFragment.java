package com.example.appmohinh.Fragment;

import android.content.Context;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.Toast;

import com.example.appmohinh.Activity.AddVoucherActivity;
import com.example.appmohinh.Adapter.VoucherAdapter;
import com.example.appmohinh.Model.Voucher;
import com.example.appmohinh.Model.VoucherUser;
import com.example.appmohinh.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VoucherFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VoucherFragment extends Fragment {
    private ImageView btn_addVoucher;
    private RecyclerView recy_voucher;
    private VoucherAdapter voucherAdapter;
    private List<Voucher> list;
    private List<VoucherUser> listVoucherUser;
    private String name;
    private String idUser;


    public VoucherFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VoucherFragment newInstance(String param1, String param2) {
        VoucherFragment fragment = new VoucherFragment();

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
        return inflater.inflate(R.layout.fragment_news, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btn_addVoucher = view.findViewById(R.id.btn_addVoucher);
        recy_voucher = view.findViewById(R.id.recy_voucher);
        SharedPreferences preferences = getActivity().getSharedPreferences("user_login", Context.MODE_PRIVATE);
        name = preferences.getString("email", "");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        idUser = user.getUid();
        if (name.equals("admin@gmail.com")) {
            btn_addVoucher.setVisibility(View.VISIBLE);

        }
        list = new ArrayList<>();
        listVoucherUser = new ArrayList<>();
        btn_addVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), AddVoucherActivity.class);
                startActivity(i);
            }
        });
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mRef = database.getReference("list_voucher");
        DatabaseReference mRef2 = database.getReference("list_VoucherUser");
        mRef2.child(idUser).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    VoucherUser voucherUser = dataSnapshot.getValue(VoucherUser.class);
                    if (voucherUser.getNameUser().equals(name)) {
                        listVoucherUser.add(voucherUser);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        mRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Voucher objVoucher = snapshot.getValue(Voucher.class);
                if (objVoucher != null) {
                    list.add(objVoucher);
                }
                voucherAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Voucher objVoucher = snapshot.getValue(Voucher.class);
                if (objVoucher == null || list == null || list.isEmpty()) {
                    return;
                }
                for (int i = 0; i < list.size(); i++) {
                    if (objVoucher.getId() == list.get(i).getId()) {
                        list.set(i, objVoucher);
                    }
                    voucherAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Voucher objVoucher = snapshot.getValue(Voucher.class);
                if (objVoucher == null || list == null || list.isEmpty()) {
                    return;
                }
                for (int i = 0; i < list.size(); i++) {
                    if (objVoucher.getId() == list.get(i).getId()) {
                        list.remove(i);
                    }
                    voucherAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        voucherAdapter = new VoucherAdapter(getActivity(), list, new VoucherAdapter.IclickListener() {
            @Override
            public void receive(Voucher voucher) {
                onClickReceiveVoucher(voucher);
            }
        });

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recy_voucher.setLayoutManager(linearLayoutManager);
        recy_voucher.addItemDecoration(dividerItemDecoration);
        recy_voucher.setAdapter(voucherAdapter);

    }


    private void onClickReceiveVoucher(Voucher objVoucher) {
        int id = 0;
        if (listVoucherUser.isEmpty() || listVoucherUser.size() == 0) {
            id = 1;
        } else {
            VoucherUser voucherUser = listVoucherUser.get((listVoucherUser.size() - 1));
            id = voucherUser.getId() + 1;
        }
        String timeVoucher = objVoucher.getTimeVoucher();
        String discountVoucher = objVoucher.getDiscountVoucher();
        VoucherUser voucherUser = new VoucherUser(id, objVoucher.getId(), discountVoucher, 1, timeVoucher, name);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mReference = database.getReference("list_VoucherUser");
        mReference.child(idUser).child(String.valueOf(voucherUser.getId())).setValue(voucherUser, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Toast.makeText(getContext(), "Nhận thành công", Toast.LENGTH_SHORT).show();
            }
        });
        FirebaseDatabase database2 = FirebaseDatabase.getInstance();
        DatabaseReference mReference2 = database2.getReference("list_voucher");
        objVoucher.setQuantity(objVoucher.getQuantity() - 1);
        mReference2.child(String.valueOf(objVoucher.getId())).updateChildren(objVoucher.updateQuantity(), new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                voucherAdapter.notifyDataSetChanged();

            }
        });
    }


}