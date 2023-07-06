package com.example.foodapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodapp.Model.ItemCart;
import com.example.foodapp.R;
import com.google.android.play.integrity.internal.m;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartAdapterViewHolder> {
    private List<ItemCart> list;
    private IclickItem mIclickItem;
    private Context mContext;
    public interface IclickItem {
        void deleteItemCart(ItemCart itemCart);
        void augmentQuantity(ItemCart itemCart);
        void reduceQuantity(ItemCart itemCart);
    }

    public CartAdapter(List<ItemCart> list, IclickItem mIclickItem, Context mContext) {
        this.list = list;
        this.mIclickItem = mIclickItem;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public CartAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new CartAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapterViewHolder holder, int position) {
        ItemCart objCart = list.get(position);
        if (objCart == null) {
            return;
        }
        Glide.with(mContext).load(objCart.getImg()).into(holder.imgProduct);
        holder.tvNameProduct.setText(objCart.getName());
        holder.tvPrice.setText(objCart.getPrice() + " VNĐ");
        holder.tvQuantity.setText("" + objCart.getQuantity());
        holder.btnXoa.setOnClickListener(view -> {
            mIclickItem.deleteItemCart(objCart);
        });
        holder.btnTang.setOnClickListener(view -> {
            mIclickItem.augmentQuantity(objCart);
        });
        holder.btnTru.setOnClickListener(view -> {
            mIclickItem.reduceQuantity(objCart);
        });
    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    public static class CartAdapterViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgProduct;
        private TextView tvNameProduct;
        private TextView tvPrice;
        private Button btnTru;
        private TextView tvQuantity;
        private Button btnTang;
        private Button btnXoa;




        public CartAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = (ImageView) itemView.findViewById(R.id.img_product);
            tvNameProduct = (TextView) itemView.findViewById(R.id.tv_nameProduct);
            tvPrice = (TextView) itemView.findViewById(R.id.tv_price);
            btnTru = (Button) itemView.findViewById(R.id.btn_tru);
            tvQuantity = (TextView) itemView.findViewById(R.id.tv_quantity);
            btnTang = (Button) itemView.findViewById(R.id.btn_tang);
            btnXoa = (Button) itemView.findViewById(R.id.btn_xoa);
        }
    }
}
