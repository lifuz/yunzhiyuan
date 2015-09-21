package com.prd.testtv.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

import com.prd.testtv.R;

/**
 * 作者：李富 on 2015/9/18.
 * 邮箱：lifuzz@163.com
 */
public class GJFragment extends Fragment {

    private EditText wv_text;
    private Button wv_btn;
    private WebView wv_web;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.gj_layout,container,false);
        return layout;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        wv_btn = (Button) view.findViewById(R.id.wv_btn);
        wv_text = (EditText) view.findViewById(R.id.wv_text);
        wv_web = (WebView) view.findViewById(R.id.wv_web);

        wv_btn.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    String str = wv_text.getText().toString();
                    wv_web.loadUrl(str);
                }
            }
        });

        wv_web.setWebViewClient(new MyWebViewClient());

        WebSettings webSettings = wv_web.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(true);
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            view.loadUrl(url);
            return true;
        }
    }
}
