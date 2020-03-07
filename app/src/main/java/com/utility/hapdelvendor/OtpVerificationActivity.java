package com.utility.hapdelvendor;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;
import com.utility.hapdelvendor.Activity.MainActivity;
import com.utility.hapdelvendor.Activity.OpenProductActivity;
import com.utility.hapdelvendor.Model.CategoryModel.ParentCategoryModel.Datum;
import com.utility.hapdelvendor.Model.LoginModel.UserModel;
import com.utility.hapdelvendor.Model.ResponseModel.ResponseModel;
import com.utility.hapdelvendor.Utils.Common;
import com.utility.hapdelvendor.Utils.LocalStorage;

import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.text.TextUtils.isEmpty;
import static com.utility.hapdelvendor.Utils.Common.getApiInstance;
import static com.utility.hapdelvendor.Utils.Common.hideKeyboard;

public class OtpVerificationActivity extends AppCompatActivity {


    private static final int SMS_CONSENT_REQUEST = 221;
    Toolbar toolbar;
    private RelativeLayout mobile_ver_layout;
    private OtpView enter_mobile_view;
    private static final String TAG = "SignInActivity";
    private Button resend, back;
    private String mobileNumber;

    private Datum parentCategory;

    private final BroadcastReceiver smsVerificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.getAction())) {
                Bundle extras = intent.getExtras();
                Status smsRetrieverStatus = (Status) extras.get(SmsRetriever.EXTRA_STATUS);

                switch (smsRetrieverStatus.getStatusCode()) {
                    case CommonStatusCodes.SUCCESS:
                        // Get consent intent
                        Intent consentIntent = extras.getParcelable(SmsRetriever.EXTRA_CONSENT_INTENT);
                        try {
                            // Start activity to show consent dialog to user, activity must be started in
                            // 5 minutes, otherwise you'll receive another TIMEOUT intent
                            startActivityForResult(consentIntent, SMS_CONSENT_REQUEST);
                        } catch (ActivityNotFoundException e) {
                            Log.d(TAG, "onReceive: "+e.getLocalizedMessage());
                            // Handle the exception ...
                        }
                        break;
                    case CommonStatusCodes.TIMEOUT:
                        // Time out occurred, handle the error.
                        break;
                }
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case SMS_CONSENT_REQUEST:
                if(resultCode == RESULT_OK){
                    String message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE);
                    // Extract one-time code from the message and complete verification
                    // `sms` contains the entire text of the SMS message, so you will need
                    // to parse the string.
                    String oneTimeCode = parseOneTimeCode(message); // define this function
                    Log.d(TAG, "onActivityResult: oneTimeCode"+oneTimeCode);
                    validateOtp(oneTimeCode);

                    // send one time code to the server
                } else {
                    // Consent canceled, handle the error ...
                    Toast.makeText(this, "Permission denied to read sms", Toast.LENGTH_SHORT).show();
                }

        }
    }


    private String parseOneTimeCode(String message) {
        int startIndex = message.lastIndexOf("is ");
        return message.substring(startIndex+3, startIndex+9);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);

        IntentFilter intentFilter = new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION);
        registerReceiver(smsVerificationReceiver, intentFilter);
        Task<Void> task = SmsRetriever.getClient(OtpVerificationActivity.this).startSmsUserConsent(null);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(OtpVerificationActivity.this, R.color.colorPrimaryGreen));
            Common.setStatusColor(OtpVerificationActivity.this, R.color.colorPrimaryGreen);
        }

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        if(getIntent()!=null){
            parentCategory = new Gson().fromJson(getIntent().getStringExtra("category"), Datum.class);
        }


        //we divide by 21 because the total count of dashes + spaces in a 10 digit layout is 21
        enter_mobile_view = findViewById(R.id.enter_mobile_view);
        mobile_ver_layout = findViewById(R.id.mobile_ver_layout);

        enter_mobile_view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                enter_mobile_view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                Log.d(TAG, "onGlobalLayout: "+enter_mobile_view.getWidth());
                Log.d(TAG, "onGlobalLayout otpitemwidth: "+enter_mobile_view.getItemWidth());
                enter_mobile_view.setItemWidth(mobile_ver_layout.getWidth()/13);
                enter_mobile_view.setItemSpacing(mobile_ver_layout.getWidth()/13);

            }
        });

        enter_mobile_view.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override
            public void onOtpCompleted(String otp) {
                validateOtp(otp);
            }
        });

        resend= findViewById(R.id.back);
        back = findViewById(R.id.proceed);
        startTimer();

        if(getIntent() != null){
            mobileNumber = getIntent().getStringExtra("mobile");
        }
    }

    private void fetchOtpforMobile(final String mobileNumber) {
        final ProgressDialog progressDialog = new ProgressDialog(OtpVerificationActivity.this, R.style.MyDialogTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.setCancelable(false);
        if(!((Activity)OtpVerificationActivity.this).isFinishing()){
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
                    Toast.makeText(OtpVerificationActivity.this, ""+response.message(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onResponse: fail "+response.code());
                    return;
                }

                Log.d(TAG, "onResponse: success"+response.code()+response.body());
                if(response.body()!=null ){
                    ResponseModel responseModel = null;
                    try {
                        responseModel = response.body();
                    } catch (Exception e) {
                        Toast.makeText(OtpVerificationActivity.this, "Error in response", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String content="";
                    Log.d(TAG, "onResponse: response msg"+response.body().getResult()+"  msg  ");
                    if (responseModel.getResult().equals("success")){ //very important conditon
                        Log.d(TAG, "onResponse: success");
                        startTimer();

                    }else{
                        content+= responseModel.getMsg();
                        Toast.makeText(OtpVerificationActivity.this, content, Toast.LENGTH_SHORT).show();
                    }

                    Log.d(TAG, "onResponse: login res"+content);
                } else {
                    Toast.makeText(OtpVerificationActivity.this, "Invalid response from server", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(OtpVerificationActivity.this, ""+t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }


    private void validateOtp(String otp) {
        Log.d(TAG, "verifyOtp: "+enter_mobile_view.getEditableText()+ "  ");
        if(otp==null || isEmpty(otp)){
            Toast.makeText(OtpVerificationActivity.this, "Please enter a valid otp", Toast.LENGTH_SHORT).show();
            return;
        }

        if(mobileNumber == null){
            Toast.makeText(OtpVerificationActivity.this, "Receiving null as mobile number", Toast.LENGTH_SHORT).show();
            return;
        }
        final ProgressDialog progressDialog = new ProgressDialog(OtpVerificationActivity.this, R.style.MyDialogTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Authenticating...");
        if(!((Activity)OtpVerificationActivity.this).isFinishing()){
            progressDialog.show();
        }

        // TODO: Implement your own authentication logic here.

        Call<UserModel> userModelCall = getApiInstance().verifyOtp( otp, mobileNumber);
        userModelCall.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                progressDialog.dismiss();
                if(!response.isSuccessful()){
                    Toast.makeText(OtpVerificationActivity.this, ""+response.message(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onResponse: fail "+response.code());
                    return;
                }

                Log.d(TAG, "onResponse: success"+response.code()+response.body());
                if(response.body()!=null ){
                    UserModel userModel = null;
                    try {
                        userModel = response.body();
                    } catch (Exception e) {
                        Toast.makeText(OtpVerificationActivity.this, "Error in response", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String content="";
                    Log.d(TAG, "onResponse: response msg"+response.body().getResult()+"  msg  ");
                    if (userModel.getResult().equals("success")){ //very important conditon
                        Log.d(TAG, "onResponse: success"+content);
                        Toast.makeText(OtpVerificationActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                        onLoginSucess(userModel);
                    } else {
                        content+= userModel.getMsg();
                        enter_mobile_view.clearComposingText();
                        Log.d(TAG, "onResponse: invalid response"+content + "  ");
                        Toast.makeText(OtpVerificationActivity.this, ""+content, Toast.LENGTH_SHORT).show();
                    }
                    Log.d(TAG, "onResponse: login res"+content);
                } else {
                    Log.d(TAG, "onResponse: invalid response from server");
                    Toast.makeText(OtpVerificationActivity.this, "Invalid response from server", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.toString());
                progressDialog.dismiss();
                Toast.makeText(OtpVerificationActivity.this, ""+t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void onLoginSucess(UserModel userModel) {
        hideKeyboard(OtpVerificationActivity.this);
        Toast.makeText(OtpVerificationActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
        Intent intent = null;
        if(parentCategory!=null){
            intent = new Intent(OtpVerificationActivity.this, OpenProductActivity.class);
            intent.putExtra("category", new Gson().toJson(parentCategory));
        } else {
            intent = new Intent(OtpVerificationActivity.this, MainActivity.class);
        }

        Log.d(TAG, "onLoginSucess: "+userModel.getData().get(0).getEmail());
        LocalStorage.setUser(userModel);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
        startActivity(intent);
    }


    private void startTimer() {
        new CountDownTimer(30000, 1000) { // adjust the milli seconds here

            public void onTick(long millisUntilFinished) {
                resend.setEnabled(false);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    resend.setTextColor(getColor(R.color.darkGray));
                    resend.setBackground(getDrawable(R.drawable.disabled_btn));
                }
                resend.setText("Resend in "+String.format("%d Sec",
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished))+"");

            }

            public void onFinish() {
                resetResendBtn();
            }
        }.start();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void resetResendBtn() {
        resend.setText("Resend");
        resend.setEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            resend.setBackground(getDrawable(R.drawable.sign_in_btn));
            resend.setTextColor(getColor(R.color.colorWhite));
        }
        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchOtpforMobile(mobileNumber);
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();

    }
}
