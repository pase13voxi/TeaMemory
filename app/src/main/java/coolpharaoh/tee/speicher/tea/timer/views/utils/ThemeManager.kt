package coolpharaoh.tee.speicher.tea.timer.views.utils

import android.os.Build.VERSION_CODES
import androidx.appcompat.app.AppCompatDelegate
import coolpharaoh.tee.speicher.tea.timer.core.settings.DarkMode
import coolpharaoh.tee.speicher.tea.timer.core.system.CurrentSdk.sdkVersion

object ThemeManager {
    @JvmStatic
    fun applyTheme(darkMode: DarkMode) {
        when (darkMode) {
            DarkMode.DISABLED -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            DarkMode.ENABLED -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            DarkMode.SYSTEM ->
                if (sdkVersion >= VERSION_CODES.Q) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY)
                }
        }
    }
}