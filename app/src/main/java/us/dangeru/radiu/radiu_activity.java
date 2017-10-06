package us.dangeru.radiu;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import static android.view.Window.*;

public class radiu_activity extends AppCompatActivity {
    public WebView getWebView() {
        return (WebView) findViewById(R.id.radiuView);
    }
    public MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radiu_activity);
        mediaPlayer = new MediaPlayer();


        WebView radiu_driver = getWebView();
        WebSettings radiu_settings = radiu_driver.getSettings();
        radiu_settings.setJavaScriptEnabled(true);
        radiu_settings.setMediaPlaybackRequiresUserGesture(false);
        radiu_driver.setInitialScale(10);
        radiu_driver.addJavascriptInterface(new radiu_javascript(this), "radiu");
        radiu_driver.loadUrl("file:///android_asset/radiu/index.html");

        try {
            mediaPlayer.setDataSource("http://radio.dangeru.us:8000/stream.ogg");
            mediaPlayer.prepare();
        } catch(Exception e) {

        }
    }
}