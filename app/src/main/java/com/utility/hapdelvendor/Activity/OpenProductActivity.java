package com.utility.hapdelvendor.Activity;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
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
import android.widget.ExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialOverlayLayout;
import com.leinardi.android.speeddial.SpeedDialView;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.utility.hapdelvendor.Adapter.ProductsAdapter;
import com.utility.hapdelvendor.Adapter.TabbedViewAdapter;
import com.utility.hapdelvendor.Dialog.AddDiscount;
import com.utility.hapdelvendor.Dialog.AddProduct;
import com.utility.hapdelvendor.DiscountList;
import com.utility.hapdelvendor.ExpandableCheckboxView.DataItem;
import com.utility.hapdelvendor.ExpandableCheckboxView.SubCategoryItem;
import com.utility.hapdelvendor.Interfaces.ResponseResult;
import com.utility.hapdelvendor.Model.CategoryModel.ParentCategoryModel.ParentCategoryModel;
import com.utility.hapdelvendor.Model.CategoryModel.ParentCategoryModel.Subcategory;
import com.utility.hapdelvendor.Model.ProducModel.ProducModel;
import com.utility.hapdelvendor.R;
import com.utility.hapdelvendor.Utils.BottomNavigation;
import com.utility.hapdelvendor.Utils.Common;
import com.utility.hapdelvendor.Utils.MovableFloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.relex.circleindicator.CircleIndicator2;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.utility.hapdelvendor.Utils.Common.getApiInstance;
import static com.utility.hapdelvendor.Utils.Common.getCurrentUser;

public class OpenProductActivity extends AppCompatActivity {
    Toolbar tl;

    //location
    TextView location_text;
    private LinearLayout location_layout;

    RecyclerView products_view;
    private ProductsAdapter productAdapter;
    private RecyclerView banner_recycler, banner_recycler2;
    private CircleIndicator2 indicator1, indicator2;
    private RelativeLayout containerLayout;
    private RelativeLayout slider_layout;;
    private ScrollView scrollView;
    private RecyclerView tabbed_view;
    private TabbedViewAdapter tabbedViewAdapter;

    //Sliding PanelLayout
    private SlidingUpPanelLayout sliding_layout;
    private ImageView slider_img;
    private Button slider_one_btn, slider_two_btn;
    private TextView slider_msg;

    //Error Msg Layout
    private LottieAnimationView error_image;
    private RelativeLayout error_msg_layout;
    private TextView error_msg;
    private RelativeLayout open_product_layout;


    private static final String TAG = "OpenProductActivity";
    private Snackbar snackbar;
    private Button snack_btn;
    private TextView textViewTop;
    private Animator spruceAnimator;
    private NestedScrollView view_scroller;
    private ShimmerRecyclerView shimmerRecycler;
    private TextView assist_text;

    private com.utility.hapdelvendor.Model.CategoryModel.ParentCategoryModel.Datum parentCategory;
    private MovableFloatingActionButton floatingActionButton;
    private HashMap<String, List<String>> expandableListDetail;
    private ArrayList<String> expandableListTitle;
    private ExpandableListAdapter expandableListAdapter;
    private Dialog dialog;
    private String currrentlySelctedCategoryId;
    public static List<String> brandFilter, weightFilter;
    private List<com.utility.hapdelvendor.Model.FilterModel.WeightListModel.Datum> weightDatum;

    //for expandablelistcheckboxview
    private ArrayList<DataItem> arCategory;
    private ArrayList<SubCategoryItem> arSubCategory;
    private ArrayList<ArrayList<SubCategoryItem>> arSubCategoryFinal;

    private ArrayList<HashMap<String, String>> parentItems;
    private ArrayList<ArrayList<HashMap<String, String>>> childItems;

    private EditText search_bar;
    private String tabCategoryId;
    private LinearLayoutManager productLayoutManager;
    private boolean isScrolling,firstLoad;
    public List<com.utility.hapdelvendor.Model.ProducModel.Product> total_products_list;
    private int scrolledOutItems,currentItems,totalItems;

    public com.utility.hapdelvendor.Model.CategoryModel.ParentCategoryModel.Datum selectedDatum = null;

    //pre-selected as we have already loaded products in page 1
    public int page = 1;
    private int AUTOCOMPLETE_REQUEST_CODE = 1515;
    private AHBottomNavigation bottomNavigation;
    private Subcategory tabCategory;
    private SpeedDialView speedDialView;
    public String current_keyword;


