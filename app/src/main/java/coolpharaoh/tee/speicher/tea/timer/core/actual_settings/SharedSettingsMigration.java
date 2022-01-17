package coolpharaoh.tee.speicher.tea.timer.core.actual_settings;

import static coolpharaoh.tee.speicher.tea.timer.core.actual_settings.SharedSettings.TEA_MEMORY_SETTINGS;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedSettingsMigration {
    private static final int NEW_VERSION = 2;

    private final SharedPreferences sharedPreferences;
    private final SharedSettings sharedSettings;

    public SharedSettingsMigration(final Application application) {
        sharedPreferences = application.getSharedPreferences(TEA_MEMORY_SETTINGS, Context.MODE_PRIVATE);
        sharedSettings = new SharedSettings(application);
    }

    public void migrate() {
        final int oldVersion = sharedSettings.getSettingsVersion();

        for (int nextVersion = oldVersion + 1; nextVersion <= NEW_VERSION; nextVersion++) {
            delegateMigrations(nextVersion);
        }

        sharedSettings.setSettingsVersion(NEW_VERSION);
    }

    private void delegateMigrations(final int nextVersion) {
        switch (nextVersion) {
            case 1:
                migration0T1();
                break;
            case 2:
                migration1T2();
                break;
        }
    }

    private void migration0T1() {
        if (sharedSettings.isFirstStart()) {
            sharedSettings.setFactorySettings();
        }
    }

    private void migration1T2() {
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.remove("IS_MIGRATED");
        editor.remove("settings_permission_alert");
        editor.putBoolean("overview_update_alert", true);

        editor.apply();
    }
}
