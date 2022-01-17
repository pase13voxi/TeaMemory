package coolpharaoh.tee.speicher.tea.timer.views.utils;

import static android.os.Build.VERSION_CODES.Q;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;

import coolpharaoh.tee.speicher.tea.timer.core.actual_settings.DarkMode;
import coolpharaoh.tee.speicher.tea.timer.core.system.CurrentSdk;

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
                if (CurrentSdk.getSdkVersion() >= Q) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY);
                }
                break;
            default:
        }
    }
}
