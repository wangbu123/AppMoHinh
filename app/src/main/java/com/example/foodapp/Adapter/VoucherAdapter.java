package com.example.foodapp.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapp.Model.Voucher;
import com.example.foodapp.Model.VoucherUser;
import com.example.foodapp.R;
import com.example.foodapp.Support.MyDiffUtilCallbackVoucher;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

public class VoucherAdapter extends RecyclerView.Adapter<VoucherAdapter.VoucherAdapterHolder> {
    private Context mContext;
    private List<Voucher> list;
    private IclickListener mIclickListener;


    private List<VoucherUser> listVoucherUser = new ArrayList<>();


    public interface IclickListener {
        void receive(Voucher voucher);
    }

    private SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
    Date currentTime = Calendar.getInstance().getTime();
    String dateNow = format.format(currentTime.getTime());
    private String name;


    public VoucherAdapter(Context mContext, List<Voucher> list, IclickListener mIclickListener) {
        this.mContext = mContext;
        this.list = list;
        this.mIclickListener = mIclickListener;
    }


    @NonNull
    @Override
    public VoucherAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_voucher, parent, false);
        return new VoucherAdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VoucherAdapterHolder holder, int position) {
        Voucher objVoucher = list.get(position);
        if (objVoucher == null) {
            return;
        }
        holder.tvNameVoucher.setText("Mã giảm: " + objVoucher.getDiscountVoucher() + " %");
        holder.tvTimeVoucher.setText("Thời hạn: " + objVoucher.getTimeVoucher());
        holder.tvQuantityVoucher.setText("Còn: " + objVoucher.getQuantity());
        if (objVoucher.getTimeVoucher().compareTo(dateNow) < 0) {
            holder.btn_receive.setVisibility(View.GONE);
            holder.itemView.setBackgroundColor(Color.GRAY);
        }
        if (objVoucher.getQuantity() == 0) {
            holder.btn_receive.setVisibility(View.GONE);
            holder.itemView.setBackgroundColor(Color.GRAY);
        }
        SharedPreferences preferences = mContext.getSharedPreferences("user_login", Context.MODE_PRIVATE);
        name = preferences.getString("email", "");
        if (name.equals("admin@gmail.com")) {
            holder.btn_receive.setVisibility(View.GONE);

        }
        holder.btn_receive.setOnClickListener(view -> {
            mIclickListener.receive(objVoucher);
        });

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mReference1 = database.getReference("list_voucher");
        DatabaseReference mReference2 = database.getReference("list_VoucherUser");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String idUser = user.getUid();
        mReference2.child(idUser).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    VoucherUser voucherUser = dataSnapshot.getValue(VoucherUser.class);
                    if (voucherUser != null) {
                        listVoucherUser.add(voucherUser);
                        if (voucherUser.getIdVoucher() == objVoucher.getId()) {
                            holder.btn_receive.setText("Đã lấy");
                            holder.btn_receive.setFocusable(false);
                            holder.btn_receive.setFocusableInTouchMode(false);
                            holder.btn_receive.setEnabled(false);
                            notifyDataSetChanged();
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

//    public void updateListByDiffUtil(List<Voucher> listVoucher) {
//        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new MyDiffUtilCallbackVoucher(list,listVoucher));
//        this.list.clear();
//        listVoucher.addAll(list);
//        diffResult.dispatchUpdatesTo(this);
//    }

    public static class VoucherAdapterHolder extends RecyclerView.ViewHolder {
        private TextView tvNameVoucher;
        private TextView tvTimeVoucher;
        private TextView tvQuantityVoucher;
        private Button btn_receive;


        public VoucherAdapterHolder(@NonNull View itemView) {
            super(itemView);
            tvNameVoucher = (TextView) itemView.findViewById(R.id.tv_nameVoucher);
            tvTimeVoucher = (TextView) itemView.findViewById(R.id.tv_timeVoucher);
            tvQuantityVoucher = (TextView) itemView.findViewById(R.id.tv_quantityVoucher);
            btn_receive = itemView.findViewById(R.id.btn_receiveVoucher);
        }
    }
}
