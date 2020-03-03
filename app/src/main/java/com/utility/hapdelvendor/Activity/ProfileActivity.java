package com.utility.hapdelvendor.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.google.android.material.textfield.TextInputEditText;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;
import com.utility.hapdelvendor.Model.LoginModel.Datum;
import com.utility.hapdelvendor.Model.LoginModel.UserModel;
import com.utility.hapdelvendor.Model.ProfileModel.UserDetailModel;
import com.utility.hapdelvendor.Model.ResponseModel.ResponseModel;
import com.utility.hapdelvendor.R;
import com.utility.hapdelvendor.Utils.BottomNavigation;
import com.utility.hapdelvendor.Utils.CircularTextView;
import com.utility.hapdelvendor.Utils.Common;
import com.utility.hapdelvendor.Utils.LocalStorage;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.text.TextUtils.isEmpty;
import static com.utility.hapdelvendor.Activity.NotificationActivity.setInitialNotification;
import static com.utility.hapdelvendor.Utils.Common.getApiInstance;
import static com.utility.hapdelvendor.Utils.Common.getCurrentUser;

public class ProfileActivity extends AppCompatActivity {
    private static final int CHANGE_VEHICAL_DOC_REQUEST = 1016;
    CircularTextView user_initial;
    CircleImageView user_pic;
    TextView logout;
    private static final String TAG = "ProfileActivity";

    private TextView error_msg;
    private RelativeLayout profile_layout;
    private RelativeLayout error_msg_layout;
    private TextView user_mail, user_name, user_phone;

    private RelativeLayout log_btn_layout;

    private LinearLayout user_details_layout;
    private TextView address_name, brief_address, change_address_btn;

    private ImageView edit_default_details;

    private TextView my_orders, my_transactions;

    //Sliding PanelLayout
    private SlidingUpPanelLayout sliding_layout;
    private ImageView slider_img;
    private TextView slider_msg;
    private TextInputEditText full_name_edit, contact_edit, email_id_edit, store_address_edit, store_name_edit;
    private Toolbar toolbar1;
    private TextView save_here_btn, upload_docs;
    private Button slider_one_btn,slider_two_btn;
    private LinearLayout default_address_layout;
    private AHBottomNavigation bottomNavigation;
    private com.utility.hapdelvendor.Model.ProfileModel.Datum currentProifile;
    private TextView all_products;


    @Override
    public void onBackPressed() {
        if(sliding_layout.getPanelState().equals(SlidingUpPanelLayout.PanelState.EXPANDED)){
            sliding_layout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            return;
        }
        super.onBackPressed();
        Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
            Common.setStatusColor(ProfileActivity.this, R.color.colorPrimary);
        }

        bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);
        BottomNavigation.initializeBottomNavigation(bottomNavigation, ProfileActivity.this);
        bottomNavigation.setCurrentItem(2, false);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");


        user_initial = findViewById(R.id.user_initial);
        user_pic = findViewById(R.id.user_pic);

        user_initial.setSolidColor("#ffffff");

        user_mail = findViewById(R.id.user_email);
        user_phone = findViewById(R.id.user_phone);
        user_name = findViewById(R.id.user_name);

        my_orders = findViewById(R.id.my_orders);
        my_transactions = findViewById(R.id.my_transactions);
        upload_docs = findViewById(R.id.upload_docs);
        logout = findViewById(R.id.logout);

        change_address_btn = findViewById(R.id.change_address_btn);

        profile_layout = findViewById(R.id.profile_layout);
        error_msg_layout = findViewById(R.id.error_layout);
        error_msg = findViewById(R.id.error_msg);
        slider_one_btn = findViewById(R.id.slider_btn_one);
        slider_two_btn = findViewById(R.id.slider_btn_two);
        log_btn_layout = findViewById(R.id.log_btn_layout);

        edit_default_details = findViewById(R.id.edit_default_details);

        address_name = findViewById(R.id.address_name);
        brief_address = findViewById(R.id.brief_address);
        default_address_layout = findViewById(R.id.default_address_layout);

        user_details_layout = findViewById(R.id.user_details_layout);

        if(getCurrentUser()==null || getCurrentUser().getId()==null){
            showErrorMessage("Please Login to see your profile", "login");
            return;
        } else {
            hideErrorMessage();
            user_details_layout.setVisibility(View.VISIBLE);
        }
        
        all_products = findViewById(R.id.all_products);

        all_products.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, AllProducts.class);
                startActivity(intent);
            }
        });


        my_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, OrderActivity.class);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog();
            }
        });

        sliding_layout = findViewById(R.id.sliding_layout);

        edit_default_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSliderLayout();
            }
        });

        my_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, OrderActivity.class);
                startActivity(intent);
            }
        });

        my_transactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, TransactionActivity.class);
                startActivity(intent);
            }
        });

        upload_docs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, UploadDocActivity.class);
                startActivity(intent);
            }
        });

        fetchUserDetails();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setInitialNotification(bottomNavigation);
    }

    private void fetchUserDetails() {
        final ProgressDialog progressDialog = new ProgressDialog(ProfileActivity.this, R.style.MyDialogTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Fetching user details...");
        progressDialog.setCancelable(false);
        if(!((Activity)ProfileActivity.this).isFinishing()){
            progressDialog.show();
        }


        Call<UserDetailModel> userDetailModelCall = getApiInstance().fetchUserDetails(getCurrentUser().getId(), getCurrentUser().getAccessToken());
        userDetailModelCall.enqueue(new Callback<UserDetailModel>() {
            @Override
            public void onResponse(Call<UserDetailModel> call, Response<UserDetailModel> response) {
                progressDialog.dismiss();
                if(!response.isSuccessful()){
                    Toast.makeText(ProfileActivity.this, ""+response.message(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onResponse: fail "+response.code());
                    return;
                }

                Log.d(TAG, "onResponse: success"+response.code()+response.body());
                if(response.body()!=null ){
                    UserDetailModel userDetailModel = null;
                    try {

                        userDetailModel = response.body();
                    } catch (Exception e) {
                        Toast.makeText(ProfileActivity.this, "Error in response", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String content="";
                    if (userDetailModel.getResult().equals("success")){
                        //very important conditon
                        if(userDetailModel.getData()!=null && userDetailModel.getData().size()>0){
                            currentProifile = userDetailModel.getData().get(0);
                            setUserDetails(currentProifile);
                        } else {
                            Toast.makeText(ProfileActivity.this, "Empty Response from server", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        content+= userDetailModel.getMsg();
                        if(content.equalsIgnoreCase("invalid access token")){
                            Toast.makeText(ProfileActivity.this, "Session Expired. Logging you out...", Toast.LENGTH_SHORT).show();
                            logoutFromDevice();
                        } else {
                            showErrorMessage(content, "server");
                        }
                    }

                    Log.d(TAG, "onResponse: fetch User details address res"+content);
                } else {
                    showErrorMessage("Invalid response from server", "con");
                }
            }

            @Override
            public void onFailure(Call<UserDetailModel> call, Throwable t) {
                progressDialog.dismiss();
                showErrorMessage("No connection found", "con");
            }
        });
    }

    private void setUserDetails(com.utility.hapdelvendor.Model.ProfileModel.Datum profileDatum) {
        user_name.setText(profileDatum.getDisplayName());
        user_phone.setText(profileDatum.getMobile());
        user_mail.setText(profileDatum.getEmail());
        if(profileDatum.getProfile()!=null && !isEmpty(profileDatum.getProfile())){
            user_pic.setVisibility(View.VISIBLE);
            user_initial.setVisibility(View.GONE);
            Picasso.get().load(profileDatum.getProfile()).fit().placeholder(R.drawable.ic_person_black_24dp).into(user_pic);
        } else {
            user_pic.setVisibility(View.GONE);
            user_initial.setVisibility(View.VISIBLE);
            user_initial.setText(profileDatum.getDisplayName().charAt(0)+"");

        }
        if (profileDatum.getStoreAddress()!=null){
            address_name.setText("Store: "+profileDatum.getStoreName());
            brief_address.setVisibility(View.VISIBLE);
            brief_address.setText("Address : "+ profileDatum.getStoreAddress());
            change_address_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showSliderLayout();
                }
            });
        } else {
            default_address_layout.setVisibility(View.GONE);
        }
    }

    public void showSliderLayout(){
        Common.hideKeyboard(this);
        LinearLayout update_profile_layout = findViewById(R.id.update_profile_layout);

        toolbar1 = findViewById(R.id.toolbar1);
        full_name_edit = findViewById(R.id.full_name);
        contact_edit = findViewById(R.id.contact_number);
        email_id_edit = findViewById(R.id.email_id);
        store_address_edit = findViewById(R.id.store_address_edit);
        store_name_edit = findViewById(R.id.store_name_edit);
        save_here_btn = findViewById(R.id.save_here_btn);

        setSupportActionBar(toolbar1);
        getSupportActionBar().setTitle("Update Profile");

        full_name_edit.setText(getCurrentUser().getDisplayName());
        contact_edit.setText(getCurrentUser().getMobile());
        email_id_edit.setText(getCurrentUser().getEmail());
        if(currentProifile != null){
            store_address_edit.setText(currentProifile.getStoreAddress());
            store_name_edit.setText(currentProifile.getStoreName());
        }
        sliding_layout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);

        update_profile_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(performValidation((ViewGroup) v)){
                    updateProfile(full_name_edit.getText().toString(), contact_edit.getText().toString(), email_id_edit.getText().toString(), store_name_edit.getText().toString(), store_address_edit.getText().toString());
                }
            }
        });
    }

    private void updateProfile(final String name, final String mobile, final String email, String store_name, String store_address) {
        final ProgressDialog progressDialog = new ProgressDialog(ProfileActivity.this, R.style.MyDialogTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Updating Profile...");
        progressDialog.setCancelable(false);
        if(!((Activity)ProfileActivity.this).isFinishing()){
            progressDialog.show();
        }

        Call<ResponseModel> responseModelCall = getApiInstance().updateProfile(getCurrentUser().getId(), getCurrentUser().getAccessToken(), email, mobile, name, store_name, store_address);
        responseModelCall.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                progressDialog.dismiss();
                if(!response.isSuccessful()){
                    Toast.makeText(ProfileActivity.this, ""+response.message(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onResponse: fail "+response.code());
                    return;
                }

                Log.d(TAG, "onResponse: success"+response.code()+response.body());
                if(response.body()!=null ){
                    ResponseModel responseModel = null;
                    try {
                        responseModel = response.body();
                    } catch (Exception e) {
                        Toast.makeText(ProfileActivity.this, "Error in response", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String content="";
                    Log.d(TAG, "onResponse: response msg"+response.body().getResult()+"  msg  ");
                    if (responseModel.getResult().equals("success")){ //very important conditon
                        Log.d(TAG, "onResponse: success");


                        UserModel userModel = LocalStorage.getUser();
                        List<Datum> userModelList = userModel.getData();
                        com.utility.hapdelvendor.Model.LoginModel.Datum datum = userModelList.get(0);
                        datum.setEmail(email);
                        datum.setDisplayName(name);
                        datum.setMobile(mobile);

                        Log.d(TAG, "onResponse: "+getCurrentUser().getDisplayName());
                        LocalStorage.setUser(userModel);

                        com.utility.hapdelvendor.Model.ProfileModel.Datum profileDatum = new com.utility.hapdelvendor.Model.ProfileModel.Datum();
                        profileDatum.setEmail(email);
                        profileDatum.setDisplayName(name);
                        profileDatum.setMobile(mobile);
                        profileDatum.setStoreAddress(store_address);
                        profileDatum.setStoreName(store_name);
                        setUserDetails(profileDatum);
                        Toast.makeText(ProfileActivity.this, "Update Profile Success", Toast.LENGTH_SHORT).show();
//                            hideKeyboardFrom(ProfileActivity.this);

                        if(sliding_layout.getPanelState().equals(SlidingUpPanelLayout.PanelState.EXPANDED)){
                            sliding_layout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                        }

                    }else{
                        content+= responseModel.getMsg();
                        Toast.makeText(ProfileActivity.this, content, Toast.LENGTH_SHORT).show();
                    }

                    Log.d(TAG, "onResponse: login res"+content);
                } else {
                    Toast.makeText(ProfileActivity.this, "Invalid response from server", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ProfileActivity.this, ""+t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Boolean performValidation(ViewGroup viewGroup) {
        ArrayList<TextInputEditText> textInputEditTexts = Common.findTextInputEditTexts(viewGroup, null);
        for(TextInputEditText textInputEditText: textInputEditTexts){
            if(isEmpty(textInputEditText.getText().toString()) && textInputEditText.getVisibility()==View.VISIBLE){
                Log.d(TAG, "performValidation: "+textInputEditText.getVisibility());
                textInputEditText.setError(textInputEditText.getHint()+" cannot be empty");
                textInputEditText.requestFocus();
                return false;
            }

            if(textInputEditText.getId()== R.id.contact_number && !Patterns.PHONE.matcher(textInputEditText.getText().toString()).matches()){
                textInputEditText.setError(textInputEditText.getHint()+" not valid");
                textInputEditText.requestFocus();
                return false;
            }

            if(textInputEditText.getId()== R.id.email_id && !Patterns.EMAIL_ADDRESS.matcher(textInputEditText.getText().toString()).matches()){
                textInputEditText.setError(textInputEditText.getHint()+" not valid");
                textInputEditText.requestFocus();
                return false;
            }
        }
        return true;
    }


    private void showAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Confirm Logout");
        alertDialog.setMessage("Are you sure to logout from the app?");
//        alertDialog.setIcon(R.drawable.ic_logout);
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                    logoutUser();
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




    private void logoutUser() {
        final ProgressDialog progressDialog = new ProgressDialog(this, R.style.MyDialogTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Logging Out...");

        Log.d(TAG, "logoutUser: "+(getCurrentUser().getId()+"   "+ getCurrentUser().getAccessToken()));
        if(!((Activity)this).isFinishing()){
            progressDialog.show();
        }
        Call<ResponseModel> logoutResponseCall = getApiInstance().logoutUser(getCurrentUser().getId(), getCurrentUser().getAccessToken());
        logoutResponseCall.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                progressDialog.dismiss();
                if(!response.isSuccessful()){
                    Toast.makeText(ProfileActivity.this, ""+response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }
                if(response.body()!=null){

                    ResponseModel responseModel = null;
                    try {
                        responseModel = response.body();
                    } catch (Exception e) {
                        Toast.makeText(ProfileActivity.this, "Error in response", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(responseModel.getResult().equals("success")){
                        logoutFromDevice();
                    } else {
                        Toast.makeText(ProfileActivity.this, ""+responseModel.getMsg(), Toast.LENGTH_SHORT).show();
                        logoutFromDevice();
                    }
                } else {
                    Toast.makeText(ProfileActivity.this, "Invalid Response from Server", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.toString());
                progressDialog.dismiss();
                Toast.makeText(ProfileActivity.this, ""+t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void logoutFromDevice() {
        LocalStorage.setUser(null);
        Intent intent = new Intent(ProfileActivity.this, SignInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(intent);
    }

    private void showErrorMessage(String message, String warning_type) {
        error_msg_layout.setVisibility(View.VISIBLE);
        error_msg.setText(message);
        profile_layout.setVisibility(View.GONE);

        if(warning_type.equalsIgnoreCase("login")){
            log_btn_layout.setVisibility(View.VISIBLE);
        } else if(warning_type.equalsIgnoreCase("con")){
            log_btn_layout.setVisibility(View.GONE);
        } else {

            error_msg_layout.setVisibility(View.GONE);
            profile_layout.setVisibility(View.VISIBLE);
            log_btn_layout.setVisibility(View.GONE);
        }

        slider_one_btn.setText("Register");

        slider_two_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, SignInActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        slider_two_btn.setText("Login Now");
    }

    private void hideErrorMessage() {
        error_msg_layout.setVisibility(View.GONE);
        profile_layout.setVisibility(View.VISIBLE);
    }


}
