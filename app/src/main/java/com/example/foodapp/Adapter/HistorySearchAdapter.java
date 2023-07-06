package com.example.foodapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapp.Activity.SearchProductActivity;
import com.example.foodapp.Model.ProductSearch;
import com.example.foodapp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class HistorySearchAdapter extends RecyclerView.Adapter<HistorySearchAdapter.HistorySearchAdapterViewHolder> {
    private List<ProductSearch> list;
    Context mContext;


    public HistorySearchAdapter(List<ProductSearch> list, Context mContext) {
        this.list = list;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public HistorySearchAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fillter_product, parent, false);

        return new HistorySearchAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistorySearchAdapterViewHolder holder, int position) {
        ProductSearch productSearch = list.get(position);

        if (productSearch == null) {
            return;
        }
        holder.tvNameFillter.setText(productSearch.getName());
        holder.itemView.setOnClickListener(view -> {
            Intent i = new Intent(mContext, SearchProductActivity.class);
            i.putExtra("text_search", productSearch.getName());
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

    public static class HistorySearchAdapterViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNameFillter, tv_xoa;

        public HistorySearchAdapterViewHolder(@NonNull View itemView) {

            super(itemView);
            tvNameFillter = (TextView) itemView.findViewById(R.id.tv_name_fillter);
        }
    }
}
