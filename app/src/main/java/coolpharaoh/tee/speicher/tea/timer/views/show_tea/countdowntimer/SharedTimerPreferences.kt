package coolpharaoh.tee.speicher.tea.timer.views.show_tea.countdowntimer

import android.app.Application
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

class SharedTimerPreferences(application: Application) {

    private val preferences: SharedPreferences

    var startedTime: Long
        get() = preferences.getLong(START_TIME, 0)
        set(startedTime) {
            val editor = preferences.edit()
            editor.putLong(START_TIME, startedTime)
            editor.apply()
        }

    init {
        preferences = PreferenceManager.getDefaultSharedPreferences(application)
        startedTime = 0
    }

    companion object {
        private const val START_TIME = "countdown_timer"
    }
}