package com.example.appmohinh.Activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appmohinh.Model.Bill;
import com.example.appmohinh.Model.ItemCart;
import com.example.appmohinh.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddBillActivity extends AppCompatActivity {
    private TextView tvSumPrice;
    private TextView tvMenu;
    private EditText edName;
    private EditText edPhoneNumber;
    private EditText edAddress;
    private TextView tvNameVoucher;
    private Button btnHuy;
    private Button btnOrder;
    private String price, menu;
    private RelativeLayout relative_selectVoucher;
    private List<ItemCart> list;
    private List<Bill> listBill;
    private int giaGiam = 0;
    private String maGiam;
    private int tong = 0;
    private String idUser;
    private String nameVoucher;
    private int idVoucher;
    private List<ItemCart> listCart;
    private String textMenu = "";
    private int idProduct;
    private int soLuongMua = 0;
    FirebaseDatabase database = FirebaseDatabase.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bill);
        findID();
        list = new ArrayList<>();
        listBill = new ArrayList<>();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        idUser = user.getUid();
        String idUser = user.getUid();
//        textMenu= getIntent().getStringExtra("menu");
//        tvMenu.setText(textMenu);
        DatabaseReference mRef = database.getReference("list_cart");
        DatabaseReference mRef2 = database.getReference("list_bill");
        mRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Bill bill = dataSnapshot.getValue(Bill.class);
                    if (bill != null) {
                        listBill.add(bill);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mRef.child(idUser).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                tong = 0;

                ItemCart objCart = snapshot.getValue(ItemCart.class);
                if (objCart != null) {
                    list.add(objCart);
                }
                for (int i = 0; i < list.size(); i++) {
                    tong += list.get(i).getPrice() * list.get(i).getQuantity();
                    textMenu += "-" + list.get(i).getName() + "(" + list.get(i).getPrice() + " VNĐ)" + "-" + "Số lượng: " + list.get(i).getQuantity() + "\n";

                }

                tvSumPrice.setText(tong + " VNĐ");
                tvMenu.setText(textMenu);

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
                tvSumPrice.setText(tong + " VNĐ");
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        btnHuy.setOnClickListener(view -> {
            finish();
        });
        relative_selectVoucher.setOnClickListener(view -> {
            Intent i = new Intent(AddBillActivity.this, VoucherActivity.class);
            launcher.launch(i);
        });
        btnOrder.setOnClickListener(view -> {
            onCLickOrder();
        });


    }

    private ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Intent data = result.getData();
                    if (data != null) {
                        maGiam = data.getStringExtra("disscount");
                        nameVoucher = data.getStringExtra("idVoucher");
                        idVoucher = Integer.parseInt(nameVoucher);
                        giaGiam = Integer.parseInt(maGiam);
                        tvNameVoucher.setText("-" + giaGiam + "%");
                        tvSumPrice.setText(tong - ((tong * giaGiam) / 100) + " VNĐ");
                        Log.d("tong", "" + giaGiam);
                    }
                }
            });

    private void onCLickOrder() {
        DatabaseReference mRefVoucherUser = database.getReference("list_VoucherUser");


        int id = 0;
        if (listBill.isEmpty()) {
            id = 1;
        } else {
            Bill bill = listBill.get(listBill.size() - 1);
            id = bill.getId() + 1;
        }
        String name = edName.getText().toString().trim();
        String phoneNumber = edPhoneNumber.getText().toString();
        String menu = tvMenu.getText().toString();
        String price = tvSumPrice.getText().toString();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        Date currentTime = Calendar.getInstance().getTime();
        String time = format.format(currentTime.getTime());
        String address = edAddress.getText().toString();
        Bill objBill = new Bill(id, name, phoneNumber, address, menu, time, price, 1, idUser);
        FirebaseDatabase database2 = FirebaseDatabase.getInstance();
        DatabaseReference mRef2 = database2.getReference("list_bill");
        mRef2.child(String.valueOf(objBill.getId())).setValue(objBill, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Toast.makeText(AddBillActivity.this, "Đặt hàng thành công", Toast.LENGTH_SHORT).show();
                DatabaseReference mRef = database2.getReference("list_cart");
                DatabaseReference mRefVoucherUser = database2.getReference("list_VoucherUser");
                mRefVoucherUser.child(idUser).child(String.valueOf(idVoucher)).removeValue();
                textMenu = "";
                tvMenu.setText("");
                mRef.child(idUser).removeValue();
                finish();
            }
        });

    }


    private void findID() {
        tvSumPrice = (TextView) findViewById(R.id.tv_sumPrice);
        tvMenu = (TextView) findViewById(R.id.tv_menu);
        edName = (EditText) findViewById(R.id.ed_name);
        edPhoneNumber = (EditText) findViewById(R.id.ed_phoneNumber);
        edAddress = (EditText) findViewById(R.id.ed_address);
        tvNameVoucher = (TextView) findViewById(R.id.tv_nameVoucher);
        btnHuy = (Button) findViewById(R.id.btn_huy);
        btnOrder = (Button) findViewById(R.id.btn_Order);
        relative_selectVoucher = findViewById(R.id.relative_selectVoucher);
    }


}