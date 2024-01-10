package com.example.appmohinh.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appmohinh.Model.VoucherUser;
import com.example.appmohinh.R;

import java.util.List;

public class VoucherUserAdapter extends RecyclerView.Adapter<VoucherUserAdapter.VoucherUserAdapterHolder> {
    private List<VoucherUser> list;
    private Context mContext;
    private IclickItem mIclickItem;

    public interface IclickItem {
        void useVoucher(VoucherUser voucherUser);
    }

    public VoucherUserAdapter(List<VoucherUser> list, Context mContext, IclickItem mIclickItem) {
        this.list = list;
        this.mContext = mContext;
        this.mIclickItem = mIclickItem;
    }

    @NonNull
    @Override
    public VoucherUserAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_voucheruser, parent, false);
        return new VoucherUserAdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VoucherUserAdapterHolder holder, int position) {
        VoucherUser objVoucherUser = list.get(position);
        if (objVoucherUser == null) {
            return;
        }
        holder.tvNameVoucher.setText("Giảm:" + objVoucherUser.getDiscountVoucher() + "%");
        holder.tvTimeVoucher.setText("Thời hạn: " + objVoucherUser.getTimeVoucher());
        holder.btn_useVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIclickItem.useVoucher(objVoucherUser);
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

    public static class VoucherUserAdapterHolder extends RecyclerView.ViewHolder {
        private ImageView imgVoucher;
        private LinearLayout linnerMain;
        private TextView tvNameVoucher;
        private TextView tvTimeVoucher;
        private Button btn_useVoucher;


        public VoucherUserAdapterHolder(@NonNull View itemView) {
            super(itemView);
            imgVoucher = (ImageView) itemView.findViewById(R.id.img_voucher);
            linnerMain = (LinearLayout) itemView.findViewById(R.id.linner_main);
            tvNameVoucher = (TextView) itemView.findViewById(R.id.tv_nameVoucher);
            tvTimeVoucher = (TextView) itemView.findViewById(R.id.tv_timeVoucher);
            btn_useVoucher = itemView.findViewById(R.id.btn_useVoucher);
        }
    }
}
