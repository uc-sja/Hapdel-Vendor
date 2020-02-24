package com.utility.hapdelvendor.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.cardview.widget.CardView;

import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;
import com.utility.hapdelvendor.Activity.AllProducts;
import com.utility.hapdelvendor.Activity.OpenProductActivity;
import com.utility.hapdelvendor.Model.CategoryModel.ParentCategoryModel.Datum;
import com.utility.hapdelvendor.Model.ProducModel.Product;
import com.utility.hapdelvendor.Model.ResponseModel.ResponseModel;
import com.utility.hapdelvendor.Model.SearchModel.SearchResultModel;
import com.utility.hapdelvendor.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.text.TextUtils.isEmpty;
import static com.utility.hapdelvendor.Utils.Common.getApiInstance;
import static com.utility.hapdelvendor.Utils.Common.getCurrentUser;

public class AddProduct extends Dialog {
    Context context;
    AppCompatAutoCompleteTextView search_bar;
    private com.utility.hapdelvendor.Utils.AutoSuggestAdapter autoSuggestAdapter;
    private static final String TAG = "AddProduct";
    private CardView product_card;
    private TextView product_name, product_desc, product_price;
    private ImageView product_img;
    private EditText set_stock_count, set_product_price;
    private Datum selected_category;
    private Button submit_result;
    private String updateType;
    private TextView update_title;
    private Product current_product;
    private TextInputLayout stock_edit_layout;

    public AddProduct(@NonNull Context context, Datum selectedDatum, String updateType) {
        super(context);
        this.context = context;
        selected_category = selectedDatum;
        this.updateType = updateType;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_product_layout);
        search_bar = findViewById(R.id.search_bar);

        product_name = findViewById(R.id.product_name);
        product_desc = findViewById(R.id.product_desc);
        product_img = findViewById(R.id.product_img);
        product_card = findViewById(R.id.product_card);
        product_price = findViewById(R.id.product_price);
        set_stock_count = findViewById(R.id.max_discount);
        set_product_price = findViewById(R.id.set_discount);
        submit_result = findViewById(R.id.submit_result);
        update_title = findViewById(R.id.update_title);
        stock_edit_layout = findViewById(R.id.stock_edit_layout);

        autoSuggestAdapter = new com.utility.hapdelvendor.Utils.AutoSuggestAdapter(context, R.layout.simple_spinner_dropdown_item, new ArrayList());
        search_bar.setAdapter(autoSuggestAdapter);

