package com.utility.hapdelvendor.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.utility.hapdelvendor.R;

public class AddDiscount extends Dialog {
    public AddDiscount(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_discount_layout);

    }
}
