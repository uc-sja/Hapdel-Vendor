package com.utility.hapdelvendor.Dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.material.textfield.TextInputEditText;
import com.utility.hapdelvendor.Activity.OpenProductActivity;
import com.utility.hapdelvendor.DiscountList;
import com.utility.hapdelvendor.Model.CategoryModel.ParentCategoryModel.Datum;
import com.utility.hapdelvendor.Model.ProducModel.Product;
import com.utility.hapdelvendor.Model.ResponseModel.ResponseModel;
import com.utility.hapdelvendor.R;
import com.utility.hapdelvendor.Utils.Common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.text.TextUtils.isEmpty;
import static com.utility.hapdelvendor.Utils.Common.getApiInstance;
import static com.utility.hapdelvendor.Utils.Common.getCurrentUser;

public class AddDiscount extends Dialog {

    private final String updateType;
    private Context context;
    private Datum selected_category;
    private TextInputEditText set_discount, max_discount, min_order, expiry_disc;
    private Button submit_result;
    private LinearLayout disc_layout;
    private static final String TAG = "AddDiscount";

    private long time;
    private String dateTimeText;
    private TextView update_title;
    private com.utility.hapdelvendor.Model.DiscountModel.Datum current_discount = null;
    private SimpleDateFormat originalFormatter;
    private SimpleDateFormat newFormatter;


    public AddDiscount(@NonNull Context context, Datum selectedDatum, String updateType, com.utility.hapdelvendor.Model.DiscountModel.Datum current_discount) {
        super(context);
        this.context = context;
        selected_category = selectedDatum;
        this.updateType = updateType;
        this.current_discount = current_discount;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_discount_layout);

        set_discount = findViewById(R.id.set_discount);
        max_discount = findViewById(R.id.max_discount);
        min_order = findViewById(R.id.min_order);
        expiry_disc = findViewById(R.id.expiry_disc);

        update_title = findViewById(R.id.update_title);
        submit_result = findViewById(R.id.submit_result);
        disc_layout = findViewById(R.id.discout_layout);


        expiry_disc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateTimePickerInit();
            }
        });


        submit_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateInput()){
                    addDiscount();
                }
            }
        });

        originalFormatter = new SimpleDateFormat("yyyy-MM-dd");
        newFormatter = new SimpleDateFormat("d MMM yyyy");

        if(updateType.equalsIgnoreCase("edit") && current_discount!=null){
            update_title.setText("Edit discount");
            initializeDiscount(current_discount);
        } else if(updateType.equalsIgnoreCase("add")){
            update_title.setText("Add discount");
        }
    }

    private void initializeDiscount(com.utility.hapdelvendor.Model.DiscountModel.Datum current_discount) {
        set_discount.setText(current_discount.getDiscount());
        max_discount.setText(current_discount.getMaxDiscount());
        min_order.setText(current_discount.getMinOrder());

        dateTimeText = current_discount.getExpiryDate();
        Date date = null;
        try {
            date = originalFormatter.parse(current_discount.getExpiryDate());
        } catch (ParseException e) {
            Log.d(TAG, "initializeDiscount: "+e.toString());
            expiry_disc.setText(newFormatter.format(date));
            return;
        }

        expiry_disc.setText(newFormatter.format(date));

    }


    private void dateTimePickerInit() {
        boolean isDateTimePicked = false;
        final String dateTime = "";
        final Calendar newCalendar = Calendar.getInstance();

        final View dialogView1 = View.inflate(context, R.layout.date_picker, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setView(dialogView1);

        dialogView1.findViewById(R.id.date_time_set).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePicker datePicker = (DatePicker) dialogView1.findViewById(R.id.date_picker);
                datePicker.setMinDate(System.currentTimeMillis() - 1000);


                final Calendar calendar = new GregorianCalendar(datePicker.getYear(),
                        datePicker.getMonth(),
                        datePicker.getDayOfMonth()
                );

                time = calendar.getTimeInMillis();

                Date date = calendar.getTime();

                String selectedDateTime = null, formattedDateTime=null;

                selectedDateTime = originalFormatter.format(date);
                formattedDateTime = newFormatter.format(date);

                //setting selected date globally
                dateTimeText = selectedDateTime;

                Log.d(TAG, "onClick: inactive "+selectedDateTime);

                expiry_disc.setText(formattedDateTime);
                expiry_disc.setError(null);

                alertDialog.dismiss();

            }
        });

        alertDialog.show();

    }

    private void addDiscount() {
            final ProgressDialog progressDialog = new ProgressDialog(context, R.style.MyDialogTheme);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Updating discount...");
            progressDialog.setCancelable(false);
            if(!((Activity)context).isFinishing()){
                progressDialog.show();
            }

            Call<ResponseModel> responseModelCall = getApiInstance().addDiscount(getCurrentUser().getId(), getCurrentUser().getAccessToken(), selected_category.getId(),  set_discount.getText().toString(), dateTimeText, max_discount.getText().toString(), min_order.getText().toString(), current_discount==null?null:current_discount.getId());
            responseModelCall.enqueue(new Callback<ResponseModel>() {
                @Override
                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                    progressDialog.dismiss();
                    if(!response.isSuccessful()){
                        Toast.makeText(context, ""+response.message(), Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onResponse: fail "+response.code());
                        return;
                    }

                    Log.d(TAG, "onResponse: success"+response.code()+response.body());
                    if(response.body()!=null ){
                        ResponseModel responseModel = null;
                        try {
                            responseModel = response.body();
                        } catch (Exception e) {
                            Toast.makeText(context, "Error in response", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        String content="";
                        Log.d(TAG, "onResponse: response msg"+response.body().getResult()+"  msg  ");
                        if (responseModel.getResult().equals("success")){ //very important conditon
                            Toast.makeText(context, responseModel.getMsg(), Toast.LENGTH_SHORT).show();
                            if(updateType.trim().equalsIgnoreCase("add")){
                                ((OpenProductActivity)context).fetchProducts(((OpenProductActivity)context).selectedDatum, "1");
                            } else {
                                ((DiscountList)context).fetchDiscounts();
                            }
                            dismiss();
                        }else{
                            content+= responseModel.getMsg();
                            Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
                        }

                        Log.d(TAG, "onResponse: delete address res"+content);
                    } else {
                        Toast.makeText(context, "Invalid response from server", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseModel> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(context, ""+t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                }
            });
        }


    private boolean validateInput() {
        ArrayList<TextInputEditText> textInputEditTexts = Common.findTextInputEditTexts(disc_layout, null);
        for(TextInputEditText textInputEditText: textInputEditTexts){
            if(isEmpty(textInputEditText.getText().toString()) && textInputEditText.getVisibility()==View.VISIBLE){
                Log.d(TAG, "performValidation: "+textInputEditText.getVisibility());
                textInputEditText.setError(textInputEditText.getHint()+" cannot be empty");
                textInputEditText.requestFocus();
                return false;
            }

            if(textInputEditText.getId() != R.id.expiry_disc){
                try {
                    double value = Double.parseDouble(textInputEditText.getText().toString().trim());
                    } catch (Exception e){
                    textInputEditText.setError("Please enter a valid "+textInputEditText.getHint());
                    textInputEditText.requestFocus();
                    return false;
                }
                }
            }

        if(dateTimeText == null || isEmpty(dateTimeText)){
            expiry_disc.requestFocus();
            expiry_disc.setError("Invalid expiry date");
            return false;
        }

        return true;

    }
}
