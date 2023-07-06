package com.example.foodapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.foodapp.Model.Bill;
import com.example.foodapp.Model.User;
import com.example.foodapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DeetailUserActivity extends AppCompatActivity {
    private CircleImageView imgAvtUser;
    private TextView tvCount;
    private EditText edName;
    private EditText edEmail;
    private EditText edPhone;
    private EditText edAge;
    private User objUser;
    private List<Bill> listBill;
    FirebaseDatabase database = FirebaseDatabase.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deetail_user);
        findID();
        listBill = new ArrayList<>();
        Bundle bundle = getIntent().getExtras();
        objUser = (User) bundle.getSerializable("inforUser");
        Glide.with(this).load(objUser.getImg()).error(R.drawable.avatar).into(imgAvtUser);
        edEmail.setText(objUser.getPhoneNumber());
        edPhone.setText(objUser.getPhoneNumber2());
        edAge.setText( objUser.getBirthday());
        edName.setText(objUser.getName());

        DatabaseReference mRefUser = database.getReference("list_bill");
        mRefUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Bill objBill = dataSnapshot.getValue(Bill.class);
                    if (objBill.getIdUser().equals(objUser.getIdUser()) && objBill.getStatus() == 4) {
                        listBill.add(objBill);

                    }

                }
                Log.d("count", "" + listBill.size());
                tvCount.setText("" + listBill.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void findID() {
        imgAvtUser = (CircleImageView) findViewById(R.id.img_avtUser);
        tvCount = (TextView) findViewById(R.id.tv_count);
        edName = (EditText) findViewById(R.id.ed_name);
        edEmail = (EditText) findViewById(R.id.ed_email);
        edPhone = (EditText) findViewById(R.id.ed_phone);
        edAge = (EditText) findViewById(R.id.ed_age);

    }
}