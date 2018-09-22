package us.dangeru.radiu;

import android.content.Context;
import android.webkit.JavascriptInterface;

import static us.dangeru.radiu.radiu_application.mBuilder;
import static us.dangeru.radiu.radiu_application.mNotificationId;

/**
 * The javascript interface that app.js will use to control the audio and notifiations
 */
public class radiu_javascript {
    /**
     * A reference for an activity context that can be used to show alerts
     */
    private Context mContext;
    /**
     * A reference to the current title, so we don't update more than necessary
     */
    private String old_title = "";
    /**
     * A reference to the current number of listeners, so we don't update more than necessary
     */
    private String old_listeners = "";
    /**
     * Instantiate the interface and set the context
     * @param c The context to save
     */
    radiu_javascript(Context c) {
        mContext = c;
    }

    /**
     * Updates the native notification if it has changed
     * @param title the song currently playing
     * @param listeners the number of current listeners
     */
    @SuppressWarnings("EqualsReplaceableByObjectsCall")
    @JavascriptInterface
    public void update_notif(String title, String listeners){
        if (title.equals(old_title) && listeners.equals(old_listeners)) {
            return;
        }
        if ("1".equals(listeners)) {
            mBuilder.setContentTitle(title);
            mBuilder.setContentText("radi/u/");
        } else {
            mBuilder.setContentTitle("radi/u/ - " + title);
            mBuilder.setContentText(listeners + " are listening.");
        }
        radiu_application.mNotificationManager.notify(mNotificationId, mBuilder.build());
        old_title = title;
        old_listeners = listeners;
    }

    /**
     * Starts the music
     */
    @JavascriptInterface
    public void play_stream() {
        radiu_application.preparePlayer(mContext);
        radiu_application.player.start();
        radiu_application.playing = true;
    }

    /**
     * Pauses the stream
     */
    @JavascriptInterface
    public static void pause_stream() {
        radiu_application.player.stop();
        radiu_application.playing = false;
    }

    /**
     * Gets the current state of the player
     * @return whether or not the stream is playing
     */
    @JavascriptInterface
    public static String is_playing() {
        return String.valueOf(radiu_application.playing);
    }

    /**
     * Sets the volume of the stream
     * @param new_volume the new volume, a float between 0.0 and 1.0
     */
    @JavascriptInterface
    public static void set_volume(String new_volume) {
        radiu_application.setVolume(Float.valueOf(new_volume));
    }

    /**
     * Gets whether the startup sound has played or not
     * @return whether the startup sound has played or not
     */
    @JavascriptInterface
    public static String hasWelcomePlayed() {
        return String.valueOf(radiu_application.hasStarted);
    }

    /**
     * Notifies the application that the startup sound has played
     * @param value true if the startup sound has played
     */
    @JavascriptInterface
    public static void setWelcomePlayed(boolean value) {
        radiu_application.hasStarted = value;
    }
}