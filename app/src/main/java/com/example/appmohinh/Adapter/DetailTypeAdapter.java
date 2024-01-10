package com.example.appmohinh.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appmohinh.Activity.DetailProductActivity;
import com.example.appmohinh.Model.Product;
import com.example.appmohinh.R;

import java.text.DecimalFormat;
import java.util.List;

public class DetailTypeAdapter extends RecyclerView.Adapter<DetailTypeAdapter.DetailTypeFoodViewHolder> {
    private List<Product> list;
    private Context mContext;

    public DetailTypeAdapter(List<Product> list, Context mContext) {
        this.list = list;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public DetailTypeFoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail_typefood, parent, false);
        return new DetailTypeFoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailTypeFoodViewHolder holder, int position) {
        Product product = list.get(position);
        if (product == null) {
            return;
        }
        holder.tvNameProduct.setText(product.getName());
        holder.tvSale.setText("Giảm: " + product.getPriceSale() + "%");
        holder.tvPriceProduct.setText(formatNumberCurrency(String.valueOf((product.getPrice() - ((product.getPrice()) * product.getPriceSale()) / 100))) + " VND");
        Glide.with(mContext).load(product.getImg()).error(R.drawable.camera).into(holder.imgProduct);
        holder.itemView.setOnClickListener(view -> {
            Intent i = new Intent(mContext, DetailProductActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("objType", product);
            i.putExtras(bundle);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    public static class DetailTypeFoodViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgProduct;
        private TextView tvNameProduct;
        private TextView tvSale;
        private TextView tvSoLuong;
        private TextView tvPriceProduct;

        public DetailTypeFoodViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = (ImageView) itemView.findViewById(R.id.img_Product);
            tvNameProduct = (TextView) itemView.findViewById(R.id.tv_nameProduct);
            tvSale = (TextView) itemView.findViewById(R.id.tv_sale);
            tvSoLuong = (TextView) itemView.findViewById(R.id.tv_soLuong);
            tvPriceProduct = (TextView) itemView.findViewById(R.id.tv_priceProduct);
        }
    }
    private static String formatNumberCurrency(String number){
        DecimalFormat format= new DecimalFormat("###,###,##0.00");
        return format.format(Double.parseDouble(number));
    }
}
