package com.utility.hapdelvendor.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.utility.hapdelvendor.Adapter.TransactionAdapter;
import com.utility.hapdelvendor.Model.TransactionModel.Datum;
import com.utility.hapdelvendor.Model.TransactionModel.TransactionModel;
import com.utility.hapdelvendor.R;
import com.utility.hapdelvendor.Utils.BottomNavigation;
import com.utility.hapdelvendor.Utils.CircularTextView;
import com.utility.hapdelvendor.Utils.Common;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.utility.hapdelvendor.Utils.Common.getApiInstance;
import static com.utility.hapdelvendor.Utils.Common.getCurrentUser;

public class TransactionActivity extends AppCompatActivity {

    ImageView cart_icon;
    private CircularTextView cart_badge;
    private Toolbar tl;
    private AppCompatAutoCompleteTextView search_bar;

    private static final String TAG = "TransactionActivity";
    private TransactionAdapter transactionAdapter;
    private LinearLayoutManager layoutManager;
    private RecyclerView user_txn_view;

    //Error Msg Layout
    private LottieAnimationView error_image;
    private RelativeLayout error_msg_layout;
    private TextView error_msg;
    private RelativeLayout view_transactions_layout;

    //Sliding PanelLayout
    private SlidingUpPanelLayout sliding_layout;
    private ImageView slider_img;
    private Button slider_one_btn, slider_two_btn;
    private TextView slider_msg;

    private int scrolledOutItems, currentItems, totalItems;


    private List<com.utility.hapdelvendor.Model.TransactionModel.Datum> total_txn_list;
    private boolean firstLoad = false;
    private boolean isScrolling;
    private int i;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        tl = findViewById(R.id.toolbar);

        setSupportActionBar(tl);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
            Common.setStatusColor(TransactionActivity.this, R.color.colorPrimaryDark);
        }

        sliding_layout = findViewById(R.id.sliding_layout);
        slider_img = findViewById(R.id.slider_img);
        slider_msg = findViewById(R.id.slider_msg);

        slider_one_btn = findViewById(R.id.slider_btn_one);
        slider_two_btn = findViewById(R.id.slider_btn_two);

        AHBottomNavigation bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);
        BottomNavigation.initializeBottomNavigation(bottomNavigation, TransactionActivity.this);
        bottomNavigation.setCurrentItem(-1, false);


        user_txn_view = findViewById(R.id.user_txn_view);
        layoutManager = new LinearLayoutManager(TransactionActivity.this, LinearLayoutManager.VERTICAL, false);
        user_txn_view.setLayoutManager(layoutManager);
        transactionAdapter = new TransactionAdapter(TransactionActivity.this, new ArrayList<Datum>());
        user_txn_view.setAdapter(transactionAdapter);


        user_txn_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.d(TAG, "onScrollStateChanged: "+newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.d(TAG, "onScrolled: "+dy);
                currentItems=layoutManager.getChildCount();
                totalItems = layoutManager.getItemCount();
                scrolledOutItems = layoutManager.findFirstVisibleItemPosition();
                Log.d(TAG, "onScrolled: "+isScrolling+" "+totalItems+" "+scrolledOutItems+" "+currentItems);
                if(!isScrolling && totalItems == scrolledOutItems+currentItems){
                    //Since we cannot assign a variable to final int i
                    Log.d(TAG, "onScrolled: if "+isScrolling+" "+totalItems+" "+scrolledOutItems+" "+currentItems);

                    fetchTransactions(++i);
                }else {
                    Log.d(TAG, "onScrolled: else "+isScrolling+" "+totalItems+" "+scrolledOutItems+" "+currentItems);
                }

            }
        });



        //Error Msg Initialization
        view_transactions_layout = findViewById(R.id.view_txn_layout);
        error_msg_layout = findViewById(R.id.error_layout);
        error_msg = findViewById(R.id.error_msg);
        error_image = findViewById(R.id.error_image);
        error_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                error_image.playAnimation();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(getCurrentUser()==null){
            showSliderLayout("Kindly login to view your orders");
            return;
        }

        i = 1;
        total_txn_list = new ArrayList<>();

        fetchTransactions(i);

    }

    private void fetchTransactions(final int i) {
        Log.d(TAG, "fetch transactions : "+i);
        firstLoad = i == 1 ? true:false;
        final ProgressDialog progressDialog = new ProgressDialog(TransactionActivity.this, R.style.MyDialogTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Fetching transactions...  ");
        progressDialog.setCancelable(false);
        if(!((Activity)this).isFinishing()){
            progressDialog.show();
        }

        Call<TransactionModel> TransactionModelCall = getApiInstance().fetchTransactions(getCurrentUser().getId(), getCurrentUser().getAccessToken(), i+"");
        TransactionModelCall.enqueue(new Callback<TransactionModel>() {
            @Override
            public void onResponse(Call<TransactionModel> call, Response<TransactionModel> response) {
                progressDialog.dismiss();
                if(!response.isSuccessful()){
                    Toast.makeText(TransactionActivity.this, ""+response.message(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onResponse: fail "+response.code());
                    return;
                }

                Log.d(TAG, "onResponse: success"+response.code()+response.body());
                if(response.body()!=null ){
                    TransactionModel TransactionModel = null;
                    try {
                        TransactionModel = response.body();
                    } catch (Exception e) {
                        Toast.makeText(TransactionActivity.this, "Error in response", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String content="";
                    Log.d(TAG, "onResponse: response msg"+response.body().getResult()+"  msg  ");
                    if (TransactionModel.getResult().equals("success")){ //very important conditon
                        if(TransactionModel.getData()!=null && TransactionModel.getData().size()>0){
                            firstLoad = false;
                            isScrolling = false;
                            total_txn_list.addAll(TransactionModel.getData());
                            transactionAdapter.updateItems(total_txn_list);
                        } else {
                            isScrolling = true;
                            if(firstLoad){
                                showErrorMessage("Currently you have not completed any order");
                            }
                        }
                    }else{
                        content+= TransactionModel.getMsg();
                        Toast.makeText(TransactionActivity.this, content, Toast.LENGTH_SHORT).show();
                    }

                    Log.d(TAG, "onResponse: fetch orders res "+content);
                } else {
                    Toast.makeText(TransactionActivity.this, "Invalid response from server", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TransactionModel> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(TransactionActivity.this, ""+t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private void showErrorMessage(String s) {
        error_msg_layout.setVisibility(View.VISIBLE);
        error_msg.setText(s);
        view_transactions_layout.setVisibility(View.GONE);
    }

    private void hideErrorMessage() {
        error_msg_layout.setVisibility(View.GONE);
        view_transactions_layout.setVisibility(View.VISIBLE);
    }

    public void showSliderLayout(String msg){
        Common.hideKeyboard(this);
        slider_msg.setText(msg);
        sliding_layout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
        slider_one_btn.setText("Login Now");
        slider_one_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TransactionActivity.this, SignInActivity.class);
                startActivity(intent);
                finish();
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
