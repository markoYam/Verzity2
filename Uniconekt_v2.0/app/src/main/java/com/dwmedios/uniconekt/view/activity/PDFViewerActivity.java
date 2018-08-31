package com.dwmedios.uniconekt.view.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toolbar;

import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.view.activity.base.BaseActivity;
import com.dwmedios.uniconekt.view.util.Utils;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.dwmedios.uniconekt.view.util.ImageUtils.getUrlImage;

public class PDFViewerActivity extends BaseActivity {
    public static final String KEY_VIEWER = "KEY_VIEWER_PDF";
    public static final String KEY_VIEWER_NOMBRE = "KEY_VIEWER_PDF2";
    @BindView(R.id.webviewPdf)
    WebView mWebView;
    @BindView(R.id.toolbar)
    android.support.v7.widget.Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfviewer);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Visor de archivos");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    ProgressDialog mProgressDialog;

    public void configureLoading() {
        mProgressDialog = new ProgressDialog(PDFViewerActivity.this);
        mProgressDialog.setMessage("Cargando contenido");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setProgress(0);
        mProgressDialog.setMax(100);
        mProgressDialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        viewPDF();
    }

    String url;
    String nombre;

    public void viewPDF() {
        url = getIntent().getExtras().getString(KEY_VIEWER);
        nombre = getIntent().getExtras().getString(KEY_VIEWER_NOMBRE);
        url = getUrlImage(url, getApplicationContext());
        String temp;
        configureLoading();
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebChromeClient(new webClient());
        mWebView.setWebViewClient(new AppWebViewClient());
        mWebView.setVisibility(View.VISIBLE);
        // http://www.fmg.org.mx/@Url.Content(item.pathArchivo)&embedded=true
        temp = "https://drive.google.com/viewerng/viewer?url=" + url + "&embedded=true";
        Log.e("Url_base", temp);
        mWebView.loadUrl(temp);
    }

    public class AppWebViewClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            //Page load finished
            super.onPageFinished(view, url);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_descargar:
                if (url != null && nombre != null)
                    new Utils.DownloadFiles(url, nombre, getApplicationContext(), PDFViewerActivity.this).execute();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.download_menu, menu);
        return true;
    }

    public class webClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            mProgressDialog.setProgress(newProgress * 100);

            if (newProgress == 100)
                mProgressDialog.dismiss();
        }
    }
}