        search_bar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                searchItem(search_bar.getText().toString(), selected_category);

            }
        });

        set_product_price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(current_product!=null){
                    current_product.setPrice(s.toString());
                    setProductPrice(current_product);
                }
            }
        });


        search_bar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                current_product = autoSuggestAdapter.getItem(position);
                initializeProduct(current_product);
            }
        });

        submit_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: ");
                if(validateInput()){
                    addProduct();
                } else {
                }
            }
        });


        if(updateType.equalsIgnoreCase("edit") && current_product!=null){
            search_bar.setVisibility(View.GONE);
            update_title.setText("Edit product");
            initializeProduct(current_product);
        } else if(updateType.equalsIgnoreCase("add")){
            search_bar.setVisibility(View.VISIBLE);
            update_title.setText("Add product");
        }
    }

    private void initializeProduct(Product product) {
        if(product!=null){

            product_card.setVisibility(View.VISIBLE);
            String[] images;
            product_name.setText(product.getProductName());
            product_desc.setText(product.getShortDescription() == null || isEmpty(product.getShortDescription()) ? "No description available" : product.getShortDescription());
            setProductPrice(product);
            current_product = product;

            //Initiating
            set_product_price.setText(product.getPrice());

            if(product.getType().equalsIgnoreCase("service")){
                stock_edit_layout.setVisibility(View.GONE);
            } else {
                stock_edit_layout.setVisibility(View.VISIBLE);
                set_stock_count.setText("1");
            }

            if (product.getImage() != null && !isEmpty(product.getImage())) {
                Picasso.get().load(product.getImage()).placeholder(R.drawable.app_icon_small_png).into(product_img);
            } else {
                product_img.setImageResource(R.drawable.app_icon_small_png);
            }
        } else {
            product_card.setVisibility(View.GONE);
            Toast.makeText(context, "Product not loaded", Toast.LENGTH_SHORT).show();
        }
    }

    private void addProduct() {
        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.MyDialogTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Updating product...");
        progressDialog.setCancelable(false);
        if(!((Activity)context).isFinishing()){
            progressDialog.show();
        }

        Call<ResponseModel> responseModelCall = getApiInstance().addProduct(getCurrentUser().getId(), getCurrentUser().getAccessToken(), current_product.getCategoryId(),  current_product.getPid(), set_stock_count.getText().toString(), set_product_price.getText().toString());
        responseModelCall.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                progressDialog.dismiss();
                if(!response.isSuccessful()){
                    Toast.makeText(context, ""+response.message(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onResponse: fail "+response.code());
                    return;
                }

                Log.d(TAG, "onResponse: success"+response.code()+response.body());
                if(response.body()!=null ){
                    ResponseModel responseModel = null;
                    try {
                        responseModel = response.body();
                    } catch (Exception e) {
                        Toast.makeText(context, "Error in response", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String content="";
                    Log.d(TAG, "onResponse: response msg"+response.body().getResult()+"  msg  ");
                    if (responseModel.getResult().equals("success")){ //very important conditon
                        Toast.makeText(context, responseModel.getMsg(), Toast.LENGTH_SHORT).show();
                        if((Activity)context instanceof OpenProductActivity){
                            ((OpenProductActivity)context).fetchProducts(((OpenProductActivity)context).selectedDatum, ((OpenProductActivity)context).current_keyword, "1");
                        } else if((Activity)context instanceof AllProducts){
                            ((AllProducts)context).fetchProducts(((AllProducts)context).current_keyword, "1");
                        }
                        dismiss();
                    }else{
                        content+= responseModel.getMsg();
                        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
                    }

                    Log.d(TAG, "onResponse: delete address res"+content);
                } else {
                    Toast.makeText(context, "Invalid response from server", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(context, ""+t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private boolean validateInput() {
        String stock_count = set_stock_count.getText().toString();
        String product_price = set_product_price.getText().toString();


        //We dont set stock in case of service
        if(current_product.getType().equalsIgnoreCase("product")){
            try {
                int stock = Integer.valueOf(stock_count.trim());
            } catch (NumberFormatException e) {
                Log.d(TAG, "validateInput: "+e.toString());
                set_stock_count.setError("Kindly enter valid stock count");
                set_stock_count.requestFocus();
                return false;
            }
        }

        try {
            double price = Double.valueOf(product_price.trim());
        } catch (NumberFormatException e) {
            Log.d(TAG, "validateInput: "+e.toString());
            set_product_price.setError("Kindly enter valid product price");
            set_product_price.requestFocus();
            return false;
        }

        if(current_product !=null && isEmpty(current_product.getPid().trim())){
            search_bar.setError("Kindly select a product first");
            search_bar.requestFocus();
            return false;
        }
        return true;
    }

    private void setProductPrice(com.utility.hapdelvendor.Model.ProducModel.Product product) {
        if(product.getPrice() != null && !isEmpty(product.getPrice().trim())){
            Log.d(TAG, "setProductPrice: "+product.getPrice());
            if (product.getType().trim().equalsIgnoreCase("product")) {
                product_price.setText(context.getResources().getString(R.string.rupee_icon) + " " + product.getPrice() + " / " + product.getPer() + " " + product.getUnit());
            } else if (product.getType().trim().equalsIgnoreCase("service")) {
                product_price.setText(context.getResources().getString(R.string.rupee_icon) + " " + product.getPrice());
            }
        } else {
            product_price.setText(null);
            product_price.setHint("Kindly set product price");
        }
    }

    private void searchItem(final String keyword, com.utility.hapdelvendor.Model.CategoryModel.ParentCategoryModel.Datum category) {
        Call<SearchResultModel> searchResultModelCall = getApiInstance().searchItem(getCurrentUser().getId(), getCurrentUser().getAccessToken(), category!=null?category.getId():null, keyword);
//        shimmerRecycler.showShimmerAdapter();
        searchResultModelCall.enqueue(new Callback<SearchResultModel>() {
            @Override
            public void onResponse(Call<SearchResultModel> call, Response<SearchResultModel> response) {
//                shimmerRecycler.hideShimmerAdapter();
                if (!response.isSuccessful()) {
                    Toast.makeText(context, "" + response.message(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onResponse: fail " + response.code());
                    return;
                }

                Log.d(TAG, "onResponse: success" + response.code() + response.body());
                if (response.body() != null) {
                    SearchResultModel searchResultModel = null;
                    try {
                        searchResultModel = response.body();
                    } catch (Exception e) {
                        Toast.makeText(context, "Error in response", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String content = "";
                    Log.d(TAG, "onResponse: response msg" + response.body().getResult() + "  msg  ");
                    if (searchResultModel.getResult().equals("success")) { //very important conditon
                        Log.d(TAG, "onResponse: success");
                        if (searchResultModel.getData() != null && searchResultModel.getData().size() > 0) {
                            autoSuggestAdapter.setData(searchResultModel.getData());
                        } else {
//                            Toast.makeText(context, "No Search Results", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        content += searchResultModel.getMsg();
                        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
                    }
                    Log.d(TAG, "onResponse: search item res" + content);
                } else {
                    Toast.makeText(context, "Invalid response from server", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SearchResultModel> call, Throwable t) {
//                shimmerRecycler.hideShimmerAdapter();
                Toast.makeText(context, "" + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }

    public void setProduct(Product product) {
        this.current_product = product;
    }
}