    public void setSelectedDatum(com.utility.hapdelvendor.Model.CategoryModel.ParentCategoryModel.Datum selectedDatum) {
        this.selectedDatum = selectedDatum;
        search_bar.setHint("Search within "+selectedDatum.getName());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: on Create ");
        Common.setAnimation(this);
        setContentView(R.layout.activity_open_product);


        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
            Common.setStatusColor(OpenProductActivity.this, R.color.colorPrimary);
        }

        bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);
        BottomNavigation.initializeBottomNavigation(bottomNavigation, OpenProductActivity.this);
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


        parentCategory = new Gson().fromJson(getIntent().getStringExtra("category"), com.utility.hapdelvendor.Model.CategoryModel.ParentCategoryModel.Datum.class);
        tabCategory = new Gson().fromJson(getIntent().getStringExtra("selected_cat"), com.utility.hapdelvendor.Model.CategoryModel.ParentCategoryModel.Subcategory.class);
//        tabCategoryId = getIntent().getStringExtra("tab_category");

        tabbed_view = findViewById(R.id.tabbed_view);
        tabbedViewAdapter = new TabbedViewAdapter(this, new ArrayList<com.utility.hapdelvendor.Model.CategoryModel.ParentCategoryModel.Datum>());
        tabbed_view.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        tabbed_view.setAdapter(tabbedViewAdapter);

        if(parentCategory!=null){
            search_bar = findViewById(R.id.search_bar);

            search_bar.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                private Handler handler = new Handler();
                private long DELAY = 500;

                private Runnable scheduledTask = new Runnable() {
                    @Override
                    public void run() {
                        current_keyword = search_bar.getText().toString();
                        fetchProducts(selectedDatum, current_keyword, "1");

                    }
                };

                @Override
                public void afterTextChanged(Editable s) {
                    handler.removeCallbacks(scheduledTask);
                    handler.postDelayed(scheduledTask, DELAY);
                }
            });
        }

        products_view = findViewById(R.id.products_view);
        products_view.setVisibility(View.GONE);
        productAdapter = new ProductsAdapter(this, new ArrayList<com.utility.hapdelvendor.Model.ProducModel.Product>());
        productLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        products_view.setLayoutManager(productLayoutManager);
        products_view.setAdapter(productAdapter);

        total_products_list = new ArrayList<>();


//        spruceAnimator = new Spruce
//                .SpruceBuilder(view_scroller)
//                .sortWith(new DefaultSort(/*interObjectDelay=*/50L))
//                .animateWith(new Animator[] {DefaultAnimations.shrinkAnimator(view_scroller, /*duration=*/800)}).start();

        shimmerRecycler = (ShimmerRecyclerView) findViewById(R.id.shimmer_recycler_view);


        slider_layout = findViewById(R.id.slider_layout);

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

