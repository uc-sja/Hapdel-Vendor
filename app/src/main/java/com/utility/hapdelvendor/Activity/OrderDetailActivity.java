package com.utility.hapdelvendor.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.google.gson.Gson;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;
import com.utility.hapdelvendor.Adapter.OrderedItemsAdapter;
import com.utility.hapdelvendor.Interfaces.ResponseResult;
import com.utility.hapdelvendor.Model.UserOrderModel.OrderDetailModel.Datum;
import com.utility.hapdelvendor.Model.UserOrderModel.OrderDetailModel.Item;
import com.utility.hapdelvendor.Model.UserOrderModel.OrderDetailModel.OrderDetailModel;
import com.utility.hapdelvendor.R;
import com.utility.hapdelvendor.Utils.Common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.text.TextUtils.isEmpty;
import static com.utility.hapdelvendor.Utils.Common.getApiInstance;
import static com.utility.hapdelvendor.Utils.Common.getCurrentUser;

public class OrderDetailActivity extends AppCompatActivity {
    private static final int AUTOCOMPLETE_REQUEST_CODE = 20120;
    private Toolbar tl;
    //location
    TextView location_text;
    private LinearLayout location_layout;

    //Error Msg Layout
    private LottieAnimationView error_image;
    private RelativeLayout error_msg_layout;
    private TextView error_msg;
    private RelativeLayout order_detail_layout;
    private RecyclerView items_view;
    private String  parentOrder;
    private static final String TAG = "OrderDetailActivity";
    private OrderedItemsAdapter orderedItemsAdapter;
    private ShimmerRecyclerView shimmerRecycler;

    private TextView house_no,apartment_name,street_details,landmark,city_details;
    private TextView house_no2,apartment_name2,street_details2,landmark2,city_details2;
    private TextView shipping_text;

    CardView shipping_cardview, shipping_cardview2, item_cardview;

