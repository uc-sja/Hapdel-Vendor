package com.utility.hapdelvendor.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.squareup.picasso.Picasso;
import com.utility.hapdelvendor.Model.ProducModel.Product;
import com.utility.hapdelvendor.R;
import com.utility.hapdelvendor.Utils.LocalStorage;

import java.util.ArrayList;
import java.util.List;

import static android.text.TextUtils.isEmpty;
import static com.utility.hapdelvendor.Utils.Common.showEmptyDialog;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductViewHolder> {
    private Context context;
    private static List<Product> productsList;

    private static final String TAG = "ProductsAdapter";
    private int mRowIndex = -1;

    public ProductsAdapter(Context context, List<Product> productsList) {
        this.context = context;
        this.productsList = productsList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_cell, null, false);
        return new ProductViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProductViewHolder productViewHolder, final int i) {
        final Product datum = productsList.get(i);
        String[] images;
        productViewHolder.product_name.setText(datum.getProductName());
        productViewHolder.product_desc.setText(datum.getShortDescription() == null || isEmpty(datum.getShortDescription()) ? "No description available" : datum.getShortDescription());
        productViewHolder.store_name.setText(datum.getStoreName());

        //initializing
        productViewHolder.cart_btn.setVisibility(View.VISIBLE);
        productViewHolder.change_number_btn.setVisibility(View.GONE);


        //this condition check if there are products in cart .
        //if yes it further check if they are of type service or product
        //and diplays the cart/remove/add buttons accordingly
        Log.d(TAG, "onBindViewHolder: type " + datum.getType());
        if (LocalStorage.getCartCount(datum) > 0) {
            if (datum.getType().trim().equalsIgnoreCase("product")) {
                productViewHolder.cart_btn.setVisibility(View.GONE);
                productViewHolder.change_number_btn.setVisibility(View.VISIBLE);
                productViewHolder.change_number_btn.setNumber("" + LocalStorage.getCartCount(datum));

            } else if (datum.getType().trim().equalsIgnoreCase("service")) {
                productViewHolder.cart_btn.setVisibility(View.GONE);
                productViewHolder.remove_btn.setVisibility(View.VISIBLE);
            }

            Log.d(TAG, "onBindViewHolder: condition true ");
        } else {
            productViewHolder.cart_btn.setVisibility(View.VISIBLE);
            productViewHolder.change_number_btn.setVisibility(View.GONE);
            productViewHolder.remove_btn.setVisibility(View.GONE);
        }

        if (datum.getType().trim().equalsIgnoreCase("product")) {
            productViewHolder.product_price.setText(context.getResources().getString(R.string.rupee_icon) + " " + datum.getPrice() + " / " + datum.getPer() + " " + datum.getUnit());

        } else if (datum.getType().trim().equalsIgnoreCase("service")) {
            productViewHolder.product_price.setText(context.getResources().getString(R.string.rupee_icon) + " " + datum.getPrice());

        }
        Log.d(TAG, "onBindViewHolder: type  " + datum.getType());
        if (datum.getImage() != null && !isEmpty(datum.getImage())) {
            Log.d(TAG, "onBindViewHolder: " + datum.getImage());
            if (datum.getImage().contains("|")) {
                images = datum.getImage().split("\\|");
                Log.d(TAG, "onBindViewHolder: length " + images[1]);
                Picasso.get().load(datum.getBaseUrl() + "" + images[0]).placeholder(R.drawable.app_icon_small_png).into(productViewHolder.product_img);
            } else {
                Picasso.get().load(datum.getBaseUrl() + "" + datum.getImage()).placeholder(R.drawable.app_icon_small_png).into(productViewHolder.product_img);
            }
        } else {
            productViewHolder.product_img.setImageResource(R.drawable.app_icon_small_png);
        }


//        if (datum.getStatus().equals("y")) {
//            productViewHolder.status.setText("In Stock");
//        } else {
//            productViewHolder.status.setText("Out of Stock");
//        }

        productViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isEmpty(datum.getShortDescription().trim())) {
                    productViewHolder.product_name.setSelected(true);
                }
            }
        });

        productViewHolder.product_desc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEmptyDialog(context, datum.getProductName(), datum.getShortDescription());
            }
        });
        productViewHolder.itemView.setTag(i);
    }


    public void setRowIndex(int index) {
        mRowIndex = index;
    }

    @Override
    public int getItemCount() {
        return (productsList != null ? productsList.size() : 0);
    }

    public void updateItems(List<Product> data) {
        Log.d(TAG, "updateItems: " + data.size());
        if (data != null && data.size() > 0) {
            this.productsList = new ArrayList<>();
            for (Product product : data) {
                Log.d(TAG, "updateItems: " + product.getProductName());
                productsList.add(product);
            }
            notifyDataSetChanged();
        }
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout product_layout, root_layout;
        ImageView product_img;
        private Button cart_btn, remove_btn;
        private ElegantNumberButton change_number_btn;
        TextView product_name, product_price, store_name, product_desc, status, unit, quantity, stock_quantity, stock_unit;

        LinearLayout status_unit_layout;

        public ProductViewHolder(@NonNull View itemView) {

            super(itemView);
            product_layout = itemView.findViewById(R.id.product_layout);
            root_layout = itemView.findViewById(R.id.root_layout);
            change_number_btn = itemView.findViewById(R.id.change_number_btn);
            cart_btn = itemView.findViewById(R.id.cart_btn);
            remove_btn = itemView.findViewById(R.id.remove_btn);
            product_name = itemView.findViewById(R.id.product_name);
            product_price = itemView.findViewById(R.id.product_price);
            store_name = itemView.findViewById(R.id.store_name);
            product_desc = itemView.findViewById(R.id.product_desc);
            status = itemView.findViewById(R.id.status);
            product_img = itemView.findViewById(R.id.product_img);
            quantity = itemView.findViewById(R.id.quantity);
            stock_quantity = itemView.findViewById(R.id.stock_quantity);

            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 0, 0, context.getResources().getDimensionPixelOffset(R.dimen._12sdp));
            root_layout.setLayoutParams(layoutParams);
        }
    }
}