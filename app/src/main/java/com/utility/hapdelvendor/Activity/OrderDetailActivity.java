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
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.google.gson.Gson;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;
import com.utility.hapdelvendor.Interfaces.ResponseResult;
import com.utility.hapdelvendor.Model.UserOrderModel.OrderDetailModel.Datum;
import com.utility.hapdelvendor.Model.UserOrderModel.OrderDetailModel.OrderDetailModel;
import com.utility.hapdelvendor.Model.UserOrderModel.OrderModel.Item;
import com.utility.hapdelvendor.R;
import com.utility.hapdelvendor.Utils.Common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.text.TextUtils.isEmpty;
import static com.utility.hapdelvendor.Utils.Common.getApiInstance;

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
    private Item parentOrder;
    private static final String TAG = "OrderDetailActivity";
    private ShimmerRecyclerView shimmerRecycler;

    private TextView house_no,apartment_name,street_details,landmark,city_details;
    private TextView house_no2,apartment_name2,street_details2,landmark2,city_details2;
    private TextView shipping_text;

    CardView shipping_cardview, shipping_cardview2, item_cardview;

    private TextView order_date,order_amount,payment_method,order_id;
    private RelativeLayout container_layout;

    //Sliding PanelLayout
    private SlidingUpPanelLayout sliding_layout;
    private ImageView slider_img;
    private Button slider_one_btn, slider_two_btn;
    private TextView slider_msg;

    TextView order_status, product_name, product_quantity, product_seller, product_price, status_color,textViewOptions;
    ImageView product_img;

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


        order_status = findViewById(R.id.order_status);
        product_img = findViewById(R.id.product_img);
        product_name = findViewById(R.id.product_name);
        product_quantity = findViewById(R.id.product_quantity);
        product_seller = findViewById(R.id.product_seller);
        product_price = findViewById(R.id.product_price);
        status_color = findViewById(R.id.status_color);
        textViewOptions = findViewById(R.id.textViewOptions);


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

        parentOrder = new Gson().fromJson(getIntent().getStringExtra("order"), com.utility.hapdelvendor.Model.UserOrderModel.OrderModel.Item.class);

        shimmerRecycler = (ShimmerRecyclerView) findViewById(R.id.shimmer_recycler_view);


        shipping_text = findViewById(R.id.shipping_text);

        sliding_layout = findViewById(R.id.sliding_layout);
        slider_img = findViewById(R.id.slider_img);
        slider_msg = findViewById(R.id.slider_msg);

        slider_one_btn = findViewById(R.id.slider_btn_one);
        slider_two_btn = findViewById(R.id.slider_btn_two);


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
        order_date = findViewById(R.id.discount_value);
        order_amount = findViewById(R.id.discount_expiry);
        payment_method = findViewById(R.id.payment_method);
        order_id = findViewById(R.id.order_id);

        shipping_cardview = findViewById(R.id.shipping_cardview);
        shipping_cardview2 = findViewById(R.id.shipping_cardview2);
        item_cardview = findViewById(R.id.item_card_view);


