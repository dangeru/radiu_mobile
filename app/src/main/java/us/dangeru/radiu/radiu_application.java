package us.dangeru.radiu;

import android.app.AlertDialog;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

/**
 * Created by Niles on 6/10/17.
 */

public class radiu_application extends Application {
    private static final String TAG = radiu_application.class.getSimpleName();
    public static MediaPlayer player = null;
    public static boolean playing = false;
    public static boolean hasStarted = false;
    public static NotificationManager mNotificationManager = null;
    public static final int mNotificationId = 15;
    public static NotificationCompat.Builder mBuilder = null;

    public static void preparePlayer(Context mContext) {
        Log.i(TAG, "preparePlayer");
        if (player == null) {
            player = new MediaPlayer();
            try {
                player.setDataSource("http://radio.dangeru.us:8000/stream.ogg");
                //player.prepare();
                player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        Log.i(TAG, "Prepared - starting.");
                        mp.start();
                    }
                });
                player.prepareAsync();
            } catch (Exception e) {
                e.printStackTrace();
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

        mNotificationManager =
                (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);


        final String channelId = "radiu";
        final String channelDescription = "radi/u/";
        mBuilder =
                new NotificationCompat.Builder(getApplicationContext(), channelId)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("radi/u/ - NULL")
                        .setOngoing(true)
                        .setContentText("NULL are listening.");

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = null;
            if (mNotificationManager != null) {
                notificationChannel = mNotificationManager.getNotificationChannel(channelId);
            }
            if (notificationChannel == null) {
                int importance = NotificationManager.IMPORTANCE_LOW; //Set the importance level
                notificationChannel = new NotificationChannel(channelId, channelDescription, importance);
                //notificationChannel.setLightColor(Color.GREEN); //Set if it is necesssary
                notificationChannel.enableVibration(false);
                mNotificationManager.createNotificationChannel(notificationChannel);
            }
            mBuilder.setChannelId(channelId);
        }

        mNotificationManager.notify(mNotificationId, mBuilder.build());

    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        mNotificationManager.cancelAll();
    }
}
