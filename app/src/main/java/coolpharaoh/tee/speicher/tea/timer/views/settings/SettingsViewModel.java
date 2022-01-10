package coolpharaoh.tee.speicher.tea.timer.views.settings;

import android.app.Application;

import androidx.annotation.VisibleForTesting;

import coolpharaoh.tee.speicher.tea.timer.core.actual_settings.DarkMode;
import coolpharaoh.tee.speicher.tea.timer.core.actual_settings.SharedSettings;
import coolpharaoh.tee.speicher.tea.timer.core.actual_settings.TemperatureUnit;
import coolpharaoh.tee.speicher.tea.timer.core.tea.TeaRepository;

class SettingsViewModel {

    private final TeaRepository teaRepository;
    private final SharedSettings sharedSettings;

    public SettingsViewModel(final Application application) {
        this(new TeaRepository(application), new SharedSettings(application));
    }

    @VisibleForTesting
    public SettingsViewModel(final TeaRepository teaRepository,
                             final SharedSettings sharedSettings) {
        this.teaRepository = teaRepository;
        this.sharedSettings = sharedSettings;
    }

    //Settings
    void setMusicChoice(final String musicChoice) {
        sharedSettings.setMusicChoice(musicChoice);
    }

    String getMusicName() {
        return sharedSettings.getMusicName();
    }

    void setMusicName(final String musicName) {
        sharedSettings.setMusicName(musicName);
    }

    boolean isVibration() {
        return sharedSettings.isVibration();
    }

    void setVibration(final boolean vibration) {
        sharedSettings.setVibration(vibration);
    }

    boolean isAnimation() {
        return sharedSettings.isAnimation();
    }

    void setAnimation(final boolean animation) {
        sharedSettings.setAnimation(animation);
    }

    TemperatureUnit getTemperatureUnit() {
        return sharedSettings.getTemperatureUnit();
    }

    void setTemperatureUnit(final TemperatureUnit temperatureUnit) {
        sharedSettings.setTemperatureUnit(temperatureUnit);
    }

    void setOverviewHeader(final boolean overviewHeader) {
        sharedSettings.setOverviewHeader(overviewHeader);
    }

    boolean isOverviewHeader() {
        return sharedSettings.isOverviewHeader();
    }

    void setDarkMode(final DarkMode darkMode) {
        sharedSettings.setDarkMode(darkMode);
    }

    DarkMode getDarkMode() {
        return sharedSettings.getDarkMode();
    }

    boolean isShowTeaAlert() {
        return sharedSettings.isShowTeaAlert();
    }

    void setShowTeaAlert(final boolean showTeaAlert) {
        sharedSettings.setShowTeaAlert(showTeaAlert);
    }

    boolean isMainUpdateAlert() {
        return sharedSettings.isOverviewUpdateAlert();
    }

    void setOverviewUpdateAlert(final boolean overviewUpdateAlert) {
        sharedSettings.setOverviewUpdateAlert(overviewUpdateAlert);
    }

    void setDefaultSettings() {
        sharedSettings.setFactorySettings();
    }

    //Tea
    void deleteAllTeas() {
        teaRepository.deleteAllTeas();
    }
}
