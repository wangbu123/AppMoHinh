package com.example.appmohinh.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appmohinh.Activity.DetailTypeActivity;
import com.example.appmohinh.Model.Type;
import com.example.appmohinh.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TypeAdapter extends RecyclerView.Adapter<TypeAdapter.ViewHolder> {
    private Context mContext;
    private List<Type> mList;

    public TypeAdapter(Context mContext, List<Type> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loai_san_pham, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Type typeFood = mList.get(position);
        if (typeFood == null) {
            return;
        }
        Glide.with(mContext).load(typeFood.getImg()).error(R.drawable.camera).into(holder.imgType);
        holder.tvNameType.setText(typeFood.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext, DetailTypeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("objTypeFood", typeFood);
                i.putExtras(bundle);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (mList != null) {
            return mList.size();
        }
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView imgType;
        private TextView tvNameType;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgType = (CircleImageView) itemView.findViewById(R.id.img_Type);
            tvNameType = (TextView) itemView.findViewById(R.id.tv_nameType);
        }
    }
}
