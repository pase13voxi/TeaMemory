package coolpharaoh.tee.speicher.tea.timer.views.showtea.timer;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

class SharedTimerPreferences {

    private static final String START_TIME = "countdown_timer";
    private final SharedPreferences preferences;

    SharedTimerPreferences(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
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
