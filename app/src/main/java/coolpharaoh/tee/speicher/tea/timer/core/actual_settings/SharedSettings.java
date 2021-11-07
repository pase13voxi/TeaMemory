package coolpharaoh.tee.speicher.tea.timer.core.actual_settings;

import static coolpharaoh.tee.speicher.tea.timer.core.actual_settings.DarkMode.SYSTEM;
import static coolpharaoh.tee.speicher.tea.timer.core.actual_settings.SortMode.LAST_USED;
import static coolpharaoh.tee.speicher.tea.timer.core.actual_settings.TemperatureUnit.CELSIUS;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedSettings {

    private static final String TEA_MEMORY_SETTINGS = "tea_memory_settings";
    private static final String FIRST_START = "first_start";
    private static final String MUSIC_CHOICE = "music_choice";
    private static final String MUSIC_NAME = "music_name";
    private static final String VIBRATION = "vibration";
    private static final String ANIMATION = "animation";
    private static final String TEMPERATURE_UNIT = "temperature_unit";
    private static final String DARK_MODE = "dark_mode";
    private static final String OVERVIEW_UPDATE_ALERT = "overview_update_alert";
    private static final String SHOW_TEA_ALERT = "show_tea_alert";
    private static final String SETTINGS_PERMISSION_ALERT = "settings_permission_alert";
    private static final String SORT_MODE = "sort_mode";
    private static final String OVERVIEW_HEADER = "overview_header";
    private static final String OVERVIEW_IN_STOCK = "overview_in_stock";
    public static final String IS_MIGRATED = "IS_MIGRATED";
    SharedPreferences sharedPreferences;

    public SharedSettings(final Application application) {
        sharedPreferences = application.getSharedPreferences(TEA_MEMORY_SETTINGS, Context.MODE_PRIVATE);
    }

    public boolean isFirstStart() {
        return sharedPreferences.getBoolean(FIRST_START, true);
    }

    public void setFirstStart(final boolean firstStart) {
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(FIRST_START, firstStart);
        editor.apply();
    }

    public String getMusicChoice() {
        return sharedPreferences.getString(MUSIC_CHOICE, null);
    }

    public void setMusicChoice(final String musicChoice) {
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(MUSIC_CHOICE, musicChoice);
        editor.apply();
    }

    public String getMusicName() {
        return sharedPreferences.getString(MUSIC_NAME, null);
    }

    public void setMusicName(final String musicName) {
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(MUSIC_NAME, musicName);
        editor.apply();
    }

    public boolean isVibration() {
        return sharedPreferences.getBoolean(VIBRATION, true);
    }

    public void setVibration(final boolean vibration) {
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(VIBRATION, vibration);
        editor.apply();
    }

    public boolean isAnimation() {
        return sharedPreferences.getBoolean(ANIMATION, true);
    }

    public void setAnimation(final boolean animation) {
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(ANIMATION, animation);
        editor.apply();
    }

    public TemperatureUnit getTemperatureUnit() {
        final String temperatureUnitString = sharedPreferences.getString(TEMPERATURE_UNIT, CELSIUS.getText());
        return TemperatureUnit.fromText(temperatureUnitString);
    }

    public void setTemperatureUnit(final TemperatureUnit temperatureUnit) {
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TEMPERATURE_UNIT, temperatureUnit.getText());
        editor.apply();
    }

    public DarkMode getDarkMode() {
        final String darkModeString = sharedPreferences.getString(DARK_MODE, SYSTEM.getText());
        return DarkMode.fromText(darkModeString);
    }

    public void setDarkMode(final DarkMode darkMode) {
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(DARK_MODE, darkMode.getText());
        editor.apply();
    }

    public boolean isOverviewUpdateAlert() {
        return sharedPreferences.getBoolean(OVERVIEW_UPDATE_ALERT, false);
    }

    public void setOverviewUpdateAlert(final boolean overviewUpdateAlert) {
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(OVERVIEW_UPDATE_ALERT, overviewUpdateAlert);
        editor.apply();
    }

    public boolean isShowTeaAlert() {
        return sharedPreferences.getBoolean(SHOW_TEA_ALERT, false);
    }

    public void setShowTeaAlert(final boolean showTeaAlert) {
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(SHOW_TEA_ALERT, showTeaAlert);
        editor.apply();
    }

    public boolean isSettingsPermissionAlert() {
        return sharedPreferences.getBoolean(SETTINGS_PERMISSION_ALERT, false);
    }

    public void setSettingsPermissionAlert(final boolean settingsPermissionAlert) {
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(SETTINGS_PERMISSION_ALERT, settingsPermissionAlert);
        editor.apply();
    }

    public SortMode getSortMode() {
        final String sortModeText = sharedPreferences.getString(SORT_MODE, LAST_USED.getText());
        return SortMode.fromText(sortModeText);
    }

    public void setSortMode(final SortMode sortMode) {
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SORT_MODE, sortMode.getText());
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

    public boolean isOverviewInStock() {
        return sharedPreferences.getBoolean(OVERVIEW_IN_STOCK, false);
    }

    public void setOverviewInStock(final boolean overviewInStock) {
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(OVERVIEW_IN_STOCK, overviewInStock);
        editor.apply();
    }

    public void setFactorySettings() {
        setFirstStart(false);
        setMusicChoice("content://settings/system/ringtone");
        setMusicName("Default");
        setVibration(true);
        setAnimation(true);
        setTemperatureUnit(CELSIUS);
        setDarkMode(SYSTEM);
        setOverviewUpdateAlert(false);
        setShowTeaAlert(true);
        setSettingsPermissionAlert(true);
        setSortMode(LAST_USED);
        setOverviewHeader(false);
        setOverviewInStock(false);
        setMigrated(true);
    }

    // Could be removed after the successful migration (In half a year 1.6.2022)
    public boolean isMigrated() {
        return sharedPreferences.getBoolean(IS_MIGRATED, false);
    }


    // Could be removed after the successful migration (In half a year 1.6.2022)
    public void setMigrated(final boolean migrated) {
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(IS_MIGRATED, migrated);
        editor.apply();
    }
}
