package com.roy.bkapp.ui.activity.common;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.roy.bkapp.R;
import com.roy.bkapp.ui.activity.BaseSwipeBackActivity;
import com.roy.bkapp.ui.activity.RootActivity;
import com.roy.bkapp.utils.BrowserLayout;
import com.roy.bkapp.utils.ToastUtils;

import butterknife.BindView;


/**
 * Created by 1vPy(Roy) on 2017/3/9.
 */

public class WebViewActivity extends RootActivity implements BrowserLayout.OnReceiveTitleListener {
    @BindView(R.id.bl_webview)
    BrowserLayout bl_webview;

    private String mUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
    }

    @Override
    protected void initViewAndEvent() {
        mToolbar.setNavigationOnClickListener(v -> WebViewActivity.this.finish());
        bl_webview.setOnReceiveTitleListener(this);
        initData();
    }


    private void initData() {
        if (getIntent() != null) {
            mUrl = getIntent().getStringExtra("url");
        }

        if (!TextUtils.isEmpty(mUrl)) {
            bl_webview.loadUrl(mUrl);
        } else {
            ToastUtils.showToast(R.string.url_error);
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_BACK:
                if (bl_webview.canGoBack()) {
                    bl_webview.goBack();
                }
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void onReceive(String title) {
        mToolbar.setTitle(title);
    }

    @Override
    public void onPageFinished() {

    }
}
