package com.utility.hapdelvendor.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.squareup.picasso.Picasso;
import com.utility.hapdelvendor.Activity.AllProducts;
import com.utility.hapdelvendor.Activity.OpenProductActivity;
import com.utility.hapdelvendor.Dialog.AddProduct;
import com.utility.hapdelvendor.Model.ProducModel.Product;
import com.utility.hapdelvendor.Model.ResponseModel.ResponseModel;
import com.utility.hapdelvendor.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.text.TextUtils.isEmpty;
import static com.utility.hapdelvendor.Utils.Common.getApiInstance;
import static com.utility.hapdelvendor.Utils.Common.getCurrentUser;
import static com.utility.hapdelvendor.Utils.Common.showEmptyDialog;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductViewHolder> {
    private Context context;
    private static List<com.utility.hapdelvendor.Model.ProducModel.Product> productsList;

    private static final String TAG = "ProductsAdapter";
    private int mRowIndex = -1;

    public ProductsAdapter(Context context, ArrayList<com.utility.hapdelvendor.Model.ProducModel.Product> productsList) {
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
        final com.utility.hapdelvendor.Model.ProducModel.Product datum = productsList.get(i);
        productViewHolder.product_name.setText(datum.getProductName());
        productViewHolder.product_desc.setText(datum.getShortDescription() == null || isEmpty(datum.getShortDescription()) ? "No description available" : datum.getShortDescription());
        if(datum.getType().equalsIgnoreCase("service")){
            productViewHolder.store_name.setVisibility(View.GONE);
        } else {
            productViewHolder.store_name.setVisibility(View.VISIBLE);
            productViewHolder.store_name.setText("Stock Available: " + datum.getStocks());

        }

        //initializing
        productViewHolder.edit_btn.setVisibility(View.VISIBLE);
        productViewHolder.change_number_btn.setVisibility(View.GONE);


        //this condition check if there are products in cart .
        //if yes it further check if they are of type service or product
        //and diplays the cart/remove/add buttons accordingly
        Log.d(TAG, "onBindViewHolder: type " + datum.getType());
        if (datum.getType().trim().equalsIgnoreCase("product")) {
            productViewHolder.product_price.setText(context.getResources().getString(R.string.rupee_icon) + " " + datum.getPrice() + " / " + datum.getPer() + " " + datum.getUnit());

        } else if (datum.getType().trim().equalsIgnoreCase("service")) {
            productViewHolder.product_price.setText(context.getResources().getString(R.string.rupee_icon) + " " + datum.getPrice());

        }
        Log.d(TAG, "onBindViewHolder: type  " + datum.getType());
        if (datum.getImage() != null && !isEmpty(datum.getImage())) {
            Log.d(TAG, "onBindViewHolder: " + datum.getImage());
            Picasso.get().load(datum.getImage()).placeholder(R.drawable.app_icon_small_png).into(productViewHolder.product_img);
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

        productViewHolder.delete_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showAlertDialog(datum);
            }
        });

        productViewHolder.edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddProduct addProduct = null;

                if((Activity)context instanceof AllProducts) {
                    addProduct = new AddProduct(context, null, "edit");
                } else if((Activity)context instanceof OpenProductActivity){
                    addProduct = new AddProduct(context, ((OpenProductActivity) context).selectedDatum, "edit");
                }
                addProduct.setProduct(datum);
                addProduct.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                addProduct.show();

            }
        });
    }
    private void showAlertDialog(Product datum) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("Delete Products");
        alertDialog.setMessage("Are you sure to remove this product?");
//        alertDialog.setIcon(R.drawable.ic_logout);

        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteProduct(datum);
            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void deleteProduct(Product datum) {
        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.MyDialogTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Removing product...");
        progressDialog.setCancelable(false);
        if (!((Activity) context).isFinishing()) {
            progressDialog.show();
        }

        Call<ResponseModel> responseModelCall = getApiInstance().deleteProduct(getCurrentUser().getId(), getCurrentUser().getAccessToken(), datum.getCategoryId(), datum.getPid());
        responseModelCall.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                progressDialog.dismiss();
                if (!response.isSuccessful()) {
                    Toast.makeText(context, "" + response.message(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onResponse: fail " + response.code());
                    return;
                }

                Log.d(TAG, "onResponse: success" + response.code() + response.body());
                if (response.body() != null) {
                    ResponseModel responseModel = null;
                    try {
                        responseModel = response.body();
                    } catch (Exception e) {
                        Toast.makeText(context, "Error in response", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String content = "";
                    Log.d(TAG, "onResponse: response msg" + response.body().getResult() + "  msg  ");
                    if (responseModel.getResult().equals("success")) { //very important conditon
                        Toast.makeText(context, responseModel.getMsg(), Toast.LENGTH_SHORT).show();
                        ((OpenProductActivity) context).fetchProducts(((OpenProductActivity) context).selectedDatum, "1");
                    } else {
                        content += responseModel.getMsg();
                        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
                    }

                    Log.d(TAG, "onResponse: delete address res" + content);
                } else {
                    Toast.makeText(context, "Invalid response from server", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(context, "" + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }


    public void setRowIndex(int index) {
        mRowIndex = index;
    }

    @Override
    public int getItemCount() {
        return (productsList != null ? productsList.size() : 0);
    }

    public void updateItems(List<com.utility.hapdelvendor.Model.ProducModel.Product> data) {
        Log.d(TAG, "updateItems: " + data.size());
        if (data != null && data.size() > 0) {
            this.productsList = new ArrayList<Product>();
            for (com.utility.hapdelvendor.Model.ProducModel.Product product : data) {
                Log.d(TAG, "updateItems: " + product.getProductName());
                productsList.add(product);
            }
            notifyDataSetChanged();
        }
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        private final ImageView delete_product;
        RelativeLayout product_layout, root_layout;
        ImageView product_img;
        private Button edit_btn, remove_btn;
        private ElegantNumberButton change_number_btn;
        TextView product_name, product_price, store_name, product_desc, status, unit, quantity, stock_quantity, stock_unit;

        LinearLayout status_unit_layout;

        public ProductViewHolder(@NonNull View itemView) {

            super(itemView);
            product_layout = itemView.findViewById(R.id.product_layout);
            root_layout = itemView.findViewById(R.id.root_layout);
            change_number_btn = itemView.findViewById(R.id.change_number_btn);
            edit_btn = itemView.findViewById(R.id.edit_btn);
            remove_btn = itemView.findViewById(R.id.remove_btn);
            product_name = itemView.findViewById(R.id.product_name);
            product_price = itemView.findViewById(R.id.product_price);
            store_name = itemView.findViewById(R.id.store_name);
            product_desc = itemView.findViewById(R.id.product_desc);
            status = itemView.findViewById(R.id.status);
            product_img = itemView.findViewById(R.id.product_img);
            quantity = itemView.findViewById(R.id.quantity);
            stock_quantity = itemView.findViewById(R.id.stock_quantity);
            delete_product = itemView.findViewById(R.id.delete_product);

            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 0, 0, context.getResources().getDimensionPixelOffset(R.dimen._12sdp));
            root_layout.setLayoutParams(layoutParams);
        }
    }
}