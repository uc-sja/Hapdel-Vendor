package com.utility.hapdelvendor.Utils;

import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

public class OnViewGlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {

    private static final String TAG = "OnViewGlobalLayoutListe";
    public static final int maxHeight =  900;
    private View view;

    public OnViewGlobalLayoutListener(View view) {
        this.view = view;
    }

    @Override
    public void onGlobalLayout() {
        Log.d(TAG, "onGlobalLayout: "+view.getHeight());
        if(view.getHeight()>maxHeight){
            view.getLayoutParams().height = maxHeight;
        }
    }
}
