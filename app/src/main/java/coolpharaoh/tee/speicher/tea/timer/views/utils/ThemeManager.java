package coolpharaoh.tee.speicher.tea.timer.views.utils;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;

import coolpharaoh.tee.speicher.tea.timer.core.actualsettings.DarkMode;

public class ThemeManager {
    private ThemeManager() {
    }

    public static void applyTheme(@NonNull final DarkMode darkMode) {
        switch (darkMode) {
            case DISABLED:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case ENABLED:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case SYSTEM:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY);
                }
                break;
            default:
        }
    }
}
