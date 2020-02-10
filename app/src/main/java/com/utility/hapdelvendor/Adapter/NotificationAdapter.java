package com.utility.hapdelvendor.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.utility.hapdelvendor.Dialog.OpenNotification;
import com.utility.hapdelvendor.Model.NotificationModel.Datum;
import com.utility.hapdelvendor.R;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private Context context;
    private List<Datum> notificationList;
    private static final String TAG = "NotificationAdapter";

    public NotificationAdapter(Context context, List<Datum> notificationList) {
        this.context = context;
        this.notificationList = notificationList;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v  =  LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notification_cell, null, false);
        return new NotificationViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder notificationViewHolder, final int i) {
        notificationViewHolder.notificationTitle.setText(notificationList.get(i).getNotificationTitle());
        notificationViewHolder.notificationDetails.setText(notificationList.get(i).getNotificationBody());
        if(notificationList.get(i).getSeen().equals("n")){
            notificationViewHolder.notificationTitle.setTypeface(null, Typeface.BOLD);
            notificationViewHolder.notificationTitle.setTextColor(Color.parseColor("#000000"));
            notificationViewHolder.notificationDetails.setTextColor(Color.parseColor("#000000"));
            notificationViewHolder.notificationCard.setCardBackgroundColor(Color.parseColor("#fafafa"));
        }
        notificationViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: "+notificationList.get(i).getNotificationTitle());
                String notificationId = notificationList.get(i).getId();
                Log.d(TAG, "onClick: "+notificationId);
//                markAsSeen(notificationId);
                notificationViewHolder.notificationCard.setCardBackgroundColor(Color.parseColor("#f0f0f1"));
                notificationViewHolder.notificationDetails.setTextColor(Color.parseColor("#746f6f"));
                notificationViewHolder.notificationTitle.setTextColor(Color.parseColor("#746f6f"));

                OpenNotification openNotification = new OpenNotification(context, notificationList.get(i).getNotificationTitle(), notificationList.get(i).getNotificationBody());
                openNotification.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                openNotification.show();
            }
        });
    }
//    private voidoid markAsSeen(String notificationId) {
//        Call<ResponseModel> setAsSeenCall = getApiInstance().setAsSeen(getCurrentUserData().getUserId(), getCurrentUserData().getAccessToken(), notificationId);
//        setAsSeenCall.enqueue(new Callback<ResponseModel>() {
//            @Override
//            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
//                if(!response.isSuccessful()){
//                    Toast.makeText(context, ""+response.message(), Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                if(response.body()!=null){
//
//                    ResponseModel responseSeenModel = null;
//                    try {
//                        responseSeenModel = response.body();
//                    } catch (Exception e) {
//                        Toast.makeText(context, "Error in response", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//                    if(responseSeenModel.getResult().equals("succcess")){
//                        Log.d(TAG, "onResponse: seen");
//                    } else {
//                        Toast.makeText(context, ""+responseSeenModel.getMsg(), Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    Toast.makeText(context, "Invalid Response from server", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//
//            @Override
//            public void onFailure(Call<ResponseModel> call, Throwable t) {
//                Log.d(TAG, "onFailure: "+t.toString());
//            }
//        });
//
//
//    }



    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public void updateItems(List<Datum> notificationList) {
        this.notificationList.clear();
        notifyDataSetChanged();
        this.notificationList.addAll(notificationList);
        notifyDataSetChanged();
    }

    public class NotificationViewHolder extends RecyclerView.ViewHolder{
        TextView notificationTitle, notificationDetails;
        CardView notificationCard;
        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            notificationTitle = itemView.findViewById(R.id.notification_title);
            notificationDetails = itemView.findViewById(R.id.notification_details);
            notificationCard = itemView.findViewById(R.id.notification_card);


            CardView.LayoutParams params = new CardView.LayoutParams(
                    CardView.LayoutParams.MATCH_PARENT,
                    CardView.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(15,15,15,15);
            notificationCard.setLayoutParams(params);

        }
    }
}
