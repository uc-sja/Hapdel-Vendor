package com.utility.hapdelvendor.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.utility.hapdelvendor.Model.CategoryModel.ParentCategoryModel.Datum;
import com.utility.hapdelvendor.Model.ResponseModel.ResponseModel;
import com.utility.hapdelvendor.OtpVerificationActivity;
import com.utility.hapdelvendor.R;
import com.utility.hapdelvendor.Utils.Common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.text.TextUtils.isEmpty;
import static com.utility.hapdelvendor.Utils.Common.getApiInstance;

public class SignUpActivity extends AppCompatActivity {

    Toolbar toolbar;
    private static final String TAG = "SignInActivity";
    private Button proceed, back;
    private EditText email_edit, phone_edit, full_name_edit, password_edit, confirm_password;
    TextView full_name_text;
    TextView continue_txt;
    ProgressBar toolbar_progress_bar;
    private LinearLayout bottom_bar_layout;
    private SlidingUpPanelLayout sliding_layout;
    private ImageView slider_img;
    private TextView slider_msg;
    private Button slider_one_btn, slider_two_btn;
    private Toolbar bottom_toolbar;
    private Datum parentCategory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        setContentView(R.layout.activity_sign_up);


        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.colorPrimaryGreen));
            Common.setStatusColor(SignUpActivity.this, R.color.colorPrimaryGreen);
        }


        if(getIntent()!=null){
            parentCategory = new Gson().fromJson(getIntent().getStringExtra("category"), Datum.class);
        }


        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        email_edit = findViewById(R.id.email_edit);
        phone_edit = findViewById(R.id.phone_edit);
        full_name_edit = findViewById(R.id.full_name_edit);
        password_edit = findViewById(R.id.password_edit);
        confirm_password = findViewById(R.id.confirm_password_edit);

