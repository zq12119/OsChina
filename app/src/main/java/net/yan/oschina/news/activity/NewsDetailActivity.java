package net.yan.oschina.news.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;

import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import net.yan.oschina.R;
import net.yan.oschina.util.WebViewURL;

public class NewsDetailActivity extends AppCompatActivity {

    String url = WebViewURL.URL_NEWS;
    private WebView wvNews;
    private WebSettings settings;
    private Toolbar toolbar;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        //设置标题栏回退按钮，及点击事件
        toolbar = findViewById(R.id.news_tool_bar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewsDetailActivity.this.finish();
            }
        });

        wvNews = findViewById(R.id.wv_news_detail);

        //让它加载这个网页
        wvNews.loadUrl(url);
        settings = wvNews.getSettings();
        settings.setBuiltInZoomControls(true);
        settings.setJavaScriptEnabled(true);
        settings.setBlockNetworkImage(false);//解决图片不能加载
        settings.setUseWideViewPort(true);//将图片调整到合适webView
        settings.setLoadWithOverviewMode(true);//缩放至屏幕的大小
        settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        //解决http和https混合问题

        //设置网页在webView打开
        wvNews.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                wvNews.loadUrl(url);
                return true;
            }
            //解决https图片不能加载

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }
        });

    }

    @Override
    protected void onDestroy() {
        if (wvNews != null) {
            wvNews.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            wvNews.clearHistory();
            ((ViewGroup) wvNews.getParent()).removeView(wvNews);
            wvNews.destroy();
            wvNews = null;
        }
        super.onDestroy();

    }
}

