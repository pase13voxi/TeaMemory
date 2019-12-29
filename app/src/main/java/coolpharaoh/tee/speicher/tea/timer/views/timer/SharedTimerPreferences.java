package coolpharaoh.tee.speicher.tea.timer.views.timer;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

class SharedTimerPreferences {

    private static final String START_TIME = "countdown_timer";
    private final SharedPreferences mPreferences;

    SharedTimerPreferences(Context context) {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        setStartedTime(0);
    }

    long getStartedTime() {
        return mPreferences.getLong(START_TIME, 0);
    }

    void setStartedTime(long startedTime) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putLong(START_TIME, startedTime);
        editor.apply();
    }
}
