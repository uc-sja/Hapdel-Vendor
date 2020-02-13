package com.utility.hapdelvendor;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.google.gson.Gson;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.utility.hapdelvendor.Adapter.DiscountAdapter;
import com.utility.hapdelvendor.Adapter.ProductsAdapter;
import com.utility.hapdelvendor.Adapter.TabbedViewAdapter;
import com.utility.hapdelvendor.Model.DiscountModel.Datum;
import com.utility.hapdelvendor.Model.DiscountModel.DiscountModel;
import com.utility.hapdelvendor.Utils.BottomNavigation;
import com.utility.hapdelvendor.Utils.Common;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator2;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.utility.hapdelvendor.Utils.Common.getApiInstance;
import static com.utility.hapdelvendor.Utils.Common.getCurrentUser;

public class DiscountList extends AppCompatActivity {
    Toolbar tl;

    //location
    TextView location_text;
    private LinearLayout location_layout;

    RecyclerView discounts_view;
    private ProductsAdapter productAdapter;
    private RecyclerView banner_recycler, banner_recycler2;
    private CircleIndicator2 indicator1, indicator2;
    private RelativeLayout containerLayout;
    private RelativeLayout slider_layout;
    ;
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
    private AHBottomNavigation bottomNavigation;
    private com.utility.hapdelvendor.Model.CategoryModel.ParentCategoryModel.Datum parentCategory;
    private EditText search_bar;
    private LinearLayoutManager discountLayoutManager;
    private DiscountAdapter discountAdapter;
    private static final String TAG = "DiscountList";
    private RelativeLayout discount_container_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discount_list);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.colorPrimaryGreen));
            Common.setStatusColor(DiscountList.this, R.color.colorPrimary);
        }

        bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);
        BottomNavigation.initializeBottomNavigation(bottomNavigation, DiscountList.this);
        bottomNavigation.setCurrentItem(-1, false);

        containerLayout = findViewById(R.id.container_layout);
        discount_container_layout = findViewById(R.id.discount_container_layout);
        
        tl = findViewById(R.id.toolbar);
        tl.setTitle("");
        setSupportActionBar(tl);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setElevation(0);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        parentCategory = new Gson().fromJson(getIntent().getStringExtra("category"), com.utility.hapdelvendor.Model.CategoryModel.ParentCategoryModel.Datum.class);
//        tabCategoryId = getIntent().getStringExtra("tab_category");

        if (parentCategory != null) {
            search_bar = findViewById(R.id.search_bar);

            search_bar.setVisibility(View.GONE);

            discounts_view = findViewById(R.id.user_discount_view);
            discountAdapter = new DiscountAdapter(new ArrayList<Datum>(), DiscountList.this);
            discountLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            discounts_view.setLayoutManager(discountLayoutManager);
            discounts_view.setAdapter(discountAdapter);

            fetchDiscounts();
        }
    }

    private void fetchDiscounts() {

        final ProgressDialog progressDialog = new ProgressDialog(DiscountList.this, R.style.MyDialogTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading notification...");
        progressDialog.setCancelable(false);
        if (!((Activity) DiscountList.this).isFinishing()) {
            progressDialog.show();
        }

        Call<DiscountModel> DiscountModelCall = getApiInstance().getDiscount(getCurrentUser().getId(), getCurrentUser().getAccessToken(), parentCategory.getId());
        DiscountModelCall.enqueue(new Callback<DiscountModel>() {
            @Override
            public void onResponse(Call<com.utility.hapdelvendor.Model.DiscountModel.DiscountModel> call, Response<DiscountModel> response) {
                progressDialog.dismiss();
                if (!response.isSuccessful()) {
                    Toast.makeText(DiscountList.this, "" + response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }
                com.utility.hapdelvendor.Model.DiscountModel.DiscountModel discountModel = null;
                try {
                    discountModel = response.body();
                } catch (Exception e) {
                    Toast.makeText(DiscountList.this, "Error in response", Toast.LENGTH_SHORT).show();
                    return;
                }
                Log.d(TAG, "onResponse: " + discountModel.getData().size());

                if (discountModel != null) {
                    if (discountModel.getData() != null && discountModel.getData().size() > 0) {
                        discountAdapter.updateItems(discountModel.getData());
                    } else {
                        showErrorMessage("No discount under this category", "");
                    }
                } else {
                    Toast.makeText(DiscountList.this, "Invalid Response from Server", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
                public void onFailure
                (Call < com.utility.hapdelvendor.Model.DiscountModel.DiscountModel > call, Throwable t){
                    Log.d(TAG, "onFailure: " + t.toString());
                    progressDialog.dismiss();

                }
            });
        }

    private void showErrorMessage(String message, String warning_type) {
        error_msg_layout.setVisibility(View.VISIBLE);
        error_msg.setText(message);
        discount_container_layout.setVisibility(View.GONE);


    }

    private void hideErrorMessage() {
        error_msg_layout.setVisibility(View.GONE);
        discount_container_layout.setVisibility(View.VISIBLE);
    }
    }
