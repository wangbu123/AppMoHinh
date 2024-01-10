package com.example.appmohinh.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.example.appmohinh.Adapter.DetailTypeAdapter;
import com.example.appmohinh.Model.Product;
import com.example.appmohinh.Model.Type;
import com.example.appmohinh.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class DetailTypeActivity extends AppCompatActivity {
    private RecyclerView recy;
    private TextView tv_nameType;
    private DetailTypeAdapter adapter;
    private Type typeFood;
    private List<Product> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_type_food);
        recy = findViewById(R.id.recy_DetailTypeFood);
        tv_nameType = findViewById(R.id.tv_nameType);
        list = new ArrayList<>();
        getDataTypeFoodFromBundle();
        tv_nameType.setText("Các sản phẩm liên quan đến: "+typeFood.getName());
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mRef = database.getReference("list_product");
        mRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Product product = snapshot.getValue(Product.class);
                if (product != null && product.getTypeFood().getName().equals(typeFood.getName())) {
                    list.add(product);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

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
        adapter = new DetailTypeAdapter(list,this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2,RecyclerView.VERTICAL,false);
        recy.setAdapter(adapter);
        recy.setLayoutManager(gridLayoutManager);


    }

    public void getDataTypeFoodFromBundle() {
        Bundle bundle = getIntent().getExtras();
        typeFood = (Type) bundle.getSerializable("objTypeFood");
    }
}