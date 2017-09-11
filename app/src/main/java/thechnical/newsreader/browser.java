package thechnical.newsreader;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class browser extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_browser);


                WebView webView = (WebView)findViewById(R.id.webView);
                webView.getSettings().setJavaScriptEnabled(true);
                webView.setWebViewClient(new WebViewClient());


                String my_url = getIntent().getStringExtra("url_name");
                webView.loadUrl(my_url);



        }
}
