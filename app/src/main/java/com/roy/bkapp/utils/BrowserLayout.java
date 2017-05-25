package com.roy.bkapp.utils;

import android.content.Context;
import android.net.http.SslError;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.roy.bkapp.R;

public class BrowserLayout extends LinearLayout {

    private static final String TAG = BrowserLayout.class.getSimpleName();

    private Context mContext = null;
    private WebView mWebView = null;

    private int mBarHeight = 5;
    private ProgressBar mProgressBar = null;
    private boolean isOverrideUrlLoading = true;

    private String mLoadUrl;

    private OnReceiveTitleListener listener;

    public BrowserLayout(Context context) {
        super(context);
        init(context);
    }

    public BrowserLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        setOrientation(VERTICAL);

        mProgressBar = (ProgressBar) LayoutInflater.from(context).inflate(R.layout.view_progress_horizontal, null);
        mProgressBar.setMax(100);
        mProgressBar.setProgress(0);
        addView(mProgressBar, LayoutParams.MATCH_PARENT, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, mBarHeight, getResources().getDisplayMetrics()));

        mWebView = new WebView(context);
        mWebView.setLayerType(LAYER_TYPE_HARDWARE, null);
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mWebView.getSettings().setDefaultTextEncodingName("UTF-8");
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.getSettings().setBuiltInZoomControls(false);
        mWebView.getSettings().setSupportMultipleWindows(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setSupportZoom(false);
        mWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setLoadsImagesAutomatically(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        //mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebView.getSettings().setAppCacheEnabled(true);
        //mWebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        LayoutParams lps = new LayoutParams(LayoutParams.MATCH_PARENT, 0, 1);
        addView(mWebView, lps);

        mWebView.setWebChromeClient(new AppCacheWebChromeClient());

        mWebView.setWebViewClient(new MonitorWebClient());
    }

    public void loadUrl(String url) {
        mWebView.loadUrl(url);
    }

    public void refresh(String url) {
        mWebView.reload();
    }

    public boolean canGoBack() {
        return null != mWebView ? mWebView.canGoBack() : false;
    }

    public boolean canGoForward() {
        return null != mWebView ? mWebView.canGoForward() : false;
    }

    public void setOverrideUrlLoading(boolean b) {
        isOverrideUrlLoading = b;
    }

    public void goBack() {
        if (null != mWebView) {
            mWebView.goBack();
        }
    }

    public void goForward() {
        if (null != mWebView) {
            mWebView.goForward();
        }
    }

    public WebView getWebView() {
        return mWebView != null ? mWebView : null;
    }


    private class MonitorWebClient extends WebViewClient {

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            String url = request.getUrl().toString().toLowerCase();
            LogUtils.log(TAG, "req -- " + url, LogUtils.DEBUG);
            if (!ADFilterUtils.hasAd(getContext(), url)) {
                return super.shouldInterceptRequest(view, request);
            } else {
                LogUtils.log(TAG, "ad:" + url, LogUtils.DEBUG);
                return new WebResourceResponse(null, null, null);
            }
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            //错误提示
            Toast toast = Toast.makeText(mContext, "Oh no! " + description + " " + failingUrl, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP | Gravity.CENTER, 0, 0);
            toast.show();
            //错误处理
            try {
                mWebView.stopLoading();
            } catch (Exception ignored) {
            }
            try {
                mWebView.loadUrl("about:blank");
            } catch (Exception ignored) {
            }
            if (mWebView.canGoBack()) {
                mWebView.goBack();
            }
            //  super.onReceivedError(view, errorCode, description, failingUrl);
        }

        //当load有ssl层的https页面时，如果这个网站的安全证书在Android无法得到认证，WebView就会变成一个空白页，而并不会像PC浏览器中那样跳出一个风险提示框
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler,
                                       SslError error) {
            //忽略证书的错误继续Load页面内容
            handler.proceed();
            //handler.cancel(); // Android默认的处理方式
            //handleMessage(Message msg); // 进行其他处理
            //  super.onReceivedSslError(view, handler, error);
        }

        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            mLoadUrl = url;
            if (listener != null) {
                listener.onPageFinished();
            }
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (isOverrideUrlLoading)
                return super.shouldOverrideUrlLoading(view, url);
            return true;
        }
    }

    private class AppCacheWebChromeClient extends WebChromeClient {
        @Override
        public void onReachedMaxAppCacheSize(long spaceNeeded, long totalUsedQuota, WebStorage.QuotaUpdater quotaUpdater) {
            //    Log.e(APP_CACHE, "onReachedMaxAppCacheSize reached, increasing space: " + spaceNeeded);
            quotaUpdater.updateQuota(spaceNeeded * 2);
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if (newProgress == 100) {
                mProgressBar.setVisibility(View.GONE);
            } else {
                mProgressBar.setVisibility(View.VISIBLE);
                mProgressBar.setProgress(newProgress);
            }
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if (listener != null)
                listener.onReceive(title);
        }
    }

    public interface OnReceiveTitleListener {
        void onReceive(String title);

        void onPageFinished();
    }

    public void setOnReceiveTitleListener(OnReceiveTitleListener listener) {
        this.listener = listener;
    }
}
