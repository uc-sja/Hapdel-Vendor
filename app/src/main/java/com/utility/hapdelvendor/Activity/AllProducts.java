package com.utility.hapdelvendor.Activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.utility.hapdelvendor.Adapter.ProductsAdapter;
import com.utility.hapdelvendor.Dialog.AddProduct;
import com.utility.hapdelvendor.Model.ProducModel.ProducModel;
import com.utility.hapdelvendor.Model.ProducModel.Product;
import com.utility.hapdelvendor.R;
import com.utility.hapdelvendor.Utils.BottomNavigation;
import com.utility.hapdelvendor.Utils.Common;
import com.utility.hapdelvendor.Utils.MovableFloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.utility.hapdelvendor.Utils.Common.getApiInstance;
import static com.utility.hapdelvendor.Utils.Common.getCurrentUser;

public class AllProducts extends AppCompatActivity {

    RecyclerView products_view;
    private ProductsAdapter productAdapter;
    private LinearLayoutManager productLayoutManager;
    private RelativeLayout containerLayout;
    private AHBottomNavigation bottomNavigation;
    private Toolbar tl;
    private RelativeLayout slider_layout;;
    private ShimmerRecyclerView shimmerRecycler;

    //Error Msg Layout
    private LottieAnimationView error_image;
    private RelativeLayout error_msg_layout;
    private TextView error_msg;
    private RelativeLayout open_product_layout;

    //Sliding PanelLayout
    private SlidingUpPanelLayout sliding_layout;
    private ImageView slider_img;
    private Button slider_one_btn, slider_two_btn;
    private TextView slider_msg;

    private boolean isScrolling,firstLoad;
    public List<Product> total_products_list;
    private static final String TAG = "AllProducts";
    private int scrolledOutItems,currentItems,totalItems;
    private int page = 1;
    MovableFloatingActionButton add_btn;
    private EditText search_bar;
    public String current_keyword="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_products);


        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
            Common.setStatusColor(AllProducts.this, R.color.colorPrimary);
        }

        bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);
        BottomNavigation.initializeBottomNavigation(bottomNavigation, AllProducts.this);
        bottomNavigation.setCurrentItem(-1, false);
        containerLayout = findViewById(R.id.container_layout);

        tl = findViewById(R.id.toolbar);
        tl.setTitle("");
        setSupportActionBar(tl);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setElevation(0);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        products_view = findViewById(R.id.products_view);
        products_view.setVisibility(View.GONE);
        productAdapter = new ProductsAdapter(this, new ArrayList<Product>());
        productLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        products_view.setLayoutManager(productLayoutManager);
        products_view.setAdapter(productAdapter);
        search_bar = findViewById(R.id.search_bar);

        shimmerRecycler = (ShimmerRecyclerView) findViewById(R.id.shimmer_recycler_view);


        slider_layout = findViewById(R.id.slider_layout);
        add_btn = findViewById(R.id.add_btn);

        sliding_layout = findViewById(R.id.sliding_layout);
        slider_img = findViewById(R.id.slider_img);
        slider_msg = findViewById(R.id.slider_msg);

        slider_one_btn = findViewById(R.id.slider_btn_one);
        slider_two_btn = findViewById(R.id.slider_btn_two);

        //Error Msg Initializtion
        open_product_layout = findViewById(R.id.open_product_layout);
        error_msg_layout = findViewById(R.id.error_layout);
        error_msg = findViewById(R.id.error_msg);
        error_image = findViewById(R.id.error_image);

        error_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                error_image.playAnimation();
            }
        });