//        bottom_toolbar = findViewById(R.id.bottom_toolbar);
//
//        toolbar_progress_bar = findViewById(R.id.toolbar_progress_bar);
        continue_txt = findViewById(R.id.continue_txt);


        sliding_layout = findViewById(R.id.sliding_layout);
        slider_img = findViewById(R.id.slider_img);
        slider_msg = findViewById(R.id.slider_msg);

        slider_one_btn = findViewById(R.id.slider_btn_one);
        slider_two_btn = findViewById(R.id.slider_btn_two);


        final String code = "+91";

        phone_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(phone_edit.getText().toString().length()==1){
                    phone_edit.setText(code+" "+s);
                    Selection.setSelection(phone_edit.getText(), phone_edit.getText().length());
                    return;
                }

                if(!s.toString().startsWith(code+" ")){
                    phone_edit.setText(code+" ");
                    Selection.setSelection(phone_edit.getText(), phone_edit.getText().length());
                }
            }
        });

        continue_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getValidationResult()){
                    startRegistration();
                } else {
                    hideProgress();
                }
            }
        });

    }

    private void startRegistration() {

        String phoneWithCode  = phone_edit.getText().toString();
        String phone = phoneWithCode.substring(4);

        final ProgressDialog progressDialog = new ProgressDialog(SignUpActivity.this, R.style.MyDialogTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Processing Registration...");
        progressDialog.setCancelable(false);
        if(!((Activity)this).isFinishing()){
            progressDialog.show();
        }


        final String mobileNumber = phone_edit.getText().toString().substring(3);


        Log.d(TAG, "login: "+mobileNumber);
        // TODO: Implement your own authentication logic here.

        Call<ResponseModel> loginResponseCall = getApiInstance().signUpUser(full_name_edit.getText().toString(), email_edit.getText().toString(),phone,password_edit.getText().toString());
        showProgress();
        loginResponseCall.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                hideProgress();
                progressDialog.dismiss();
                if(!response.isSuccessful()){
                    Toast.makeText(SignUpActivity.this, ""+response.message(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onResponse: fail "+response.code());
                    return;
                }

                Log.d(TAG, "onResponse: success"+response.code()+response.body());
                if(response.body()!=null ){
                    ResponseModel responseModel = null;
                    try {
                        responseModel = response.body();
                    } catch (Exception e) {
                        Toast.makeText(SignUpActivity.this, "Error in response", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String content="";
                    Log.d(TAG, "onResponse: response msg"+response.body().getResult()+"  msg  ");
                    if (responseModel.getResult().equals("success")){ //very important conditon
                        Toast.makeText(SignUpActivity.this, ""+responseModel.getMsg(), Toast.LENGTH_SHORT).show();
                        fetchOtpforMobile(mobileNumber);
                    }else{
                        content+= responseModel.getMsg();

                        showSliderLayout(content);
                    }

                    Log.d(TAG, "onResponse: login res"+content);
                } else {
                    Toast.makeText(SignUpActivity.this, "Invalid response from server", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                hideProgress();
                progressDialog.dismiss();
                Toast.makeText(SignUpActivity.this, ""+t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }


    private void fetchOtpforMobile(final String mobileNumber) {
        final ProgressDialog progressDialog = new ProgressDialog(SignUpActivity.this, R.style.MyDialogTheme);
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
                    Toast.makeText(SignUpActivity.this, ""+response.message(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onResponse: fail "+response.code());
                    return;
                }

                Log.d(TAG, "onResponse: success"+response.code()+response.body());
                if(response.body()!=null ){
                    ResponseModel responseModel = null;
                    try {
                        responseModel = response.body();
                    } catch (Exception e) {
                        Toast.makeText(SignUpActivity.this, "Error in response", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String content="";
                    Log.d(TAG, "onResponse: response msg"+response.body().getResult()+"  msg  ");
                    if (responseModel.getResult().equals("success")){ //very important conditon
                        Log.d(TAG, "onResponse: success");
                        Intent intent = new Intent(SignUpActivity.this, OtpVerificationActivity.class);
                        intent.putExtra("mobile", mobileNumber);
                        intent.putExtra("category", new Gson().toJson(parentCategory));
                        startActivity(intent);
                    }else{
                        content+= responseModel.getMsg();
                        showSliderLayout(content);
                    }

                    Log.d(TAG, "onResponse: login res"+content);
                } else {
                    Toast.makeText(SignUpActivity.this, "Invalid response from server", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(SignUpActivity.this, ""+t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }


    private void hideProgress() {
//        continue_txt.setVisibility(View.VISIBLE);
//        toolbar_progress_bar.setVisibility(View.GONE);
    }

    private void showProgress() {
//        continue_txt.setVisibility(View.GONE);
//        toolbar_progress_bar.setVisibility(View.VISIBLE);
    }


    private boolean getValidationResult() {
        if(isEmpty(full_name_edit.getText().toString())){
            full_name_edit.setError("Enter full name");
            full_name_edit.requestFocus();
            return false ;
        }


        if(isEmpty(email_edit.getText().toString())){
            email_edit.setError("Enter email edit");
            email_edit.requestFocus();
            return false;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email_edit.getText().toString()).matches()){
            email_edit.setError("Enter a valid email address");
            email_edit.requestFocus();
            return false;
        }

        if(isEmpty(phone_edit.getText().toString())){
            phone_edit.setError("Enter phone number");
            phone_edit.requestFocus();
            return false;
        }

        String phoneWithCode  = phone_edit.getText().toString();
        String phone = phoneWithCode.substring(4);

        Log.d(TAG, "getValidationResult: "+phone);
        if(phone.length()!=10 || !android.util.Patterns.PHONE.matcher(phone).matches()){
            phone_edit.setError("Enter a valid phone number");
            phone_edit.requestFocus();
            return false;
        }


        if(isEmpty(password_edit.getText().toString())){
            password_edit.setError("Password cannot be empty");
            password_edit
                    .requestFocus();
            return false;
        }


        String pattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
        Pattern pswd_pattern = Pattern.compile(pattern);
        Matcher matcher = pswd_pattern.matcher(password_edit.getText().toString());

//        ^                 # start-of-string
//        (?=.*[0-9])       # a digit must occur at least once
//        (?=.*[a-z])       # a lower case letter must occur at least once
//        (?=.*[A-Z])       # an upper case letter must occur at least once
//        (?=.*[@#$%^&+=])  # a special character must occur at least once
//        (?=\S+$)          # no whitespace allowed in the entire string
//        .{8,}             # anything, at least six places though
//        $                 # end-of-string

        Log.d(TAG, "getValidationResult: matcher "+matcher.matches());
//        if(!matcher.matches()){
//            password_edit.setError("Please enter a valid password min length 6 with atleast a number, lowercase, and a special character");
//            password_edit.requestFocus();
//            return false;
//        }

        if(isEmpty(confirm_password.getText().toString())){
            confirm_password.setError("Confirm pasword cannot be empty");
            confirm_password.requestFocus();
            return false;
        }

        if(!password_edit.getText().toString().equals(confirm_password.getText().toString())){
            confirm_password.setError("Password & confirm password do not match");
            confirm_password.requestFocus();
            return false;
        }
        return true;
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
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
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
