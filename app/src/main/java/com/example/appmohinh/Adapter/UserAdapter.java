package com.example.appmohinh.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appmohinh.Activity.DeetailUserActivity;
import com.example.appmohinh.Model.User;
import com.example.appmohinh.R;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserAdapterViewHolder> implements Filterable {
    private Context mContext;
    private List<User> list;
    private List<User> listUserSearch;

    public UserAdapter(Context mContext, List<User> list) {
        this.mContext = mContext;
        this.list = list;
        this.listUserSearch = list;
    }

    @NonNull
    @Override
    public UserAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapterViewHolder holder, int position) {
        User user = list.get(position);
        if (user == null) {
            return;
        }
        Glide.with(mContext).load(user.getImg()).error(R.drawable.avatar).into(holder.imgAvtUser);
        holder.tvEmailUser.setText(user.getPhoneNumber());
        holder.tvNameUser.setText(user.getName());
        holder.itemView.setOnClickListener(view -> {
            Intent i = new Intent(mContext, DeetailUserActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("inforUser", user);
            i.putExtras(bundle);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String search = charSequence.toString();
                if (search.length() == 0 || search.isEmpty()) {
                    list = listUserSearch;

                } else {
                    List<User> listFt = new ArrayList<>();
                    for (User mUser : listUserSearch) {
                        if (mUser.getPhoneNumber().toLowerCase().contains(search.toLowerCase()) ||
                                mUser.getName().toLowerCase().contains(search.toLowerCase())
                        ) {
                            listFt.add(mUser);
                        }
                    }
                    list = listFt;
                }

                FilterResults result = new FilterResults();
                result.values = list;
                return result;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                list = (List<User>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public static class UserAdapterViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgAvtUser;
        private TextView tvNameUser;
        private TextView tvEmailUser;

        public UserAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAvtUser = (ImageView) itemView.findViewById(R.id.img_avtUser);
            tvNameUser = (TextView) itemView.findViewById(R.id.tv_nameUser);
            tvEmailUser = (TextView) itemView.findViewById(R.id.tv_emailUser);
        }
    }
}
