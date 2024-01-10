package com.example.appmohinh.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.appmohinh.Model.ItemCart;
import com.example.appmohinh.Model.Product;
import com.example.appmohinh.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class DetailProductActivity extends AppCompatActivity {
    private ImageView imgProduct;
    private TextView tvNameProduct;
    private TextView tvPriceSale;
    private TextView tvPrice;
    private TextView tvDescribe;
    private Button btnAddToCart;
    private Button btnBuyNow;
    private Product objProduct;
    private ImageView imgProduct2;
    private TextView tvNameProduct2;
    private TextView tvPrice2;
    private Button btnTru;
    private EditText edQuantity;
    private Button btnTang;
    private Button btnHuy;
    private Button btnOK;
    private int num = 0;
    private List<ItemCart> list;
    private List<ItemCart> list2;
    private String idUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_product);
        findID();
        list = new ArrayList<>();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        idUser = user.getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mRef = database.getReference("list_cart");
        mRef.child(idUser).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ItemCart objCart = dataSnapshot.getValue(ItemCart.class);
                    if (objCart != null) {
                        list.add(objCart);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        getDataTypeFoodFromBundle();
        if(objProduct.getPriceSale()==0){
            tvPrice.setVisibility(View.GONE);
        }
        if(objProduct.getStatus()==2){
            btnAddToCart.setText("Hết hàng");
            btnAddToCart.setEnabled(false);
        }

        tvPrice.setPaintFlags(tvPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        Glide.with(this).load(objProduct.getImg()).into(imgProduct);
        tvNameProduct.setText(objProduct.getName());
        tvPrice.setText("" + objProduct.getPrice() + " VND");
        tvPriceSale.setText(formatNumberCurrency(String.valueOf((objProduct.getPrice() - ((objProduct.getPrice()) * objProduct.getPriceSale()) / 100))) + " VND");
        tvDescribe.setText(objProduct.getDescribe());
        btnAddToCart.setOnClickListener(view -> {
            onClickAddtoCart();
        });
    }

    private void onClickAddtoCart() {
        View viewDiaLog = getLayoutInflater().inflate(R.layout.dialog_addtocart, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(viewDiaLog);
        imgProduct2 = (ImageView) bottomSheetDialog.findViewById(R.id.img_product);
        tvNameProduct2 = (TextView) bottomSheetDialog.findViewById(R.id.tv_nameProduct);
        tvPrice2 = (TextView) bottomSheetDialog.findViewById(R.id.tv_price);
        btnTru = (Button) bottomSheetDialog.findViewById(R.id.btn_tru);
        edQuantity = (EditText) bottomSheetDialog.findViewById(R.id.ed_quantity);
        btnTang = (Button) bottomSheetDialog.findViewById(R.id.btn_tang);
        btnHuy = (Button) bottomSheetDialog.findViewById(R.id.btn_huy);
        btnOK = (Button) bottomSheetDialog.findViewById(R.id.btn_OK);
        num = 1;
        Glide.with(this).load(objProduct.getImg()).into(imgProduct2);
        tvNameProduct2.setText(objProduct.getName());
        tvPrice2.setText( objProduct.getPrice()-((objProduct.getPrice()* objProduct.getPriceSale())/100) + " VNĐ");
        btnTru.setEnabled(false);
        bottomSheetDialog.show();
        bottomSheetDialog.setCancelable(false);
        btnHuy.setOnClickListener(view -> {
            bottomSheetDialog.dismiss();
        });

        btnTang.setOnClickListener(view -> {
            num += 1;
            edQuantity.setText("" + num);
            if (num > 1) {
                btnTru.setEnabled(true);
            }

        });
        btnTru.setOnClickListener(view -> {
            num -= 1;
            edQuantity.setText("" + num);
            if (num <= 1) {
                btnTru.setEnabled(false);
            }
        });
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToCart();
                bottomSheetDialog.dismiss();
            }
        });


    }

    private void addToCart() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mRef = database.getReference("list_cart");
        int id = 0;
        if (list.isEmpty() || list.size() == 0) {
            id = 1;
        } else {
            ItemCart objCartNew = list.get((list.size()) - 1);
            id = objCartNew.getId() + 1;
        }
        String name = objProduct.getName();
        int price = objProduct.getPrice()-((objProduct.getPrice()* objProduct.getPriceSale())/100);
        String img = objProduct.getImg();
        int quantity = Integer.parseInt(edQuantity.getText().toString());
        ItemCart objCartNew = new ItemCart(id, name, price, img, quantity, objProduct.getId());
        mRef.child(idUser).child(String.valueOf(objCartNew.getId())).setValue(objCartNew, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Toast.makeText(DetailProductActivity.this, "Thêm vào giỏ hàng thành công", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void findID() {
        imgProduct = (ImageView) findViewById(R.id.img_product);
        tvNameProduct = (TextView) findViewById(R.id.tv_nameProduct);
        tvPriceSale = (TextView) findViewById(R.id.tv_priceSale);
        tvPrice = (TextView) findViewById(R.id.tv_price);
        tvDescribe = (TextView) findViewById(R.id.tv_describe);
        btnAddToCart = (Button) findViewById(R.id.btn_addToCart);
    }

    public void getDataTypeFoodFromBundle() {
        Bundle bundle = getIntent().getExtras();
        objProduct = (Product) bundle.getSerializable("objTypeFood");
    }

    private static String formatNumberCurrency(String number){
        DecimalFormat format= new DecimalFormat("###,###,##0.00");
        return format.format(Double.parseDouble(number));
    }
}