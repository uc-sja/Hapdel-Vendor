package com.utility.hapdelvendor.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.utility.hapdelvendor.Interfaces.OnItemCheckListener;
import com.utility.hapdelvendor.Model.PackageCategoryModel.WeightTypeModel.Datum;
import com.utility.hapdelvendor.R;

import java.util.List;

public class WeightTypeAdapter extends RecyclerView.Adapter<WeightTypeAdapter.WeightTypeViewHolder> {
    Context context;
    List<Datum> weightTypeList;
    OnItemCheckListener onItemCheckListener;
    public int mSelectedItem = -1;

    public WeightTypeAdapter(Context context, List<Datum> weightTypeList, OnItemCheckListener onItemCheckListener) {
        this.context = context;
        this.weightTypeList = weightTypeList;
        this.onItemCheckListener = onItemCheckListener;
    }

    @NonNull
    @Override
    public WeightTypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weight_type_cell, null, false);
        return new WeightTypeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final WeightTypeViewHolder holder, final int position) {
        final Datum datum = weightTypeList.get(position);
        final String weightTypeText = datum.getSize().substring(0,1).toUpperCase()+datum.getSize().substring(1)+" (Upto "+datum.getMaxWeightKg()+" kg)";
        holder.contentRadioButton.setText(weightTypeText);

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectedItem = position;
                notifyDataSetChanged();
                onItemCheckListener.onItemCheck(datum.getSize()+" id "+datum.getId());

            }
        };

        holder.itemView.setOnClickListener(clickListener);
        holder.contentRadioButton.setOnClickListener(clickListener);

        holder.contentRadioButton.setChecked(position == mSelectedItem);

    }

    @Override
    public int getItemCount() {
        return weightTypeList.size();
    }

    public void updateItems(List<Datum> data) {
        weightTypeList.clear();
        notifyDataSetChanged();
        weightTypeList.addAll(data);
        notifyDataSetChanged();
    }

    public class WeightTypeViewHolder extends RecyclerView.ViewHolder {
        RadioButton contentRadioButton;
        public WeightTypeViewHolder(@NonNull View itemView) {
            super(itemView);
            contentRadioButton = itemView.findViewById(R.id.content_checkBox);
        }
    }
}
