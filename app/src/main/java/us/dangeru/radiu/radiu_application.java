package us.dangeru.radiu;

import android.app.AlertDialog;
import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import android.media.MediaPlayer;
import android.support.v4.app.NotificationCompat;

/**
 * Created by Niles on 6/10/17.
 */

public class radiu_application extends Application {
    public static MediaPlayer player = null;
    public static boolean playing = false;
    public static boolean hasStarted = false;
    public static NotificationCompat.Builder mBuilder = null;
    public static int mNotificationId = 15;
    public static NotificationManager mNotificationManager = null;

    public static void preparePlayer(Context mContext) {
        if (player == null) {
            player = new MediaPlayer();
            try {
                player.setDataSource("http://radio.dangeru.us:8000/stream.ogg");
                player.prepare();
            } catch (Exception e) {
                new AlertDialog.Builder(mContext).setMessage(e.toString()).create().show();
            }
        }
    }

    public static void setVolume(float volume) {
        if (player == null) return;
        player.setVolume(volume, volume);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mBuilder =
                new NotificationCompat.Builder(getApplicationContext())
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("radi/u/ - NULL")
                        .setContentText("NULL are listening.");

        mNotificationManager =
                (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(mNotificationId, mBuilder.build());

    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        mNotificationManager.cancelAll();
    }
}
