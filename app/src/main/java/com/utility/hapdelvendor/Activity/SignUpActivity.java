package com.utility.hapdelvendor.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.google.gson.Gson;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;
import com.utility.hapdelvendor.Model.CategoryModel.ParentCategoryModel.Datum;
import com.utility.hapdelvendor.Model.ResponseModel.ResponseModel;
import com.utility.hapdelvendor.OtpVerificationActivity;
import com.utility.hapdelvendor.R;
import com.utility.hapdelvendor.Utils.Common;
import com.utility.hapdelvendor.Utils.FileUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.text.TextUtils.isEmpty;
import static com.utility.hapdelvendor.Utils.Common.getApiInstance;

public class SignUpActivity extends AppCompatActivity {

    Toolbar toolbar;
    private static final String TAG = "SignInActivity";
    private Button proceed, back;
    private EditText email_edit, phone_edit, full_name_edit, password_edit, confirm_password, store_name_edit, store_address_edit;
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
    private TextView doc_one, store_pic, doc_two, doc_three, doc_four, doc_five;
    private ImageView store_pic_img, doc_one_img, doc_two_img, doc_three_img, doc_four_img, doc_five_img;

    List<MultipartBody.Part> parts;
    private static HashMap<String, Uri> imgUrisList;
    private Bitmap bitmap;
    private File f;
    private Uri mImageUri;
    private String current_doc_type;
    
    private static final int CAM_PICTURE = 1122, GALLERY_PICTURE = 2223;
    private static final int CAMERA_PERMISSIOM_CODE = 22331, FILE_PERMISSION_CODE = 9494;
    private ProgressDialog progressDialog;
    private boolean logo_updated = false;


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
        store_name_edit = findViewById(R.id.store_name_edit);
        store_address_edit = findViewById(R.id.store_address_edit);

        store_pic = findViewById(R.id.store_pic);
        doc_one = findViewById(R.id.doc_one);
        doc_two = findViewById(R.id.doc_two);
        doc_three = findViewById(R.id.doc_three);
        doc_four = findViewById(R.id.doc_four);
        doc_five = findViewById(R.id.doc_five);

        store_pic_img = findViewById(R.id.seller_logo);
        doc_one_img = findViewById(R.id.document_one_img);
        doc_two_img = findViewById(R.id.document_two_img);
        doc_three_img = findViewById(R.id.document_three_img);
        doc_four_img = findViewById(R.id.document_four_img);
        doc_five_img = findViewById(R.id.document_five_img);

//        store_pic.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                openFileChooser("store_pic");
//            }
//        });

        store_pic_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser("store_logo");

            }
        });

        doc_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    openFileChooser("document_1");
            }
        });

        doc_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openFileChooser("document_2");
            }
        });

        doc_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openFileChooser("document_3");
            }
        });

        doc_four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openFileChooser("document_4");
            }
        });

        doc_five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openFileChooser("document_5");
            }
        });

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
                    performUploadTask();
            }
        });


        parts = new ArrayList<>();
        imgUrisList = new HashMap<>();

    }


    private void openFileChooser(String doctype) {
        current_doc_type = doctype;
        startDialog();
    }


    private void startDialog() {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(
                this);
        myAlertDialog.setTitle("Upload Pictures Option");
        myAlertDialog.setMessage("How do you want to choose your document?");

        myAlertDialog.setPositiveButton("Gallery",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if ((ContextCompat.checkSelfPermission(SignUpActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) || (ContextCompat.checkSelfPermission(SignUpActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
                                ActivityCompat.requestPermissions((Activity) SignUpActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, FILE_PERMISSION_CODE);
                                return;
                            }
                        }
                        startGalleryIntent();
                    }
                });

        myAlertDialog.setNegativeButton("Camera",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if ((ContextCompat.checkSelfPermission(SignUpActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) || (ContextCompat.checkSelfPermission(SignUpActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) || (ContextCompat.checkSelfPermission(SignUpActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)) {
                                ActivityCompat.requestPermissions((Activity) SignUpActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, CAMERA_PERMISSIOM_CODE);
                                    return;
                            }
                            startCamertaIntent();
                        }
                    }
                });
        myAlertDialog.show();
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case FILE_PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startGalleryIntent();
                } else {
                    Toast.makeText(SignUpActivity.this, "Permission Denied to read your external storage", Toast.LENGTH_SHORT).show();
                }
                break;

            case CAMERA_PERMISSIOM_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startCamertaIntent();
                } else {
                    Toast.makeText(SignUpActivity.this, "Permission Denied to access your camera", Toast.LENGTH_SHORT).show();

                }
                break;

            default:
                break;
        }
    }

    private void startCamertaIntent() {
        Log.d(TAG, "startCamertaIntent: ");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photo;
        try {
            // place where to store camera taken picture
            photo = createTemporaryFile("picture", ".jpg");
            photo.delete();
        }
        catch(Exception e) {
            Log.d(TAG, "Can't create file to take picture! "+e.toString());
            Toast.makeText(SignUpActivity.this, "Please check SD card! Image shot is impossible!", Toast.LENGTH_SHORT);
            return;
        }

        mImageUri = FileProvider.getUriForFile(getApplicationContext(), "com.utility.hapdelvendor.fileprovider", photo);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
        startActivityForResult(intent, CAM_PICTURE);
    }

    private void startGalleryIntent() {
        Intent pictureActionIntent = null;
        pictureActionIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pictureActionIntent, GALLERY_PICTURE);
    }


