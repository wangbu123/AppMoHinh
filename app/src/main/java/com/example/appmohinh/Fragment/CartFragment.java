package com.example.appmohinh.Fragment;

import android.content.Intent;
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
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.appmohinh.Activity.AddBillActivity;
import com.example.appmohinh.Adapter.CartAdapter;
import com.example.appmohinh.Model.ItemCart;
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
 * Use the {@link CartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CartFragment extends Fragment {

    private RecyclerView recy_cart;
    private CartAdapter adapter;
    private TextView tv_sumPrice;
    private Button btn_oder;
    private List<ItemCart> list;

    private String textMenu = "";
    private RelativeLayout layout_cardNull;


    public CartFragment() {
        // Required empty public constructor
    }


    public static CartFragment newInstance(String param1, String param2) {
        CartFragment fragment = new CartFragment();

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
        return inflater.inflate(R.layout.fragment_cartragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recy_cart = view.findViewById(R.id.recy_cart);
        tv_sumPrice = view.findViewById(R.id.tv_sumPrice);
        btn_oder = view.findViewById(R.id.btn_Order);
        layout_cardNull = view.findViewById(R.id.layout_cardNull);
        list = new ArrayList<>();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String idUser = user.getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mRef = database.getReference("list_cart");
        mRef.child(idUser).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                int tong = 0;
                ItemCart objCart = snapshot.getValue(ItemCart.class);
                if (objCart != null) {
                    list.add(objCart);
                }
                for (int i = 0; i < list.size(); i++) {
                    tong += list.get(i).getPrice() * list.get(i).getQuantity();
                }
                tv_sumPrice.setText(tong + " VNĐ");
                if (!(list.isEmpty() || list.size() == 0 || list == null)) {
                    layout_cardNull.setVisibility(View.GONE);
                }else {
                    layout_cardNull.setVisibility(View.VISIBLE);
                }
                adapter.notifyDataSetChanged();


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                ItemCart objCart = snapshot.getValue(ItemCart.class);
                int tong = 0;
                if (objCart == null || list == null || list.isEmpty()) {
                    return;
                }
                for (int i = 0; i < list.size(); i++) {
                    if (objCart.getId() == list.get(i).getId()) {
                        list.set(i, objCart);

                    }
                }
                for (int i = 0; i < list.size(); i++) {
                    tong += list.get(i).getPrice() * list.get(i).getQuantity();
                }
                tv_sumPrice.setText(tong + " VNĐ");
                adapter.notifyDataSetChanged();
                if (!(list.isEmpty() || list.size() == 0 || list == null)) {
                    layout_cardNull.setVisibility(View.GONE);
                }else {
                    layout_cardNull.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                ItemCart objCart = snapshot.getValue(ItemCart.class);
                int tong = 0;
                if (objCart == null || list == null || list.isEmpty()) {
                    return;
                }
                for (int i = 0; i < list.size(); i++) {
                    if (objCart.getId() == list.get(i).getId()) {
                        list.remove(i);

                    }
                }
                for (int i = 0; i < list.size(); i++) {
                    tong += list.get(i).getPrice() * list.get(i).getQuantity();
                }
                tv_sumPrice.setText(tong + " VNĐ");
                adapter.notifyDataSetChanged();
                if (!(list.isEmpty() || list.size() == 0 || list == null)) {
                    layout_cardNull.setVisibility(View.GONE);
                }else {
                    layout_cardNull.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        adapter = new CartAdapter(list, new CartAdapter.IclickItem() {
            @Override
            public void deleteItemCart(ItemCart itemCart) {
                onClickRemoveItem(itemCart);
            }

            @Override
            public void augmentQuantity(ItemCart itemCart) {
                onclickAugmentQuantity(itemCart);
            }

            @Override
            public void reduceQuantity(ItemCart itemCart) {
                onclickReduceQuantity(itemCart);

            }
        }, getActivity());
        btn_oder.setOnClickListener(view1 -> {
            goToActivityAddBIll();

        });
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recy_cart.addItemDecoration(dividerItemDecoration);
        recy_cart.setAdapter(adapter);
        recy_cart.setLayoutManager(linearLayoutManager);
    }

    private void goToActivityAddBIll() {

//
        for (int i = 0; i < list.size(); i++) {
            textMenu += "-" + list.get(i).getName() + "(" + list.get(i).getPrice() + " VNĐ)" + "-" + "Số lượng: " + list.get(i).getQuantity() + "\n";
        }
        Intent i = new Intent(getActivity(), AddBillActivity.class);
        i.putExtra("sumPrice", tv_sumPrice.getText().toString());
        i.putExtra("menu", textMenu);
        startActivity(i);


    }

    private void onclickReduceQuantity(ItemCart itemCart) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String idUser = user.getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mRef = database.getReference("list_cart");

        itemCart.setQuantity(itemCart.getQuantity() - 1);

        mRef.child(idUser).child(String.valueOf(itemCart.getId())).setValue(itemCart.updateQuantity(), new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                adapter.notifyDataSetChanged();
            }
        });
        if (itemCart.getQuantity() == 0) {
            onClickRemoveItem(itemCart);
        }
    }

    private void onclickAugmentQuantity(ItemCart itemCart) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String idUser = user.getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mRef = database.getReference("list_cart");
        itemCart.setQuantity(itemCart.getQuantity() + 1);
        mRef.child(idUser).child(String.valueOf(itemCart.getId())).setValue(itemCart.updateQuantity(), new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                adapter.notifyDataSetChanged();
            }
        });

    }

    private void onClickRemoveItem(ItemCart itemCart) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String idUser = user.getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mRef = database.getReference("list_cart");
        mRef.child(idUser).child(String.valueOf(itemCart.getId())).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {

            }
        });

    }


}