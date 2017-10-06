package us.dangeru.radiu;

import android.app.NotificationManager;
import android.content.Context;
import android.media.MediaPlayer;
import android.support.v7.app.NotificationCompat;
import android.webkit.JavascriptInterface;

/**
 * Created by prefetcher on 16.09.2017.
 */

public class radiu_javascript {
    Context mContext;
    NotificationCompat.Builder mBuilder;
    int mNotificationId;
    NotificationManager mNotificationManager;
    MediaPlayer mMediaPlayer;

    /** Instantiate the interface and set the context */
    radiu_javascript(Context c) {
        mContext = c;
        mNotificationId = 15;

        mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(c)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("radi/u/ - NULL")
                        .setContentText("NULL are listening.");

        mNotificationManager =
                (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(mNotificationId, mBuilder.build());
    }

    /*
        Updates the notification with the new data about listeners and the currently played track.
        Refreshes every 15 seconds.
     */
    @JavascriptInterface
    public void update_notif(String title, String listeners){
        mBuilder.setContentTitle("radi/u/ - " + title);
        mBuilder.setContentText(listeners + " are listening.");

        mNotificationManager.notify(mNotificationId, mBuilder.build());
    }

    /*
        MediaPlayer integration.
        Added that because @Lain was complaining about me using <audio> in WebView.
     */
    @JavascriptInterface
    public void play_stream() {
        mMediaPlayer.start();
    }

    @JavascriptInterface
    public void pause_stream() {
        mMediaPlayer.pause();
    }
}