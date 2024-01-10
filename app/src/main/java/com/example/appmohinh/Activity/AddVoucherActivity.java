package com.example.appmohinh.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appmohinh.Model.Voucher;
import com.example.appmohinh.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class AddVoucherActivity extends AppCompatActivity {
    private TextView tvNameVoucher;
    private EditText edNameVoucher;
    private TextView tvTimeVoucher;
    private EditText edTimeVoucher;
    private TextView tvQuantityVoucher;
    private EditText edQuantityVoucher;
    private Button btnAddVoucher;
    private List<Voucher> list;

    private SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
    int mYear, mMonth, mDay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_voucher);
        findID();
        list = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mRef = database.getReference("list_voucher");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Voucher voucher = dataSnapshot.getValue(Voucher.class);
                    list.add(voucher);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        edTimeVoucher.setFocusable(false);
        edTimeVoucher.setFocusableInTouchMode(false);
        DatePickerDialog.OnDateSetListener date = (datePicker, year, month, day) -> {
            mYear = year;
            mMonth = month;
            mDay = day;
            GregorianCalendar calendar = new GregorianCalendar(mYear, mMonth, mDay);
            edTimeVoucher.setText(format.format(calendar.getTime()));
        };
        edTimeVoucher.setOnClickListener(view -> {
            Calendar calendar = Calendar.getInstance();
            mYear = calendar.get(Calendar.YEAR);
            mMonth = calendar.get(Calendar.MONTH);
            mDay = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                    date, mYear, mMonth, mDay);
            datePickerDialog.show();
        });
        btnAddVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference mRef = database.getReference("list_voucher");
                String magiam = edNameVoucher.getText().toString();
                int soluong = Integer.parseInt(edQuantityVoucher.getText().toString());
                String date = edTimeVoucher.getText().toString();
                int id = 0;
                if (list.isEmpty()) {
                    id = 1;
                } else {
                    Voucher voucher = list.get((list.size()) - 1);
                    id = voucher.getId() + 1;
                }
                Voucher objVoucher = new Voucher(id, magiam, soluong, date);
                mRef.child(String.valueOf(objVoucher.getId())).setValue(objVoucher, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        Toast.makeText(AddVoucherActivity.this, "Thêm Voucher thành công", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });


            }
        });
    }


    public void findID() {
        tvNameVoucher = (TextView) findViewById(R.id.tv_nameVoucher);
        edNameVoucher = (EditText) findViewById(R.id.ed_nameVoucher);
        tvTimeVoucher = (TextView) findViewById(R.id.tv_timeVoucher);
        edTimeVoucher = (EditText) findViewById(R.id.ed_timeVoucher);
        tvQuantityVoucher = (TextView) findViewById(R.id.tv_quantityVoucher);
        edQuantityVoucher = (EditText) findViewById(R.id.ed_quantityVoucher);
        btnAddVoucher = (Button) findViewById(R.id.btn_addVoucher);
    }
}