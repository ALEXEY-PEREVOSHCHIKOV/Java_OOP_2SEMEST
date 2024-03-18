package gui;
import java.util.prefs.Preferences;

public class AppConfig {

    private static final String WINDOW_PREFIX = "window_";
    private static final String WINDOW_STATE_SUFFIX = "_state";

    private Preferences preferences;

    public AppConfig() {
        preferences = Preferences.userNodeForPackage(getClass());
    }

    public void saveWindowPosition(String windowId, int x, int y) {
        preferences.putInt(WINDOW_PREFIX + windowId + "_x", x);
        preferences.putInt(WINDOW_PREFIX + windowId + "_y", y);
    }

    public int getWindowX(String windowId) {
        return preferences.getInt(WINDOW_PREFIX + windowId + "_x", 0);
    }

    public int getWindowY(String windowId) {
        return preferences.getInt(WINDOW_PREFIX + windowId + "_y", 0);
    }

    public void saveWindowState(String windowId, boolean maximized) {
        preferences.putBoolean(WINDOW_PREFIX + windowId + "_maximized", maximized);
    }

    public boolean getWindowState(String windowId) {
        return preferences.getBoolean(WINDOW_PREFIX + windowId + "_maximized", false);
    }
}




