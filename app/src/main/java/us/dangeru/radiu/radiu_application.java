package us.dangeru.radiu;

import android.app.AlertDialog;
import android.app.Application;
import android.media.MediaPlayer;

import java.lang.ref.WeakReference;

/**
 * Created by Niles on 6/10/17.
 */

public class radiu_application extends Application {
    public static MediaPlayer player = null;
    public static boolean playing = false;
    @Override
    public void onCreate() {
        super.onCreate();
        if (player == null) {
            player = new MediaPlayer();
            try {
                player.setDataSource("http://radio.dangeru.us:8000/stream.ogg");
                player.prepare();
            } catch (Exception e) {
                new AlertDialog.Builder(getApplicationContext()).setMessage(e.toString()).create().show();
            }
        }
    }
}
