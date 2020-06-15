package coolpharaoh.tee.speicher.tea.timer.views.settings;

import coolpharaoh.tee.speicher.tea.timer.models.daos.ActualSettingsDAO;
import coolpharaoh.tee.speicher.tea.timer.models.daos.TeaDAO;
import coolpharaoh.tee.speicher.tea.timer.models.database.TeaMemoryDatabase;
import coolpharaoh.tee.speicher.tea.timer.models.entities.ActualSettings;

public class SettingsViewModel {

    private final ActualSettingsDAO actualSettingsDAO;
    private final TeaDAO teaDAO;

    private final ActualSettings actualSettings;

    public SettingsViewModel(TeaMemoryDatabase database) {

        actualSettingsDAO = database.getActualSettingsDAO();
        actualSettings = actualSettingsDAO.getSettings();

        teaDAO = database.getTeaDAO();
    }

    //Settings
    public void setMusicchoice(String musicchoice) {
        actualSettings.setMusicChoice(musicchoice);
        actualSettingsDAO.update(actualSettings);
    }

    public String getMusicname() {
        return actualSettings.getMusicName();
    }

    public void setMusicname(String musicname) {
        actualSettings.setMusicName(musicname);
        actualSettingsDAO.update(actualSettings);
    }

    public boolean isVibration() {
        return actualSettings.isVibration();
    }

    public void setVibration(boolean vibration) {
        actualSettings.setVibration(vibration);
        actualSettingsDAO.update(actualSettings);
    }

    public boolean isAnimation() {
        return actualSettings.isAnimation();
    }

    public void setAnimation(boolean animation) {
        actualSettings.setAnimation(animation);
        actualSettingsDAO.update(actualSettings);
    }

    public String getTemperatureunit() {
        return actualSettings.getTemperatureUnit();
    }

    public void setTemperatureunit(String temperatureunit) {
        actualSettings.setTemperatureUnit(temperatureunit);
        actualSettingsDAO.update(actualSettings);
    }

    public boolean isShowteaalert() {
        return actualSettings.isShowTeaAlert();
    }

    public void setShowteaalert(boolean showteaalert) {
        actualSettings.setShowTeaAlert(showteaalert);
        actualSettingsDAO.update(actualSettings);
    }

    public boolean isMainratealert() {
        return actualSettings.isMainRateAlert();
    }

    public void setMainratealert(boolean mainratealert) {
        actualSettings.setMainRateAlert(mainratealert);
        actualSettingsDAO.update(actualSettings);
    }

    public boolean isSettingspermissionalert(){
        return actualSettings.isSettingsPermissionAlert();
    }

    public void setSettingsPermissionAlert(boolean settingsPermissionAlert) {
        actualSettings.setSettingsPermissionAlert(settingsPermissionAlert);
        actualSettingsDAO.update(actualSettings);
    }

    public void setDefaultSettings() {
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
    public void deleteAllTeas() {
        teaDAO.deleteAll();
    }
}