//
        products_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItems = productLayoutManager.getChildCount();
                totalItems = productLayoutManager.getItemCount();
                scrolledOutItems = productLayoutManager.findFirstVisibleItemPosition();
                Log.d(TAG, "onScrolled: " + isScrolling + " " + totalItems + " " + scrolledOutItems + " " + currentItems);
                if (!isScrolling && totalItems == scrolledOutItems + currentItems) {
                    //Since we cannot assign a variable to final int i
                    Log.d(TAG, "onScrolled: if " + isScrolling + " " + totalItems + " " + scrolledOutItems + " " + currentItems);

                    fetchProducts(current_keyword, ++page + "");
                    Log.d(TAG, "onScrolled: page " + page);
                } else {
                    Log.d(TAG, "onScrolled: else " + isScrolling + " " + totalItems + " " + scrolledOutItems + " " + currentItems);
                }
            }
        });

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddProduct addProduct = new AddProduct(AllProducts.this, null, "add");
                addProduct.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                addProduct.show();
            }
        });

        search_bar.addTextChangedListener(new TextWatcher() {
                  @Override
                  public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                  }

                  @Override
                  public void onTextChanged(CharSequence s, int start, int before, int count) {
                  }


            private Handler handler=new Handler();
            private final long DELAY = 500; // milliseconds

            Runnable mFilterTask = new Runnable() {
                @Override
                public void run() {
                    current_keyword = search_bar.getText().toString();
                    fetchProducts(current_keyword, "1");
                }
            };

            @Override
                  public void afterTextChanged(Editable s) {

                      handler.removeCallbacks(mFilterTask);
                      handler.postDelayed(mFilterTask, DELAY);

                  }
              }
        );

        fetchProducts(current_keyword,"1");

    }

    public void fetchProducts(String keyword, final String page) {
        //needed while fetching filters
        Log.d(TAG, "fetchProducts: page "+page);
        if(page.equalsIgnoreCase("1")){
            firstLoad = true;
            this.page = 1;
            total_products_list = new ArrayList<>();
        }

        Call<ProducModel> productModel = getApiInstance().fetchProducts(getCurrentUser().getId(), getCurrentUser().getAccessToken(), null, keyword, page);
        shimmerRecycler.showShimmerAdapter();

        productModel.enqueue(new Callback<ProducModel>() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onResponse(Call<ProducModel> call, Response<ProducModel> response) {
                shimmerRecycler.hideShimmerAdapter();
                if(!response.isSuccessful()){

                    Toast.makeText(AllProducts.this, ""+response.message(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onResponse: "+response.code()+"  mes "+response.message());
                    return;
                }

                if(response.body() != null){
                    ProducModel productModel = null;
                    try {
                        productModel = response.body();
                    } catch (Exception e) {
                        Log.d(TAG, "onResponse: "+e.toString());
                        Toast.makeText(AllProducts.this, "Error parsing response.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String content = "";
                    if(productModel.getResult().equals("success")){
                        Log.d(TAG, "onResponse: parentcategory fetch success");
                        if(productModel.getData() != null && productModel.getData().size()>0){
                            hideErrorMessage();
                            products_view.setVisibility(View.VISIBLE);


                            firstLoad = false;
                            isScrolling = false;
                            total_products_list.addAll(productModel.getData());
                            productAdapter.updateItems(total_products_list);


                        } else {
                            isScrolling = true;
                            if(firstLoad){
                                showErrorMessage("No Search Results found for keyword "+"\""+keyword+"\"");
                            }
                        }

                    } else {
                        showErrorMessage(productModel.getMsg());
                        Log.d(TAG, "onResponse: "+ productModel.getMsg());
                        Toast.makeText(AllProducts.this, ""+ productModel.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ProducModel> call, Throwable t) {
                showErrorMessage("There are no products under this category");
                shimmerRecycler.hideShimmerAdapter();
                Log.d(TAG, "onFailure: "+t.getLocalizedMessage());
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private void showErrorMessage(String s) {
        error_msg_layout.setVisibility(View.VISIBLE);
        error_msg.setText(s);
        open_product_layout.setVisibility(View.GONE);
    }

    private void hideErrorMessage() {
        error_msg_layout.setVisibility(View.GONE);
        open_product_layout.setVisibility(View.VISIBLE);
    }


}
