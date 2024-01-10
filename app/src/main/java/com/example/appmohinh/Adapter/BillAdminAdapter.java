package com.example.appmohinh.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appmohinh.Model.Bill;
import com.example.appmohinh.Model.User;
import com.example.appmohinh.R;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class BillAdminAdapter extends RecyclerView.Adapter<BillAdminAdapter.BillAdapterViewHolder> {
    private Context mContext;
    private List<Bill> list;
    private List<User> listUser;
    private TextView tv_xacNhan, tv_dangGiao, tv_hoanThanh, tv_huyDon, tv_huy;


    public BillAdminAdapter(Context mContext, List<Bill> list) {
        this.mContext = mContext;
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BillAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itembill, parent, false);
        return new BillAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BillAdapterViewHolder holder, int position) {
        Bill objBill = list.get(position);
        int index = position;
        if (objBill == null) {
            return;
        }
        holder.tvCodeOder.setText("" + objBill.getId());
        holder.tvAddress.setText(objBill.getAddress());
        holder.tvName.setText(objBill.getName());
        holder.tvPhoneNumber.setText(objBill.getPhoneNumber());
        holder.tvMenu.setText(objBill.getMenu());
        if (objBill.getStatus() == 1) {
            holder.tvStatus.setText("Chờ xác nhận");
            holder.tvStatus.setTextColor(Color.GRAY);
        }
        if (objBill.getStatus() == 2) {
            holder.tvStatus.setText("Đã xác nhận ");
            holder.tvStatus.setTextColor(Color.BLUE);
        }
        if (objBill.getStatus() == 3) {
            holder.tvStatus.setText("Đã giao");
            holder.tvStatus.setTextColor(Color.CYAN);
        }
        if (objBill.getStatus() == 5) {
            holder.tvStatus.setText("Đã hủy");
            holder.tvStatus.setTextColor(Color.RED);
        }
        if (objBill.getStatus() == 4) {
            holder.tvStatus.setText("Đã thanh toán");
            holder.tvStatus.setTextColor(Color.GREEN);
            holder.img_option.setVisibility(View.GONE);
        }
        if (objBill.getStatus() == 5) {
            holder.img_option.setVisibility(View.GONE);
        }
        holder.tvDate.setText(objBill.getDate());
        holder.tvSumPrice.setText(objBill.getSumPrice());
        holder.img_option.setOnClickListener(view -> {
            onClickSelectedOption(objBill, index);
        });


    }

    private void onClickSelectedOption(Bill objBill, int index) {
        Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.dialog_option_bill);
        dialog.getWindow().setBackgroundDrawable(mContext.getDrawable(R.drawable.bg_dialogoption));
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        window.setAttributes(windowAttributes);
        windowAttributes.gravity = Gravity.BOTTOM;
        dialog.show();
        tv_xacNhan = dialog.findViewById(R.id.tv_xacnhan);
        tv_huyDon = dialog.findViewById(R.id.tv_huydon);
        tv_dangGiao = dialog.findViewById(R.id.tv_danggiao);
        tv_hoanThanh = dialog.findViewById(R.id.tv_hoanthanh);
        tv_huy = dialog.findViewById(R.id.tv_cancel);
            SharedPreferences preferences = mContext.getSharedPreferences("user_login", mContext.MODE_PRIVATE);
            String email = preferences.getString("email", "");
        if (email.equals("admin@gmail.com")) {
            if (objBill.getStatus() == 1) {
                tv_dangGiao.setVisibility(View.GONE);
                tv_hoanThanh.setVisibility(View.GONE);
                tv_huyDon.setVisibility(View.GONE);
            }
            if (objBill.getStatus() == 2) {
                tv_xacNhan.setVisibility(View.GONE);
                tv_hoanThanh.setVisibility(View.GONE);
                tv_huyDon.setVisibility(View.GONE);
            }
            if (objBill.getStatus() == 3) {
                tv_xacNhan.setVisibility(View.GONE);
                tv_dangGiao.setVisibility(View.GONE);
                tv_huyDon.setVisibility(View.GONE);
            }
        } else {
            tv_xacNhan.setVisibility(View.GONE);
            tv_dangGiao.setVisibility(View.GONE);
            tv_hoanThanh.setVisibility(View.GONE);
        }

        listUser = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();


        tv_xacNhan.setOnClickListener(view -> {
            DatabaseReference mRef = database.getReference("list_bill");
            objBill.setStatus(2);
            mRef.child(String.valueOf(objBill.getId())).setValue(objBill.updateStatus(), new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                    Toast.makeText(mContext, "Xác nhận đơn thành công", Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();
                    dialog.dismiss();
                }
            });
        });
        tv_dangGiao.setOnClickListener(view -> {
            DatabaseReference mRef = database.getReference("list_bill");

            objBill.setStatus(3);
            mRef.child(String.valueOf(objBill.getId())).setValue(objBill.updateStatus(), new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                    Toast.makeText(mContext, "Xác nhận đơn thành công", Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();
                    dialog.dismiss();
                }
            });
        });
        tv_hoanThanh.setOnClickListener(view -> {
            DatabaseReference mRef = database.getReference("list_bill");

            objBill.setStatus(4);
            mRef.child(String.valueOf(objBill.getId())).setValue(objBill.updateStatus(), new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                    Toast.makeText(mContext, "Xác nhận đơn thành công", Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();
                    dialog.dismiss();
                }
            });
        });
        tv_huyDon.setOnClickListener(view -> {
            DatabaseReference mRef = database.getReference("list_bill");

            objBill.setStatus(5);
            mRef.child(String.valueOf(objBill.getId())).setValue(objBill.updateStatus(), new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                    Toast.makeText(mContext, "Xác nhận đơn thành công", Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();
                    dialog.dismiss();
                }
            });
        });

    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    public static class BillAdapterViewHolder extends RecyclerView.ViewHolder {
        private TextView tvCodeOder;
        private TextView tvName;
        private TextView tvPhoneNumber;
        private TextView tvAddress;
        private TextView tvMenu;
        private TextView tvDate;
        private TextView tvSumPrice;
        private TextView tvStatus;
        private ImageView img_option;


        public BillAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCodeOder = (TextView) itemView.findViewById(R.id.tv_codeOder);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvPhoneNumber = (TextView) itemView.findViewById(R.id.tv_phoneNumber);
            tvAddress = (TextView) itemView.findViewById(R.id.tv_address);
            tvMenu = (TextView) itemView.findViewById(R.id.tv_menu);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date);
            tvSumPrice = (TextView) itemView.findViewById(R.id.tv_sumPrice);
            tvStatus = (TextView) itemView.findViewById(R.id.tv_status);
            img_option = itemView.findViewById(R.id.img_option);
        }
    }
}
