package com.wyson.googlesample;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.http.SslError;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.wyson.googlesample.fragment.NetworkFragment;
import com.wyson.googlesample.hickey.DownloadCallback;

public class MainActivity extends FragmentActivity implements DownloadCallback {
    private static final String TAG = "MainActivity";

    private NetworkFragment mNetworkFragment;
    private String mUrlString = "https://www.baidu.com/";
    private boolean mDownloading = false;
    private TextView tvData;
    private WebView webData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        tvData = findViewById(R.id.tv_data);
        tvData.setMovementMethod(new ScrollingMovementMethod());
        webData = findViewById(R.id.web_data);
        initWebView();
        mNetworkFragment = NetworkFragment
                .getInstance(getSupportFragmentManager(),mUrlString);

        tvData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDownload();
            }
        });
    }

    private void startDownload(){
        if (!mDownloading && mNetworkFragment != null){
            mNetworkFragment.startDownload();
            mDownloading = true;
        }
    }

    @Override
    public void updateFromDownload(Object result) {
        Log.e(TAG, "updateFromDownload: "+result.toString() );
        tvData.setText(result.toString());
        webData.loadDataWithBaseURL("", result.toString(), "text/html", "UTF-8", null);
    }

    @Override
    public NetworkInfo getActivityNetworkInfo() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo;
    }

    @Override
    public void onProgressUpdate(int progressCode, int percentComplete) {
        switch (progressCode) {
            // You can add UI behavior for progress updates here.
            case Progress.ERROR:
                break;
            case Progress.CONNECT_SUCCESS:
                break;
            case Progress.GET_INPUT_STREAM_SUCCESS:
                break;
            case Progress.PROCESS_INPUT_STREAM_IN_PROGRESS:
                break;
            case Progress.PROCESS_INPUT_STREAM_SUCCESS:
                break;
            default:
                break;
        }
    }

    @Override
    public void finishDownloading() {
        mDownloading = false;
        if (mNetworkFragment != null) {
            mNetworkFragment.cancelDownload();
        }
    }

    private void initWebView() {
        webData.getSettings().setBuiltInZoomControls(true);
        webData.getSettings().setDisplayZoomControls(false);
        //取消滚动条白边效果
        webData.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webData.getSettings().setDefaultTextEncodingName("UTF-8");
        webData.getSettings().setBlockNetworkImage(false);
        webData.getSettings().setJavaScriptEnabled(true);

        webData.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return true;
            }

            @Override
            public void onReceivedError(WebView var1, int var2, String var3, String var4) {

            }

            @Override
            public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
                super.onPageStarted(webView, s, bitmap);
            }

            @Override
            public void onPageFinished(WebView webView, String s) {
                super.onPageFinished(webView, s);
            }

            @Override
            public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
                super.onReceivedSslError(webView, sslErrorHandler, sslError);
                sslErrorHandler.proceed(); // 接受所有证书
            }
        });
    }
}
