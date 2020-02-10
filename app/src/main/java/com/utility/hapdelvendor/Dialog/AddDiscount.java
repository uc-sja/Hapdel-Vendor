package com.utility.hapdelvendor.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.utility.hapdelvendor.Model.CategoryModel.ParentCategoryModel.Datum;
import com.utility.hapdelvendor.R;

public class AddDiscount extends Dialog {

    private Context context;
    private Datum selected_category;

    public AddDiscount(@NonNull Context context, Datum selectedDatum) {
        super(context);
        this.context = context;
        selected_category = selectedDatum;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_discount_layout);

    }
}
