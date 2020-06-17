package coolpharaoh.tee.speicher.tea.timer.views.settings;

import coolpharaoh.tee.speicher.tea.timer.models.daos.ActualSettingsDAO;
import coolpharaoh.tee.speicher.tea.timer.models.daos.TeaDAO;
import coolpharaoh.tee.speicher.tea.timer.models.database.TeaMemoryDatabase;
import coolpharaoh.tee.speicher.tea.timer.models.entities.ActualSettings;

class SettingsViewModel {

    private final ActualSettingsDAO actualSettingsDAO;
    private final TeaDAO teaDAO;

    private final ActualSettings actualSettings;

    public SettingsViewModel(TeaMemoryDatabase database) {

        actualSettingsDAO = database.getActualSettingsDAO();
        actualSettings = actualSettingsDAO.getSettings();

        teaDAO = database.getTeaDAO();
    }

    //Settings
    void setMusicchoice(String musicchoice) {
        actualSettings.setMusicChoice(musicchoice);
        actualSettingsDAO.update(actualSettings);
    }

    String getMusicname() {
        return actualSettings.getMusicName();
    }

    void setMusicname(String musicname) {
        actualSettings.setMusicName(musicname);
        actualSettingsDAO.update(actualSettings);
    }

    boolean isVibration() {
        return actualSettings.isVibration();
    }

    void setVibration(boolean vibration) {
        actualSettings.setVibration(vibration);
        actualSettingsDAO.update(actualSettings);
    }

    boolean isAnimation() {
        return actualSettings.isAnimation();
    }

    void setAnimation(boolean animation) {
        actualSettings.setAnimation(animation);
        actualSettingsDAO.update(actualSettings);
    }

    String getTemperatureunit() {
        return actualSettings.getTemperatureUnit();
    }

    void setTemperatureunit(String temperatureunit) {
        actualSettings.setTemperatureUnit(temperatureunit);
        actualSettingsDAO.update(actualSettings);
    }

    boolean isShowteaalert() {
        return actualSettings.isShowTeaAlert();
    }

    void setShowteaalert(boolean showteaalert) {
        actualSettings.setShowTeaAlert(showteaalert);
        actualSettingsDAO.update(actualSettings);
    }

    boolean isMainratealert() {
        return actualSettings.isMainRateAlert();
    }

    void setMainratealert(boolean mainratealert) {
        actualSettings.setMainRateAlert(mainratealert);
        actualSettingsDAO.update(actualSettings);
    }

    boolean isSettingspermissionalert() {
        return actualSettings.isSettingsPermissionAlert();
    }

    void setSettingsPermissionAlert(boolean settingsPermissionAlert) {
        actualSettings.setSettingsPermissionAlert(settingsPermissionAlert);
        actualSettingsDAO.update(actualSettings);
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

        actualSettingsDAO.update(actualSettings);
    }

    //Tea
    void deleteAllTeas() {
        teaDAO.deleteAll();
    }
}
