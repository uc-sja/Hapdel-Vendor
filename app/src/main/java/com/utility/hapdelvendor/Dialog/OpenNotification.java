package com.utility.hapdelvendor.Dialog;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toolbar;

import com.utility.hapdelvendor.R;

public class OpenNotification extends Dialog implements
        android.view.View.OnClickListener {

    public String content, title;
    private Button exit;
    TextView text_content,text_title;
    Toolbar tl;



    public OpenNotification(Context context, String title, String content) {
        super(context);
        this.content = content;
        this.title = title;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_notification_dialog);
        exit = (Button) findViewById(R.id.btn_exit);
        exit.setOnClickListener(this);
//        tl = findViewById(R.id.tl);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            TextView title = tl.findViewById(R.id.toolbar_title);
//            title.setText("Notification View");
//        }
//
        text_title = findViewById(R.id.event_title);
        text_content = findViewById(R.id.event_content);

        text_title.setText(Html.fromHtml(title));
        text_content.setText(content);
        text_content.setScroller(new Scroller(getContext()));
        text_content.setVerticalScrollBarEnabled(true);
        text_content.setMovementMethod(ScrollingMovementMethod.getInstance());
        text_content.setScrollBarFadeDuration(0);
        text_content.setScrollbarFadingEnabled(false);
        text_content.setScrollBarSize(20);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_exit:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }
}