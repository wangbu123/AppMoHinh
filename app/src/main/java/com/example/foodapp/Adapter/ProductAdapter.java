package com.example.foodapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodapp.Activity.DetailProductActivity;
import com.example.foodapp.Activity.UpdateProductActivity;
import com.example.foodapp.Model.Product;
import com.example.foodapp.R;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private Context mContext;
    private List<Product> list;

    public ProductAdapter(Context mContext, List<Product> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = list.get(position);
        if (product == null) {
            return;
        }
        holder.tvNameProduct.setText(product.getName());
        holder.tvSale.setText("Giảm: " + product.getPriceSale() + "%");
        holder.tvPriceProduct.setText((product.getPrice() - ((product.getPrice()) * product.getPriceSale()) / 100) + " VND");
        Glide.with(mContext).load(product.getImg()).error(R.drawable.camera).into(holder.imgProduct);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext, DetailProductActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("objTypeFood", product);
                i.putExtras(bundle);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(i);
            }
        });
        SharedPreferences preferences = mContext.getSharedPreferences("user_login", mContext.MODE_PRIVATE);
        String email = preferences.getString("email", "");
        if (!email.equals("admin@gmail.com")) {
            holder.img_edit.setVisibility(View.INVISIBLE);
        }
        if (product.getPriceSale() == 0) {
            holder.tvSale.setVisibility(View.GONE);
        }
        holder.img_edit.setOnClickListener(view -> {
            Intent i = new Intent(mContext, UpdateProductActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("objProduct",product);
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

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgProduct, img_edit;
        private TextView tvNameProduct;
        private TextView tvSale;
        private TextView tvPriceProduct;


        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = (ImageView) itemView.findViewById(R.id.img_Product);
            tvNameProduct = (TextView) itemView.findViewById(R.id.tv_nameProduct);
            tvSale = (TextView) itemView.findViewById(R.id.tv_sale);
            tvPriceProduct = (TextView) itemView.findViewById(R.id.tv_priceProduct);
            img_edit = itemView.findViewById(R.id.img_edit);
        }
    }
}
