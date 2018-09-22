package us.dangeru.radiu;

import android.app.Activity;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

/**
 * Created by Niles on 6/10/17.
 */

public class radiu_application extends Application {
    public static final int mNotificationId = 15;
    private static final String TAG = radiu_application.class.getSimpleName();
    public static DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
    public static TrackSelection.Factory videoTrackSelectionFactory =
            new AdaptiveTrackSelection.Factory(bandwidthMeter);
    public static DefaultTrackSelector trackSelector =
            new DefaultTrackSelector(videoTrackSelectionFactory);
    public static SimpleExoPlayer player = null;
    public static boolean playing = false;
    public static boolean hasStarted = false;
    public static NotificationManager mNotificationManager = null;
    public static NotificationCompat.Builder mBuilder = null;

    public static void preparePlayer(Context mContext) {
        if (player != null) {
            player.stop();
        }
        LoadControl loadControl = new DefaultLoadControl();
        player = ExoPlayerFactory.newSimpleInstance(mContext, trackSelector, loadControl);

        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(mContext, Util.getUserAgent(mContext, "radi/u/"), bandwidthMeter);

        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

        MediaSource mediaSource = new ExtractorMediaSource(Uri.parse("http://radio.dangeru.us:8000/stream.ogg"), dataSourceFactory, extractorsFactory, null, null);
        player.setPlayWhenReady(true);
        player.prepare(mediaSource);
    }

    public static void setVolume(float volume) {
        if (player == null) return;
        player.setVolume(volume);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mNotificationManager =
                (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        final String channelId = "radiu";
        final String channelDescription = "radi/u/";


        ServiceConnection mConnection = new ServiceConnection() {
            public void onServiceConnected(ComponentName className,
                                           IBinder binder) {

                ((radiu_service.KillBinder) binder).service.startService(new Intent(
                        getApplicationContext(), radiu_service.class));


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

            public void onServiceDisconnected(ComponentName className) {
            }

        };
        bindService(new Intent(getApplicationContext(),
                        radiu_service.class), mConnection,
                Context.BIND_AUTO_CREATE);

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle bundle) {

            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                NotificationManager mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                if (mNM == null) return;
                mNM.cancelAll();
            }
        });
    }
}
