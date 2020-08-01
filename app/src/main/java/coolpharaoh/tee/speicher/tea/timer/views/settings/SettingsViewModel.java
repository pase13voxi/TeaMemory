package coolpharaoh.tee.speicher.tea.timer.views.settings;

import android.app.Application;

import coolpharaoh.tee.speicher.tea.timer.core.actualsettings.ActualSettings;
import coolpharaoh.tee.speicher.tea.timer.core.actualsettings.ActualSettingsRepository;
import coolpharaoh.tee.speicher.tea.timer.core.tea.TeaRepository;

class SettingsViewModel {

    private final ActualSettingsRepository actualSettingsRepository;
    private final TeaRepository teaRepository;

    private final ActualSettings actualSettings;

    public SettingsViewModel(Application application) {

        actualSettingsRepository = new ActualSettingsRepository(application);
        actualSettings = actualSettingsRepository.getSettings();

        teaRepository = new TeaRepository(application);
    }

    //Settings
    void setMusicchoice(String musicchoice) {
        actualSettings.setMusicChoice(musicchoice);
        actualSettingsRepository.updateSettings(actualSettings);
    }

    String getMusicname() {
        return actualSettings.getMusicName();
    }

    void setMusicname(String musicname) {
        actualSettings.setMusicName(musicname);
        actualSettingsRepository.updateSettings(actualSettings);
    }

    boolean isVibration() {
        return actualSettings.isVibration();
    }

    void setVibration(boolean vibration) {
        actualSettings.setVibration(vibration);
        actualSettingsRepository.updateSettings(actualSettings);
    }

    boolean isAnimation() {
        return actualSettings.isAnimation();
    }

    void setAnimation(boolean animation) {
        actualSettings.setAnimation(animation);
        actualSettingsRepository.updateSettings(actualSettings);
    }

    String getTemperatureunit() {
        return actualSettings.getTemperatureUnit();
    }

    void setTemperatureunit(String temperatureunit) {
        actualSettings.setTemperatureUnit(temperatureunit);
        actualSettingsRepository.updateSettings(actualSettings);
    }

    boolean isShowteaalert() {
        return actualSettings.isShowTeaAlert();
    }

    void setShowteaalert(boolean showteaalert) {
        actualSettings.setShowTeaAlert(showteaalert);
        actualSettingsRepository.updateSettings(actualSettings);
    }

    boolean isMainratealert() {
        return actualSettings.isMainRateAlert();
    }

    void setMainratealert(boolean mainratealert) {
        actualSettings.setMainRateAlert(mainratealert);
        actualSettingsRepository.updateSettings(actualSettings);
    }

    boolean isSettingspermissionalert() {
        return actualSettings.isSettingsPermissionAlert();
    }

    void setSettingsPermissionAlert(boolean settingsPermissionAlert) {
        actualSettings.setSettingsPermissionAlert(settingsPermissionAlert);
        actualSettingsRepository.updateSettings(actualSettings);
    }

    void setDefaultSettings() {
        actualSettings.setMusicChoice("content://settings/system/ringtone");
        actualSettings.setMusicName("Default");
        actualSettings.setVibration(true);
        actualSettings.setAnimation(true);
        actualSettings.setTemperatureUnit("Celsius");
        actualSettings.setShowTeaAlert(true);
        actualSettings.setMainRateAlert(true);
        actualSettings.setMainRateCounter(0);
        actualSettings.setSort(0);

        actualSettingsRepository.updateSettings(actualSettings);
    }

    //Tea
    void deleteAllTeas() {
        teaRepository.deleteAllTeas();
    }
}
