package coolpharaoh.tee.speicher.tea.timer.core.actual_settings;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedSettings {

    private static final String TEA_MEMORY_SETTINGS = "tea_memory_settings";
    private static final String DARK_MODE = "dark_mode";
    private static final String OVERVIEW_HEADER = "overview_header";
    SharedPreferences sharedPreferences;

    public SharedSettings(Application application) {
        sharedPreferences = application.getSharedPreferences(TEA_MEMORY_SETTINGS, Context.MODE_PRIVATE);
    }

    public DarkMode getDarkMode() {
        final String darkModeString = sharedPreferences.getString(DARK_MODE, null);
        return DarkMode.fromText(darkModeString);
    }

    public void setSetDarkMode(final DarkMode darkMode) {
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(DARK_MODE, darkMode.getText());
        editor.apply();
    }

    public boolean isOverviewHeader() {
        return sharedPreferences.getBoolean(OVERVIEW_HEADER, false);
    }

    public void setOverviewHeader(final boolean overviewHeader) {
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(OVERVIEW_HEADER, overviewHeader);
        editor.apply();
    }
}
