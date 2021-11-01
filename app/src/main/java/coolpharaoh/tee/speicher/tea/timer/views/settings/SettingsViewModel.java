package coolpharaoh.tee.speicher.tea.timer.views.settings;

import android.app.Application;

import androidx.annotation.VisibleForTesting;

import coolpharaoh.tee.speicher.tea.timer.core.actual_settings.ActualSettings;
import coolpharaoh.tee.speicher.tea.timer.core.actual_settings.ActualSettingsRepository;
import coolpharaoh.tee.speicher.tea.timer.core.actual_settings.DarkMode;
import coolpharaoh.tee.speicher.tea.timer.core.actual_settings.SharedSettings;
import coolpharaoh.tee.speicher.tea.timer.core.tea.TeaRepository;

class SettingsViewModel {

    private final ActualSettingsRepository actualSettingsRepository;
    private final TeaRepository teaRepository;
    private final SharedSettings sharedSettings;

    private final ActualSettings actualSettings;

    public SettingsViewModel(final Application application) {
        this(new TeaRepository(application), new ActualSettingsRepository(application),
                new SharedSettings(application));
    }

    @VisibleForTesting
    public SettingsViewModel(final TeaRepository teaRepository, final ActualSettingsRepository actualSettingsRepository,
                             final SharedSettings sharedSettings) {
        this.teaRepository = teaRepository;
        this.actualSettingsRepository = actualSettingsRepository;
        this.sharedSettings = sharedSettings;
        actualSettings = actualSettingsRepository.getSettings();
    }

    //Settings
    void setMusicChoice(final String musicChoice) {
        actualSettings.setMusicChoice(musicChoice);
        actualSettingsRepository.updateSettings(actualSettings);
    }

    String getMusicName() {
        return actualSettings.getMusicName();
    }

    void setMusicName(final String musicName) {
        actualSettings.setMusicName(musicName);
        actualSettingsRepository.updateSettings(actualSettings);
    }

    boolean isVibration() {
        return actualSettings.isVibration();
    }

    void setVibration(final boolean vibration) {
        actualSettings.setVibration(vibration);
        actualSettingsRepository.updateSettings(actualSettings);
    }

    boolean isAnimation() {
        return actualSettings.isAnimation();
    }

    void setAnimation(final boolean animation) {
        actualSettings.setAnimation(animation);
        actualSettingsRepository.updateSettings(actualSettings);
    }

    String getTemperatureUnit() {
        return actualSettings.getTemperatureUnit();
    }

    void setTemperatureUnit(final String temperatureUnit) {
        actualSettings.setTemperatureUnit(temperatureUnit);
        actualSettingsRepository.updateSettings(actualSettings);
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
        return actualSettings.isShowTeaAlert();
    }

    void setShowTeaAlert(final boolean showTeaAlert) {
        actualSettings.setShowTeaAlert(showTeaAlert);
        actualSettingsRepository.updateSettings(actualSettings);
    }

    boolean isMainUpdateAlert() {
        return actualSettings.isMainUpdateAlert();
    }

    void setMainUpdateAlert(final boolean mainUpdateAlert) {
        actualSettings.setMainUpdateAlert(mainUpdateAlert);
        actualSettingsRepository.updateSettings(actualSettings);
    }

    boolean isSettingsPermissionAlert() {
        return actualSettings.isSettingsPermissionAlert();
    }

    void setSettingsPermissionAlert(final boolean settingsPermissionAlert) {
        actualSettings.setSettingsPermissionAlert(settingsPermissionAlert);
        actualSettingsRepository.updateSettings(actualSettings);
    }

    void setDefaultSettings() {
        actualSettings.setMusicChoice("content://settings/system/ringtone");
        actualSettings.setMusicName("Default");
        actualSettings.setVibration(true);
        actualSettings.setAnimation(true);
        actualSettings.setTemperatureUnit("Celsius");
        actualSettings.setMainRateAlert(true);
        actualSettings.setMainRateCounter(0);
        actualSettings.setMainUpdateAlert(false);
        actualSettings.setShowTeaAlert(true);
        actualSettings.setSettingsPermissionAlert(true);
        actualSettings.setSort(0);

        actualSettingsRepository.updateSettings(actualSettings);
    }

    //Tea
    void deleteAllTeas() {
        teaRepository.deleteAllTeas();
    }
}
