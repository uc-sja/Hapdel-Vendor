package com.utility.hapdelvendor.Activity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
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
import com.utility.hapdelvendor.Adapter.OrderedItemsAdapter;
import com.utility.hapdelvendor.Model.UserOrderModel.OrderDetailModel.Customer;
import com.utility.hapdelvendor.Model.UserOrderModel.OrderDetailModel.Datum;
import com.utility.hapdelvendor.Model.UserOrderModel.OrderDetailModel.Item;
import com.utility.hapdelvendor.Model.UserOrderModel.OrderDetailModel.OrderDetailModel;
import com.utility.hapdelvendor.R;
import com.utility.hapdelvendor.Utils.Common;

import java.util.ArrayList;

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
    private com.utility.hapdelvendor.Model.RecentOrderModel.Datum parentOrder;
    private static final String TAG = "OrderDetailActivity";
    private OrderedItemsAdapter orderedItemsAdapter;
    private ShimmerRecyclerView shimmerRecycler;

    private TextView customer_name,customer_contact,customer_address,landmark,city_details;
    private TextView customer_name2,customer_contact2,customer_address2,landmark2,city_details2;
    private TextView shipping_text;

    CardView shipping_cardview, shipping_cardview2, item_cardview;

    private TextView order_date,order_amount,payment_method,order_id,service_time;
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

        parentOrder = new Gson().fromJson(getIntent().getStringExtra("order"), com.utility.hapdelvendor.Model.RecentOrderModel.Datum.class);
        shimmerRecycler = (ShimmerRecyclerView) findViewById(R.id.shimmer_recycler_view);

        items_view = findViewById(R.id.items_view);
        orderedItemsAdapter = new OrderedItemsAdapter(this, new ArrayList<Item>());
        items_view.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        items_view.setAdapter(orderedItemsAdapter);
        shipping_text = findViewById(R.id.shipping_text);

        //Initializing shipping layout
        customer_name = findViewById(R.id.customer_name);
        customer_contact = findViewById(R.id.customer_contact);
        customer_address = findViewById(R.id.customer_address);

        //Initializing shipping layout
//        customer_name2 = findViewById(R.id.customer_name2);
//        customer_contact2 = findViewById(R.id.customer_contact2);
//        customer_address2 = findViewById(R.id.customer_address2);

        //Initializing Order Details Layout
        order_date = findViewById(R.id.order_date);
        order_amount = findViewById(R.id.order_amount);
        payment_method = findViewById(R.id.payment_method);
        order_id = findViewById(R.id.order_id);
        service_time = findViewById(R.id.service_time);

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

        Call<OrderDetailModel> orderDetailModelCall = getApiInstance().fetchUserOrderDetails(getCurrentUser().getId(), getCurrentUser().getAccessToken(), parentOrder.getTxnId());
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
                            Item item = datum.getItems().get(0);
                            Customer customer = datum.getCustomer().get(0);
                            customer_name.setText(customer.getName());
                            customer_contact.setText(customer.getMobile());

                            if(isEmpty(customer.getHouseNo().trim()) && isEmpty(customer.getApartmentName().trim())){
                                customer_address.setText(customer.getAddress());
                            } else {
                                customer_address.setText(customer.getHouseNo()+", "+customer.getApartmentName()+", "+
                                        customer.getStreetAddress()+", "+customer.getAddress()+", "+customer.getState()+"- "+customer.getPincode());
                            }

                            order_id.setText("#"+item.getOrderId());
                            order_date.setText(item.getTxnDate());
                            payment_method.setText(item.getPayment_method());
                            order_amount.setText(getResources().getString(R.string.rupee_icon)+parentOrder.getAmount());

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
