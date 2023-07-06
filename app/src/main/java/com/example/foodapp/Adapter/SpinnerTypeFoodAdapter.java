package com.example.foodapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.foodapp.Model.TypeFood;
import com.example.foodapp.R;

import java.util.List;

public class SpinnerTypeFoodAdapter extends ArrayAdapter<TypeFood> {
    private List<TypeFood> list;
    private Context mContext;
    private TextView tv_name;
    private ImageView img_avt;

    public SpinnerTypeFoodAdapter(Context context, List<TypeFood> list) {
        super(context, 0, list);
        this.mContext = context;
        this.list = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View holder = convertView;
        if (holder == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            holder = inflater.inflate(R.layout.item_spinner_typefood, null);
        }
        final TypeFood typeFood = list.get(position);
        if (typeFood != null) {
            tv_name = holder.findViewById(R.id.tv_name);
            img_avt = holder.findViewById(R.id.img_avt);
            tv_name.setText(typeFood.getName());
            Glide.with(mContext).load(typeFood.getImg()).error(R.drawable.camera).into(img_avt);
        }
        return holder;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View holder = convertView;
        if (holder == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            holder = inflater.inflate(R.layout.item_spinner_typefood, null);
        }
        final TypeFood typeFood = list.get(position);
        if (typeFood != null) {
            tv_name = holder.findViewById(R.id.tv_name);
            img_avt = holder.findViewById(R.id.img_avt);
            tv_name.setText(typeFood.getName());
            Glide.with(mContext).load(typeFood.getImg()).error(R.drawable.camera).into(img_avt);
        }
        return holder;
    }
}