    private TextView order_date,order_amount,payment_method,order_id;
    private RelativeLayout container_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.colorPrimaryGreen));
            Common.setStatusColor(OrderDetailActivity.this, R.color.colorPrimary);
        }

        tl = findViewById(R.id.toolbar);
        tl.setTitle("Order Details");
        setSupportActionBar(tl);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        container_layout = findViewById(R.id.container_layout);

        //Error Msg Initializtion
        order_detail_layout = findViewById(R.id.order_detail_layout);
        error_msg_layout = findViewById(R.id.error_layout);
        error_msg = findViewById(R.id.error_msg);
        error_image = findViewById(R.id.error_image);

        error_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                error_image.playAnimation();
            }
        });

        parentOrder = getIntent().getStringExtra("order");
        shimmerRecycler = (ShimmerRecyclerView) findViewById(R.id.shimmer_recycler_view);

        items_view = findViewById(R.id.items_view);
        orderedItemsAdapter = new OrderedItemsAdapter(this, new ArrayList<Item>());
        items_view.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        items_view.setAdapter(orderedItemsAdapter);


        shipping_text = findViewById(R.id.shipping_text);

        //Initializing shipping layout
        house_no = findViewById(R.id.house_no);
        apartment_name = findViewById(R.id.apartment_name);
        street_details = findViewById(R.id.street_details);
        landmark = findViewById(R.id.landmark);
        city_details = findViewById(R.id.city_details);


        //Initializing shipping layout
        house_no2 = findViewById(R.id.house_no2);
        apartment_name2 = findViewById(R.id.apartment_name2);
        street_details2 = findViewById(R.id.street_details2);
        landmark2 = findViewById(R.id.landmark2);
        city_details2 = findViewById(R.id.city_details2);

        //Initializing Order Details Layout
        order_date = findViewById(R.id.order_date);
        order_amount = findViewById(R.id.order_amount);
        payment_method = findViewById(R.id.payment_method);
        order_id = findViewById(R.id.order_id);

        shipping_cardview = findViewById(R.id.shipping_cardview);
        shipping_cardview2 = findViewById(R.id.shipping_cardview2);
        item_cardview = findViewById(R.id.item_card_view);

        if (parentOrder != null) {
            fetchOrderDetails();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    public void fetchOrderDetails() {

        Call<OrderDetailModel> orderDetailModelCall = getApiInstance().fetchUserOrderDetails(getCurrentUser().getId(), getCurrentUser().getAccessToken(), parentOrder);
        shimmerRecycler.showShimmerAdapter();
        container_layout.setVisibility(View.GONE);
        orderDetailModelCall.enqueue(new Callback<OrderDetailModel>() {
            @Override
            public void onResponse(Call<OrderDetailModel> call, Response<OrderDetailModel> response) {
                shimmerRecycler.hideShimmerAdapter();
                container_layout.setVisibility(View.VISIBLE);
                if (!response.isSuccessful()) {
                    Toast.makeText(OrderDetailActivity.this, "" + response.message(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onResponse: fail " + response.code());
                    return;
                }

                Log.d(TAG, "onResponse: success" + response.code() + response.body());
                if (response.body() != null) {
                    OrderDetailModel orderDetailModel = null;
                    try {
                        orderDetailModel = response.body();
                    } catch (Exception e) {
                        Toast.makeText(OrderDetailActivity.this, "Error in response", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String content = "";
                    Log.d(TAG, "onResponse: response msg" + response.body().getResult() + "  msg  ");
                    if (orderDetailModel.getResult().equals("success")) { //very important conditon
                        if (orderDetailModel.getData() != null && orderDetailModel.getData().size() > 0) {
                            orderedItemsAdapter.updateItems(orderDetailModel.getData().get(0).getItems());

                            Datum datum = orderDetailModel.getData().get(0);


//                            if(datum.getType().equals("product")||datum.getType().equals("service")){
//                                house_no.setText(datum.getHouseNo());
//                                apartment_name.setText(datum.getApartmentName());
//                                street_details.setText(datum.getStreetAddress());
//                                landmark.setText(datum.getLandmark());
//                                city_details.setText(datum.getState()+", "+datum.getCity()+"- "+datum.getPincode()) ;
//                                //when location was updated by google place picker
//
//                                if(isEmpty(datum.getHouseNo().trim()) && isEmpty(datum.getApartmentName().trim())){
//                                    //if location was updated from placee picker
//                                    house_no.setVisibility(View.GONE);
//                                    landmark.setVisibility(View.GONE);
//                                    street_details.setVisibility(View.GONE);
//                                    city_details.setVisibility(View.GONE);
//                                    apartment_name.setText(datum.getAddress());
//                                }
//
//                            } else if(datum.getType().equals("delivery")){
//                                Log.d(TAG, "onResponse: del ");
//
//                                shipping_text.setText("Pickup Address");
//                                shipping_cardview2.setVisibility(View.VISIBLE);
//                                item_cardview.setVisibility(View.GONE);
//
//                                house_no.setText(datum.getPickupAddress().getHouseNo());
//                                apartment_name.setText(datum.getPickupAddress().getApartmentName());
//                                street_details.setText(datum.getPickupAddress().getStreetAddress());
//                                landmark.setText(datum.getPickupAddress().getLandmark());
//                                city_details.setText(datum.getPickupAddress().getState()+", "+datum.getPickupAddress().getCity()+"- "+datum.getPickupAddress().getPincode());
//
//                                house_no2.setText(datum.getDropAddress().getHouseNo());
//                                apartment_name2.setText(datum.getDropAddress().getApartmentName());
//                                street_details2.setText(datum.getDropAddress().getStreetAddress());
//                                landmark2.setText(datum.getDropAddress().getLandmark());
//                                city_details2.setText(datum.getDropAddress().getState()+", "+datum.getDropAddress().getCity()+"- "+datum.getDropAddress().getPincode());
//
//                                Log.d(TAG, "onResponse: getAddress  "+datum.getDropAddress().getAddress());
//
//                                if(isEmpty(datum.getPickupAddress().getHouseNo().trim()) && isEmpty(datum.getPickupAddress().getApartmentName().trim())){
//                                    //if location was updated from placee picker
//                                    house_no.setVisibility(View.GONE);
//                                    landmark.setVisibility(View.GONE);
//                                    street_details.setVisibility(View.GONE);
//                                    city_details.setVisibility(View.GONE);
//                                    apartment_name.setText(datum.getPickupAddress().getAddress());
//                                }
//                                if(isEmpty(datum.getDropAddress().getHouseNo().trim()) && isEmpty(datum.getDropAddress().getApartmentName().trim())){
//                                    //if location was updated from placee picker
//                                    house_no2.setVisibility(View.GONE);
//                                    landmark2.setVisibility(View.GONE);
//                                    street_details2.setVisibility(View.GONE);
//                                    city_details2.setVisibility(View.GONE);
//                                    apartment_name2.setText(datum.getDropAddress().getAddress());
//                                }

//                            }

//
//                            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                            SimpleDateFormat format2 = new SimpleDateFormat("EE, d MMM yyyy hh:mm a");
//                            Date date = null;
//                            try {
//                                date = format1.parse(datum.getTxnDate());
//                            } catch (ParseException e) {
//                                e.printStackTrace();
//                            }
//
//
//                            order_date.setText(""+format2.format(date));
//                            order_amount.setText(getResources().getString(R.string.rupee_icon)+" "+parentOrder.getTxnAmount());
//                            payment_method.setText(parentOrder.getPaymentMethod());
//                            order_id.setText("#"+parentOrder.getOrderId());

                        } else {
                            showErrorMessage("There is no item in this order");
                        }

                    } else {
                        content += orderDetailModel.getMsg();
                        showErrorMessage(""+content);
                    }

                    Log.d(TAG, "onResponse: fetch orders res" + content);
                } else {
                    showErrorMessage("Invalid response from server");

                }
            }

            @Override
            public void onFailure(Call<OrderDetailModel> call, Throwable t) {
                container_layout.setVisibility(View.VISIBLE);
                showErrorMessage(""+t.getLocalizedMessage());

            }
        });
    }

    private void showErrorMessage(String s) {
        error_msg_layout.setVisibility(View.VISIBLE);
        error_msg.setText(s);
        order_detail_layout.setVisibility(View.GONE);
    }

    private void hideErrorMessage() {
        error_msg_layout.setVisibility(View.GONE);
        order_detail_layout.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
