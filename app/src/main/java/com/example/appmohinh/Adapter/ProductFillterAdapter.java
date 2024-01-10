package com.example.appmohinh.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appmohinh.Activity.SearchProductActivity;
import com.example.appmohinh.Model.Product;
import com.example.appmohinh.Model.ProductSearch;
import com.example.appmohinh.R;

import java.util.ArrayList;
import java.util.List;

public class ProductFillterAdapter extends RecyclerView.Adapter<ProductFillterAdapter.ProductFillterAdapterHolder> implements Filterable {
    private Context mContext;
    private List<Product> list;
    private List<Product> listFillter;
    private List<ProductSearch> listSearch;


    public ProductFillterAdapter(Context mContext, List<Product> list) {
        this.mContext = mContext;
        this.list = list;
        this.listFillter = list;
    }

    @NonNull
    @Override
    public ProductFillterAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fillter_product, parent, false);

        return new ProductFillterAdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductFillterAdapterHolder holder, int position) {
        Product product = list.get(position);
        if (product == null) {
            return;
        }
        holder.tvNameFillter.setText(product.getName());
        holder.itemView.setOnClickListener(view -> {
            Intent i = new Intent(mContext, SearchProductActivity.class);
            i.putExtra("text_search", product.getName());
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


    public static class ProductFillterAdapterHolder extends RecyclerView.ViewHolder {
        private TextView tvNameFillter;


        public ProductFillterAdapterHolder(@NonNull View itemView) {
            super(itemView);
            tvNameFillter = (TextView) itemView.findViewById(R.id.tv_name_fillter);
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String search = charSequence.toString();
                List<Product> listFt = new ArrayList<>();
                for (Product mProduct : listFillter) {
                    if (mProduct.getName().toLowerCase().contains(search.toLowerCase()) ||
                            mProduct.getTypeFood().getName().toLowerCase().contains(search.toLowerCase())
                    ) {
                        listFt.add(mProduct);
                    }
                }

                list = listFt;
                FilterResults filterResults = new FilterResults();
                filterResults.values = list;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                list = (List<Product>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