//    private void startRegistration() {
//
//        String phoneWithCode  = phone_edit.getText().toString();
//        String phone = phoneWithCode.substring(4);
//
//        final ProgressDialog progressDialog = new ProgressDialog(SignUpActivity.this, R.style.MyDialogTheme);
//        progressDialog.setIndeterminate(true);
//        progressDialog.setMessage("Processing Registration...");
//        progressDialog.setCancelable(false);
//        if(!((Activity)this).isFinishing()){
//            progressDialog.show();
//        }
//
//
//        final String mobileNumber = phone_edit.getText().toString().substring(3);
//
//
//        Log.d(TAG, "login: "+mobileNumber);
//        // TODO: Implement your own authentication logic here.
//
//        Call<ResponseModel> loginResponseCall = getApiInstance().signUpUser(full_name_edit.getText().toString(), email_edit.getText().toString(),phone,password_edit.getText().toString());
//        showProgress();
//        loginResponseCall.enqueue(new Callback<ResponseModel>() {
//            @Override
//            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
//                hideProgress();
//                progressDialog.dismiss();
//                if(!response.isSuccessful()){
//                    Toast.makeText(SignUpActivity.this, ""+response.message(), Toast.LENGTH_SHORT).show();
//                    Log.d(TAG, "onResponse: fail "+response.code());
//                    return;
//                }
//
//                Log.d(TAG, "onResponse: success"+response.code()+response.body());
//                if(response.body()!=null ){
//                    ResponseModel responseModel = null;
//                    try {
//                        responseModel = response.body();
//                    } catch (Exception e) {
//                        Toast.makeText(SignUpActivity.this, "Error in response", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//                    String content="";
//                    Log.d(TAG, "onResponse: response msg"+response.body().getResult()+"  msg  ");
//                    if (responseModel.getResult().equals("success")){ //very important conditon
//                        Toast.makeText(SignUpActivity.this, ""+responseModel.getMsg(), Toast.LENGTH_SHORT).show();
//                        fetchOtpforMobile(mobileNumber);
//                    }else{
//                        content+= responseModel.getMsg();
//
//                        showSliderLayout(content);
//                    }
//
//                    Log.d(TAG, "onResponse: login res"+content);
//                } else {
//                    Toast.makeText(SignUpActivity.this, "Invalid response from server", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseModel> call, Throwable t) {
//                hideProgress();
//                progressDialog.dismiss();
//                Toast.makeText(SignUpActivity.this, ""+t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
//
//            }
//        });
//
//    }

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
                        finish();
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
            password_edit.requestFocus();
            return false;
        }


        if(isEmpty(store_name_edit.getText().toString())){
            store_name_edit.setError("Store name cannot be empty");
            store_name_edit.requestFocus();
            return false;
        }


        if(isEmpty(store_address_edit.getText().toString())){
            store_address_edit.setError("Store address cannot be empty");
            store_address_edit.requestFocus();
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

        if (!logo_updated) {
            Toast.makeText(this, "Kindly upload store logo", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (doc_one_img.getVisibility() != View.VISIBLE) {
            Toast.makeText(this, "Kindly upload document one", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (doc_two_img.getVisibility() != View.VISIBLE) {
            Toast.makeText(this, "Kindly upload document two", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (doc_three_img.getVisibility() != View.VISIBLE) {
            Toast.makeText(this, "Kindly upload document three", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (doc_four_img.getVisibility() != View.VISIBLE) {
            Toast.makeText(this, "Kindly upload document four", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (doc_five_img.getVisibility() != View.VISIBLE) {
            Toast.makeText(this, "Kindly upload document five", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private File createTemporaryFile(String part, String ext) throws Exception {
        File tempDir= Environment.getExternalStorageDirectory();
        tempDir=new File(tempDir.getAbsolutePath()+"/.temp/");
        if(!tempDir.exists()) {
            tempDir.mkdirs();
        }

        tempDir.deleteOnExit();
        return File.createTempFile(part, ext, tempDir);
    }

    public Uri grabImage() {
        this.getContentResolver().notifyChange(mImageUri, null);
        ContentResolver cr = this.getContentResolver();
        Bitmap bitmap = null;
        try {
            bitmap = android.provider.MediaStore.Images.Media.getBitmap(cr, mImageUri);
        }
        catch (Exception e) {
            Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Failed to load", e);
        }
        return getImageUri(SignUpActivity.this, bitmap);

    }

    private Uri getImageUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: " + requestCode + "  " + resultCode + "  " + data);
        if ((requestCode == GALLERY_PICTURE) && resultCode == RESULT_OK && data != null) {
            Log.d(TAG, "onActivityResult: camera selected ");
            Uri selectedImage = data.getData();
            imgUrisList.put(current_doc_type, selectedImage);
            changeBtnMessage(current_doc_type, selectedImage);
        } else if (requestCode == CAM_PICTURE && resultCode == RESULT_OK) {
            // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
            if(grabImage()!=null){
                Uri uri =grabImage();
                imgUrisList.put(current_doc_type, uri);
                changeBtnMessage(current_doc_type, uri);
            } else {
                Toast.makeText(this, "Some error in capturing file...please retry", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Some error. Please retry.", Toast.LENGTH_SHORT).show();
        }
    }


    private void changeBtnMessage(String current_doc_type, Uri selectedImage) {
        switch (current_doc_type) {
            case "store_logo":
                logo_updated = true;
                Picasso.get().load(selectedImage).fit().into(store_pic_img);
//                dl_back.setText("Change");
                break;
            case "document_1":
                doc_one_img.setVisibility(View.VISIBLE);
                Picasso.get().load(selectedImage).fit().into(doc_one_img);
//                dl_front .setText("Change");
                break;
            case "document_2":
                doc_two_img.setVisibility(View.VISIBLE);
                Picasso.get().load(selectedImage).fit().into(doc_two_img);
//                dl_front .setText("Change");
                break;
            case "document_3":
                doc_three_img.setVisibility(View.VISIBLE);
                Picasso.get().load(selectedImage).fit().into(doc_three_img);
//                rc_front.setText("Change");
                break;
            case "document_4":
                doc_four_img.setVisibility(View.VISIBLE);
                Picasso.get().load(selectedImage).fit().into(doc_four_img);
//                pan.setText("Change");
                break;
            case "document_5":
                doc_five_img.setVisibility(View.VISIBLE);
                Picasso.get().load(selectedImage).fit().into(doc_five_img);
                break;
        }
    }

    private void performUploadTask() {
        Log.d(TAG, "performUploadTask: ");
        HashMap<String, Uri> uriListClone = new HashMap<>();
        uriListClone.putAll(imgUrisList);
            if (uriListClone.size() > 0) {
                //By validation we have made sure that size of imgUrisList is always going to be6
            if (getValidationResult()) {
//                Iterator it = uriListClone.entrySet().iterator();
//                int i = 0;
//                while (it.hasNext()) {
//                    Map.Entry pair = (Map.Entry) it.next();
//                    uploadFile(pair.getKey().toString(), (Uri) pair.getValue(), ++i);
//                    it.remove(); // avoids a ConcurrentModificationException
                uploadFile(uriListClone);
//                }
            }
        } else {
            Toast.makeText(SignUpActivity.this, "Kindly upload documents", Toast.LENGTH_SHORT).show();
        }
    }



    private void uploadFile(HashMap<String, Uri> uriListClone) {
        // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
        // use the FileUtils to get the actual file by uri
        progressDialog = new ProgressDialog(SignUpActivity.this, R.style.MyDialogTheme);


        if (progressDialog != null) {
            progressDialog = new ProgressDialog(SignUpActivity.this);
        }
        progressDialog.setMessage("Uploading files...Please Wait");
        progressDialog.setIndeterminate(false);
        progressDialog.setProgress(0);
        if (!((Activity) SignUpActivity.this).isFinishing()) {
            progressDialog.show();
        }

        File store_logo_file = FileUtils.getFile(SignUpActivity.this, uriListClone.get("store_logo"));
        RequestBody logo_requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), store_logo_file);
        MultipartBody.Part body1 = MultipartBody.Part.createFormData("store_logo", store_logo_file.getName(), logo_requestFile);

        File document1_file = FileUtils.getFile(SignUpActivity.this, uriListClone.get("document_1"));
        RequestBody requestFile1 = RequestBody.create(MediaType.parse("multipart/form-data"), document1_file);
        MultipartBody.Part body2 = MultipartBody.Part.createFormData("document_1", store_logo_file.getName(), requestFile1);

        File document2_file = FileUtils.getFile(SignUpActivity.this, uriListClone.get("document_2"));
        RequestBody requestFile2 = RequestBody.create(MediaType.parse("multipart/form-data"), document2_file);
        MultipartBody.Part body3 = MultipartBody.Part.createFormData("document_2", store_logo_file.getName(), requestFile2);

        File document3_file = FileUtils.getFile(SignUpActivity.this, uriListClone.get("document_3"));
        RequestBody requestFile3 = RequestBody.create(MediaType.parse("multipart/form-data"), document3_file);
        MultipartBody.Part body4 = MultipartBody.Part.createFormData("document_3", store_logo_file.getName(), requestFile3);

        File document4_file = FileUtils.getFile(SignUpActivity.this, uriListClone.get("document_4"));
        RequestBody requestFile4 = RequestBody.create(MediaType.parse("multipart/form-data"), document4_file);
        MultipartBody.Part body5 = MultipartBody.Part.createFormData("document_4", store_logo_file.getName(), requestFile4);

        File document5_file = FileUtils.getFile(SignUpActivity.this, uriListClone.get("document_5"));
        RequestBody requestFile5 = RequestBody.create(MediaType.parse("multipart/form-data"), document5_file);
        MultipartBody.Part body6 = MultipartBody.Part.createFormData("document_5", store_logo_file.getName(), requestFile5);

//        // create RequestBody instance from file
//        ProgressRequestBody requestFile = new ProgressRequestBody(file, "image", new ProgressRequestBody.UploadCallbacks() {
//            @Override
//            public void onProgressUpdate(int percentage) {
//
//                if (progressDialog != null && progressDialog.isShowing()) {
//
//                    progressDialog.setProgress(percentage);
//                    Log.d(TAG, "onProgressUpdate: " + percentage);
//                }
//            }
//
//            @Override
//            public void onError() {
//
//            }
//
//            @Override
//            public void onFinish() {
//                progressDialog.setProgress(100);
//            }
//        });

        final String mobileNumber = phone_edit.getText().toString().substring(3);

        RequestBody full_name_edit_body = RequestBody.create(MediaType.parse("text/plain"), full_name_edit.getText().toString());
        RequestBody email_edit_body = RequestBody.create(MediaType.parse("text/plain"), email_edit.getText().toString());
        RequestBody phone_edit_body = RequestBody.create(MediaType.parse("text/plain"), mobileNumber);
        RequestBody password_edit_body = RequestBody.create(MediaType.parse("text/plain"), password_edit.getText().toString());
        RequestBody store_name_edit_body = RequestBody.create(MediaType.parse("text/plain"), store_name_edit.getText().toString());
        RequestBody store_address_edit_body = RequestBody.create(MediaType.parse("text/plain"), store_address_edit.getText().toString());

        // finally, execute the request
        Call<ResponseModel> call = getApiInstance().registration(body1, body2, body3, body4, body5, body6, full_name_edit_body, email_edit_body,  phone_edit_body, password_edit_body, store_name_edit_body, store_address_edit_body);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call,
                                   Response<ResponseModel> response) {
                progressDialog.dismiss();
                if (!response.isSuccessful()) {
                    Toast.makeText(SignUpActivity.this, "" + response.message(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onResponse: fail " + response.code());
                    return;
                }
                Log.d(TAG, "onResponse: success" + response.code() + response.body());
                if (response.body() != null) {
                    ResponseModel responseModel = null;
                    try {
                        responseModel = response.body();
                    } catch (Exception e) {
                        Toast.makeText(SignUpActivity.this, "Error in response", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String content = "";
                    Log.d(TAG, "onResponse: response msg" + response.body().getResult() + "  msg  ");
                    if (responseModel.getResult().equals("success")) {
                        //very important condition
                        Log.d(TAG, "onResponse: registration  success");
                        Toast.makeText(SignUpActivity.this, "" + responseModel.getMsg(), Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onResponse: success upload images");
                        finish();
                    } else {
                        content += responseModel.getMsg();
                        Log.d(TAG, "onResponse: invalid response" + content);
                        Toast.makeText(SignUpActivity.this, "" + content, Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Log.d(TAG, "onResponse: invalid response");
                    Toast.makeText(SignUpActivity.this, "Invalid response from server", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(SignUpActivity.this, "Upload error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("Upload error:", t.getMessage());
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