//        if(getCurrentUser()==null || getCurrentUser().getId()==null){
//            showSliderLayout("Please login to see your orders", true, "login", null);
//            return;
//        }

        if (parentOrder != null) {
            fetchOrderDetails();
        }

    }

    public void showSliderLayout(String msg, boolean showBtns, String btn_function, final ResponseResult cleartCartResponseResult) {
        Common.hideKeyboard(this);
        slider_msg.setText(msg);
        sliding_layout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
        if (!showBtns) {
            slider_one_btn.setVisibility(View.INVISIBLE);
            slider_two_btn.setVisibility(View.INVISIBLE);
        }

        if (btn_function.trim().equalsIgnoreCase("login")) {
            slider_one_btn.setText("Register");
            slider_two_btn.setText("Login Now");
            slider_one_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(OrderDetailActivity.this, SignUpActivity.class));
                }
            });

            slider_two_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(OrderDetailActivity.this, SignInActivity.class));

                }
            });

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void setStatusColor(TextView status_color, String orderStatus) {
        switch (orderStatus){
            case "completed": status_color.setBackgroundColor(getResources().getColor(R.color.colorGreen));
                break;
            case "processing": status_color.setBackgroundColor(getResources().getColor(R.color.quantum_yellow));
                break;
            case "received": status_color.setBackgroundColor(getResources().getColor(R.color.quantum_orange));
                break;
            case "failed": status_color.setBackgroundColor(getResources().getColor(R.color.red));
                break;
            case "pickup": status_color.setBackgroundColor(getResources().getColor(R.color.com_facebook_blue));
                break;
            case "cancelled": status_color.setBackgroundColor(getResources().getColor(R.color.colorDullRed));
                break;
            default:break;
        }
    }

    public void fetchOrderDetails() {

//        Call<OrderDetailModel> orderDetailModelCall = getApiInstance().fetchVendorOrderDetails(getCurrentUser().getId(), getCurrentUser().getAccessToken(),  parentOrder.getId());
        Call<OrderDetailModel> orderDetailModelCall = getApiInstance().fetchVendorOrderDetails("1","","128");
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
//                            orderedItemsAdapter.updateItems(orderDetailModel.getData());

                            Datum orderDetail = orderDetailModel.getData().get(0);


                            setStatusColor(status_color, orderDetail.getOrderStatus());
                            order_status.setText(( orderDetail.getOrderStatus().substring(0,1).toUpperCase() + orderDetail.getOrderStatus().substring(1)));


                            product_name.setText(orderDetail.getProductName());
                            product_quantity.setText("Qty:" + orderDetail.getQuantity());
                            product_seller.setText("Sold By: "   );

                            if(orderDetail.getImage()!=null && !isEmpty(orderDetail.getImage())){
                                Picasso.get().load(orderDetail.getImage()).placeholder(R.drawable.app_icon_png).fit().into(product_img);
                            }

                            product_price.setText(getString(R.string.rupee_icon)+orderDetail.getTxnAmount());

                            textViewOptions.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //creating a pop up menu
                                    PopupMenu popupMenu = new PopupMenu(OrderDetailActivity.this, textViewOptions);
                                    //inflating menu from xml resource

                                    popupMenu.inflate(R.menu.order_menu);

                                    //adding  a click listener

                                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                        @Override
                                        public boolean onMenuItemClick(MenuItem item) {
                                            if(item.getItemId() == R.id.cancel_order){
                                                showAlertDialog(orderDetail);
                                                return true;
                                            }
                                            return false;
                                        }
                                    });

                                    //displaying the popup
                                    popupMenu.show();
                                }
                            });



                            Log.d(TAG, "onResponse: type "+orderDetail.getType());

                            if(orderDetail.getType().equals("product")||orderDetail.getType().equals("service")) {
                                house_no.setText(orderDetail.getHouseNo());
                                apartment_name.setText(orderDetail.getApartmentName());
                                street_details.setText(orderDetail.getStreetAddress());
                                landmark.setText(orderDetail.getLandmark());
                                city_details.setText(orderDetail.getState() + ", " + orderDetail.getCity() + "- " + orderDetail.getPincode());
                                //when location was updated by google place picker

                                if (isEmpty(orderDetail.getHouseNo().trim()) && isEmpty(orderDetail.getApartmentName().trim())) {
                                    //if location was updated from placee picker
                                    house_no.setVisibility(View.GONE);
                                    landmark.setVisibility(View.GONE);
                                    street_details.setVisibility(View.GONE);
                                    city_details.setVisibility(View.GONE);
                                    apartment_name.setText(orderDetail.getAddress());
                                }

                            }

                            SimpleDateFormat format1 = new SimpleDateFormat("dd-MMM-yyyy HH:mm a");
                            SimpleDateFormat format2 = new SimpleDateFormat("EE, d MMM yyyy hh:mm a");
                            Date date = null;
                            try {
                                date = format1.parse(orderDetail.getTxnDate());
                            } catch (ParseException e) {
                                Log.d(TAG, "onResponse: "+e.toString());
                            }


                            order_date.setText(""+format2.format(date));
                            order_amount.setText(getResources().getString(R.string.rupee_icon)+" "+orderDetail.getTxnAmount());
                            payment_method.setText(orderDetail.getPaymentMethod());
                            order_id.setText("#"+parentOrder.getOrderId());

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

    private void showAlertDialog(final Datum datum) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(OrderDetailActivity.this);
        alertDialog.setTitle("Cancel Item");
        alertDialog.setMessage("Are you sure to cancel this item from your order ?");
//        alertDialog.setIcon(R.drawable.ic_logout);
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
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
