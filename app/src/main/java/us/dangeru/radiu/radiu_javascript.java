package us.dangeru.radiu;

import android.app.NotificationManager;
import android.content.Context;
import android.media.MediaPlayer;
import android.support.v7.app.NotificationCompat;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import static us.dangeru.radiu.radiu_application.mBuilder;
import static us.dangeru.radiu.radiu_application.mNotificationId;

/**
 * Created by prefetcher on 16.09.2017.
 */

public class radiu_javascript {
    Context mContext;

    /** Instantiate the interface and set the context */
    radiu_javascript(Context c) {
        mContext = c;
    }

    /*
        Updates the notification with the new data about listeners and the currently played track.
        Refreshes every 15 seconds.
     */
    @JavascriptInterface
    public void update_notif(String title, String listeners){
        mBuilder.setContentTitle("radi/u/ - " + title);
        mBuilder.setContentText(listeners + " are listening.");

        radiu_application.mNotificationManager.notify(mNotificationId, mBuilder.build());
    }

    /*
        MediaPlayer integration.
        Added that because @Lain was complaining about me using <audio> in WebView.
     */
    @JavascriptInterface
    public void play_stream() {
        radiu_application.preparePlayer(mContext);
        radiu_application.player.start();
        radiu_application.playing = true;
    }

    @JavascriptInterface
    public void pause_stream() {
        radiu_application.player.stop();
        radiu_application.playing = false;
    }
    @JavascriptInterface
    public String is_playing() {
        return String.valueOf(radiu_application.playing);
    }

    @JavascriptInterface
    public void set_volume(String new_volume) {
        Toast.makeText(mContext, "TODO set volume to " + new_volume, Toast.LENGTH_LONG).show();
    }

    @JavascriptInterface
    public String hasWelcomePlayed() {
        return String.valueOf(radiu_application.hasStarted);
    }

    @JavascriptInterface
    public void setWelcomePlayed(boolean value) {
        radiu_application.hasStarted = value;
    }
}