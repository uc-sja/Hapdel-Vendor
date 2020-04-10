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
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
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

import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;
import com.utility.hapdelvendor.Model.CategoryModel.ParentCategoryModel.Datum;
import com.utility.hapdelvendor.Model.ResponseModel.ResponseModel;
import com.utility.hapdelvendor.Model.UploadDocModel.UploadDocModel;
import com.utility.hapdelvendor.R;
import com.utility.hapdelvendor.Utils.Common;
import com.utility.hapdelvendor.Utils.FileUtils;
import com.utility.hapdelvendor.Utils.ProgressRequestBody;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.text.TextUtils.isEmpty;
import static com.utility.hapdelvendor.Utils.Common.getApiInstance;
import static com.utility.hapdelvendor.Utils.Common.getCurrentUser;

public class UploadDocActivity extends AppCompatActivity {
    Toolbar toolbar;
    private static final String TAG = "SignInActivity";
    private Button proceed, back;
    TextView full_name_text;
    TextView submit_btn;
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

        setContentView(R.layout.activity_upload_doc);


        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.colorPrimaryGreen));
            Common.setStatusColor(UploadDocActivity.this, R.color.colorPrimaryGreen);
        }


        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


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
        submit_btn = findViewById(R.id.submit_text);
        sliding_layout = findViewById(R.id.sliding_layout);
        slider_img = findViewById(R.id.slider_img);
        slider_msg = findViewById(R.id.slider_msg);

        slider_one_btn = findViewById(R.id.slider_btn_one);
        slider_two_btn = findViewById(R.id.slider_btn_two);

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performUploadTask();
            }
        });

        parts = new ArrayList<>();
        imgUrisList = new HashMap<>();

        fetchUserDocs();
    }

    private void fetchUserDocs() {
        final ProgressDialog progressDialog = new ProgressDialog(UploadDocActivity.this, R.style.MyDialogTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Fetching docs...");
        progressDialog.setCancelable(false);
        if (!((Activity) UploadDocActivity.this).isFinishing()) {
            progressDialog.show();
        }

        Call<UploadDocModel> uploadDocModelCall = getApiInstance().fetchDocs(getCurrentUser().getId(), getCurrentUser().getAccessToken());
        uploadDocModelCall.enqueue(new Callback<UploadDocModel>() {
            @Override
            public void onResponse(Call<UploadDocModel> call, Response<UploadDocModel> response) {
                progressDialog.dismiss();
                if (!response.isSuccessful()) {
                    Toast.makeText(UploadDocActivity.this, "" + response.message(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onResponse: fail " + response.code());
                    return;
                }

                Log.d(TAG, "onResponse: success" + response.code() + response.body());
                if (response.body() != null) {
                    UploadDocModel uploadDocModel = null;
                    try {
                        uploadDocModel = response.body();
                    } catch (Exception e) {
                        Toast.makeText(UploadDocActivity.this, "Error in response", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String content = "";
                    Log.d(TAG, "onResponse: response msg" + response.body().getResult() + "  msg  ");
                    if (uploadDocModel.getResult().equals("success")) { //very important conditon
                        Log.d(TAG, "onResponse: success");
                        if (uploadDocModel.getData() != null && uploadDocModel.getData().size() > 0) {
                            setSavedData(uploadDocModel);
                        } else {
                            Toast.makeText(UploadDocActivity.this, "Invalid response from server", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        content += uploadDocModel.getMsg();
                        Toast.makeText(UploadDocActivity.this, content, Toast.LENGTH_SHORT).show();
                    }

                    Log.d(TAG, "onResponse: login res" + content);
                } else {
                    Toast.makeText(UploadDocActivity.this, "Invalid response from server", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UploadDocModel> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(UploadDocActivity.this, "" + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setSavedData(UploadDocModel uploadDocModel) {
        for (com.utility.hapdelvendor.Model.UploadDocModel.Datum datumL : uploadDocModel.getData()) {
            if (!isEmpty(datumL.getProfile())) {
                store_pic_img.setVisibility(View.VISIBLE);
                logo_updated = true;
                Picasso.get().load(datumL.getProfile()).placeholder(R.drawable.app_icon_small_png).fit().into(store_pic_img);
            }

            if (!isEmpty(datumL.getDocument1())) {
                doc_one_img.setVisibility(View.VISIBLE);
                Picasso.get().load(datumL.getDocument1()).fit().into(doc_one_img);
            }

            if (!isEmpty(datumL.getDocument2())) {
                doc_two_img.setVisibility(View.VISIBLE);
                Picasso.get().load(datumL.getDocument2()).fit().into(doc_two_img);
            }

            if (!isEmpty(datumL.getDocument3())) {
                doc_three_img.setVisibility(View.VISIBLE);
                Picasso.get().load(datumL.getDocument3()).fit().into(doc_three_img);
            }

            if (!isEmpty(datumL.getDocument4())) {
                doc_four_img.setVisibility(View.VISIBLE);
                Picasso.get().load(datumL.getDocument4()).fit().into(doc_four_img);
            }

            if (!isEmpty(datumL.getDocument5())) {
                doc_five_img.setVisibility(View.VISIBLE);
                Picasso.get().load(datumL.getDocument5()).fit().into(doc_five_img);
            }
        }
    }

    private void openFileChooser(String doctype) {
        current_doc_type = doctype;
        startDialog();
    }

    private void    startDialog() {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(
                this);
        myAlertDialog.setTitle("Upload Pictures Option");
        myAlertDialog.setMessage("How do you want to choose your document?");

        myAlertDialog.setPositiveButton("Gallery",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if ((ContextCompat.checkSelfPermission(UploadDocActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) || (ContextCompat.checkSelfPermission(UploadDocActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
                                ActivityCompat.requestPermissions((Activity) UploadDocActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, FILE_PERMISSION_CODE);
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
                            if ((ContextCompat.checkSelfPermission(UploadDocActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) || (ContextCompat.checkSelfPermission(UploadDocActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) || (ContextCompat.checkSelfPermission(UploadDocActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)) {
                                ActivityCompat.requestPermissions((Activity) UploadDocActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, CAMERA_PERMISSIOM_CODE);
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
                    Toast.makeText(UploadDocActivity.this, "Permission Denied to read your external storage", Toast.LENGTH_SHORT).show();
                }
                break;

            case CAMERA_PERMISSIOM_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startCamertaIntent();
                } else {
                    Toast.makeText(UploadDocActivity.this, "Permission Denied to access your camera", Toast.LENGTH_SHORT).show();
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
        } catch (Exception e) {
            Log.d(TAG, "Can't create file to take picture! " + e.toString());
            Toast.makeText(UploadDocActivity.this, "Please check SD card! Image shot is impossible!", Toast.LENGTH_SHORT);
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

    private boolean getValidationResult() {
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
        File tempDir = Environment.getExternalStorageDirectory();
        tempDir = new File(tempDir.getAbsolutePath() + "/.temp/");
        if (!tempDir.exists()) {
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
        } catch (Exception e) {
            Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Failed to load", e);
        }
        return getImageUri(UploadDocActivity.this, bitmap);

    }

    private Uri getImageUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

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
            if (grabImage() != null) {
                Uri uri = grabImage();
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
                Iterator it = uriListClone.entrySet().iterator();
                int i = 0;
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry) it.next();
                    uploadFile(pair.getKey().toString(), (Uri) pair.getValue(), ++i);
                    it.remove(); // avoids a ConcurrentModificationException
                }
            }
        } else {
            Toast.makeText(UploadDocActivity.this, "Kindly upload documents", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadFile(String doct_type, Uri mImageUri, int i) {
        // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
        // use the FileUtils to get the actual file by uri
        progressDialog = new ProgressDialog(UploadDocActivity.this, R.style.MyDialogTheme);
        if (progressDialog != null) {
            progressDialog = new ProgressDialog(UploadDocActivity.this);
        }
        progressDialog.setMessage("Uploading file " + i + " of " + imgUrisList.size());
        progressDialog.setIndeterminate(false);
        progressDialog.setProgress(0);
        if (!((Activity) UploadDocActivity.this).isFinishing()) {
            progressDialog.show();
        }

        RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), getCurrentUser().getId().toString());

        File file = FileUtils.getFile(UploadDocActivity.this, mImageUri);
        // create RequestBody instance from file
        ProgressRequestBody requestFile = new ProgressRequestBody(file, "image", new ProgressRequestBody.UploadCallbacks() {
            @Override
            public void onProgressUpdate(int percentage) {

                if (progressDialog != null && progressDialog.isShowing()) {

                    progressDialog.setProgress(percentage);
                    Log.d(TAG, "onProgressUpdate: " + percentage);
                }
            }

            @Override
            public void onError() {

            }

            @Override
            public void onFinish() {
                progressDialog.setProgress(100);
            }
        });

        MultipartBody.Part body = MultipartBody.Part.createFormData(doct_type, file.getName(), requestFile);

        // finally, execute the request
        Call<ResponseModel> call = getApiInstance().uploadDoc(userId, body);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call,
                                   Response<ResponseModel> response) {
                progressDialog.dismiss();
                if (!response.isSuccessful()) {
                    Toast.makeText(UploadDocActivity.this, "" + response.message(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onResponse: fail " + response.code());
                    return;
                }
                Log.d(TAG, "onResponse: success" + response.code() + response.body());
                if (response.body() != null) {
                    ResponseModel responseModel = null;
                    try {
                        responseModel = response.body();
                    } catch (Exception e) {
                        Toast.makeText(UploadDocActivity.this, "Error in response", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String content = "";
                    Log.d(TAG, "onResponse: response msg" + response.body().getResult() + "  msg  ");
                    if (responseModel.getResult().equals("success")) {
                        //very important condition
                        Toast.makeText(UploadDocActivity.this, "" + responseModel.getMsg(), Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onResponse: success upload images");
                        finish();
                    } else {
                        content += responseModel.getMsg();
                        Log.d(TAG, "onResponse: invalid response" + content);
                        Toast.makeText(UploadDocActivity.this, "" + content, Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Log.d(TAG, "onResponse: invalid response");
                    Toast.makeText(UploadDocActivity.this, "Invalid response from server", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(UploadDocActivity.this, "Upload error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("Upload error:", t.getMessage());
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        if (sliding_layout.getPanelState().equals(SlidingUpPanelLayout.PanelState.EXPANDED)) {
            sliding_layout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            return;
        }
        super.onBackPressed();

    }


}
