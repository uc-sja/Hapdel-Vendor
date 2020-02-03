package com.utility.hapdelvendor.Utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.utility.hapdelvendor.Activity.OpenProductActivity;
import com.utility.hapdelvendor.R;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class CustomExpandableListAdapter extends BaseExpandableListAdapter {
    private static final String TAG = "applyFilter CustomExpandableListAda";

    private Context context;
    private List<String> expandableListTitle;
    private HashMap<String, List<String>> expandableListDetail;

    public CustomExpandableListAdapter(Context context, List<String> expandableListTitle,
                                       HashMap<String, List<String>> expandableListDetail) {
        this.context = context;
        this.expandableListTitle = expandableListTitle;
        this.expandableListDetail = expandableListDetail;
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition))
                .get(expandedListPosition);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(final int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final String expandedListText = (String) getChild(listPosition, expandedListPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_item, null);
        }
        TextView expandedListTextView = (TextView) convertView
                .findViewById(R.id.expandedListItem);
        expandedListTextView.setText(expandedListText);
        final CheckBox expandableListCheckbox = (CheckBox)convertView.findViewById(R.id.filter_check);

        final RelativeLayout root_child_layout = convertView.findViewById(R.id.root_child_layout);
        root_child_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Boolean CheckStatus = expandableListCheckbox.isChecked();
                expandableListCheckbox.setChecked(!CheckStatus);
                Log.d(TAG, "onClick: "+expandableListCheckbox.isChecked());
                if(expandableListCheckbox.isChecked()){
                    switch (expandableListTitle.get(listPosition)){
                        case "Brands":
                            addBrandFilter(expandedListText);
                            break;
                        case "Weight":
                            addWeightFilter(expandedListText);
                    }

                } else{
                    switch (expandableListTitle.get(listPosition)){
                        case "Brands":
                            removeBrandFilter(expandedListText);
                            break;
                        case "Weight":
                            removeWeightFilter(expandedListText);
                    }
                }
            }
        });

        return convertView;
    }

    private void removeWeightFilter(String expandedListText) {
        if(((OpenProductActivity)context).weightFilter!=null && ((OpenProductActivity)context).weightFilter.contains(expandedListText)){
            Log.d(TAG, "removeWeightFilter: "+expandedListText);
            ((OpenProductActivity)context).weightFilter.removeAll(Collections.singleton(expandedListText));
        }
    }

    private void addWeightFilter(String expandedListText) {
        if(((OpenProductActivity)context).weightFilter!=null && !((OpenProductActivity)context).weightFilter.contains(expandedListText)){
            Log.d(TAG, "addWeightFilter: "+expandedListText);
            ((OpenProductActivity)context).weightFilter.add(expandedListText);
        }
    }

    private void removeBrandFilter(String expandedListText) {
        if(((OpenProductActivity)context).brandFilter!=null && ((OpenProductActivity)context).brandFilter.contains(expandedListText)){
            Log.d(TAG, "removeBrandFilter: "+expandedListText);
            ((OpenProductActivity)context).brandFilter.removeAll(Collections.singleton(expandedListText));
        }
    }

    private void addBrandFilter(String expandedListText) {
        if(((OpenProductActivity)context).brandFilter!=null && !((OpenProductActivity)context).brandFilter.contains(expandedListText)){
            Log.d(TAG, "addBrandFilter: "+expandedListText);
            ((OpenProductActivity)context).brandFilter.add(expandedListText);
        }

    }

    @Override
    public int getChildrenCount(int listPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition))
                .size();
    }

    @Override
    public Object getGroup(int listPosition) {
        return this.expandableListTitle.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return this.expandableListTitle.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String listTitle = (String) getGroup(listPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_group, null);
        }
        TextView listTitleTextView = (TextView) convertView
                .findViewById(R.id.listTitle);
        listTitleTextView.setTypeface(null, Typeface.BOLD);
        listTitleTextView.setText(listTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }
}
