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

import com.squareup.picasso.Picasso;
import com.utility.hapdelvendor.Model.CategoryModel.ParentCategoryModel.Datum;
import com.utility.hapdelvendor.Model.ProducModel.Product;
import com.utility.hapdelvendor.Model.ResponseModel.ResponseModel;
import com.utility.hapdelvendor.Model.SearchModel.SearchResultModel;
import com.utility.hapdelvendor.OtpVerificationActivity;
import com.utility.hapdelvendor.R;
import com.utility.hapdelvendor.Utils.AutoSuggestAdapter;

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
    private AutoSuggestAdapter autoSuggestAdapter;
    private static final String TAG = "AddProduct";
    private CardView product_card;
    private TextView product_name, product_desc, product_price;
    private ImageView product_img;
    private EditText set_stock_count, set_product_price;
    private Product product;
    private Datum selected_category;
    private Button submit_result;

    public AddProduct(@NonNull Context context, Datum selectedDatum) {
        super(context);
        this.context = context;
        selected_category = selectedDatum;
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
        set_stock_count = findViewById(R.id.set_stock_count);
        set_product_price = findViewById(R.id.set_product_price);
        submit_result = findViewById(R.id.submit_result);

        autoSuggestAdapter = new AutoSuggestAdapter(context, android.R.layout.simple_spinner_dropdown_item, new ArrayList());
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
                if(product!=null){
                    product.setPrice(s.toString());
                    setProductPrice(product);
                }
            }
        });


        search_bar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                product = autoSuggestAdapter.getItem(position);
                if(product!=null){

                    product_card.setVisibility(View.VISIBLE);
                    String[] images;
                    product_name.setText(product.getProductName());
                    product_desc.setText(product.getShortDescription() == null || isEmpty(product.getShortDescription()) ? "No description available" : product.getShortDescription());
                    setProductPrice(product);

                    //Initiating
                    set_product_price.setText(product.getPrice());
                    set_stock_count.setText("1");

                    if (product.getImage() != null && !isEmpty(product.getImage())) {
                        Log.d(TAG, "onBindViewHolder: " + product.getImage());
                        if (product.getImage().contains("|")) {
                            images = product.getImage().split("\\|");
                            Log.d(TAG, "onBindViewHolder: length " + images[1]);
                            Picasso.get().load(product.getBaseUrl() + "" + images[0]).placeholder(R.drawable.app_icon_small_png).into(product_img);
                        } else {
                            Picasso.get().load(product.getBaseUrl() + "" + product.getImage()).placeholder(R.drawable.app_icon_small_png).into(product_img);
                        }
                    } else {
                        product_img.setImageResource(R.drawable.app_icon_small_png);
                    }
                } else {
                    product_card.setVisibility(View.GONE);
                    Toast.makeText(context, "Product not loaded", Toast.LENGTH_SHORT).show();
                }
            }
        });


        submit_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateInput()){
                    addProduct();
                }
            }
        });
    }

    private void addProduct() {

        final ProgressDialog progressDialog = new ProgressDialog(OtpVerificationActivity.this, R.style.MyDialogTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Adding product...");
        progressDialog.setCancelable(false);
        if(!((Activity)OtpVerificationActivity.this).isFinishing()){
            progressDialog.show();
        }


        Call<ResponseModel> responseModelCall = getApiInstance().addProduct(getCurrentUser().getId(), getCurrentUser().getAccessToken(), datum.getId());
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

        boolean isValid = false;

        try {
            int stock = Integer.valueOf(stock_count.trim());
        } catch (NumberFormatException e) {
            Log.d(TAG, "validateInput: "+e.toString());
            set_stock_count.setError("Kindly enter valid stock count");
            set_stock_count.requestFocus();
            return false;
        }

        try {
            int price = Integer.valueOf(product_price.trim());
        } catch (NumberFormatException e) {
            Log.d(TAG, "validateInput: "+e.toString());
            set_product_price.setError("Kindly enter valid product price");
            set_product_price.requestFocus();
            return false;
        }

        return true;
    }

    private void setProductPrice(Product product) {
        if (product.getType().trim().equalsIgnoreCase("product")) {
            product_price.setText(context.getResources().getString(R.string.rupee_icon) + " " + product.getPrice() + " / " + product.getPer() + " " + product.getUnit());
            } else if (product.getType().trim().equalsIgnoreCase("service")) {
            product_price.setText(context.getResources().getString(R.string.rupee_icon) + " " + product.getPrice());

        }
    }

    private void searchItem(final String keyword, com.utility.hapdelvendor.Model.CategoryModel.ParentCategoryModel.Datum category) {
        Log.d(TAG, "searchItem: catID "+category.getIcon());
        Call<SearchResultModel> searchResultModelCall = getApiInstance().searchItem(getCurrentUser().getId(), getCurrentUser().getAccessToken(), category.getId(), "");
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

}
