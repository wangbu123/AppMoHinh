package com.example.foodapp.Support;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.example.foodapp.Model.Voucher;

import java.util.List;

public class MyDiffUtilCallbackVoucher extends DiffUtil.Callback {
    private List<Voucher> oldList;
    private List<Voucher> newList;

    public MyDiffUtilCallbackVoucher(List<Voucher> oldList, List<Voucher> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).getId()==newList.get(newItemPosition).getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Voucher oldVoucher = oldList.get(oldItemPosition);
        Voucher newVoucher = newList.get(newItemPosition);
        return oldVoucher.equals(newVoucher);
    }
}