//        rangeBar = findViewById(R.id.rangebar1);
//        floatingActionButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AddProduct addProduct = new AddProduct(OpenProductActivity.this);
//                addProduct.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
//                addProduct.show();
//            }
//        });

        speedDialView = (SpeedDialView) findViewById(R.id.speedDial);
        SpeedDialOverlayLayout overlayLayout = findViewById(R.id.overlay);
                speedDialView.setOverlayLayout(overlayLayout);

        speedDialView.addActionItem(new SpeedDialActionItem.Builder(R.id.fab_add_btn, R.drawable.ic_order)
                .setFabBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorWhite, getTheme()))
                .setFabImageTintColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimary, getTheme()))
                .setLabel(getString(R.string.add_prod))
                .setFabSize(20)
                .setLabelColor(ResourcesCompat.getColor(getResources(), R.color.darkGray,getTheme()))
                .setLabelBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorWhite,getTheme()))
                .setLabelClickable(false)
                .create());

        speedDialView.addActionItem(new SpeedDialActionItem.Builder(R.id.fab_disc_btn, R.drawable.ic_hot_sale)
                .setFabBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorWhite, getTheme()))
                .setFabImageTintColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimary, getTheme()))
                .setLabel(getString(R.string.add_discout))
                .setFabSize(20)
                .setLabelColor(ResourcesCompat.getColor(getResources(), R.color.darkGray,getTheme()))
                .setLabelBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorWhite,getTheme()))
                .setLabelClickable(false)
                .create());

        speedDialView.addActionItem(new SpeedDialActionItem.Builder(R.id.fab_disc_list, R.drawable.ic_hot_sale)
                .setFabBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorWhite, getTheme()))
                .setFabImageTintColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimary, getTheme()))
                .setLabel(getString(R.string.discout_list))
                .setFabSize(20)
                .setLabelColor(ResourcesCompat.getColor(getResources(), R.color.darkGray,getTheme()))
                .setLabelBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorWhite,getTheme()))
                .setLabelClickable(false)
                .create());

        speedDialView.setOnActionSelectedListener(new SpeedDialView.OnActionSelectedListener() {
            @Override
            public boolean onActionSelected(SpeedDialActionItem actionItem) {
                switch (actionItem.getId()){
                    case R.id.fab_add_btn :
                        AddProduct addProduct = new AddProduct(OpenProductActivity.this, selectedDatum, "add");
                        addProduct.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                        addProduct.show();
                        break;

                    case R.id.fab_disc_btn :
                        AddDiscount addDiscount = new AddDiscount(OpenProductActivity.this, selectedDatum, "add", null);
                        addDiscount.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                        addDiscount.show();
                        break;

                    case R.id.fab_disc_list :
                        Intent intent = new Intent(OpenProductActivity.this, DiscountList.class);
                        intent.putExtra("category", new Gson().toJson(selectedDatum));
                        startActivity(intent);
                        break;
                }
                return false;
            }
        });
    }

    public void intializeEnlessScrolling(){
        if (selectedDatum != null) {
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

                        fetchProducts(selectedDatum,  current_keyword, ++page + "");
                        Log.d(TAG, "onScrolled: page " + page);
                    } else {
                        Log.d(TAG, "onScrolled: else " + isScrolling + " " + totalItems + " " + scrolledOutItems + " " + currentItems);
                    }
                }
            });

        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
                fetchHomePage();
    }

    private void fetchHomePage(){
        fetchTabbedCategories(parentCategory);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(dialog!=null){
            dialog.dismiss();
            dialog = null;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private void fetchTabbedCategories(final com.utility.hapdelvendor.Model.CategoryModel.ParentCategoryModel.Datum datum) {
        Call<ParentCategoryModel> categoryModel = getApiInstance().fetchSubCategory(getCurrentUser().getId(), getCurrentUser().getAccessToken(), datum.getId());
        categoryModel.enqueue(new Callback<ParentCategoryModel>() {
            @Override
            public void onResponse(Call<ParentCategoryModel> call, Response<ParentCategoryModel> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(OpenProductActivity.this, ""+response.message(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onResponse: "+response.code()+"  mes "+response.message());
                    return;
                }

                if(response.body() != null){
                    ParentCategoryModel parentCategoryModel = null;
                    try {
                        parentCategoryModel = response.body();
                    } catch (Exception e) {
                        Log.d(TAG, "onResponse: tabbed view "+e.toString());
                        Toast.makeText(OpenProductActivity.this, "Error parsing response.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String content = "";
                    if(parentCategoryModel.getResult().equals("success")){
                        Log.d(TAG, "onResponse: tabbed view fetch success");
                        if(parentCategoryModel.getData()!=null && parentCategoryModel.getData().size()>0){
                            hideErrorMessage();
                            Log.d(TAG, "onResponse: tabbed view size "+ parentCategoryModel.getData().size());
                            tabbedViewAdapter.updateItems(parentCategoryModel.getData(), tabCategory);
//                            for(int i = 0; i<parentCategoryModel.getData().size(); i++){
//                                com.utility.hapdelvendor.Model.CategoryModel.ParentCategoryModel.Datum datum1 = parentCategoryModel.getData().get(i);
//                                Log.d(TAG, "onResponse : fetchTabbedCategories "+i+" "+datum.getName());
//                                if(datum1.getName().equalsIgnoreCase("maida")){
//                                    TabbedViewAdapter.TabbedViewHolder viewHolder = (TabbedViewAdapter.TabbedViewHolder) tabbed_view.findViewHolderForAdapterPosition(i);
//                                    if(viewHolder != null){
//                                        viewHolder.tabbed_btn.performClick();
//                                    }
//                                }
//                            }

                        } else {
                            Log.d(TAG, "onResponse: empty result tabbed view");
                            showErrorMessage("There are no further product categories", false);
                        }

                    } else {
                        showErrorMessage(parentCategoryModel.getMsg(), false);
                        Log.d(TAG, "onResponse: tabbed view"+parentCategoryModel.getMsg());
                        Toast.makeText(OpenProductActivity.this, ""+parentCategoryModel.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ParentCategoryModel> call, Throwable t) {
                showErrorMessage(t.toString(), false);
                Log.d(TAG, "onFailure: tabbed view"+t.getLocalizedMessage());
            }
        });

    }

    public void fetchProducts(final com.utility.hapdelvendor.Model.CategoryModel.ParentCategoryModel.Datum datum, final String keyword, String page) {
        //needed while fetching filters
        Log.d(TAG, "fetchProducts: page "+page);
        if(page.equalsIgnoreCase("1")){
            firstLoad = true;
            this.page = 1;
            total_products_list = new ArrayList<>();
        }

        currrentlySelctedCategoryId = datum.getId();

        //filter window needs to be reset while fetching products
        dialog = null;

        Log.d(TAG, "fetchProducts: product_id "+ datum.getId());
        Call<ProducModel> productModel = getApiInstance().fetchProducts(getCurrentUser().getId(), getCurrentUser().getAccessToken(), datum.getId(), keyword, page);
        shimmerRecycler.showShimmerAdapter();

        productModel.enqueue(new Callback<ProducModel>() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onResponse(Call<ProducModel> call, Response<ProducModel> response) {
                shimmerRecycler.hideShimmerAdapter();
                if(!response.isSuccessful()){

                    Toast.makeText(OpenProductActivity.this, ""+response.message(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onResponse: "+response.code()+"  mes "+response.message());
                    return;
                }

                if(response.body() != null){
                    ProducModel productModel = null;
                    try {
                        productModel = response.body();
                    } catch (Exception e) {
                        Log.d(TAG, "onResponse: "+e.toString());
                        Toast.makeText(OpenProductActivity.this, "Error parsing response.", Toast.LENGTH_SHORT).show();
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
                                showErrorMessage("There are no products under this category", true);
                            }
                        }

                    } else {
                        showErrorMessage(productModel.getMsg(), true);
                        Log.d(TAG, "onResponse: "+ productModel.getMsg());
                        Toast.makeText(OpenProductActivity.this, ""+ productModel.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ProducModel> call, Throwable t) {
                showErrorMessage("There are no products under this category", true);
                shimmerRecycler.hideShimmerAdapter();
                Log.d(TAG, "onFailure: "+t.getLocalizedMessage());
            }
        });
    }

    public void scrollTo(int j) {
        tabbed_view.smoothScrollToPosition(j);
    }

    private void showErrorMessage(String s, Boolean isTabsFetched) {
        error_msg_layout.setVisibility(View.VISIBLE);
        error_msg.setText(s);
        open_product_layout.setVisibility(View.GONE);
        tabbed_view.setVisibility(isTabsFetched?View.VISIBLE:View.GONE);
    }

    private void hideErrorMessage() {
        tabbed_view.setVisibility(View.VISIBLE);
        error_msg_layout.setVisibility(View.GONE);
        open_product_layout.setVisibility(View.VISIBLE);
    }

    public void showSliderLayout(String msg, boolean showBtns, String btn_function, final ResponseResult cleartCartResponseResult){
        Common.hideKeyboard(this);
        slider_msg.setText(msg);
        sliding_layout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
        if(!showBtns){
            slider_one_btn.setVisibility(View.INVISIBLE);
            slider_two_btn.setVisibility(View.INVISIBLE);
        }

        if(btn_function.trim().equalsIgnoreCase("login")){
            slider_one_btn.setText("Register");
            slider_two_btn.setText("Login Now");
            slider_one_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(OpenProductActivity.this, SignUpActivity.class));
                }
            });

            slider_two_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(OpenProductActivity.this, SignInActivity.class);
                    intent.putExtra("category", new Gson().toJson(parentCategory));
                    startActivity(intent);
                }
            });

        } else if(btn_function.trim().equalsIgnoreCase("clear")){
            slider_one_btn.setText("Cancel");
            slider_two_btn.setText("Clear");
            slider_one_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sliding_layout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                }
            });

            slider_two_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sliding_layout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);

                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        if(sliding_layout.getPanelState().equals(SlidingUpPanelLayout.PanelState.EXPANDED)){
            sliding_layout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            return;
        }
        super.onBackPressed();
    }


}
