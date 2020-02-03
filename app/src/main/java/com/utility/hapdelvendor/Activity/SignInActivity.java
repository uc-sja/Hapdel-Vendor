package com.utility.hapdelvendor.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.utility.hapdelvendor.Model.CategoryModel.ParentCategoryModel.Datum;
import com.utility.hapdelvendor.Model.ResponseModel.ResponseModel;
import com.utility.hapdelvendor.OtpVerificationActivity;
import com.utility.hapdelvendor.R;
import com.utility.hapdelvendor.Utils.Common;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.utility.hapdelvendor.Utils.Common.getApiInstance;

public class SignInActivity extends AppCompatActivity {

    Toolbar toolbar;
    private RelativeLayout mobile_ver_layout;
    private OtpView enter_mobile_view;
    private static final String TAG = "SignInActivity";
    private Button proceed, back;
    private SlidingUpPanelLayout sliding_layout;
    private ImageView slider_img;
    private TextView slider_msg;
    private Button slider_one_btn, slider_two_btn;

    private Datum parentCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.colorPrimaryGreen));
            Common.setStatusColor(SignInActivity.this, R.color.colorPrimaryGreen);
        }


        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(getIntent()!=null){
            parentCategory = new Gson().fromJson(getIntent().getStringExtra("category"), Datum.class);
        }

        sliding_layout = findViewById(R.id.sliding_layout);
        slider_img = findViewById(R.id.slider_img);
        slider_msg = findViewById(R.id.slider_msg);

        slider_one_btn = findViewById(R.id.slider_btn_one);
        slider_two_btn = findViewById(R.id.slider_btn_two);

        //we divide by 21 because the total count of dashes + spaces in a 10 digit layout is 21
        enter_mobile_view = findViewById(R.id.enter_mobile_view);
        mobile_ver_layout = findViewById(R.id.mobile_ver_layout);

        enter_mobile_view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                enter_mobile_view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                Log.d(TAG, "onGlobalLayout: "+enter_mobile_view.getWidth());
                Log.d(TAG, "onGlobalLayout otpitemwidth: "+enter_mobile_view.getItemWidth());
                enter_mobile_view.setItemWidth(mobile_ver_layout.getWidth()/20);
                enter_mobile_view.setItemSpacing(mobile_ver_layout.getWidth()/25);

            }
        });


        enter_mobile_view.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override
            public void onOtpCompleted(String otp) {
                validateMobile();
            }
        });

        proceed = findViewById(R.id.proceed);
        back = findViewById(R.id.back);

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateMobile();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void validateMobile() {
        String user_input = enter_mobile_view.getText().toString();
        if(user_input.length()==10 && Patterns.PHONE.matcher(user_input).matches()){
            fetchOtpforMobile(user_input);
        } else {
            Toast.makeText(this, "Invalid Mobile Number", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchOtpforMobile(final String mobileNumber) {
        final ProgressDialog progressDialog = new ProgressDialog(SignInActivity.this, R.style.MyDialogTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.setCancelable(false);
        if(!((Activity)this).isFinishing()){
            progressDialog.show();
        }

        Log.d(TAG, "login: "+mobileNumber);
        // TODO: Implement your own authentication logic here.

        Call<ResponseModel> loginResponseCall = getApiInstance().loginUser(mobileNumber);
        loginResponseCall.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                progressDialog.dismiss();
                if(!response.isSuccessful()){
                    Toast.makeText(SignInActivity.this, ""+response.message(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onResponse: fail "+response.code());
                    return;
                }

                Log.d(TAG, "onResponse: success"+response.code()+response.body());
                if(response.body()!=null ){
                    ResponseModel responseModel = null;
                    try {
                        responseModel = response.body();
                    } catch (Exception e) {
                        Toast.makeText(SignInActivity.this, "Error in response", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String content="";
                    Log.d(TAG, "onResponse: response msg"+response.body().getResult()+"  msg  ");
                    if (responseModel.getResult().equals("success")){ //very important conditon
                        Log.d(TAG, "onResponse: success");
                        Intent intent = new Intent(SignInActivity.this, OtpVerificationActivity.class);
                        intent.putExtra("mobile", mobileNumber);
                        intent.putExtra("category", new Gson().toJson(parentCategory));
                        startActivity(intent);
                    }else{
                        content+= responseModel.getMsg();
                        showSliderLayout(content);
                    }

                    Log.d(TAG, "onResponse: login res"+content);
                } else {
                    Toast.makeText(SignInActivity.this, "Invalid response from server", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(SignInActivity.this, ""+t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }


    public void showSliderLayout(String msg){
        Common.hideKeyboard(this);
        slider_msg.setText(msg);
        sliding_layout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
        slider_one_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sliding_layout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });
        slider_two_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
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
