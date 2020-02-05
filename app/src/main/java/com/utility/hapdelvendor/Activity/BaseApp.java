package com.utility.hapdelvendor.Activity;

import android.app.Activity;
import android.app.Application;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.utility.hapdelvendor.R;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;

public class BaseApp extends Application {
    public static BaseApp AppContext;
    private static final String TAG = "BaseApp";

    @Override
    public void onCreate() {
        super.onCreate();

        AppContext = this;
//        Slider.init(new PicassoImageLoadingService(AppContext));


        ViewPump.init(ViewPump.builder()
                .addInterceptor(new CalligraphyInterceptor(
                        new CalligraphyConfig.Builder()
                                .setDefaultFontPath("fonts/Heebo-Regulat.ttf")
                                .setFontAttrId(R.attr.fontPath)
                                .build()))
                .build());


        //to make all the activities fixed portrait
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
//                Log.d(TAG, "onActivityCreated: "+activity.getLocalClassName());
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });

    }
}
