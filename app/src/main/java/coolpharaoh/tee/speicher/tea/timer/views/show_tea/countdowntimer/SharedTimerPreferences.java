package coolpharaoh.tee.speicher.tea.timer.views.show_tea.countdowntimer;


import android.app.Application;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

public class SharedTimerPreferences {

    private static final String START_TIME = "countdown_timer";
    private final SharedPreferences preferences;

    public SharedTimerPreferences(Application application) {
        preferences = PreferenceManager.getDefaultSharedPreferences(application);
        setStartedTime(0);
    }

    long getStartedTime() {
        return preferences.getLong(START_TIME, 0);
    }

    void setStartedTime(long startedTime) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(START_TIME, startedTime);
        editor.apply();
    }
}
