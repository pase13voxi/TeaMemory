package coolpharaoh.tee.speicher.tea.timer.viewmodels;

import android.content.Context;

import androidx.room.Room;

import coolpharaoh.tee.speicher.tea.timer.daos.ActualSettingsDAO;
import coolpharaoh.tee.speicher.tea.timer.daos.TeaDAO;
import coolpharaoh.tee.speicher.tea.timer.database.TeaMemoryDatabase;
import coolpharaoh.tee.speicher.tea.timer.entities.ActualSettings;

public class SettingsViewModel {

    private ActualSettingsDAO actualSettingsDAO;
    private TeaDAO teaDAO;

    private ActualSettings actualSettings;

    public SettingsViewModel(Context context) {
        TeaMemoryDatabase database = Room.databaseBuilder(context, TeaMemoryDatabase.class, "teamemory")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        actualSettingsDAO = database.getActualSettingsDAO();
        actualSettings = actualSettingsDAO.getSettings();

        teaDAO = database.getTeaDAO();
    }

    //Settings
    public void setMusicchoice(String musicchoice) {
        actualSettings.setMusicchoice(musicchoice);
        actualSettingsDAO.update(actualSettings);
    }

    public String getMusicname() {
        return actualSettings.getMusicname();
    }

    public void setMusicname(String musicname) {
        actualSettings.setMusicname(musicname);
        actualSettingsDAO.update(actualSettings);
    }

    public boolean isVibration() {
        return actualSettings.isVibration();
    }

    public void setVibration(boolean vibration) {
        actualSettings.setVibration(vibration);
        actualSettingsDAO.update(actualSettings);
    }

    public boolean isNotification() {
        return actualSettings.isNotification();
    }

    public void setNotification(boolean notification) {
        actualSettings.setNotification(notification);
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
        return actualSettings.getTemperatureunit();
    }

    public void setTemperatureunit(String temperatureunit) {
        actualSettings.setTemperatureunit(temperatureunit);
        actualSettingsDAO.update(actualSettings);
    }

    public boolean isShowteaalert() {
        return actualSettings.isShowteaalert();
    }

    public void setShowteaalert(boolean showteaalert) {
        actualSettings.setShowteaalert(showteaalert);
        actualSettingsDAO.update(actualSettings);
    }

    public boolean isMainproblemalert() {
        return actualSettings.isMainproblemalert();
    }

    public void setMainproblemalert(boolean mainproblemalert) {
        actualSettings.setMainproblemalert(mainproblemalert);
        actualSettingsDAO.update(actualSettings);
    }

    public boolean isMainratealert() {
        return actualSettings.isMainratealert();
    }

    public void setMainratealert(boolean mainratealert) {
        actualSettings.setMainratealert(mainratealert);
        actualSettingsDAO.update(actualSettings);
    }

    public void setDefaultSettings() {
        actualSettings.setMusicchoice("content://settings/system/ringtone");
        actualSettings.setMusicname("Default");
        actualSettings.setVibration(false);
        actualSettings.setNotification(true);
        actualSettings.setAnimation(true);
        actualSettings.setTemperatureunit("Celsius");
        actualSettings.setShowteaalert(true);
        actualSettings.setMainproblemalert(true);
        actualSettings.setMainratealert(true);
        actualSettings.setMainratecounter(0);
        actualSettings.setSort(0);

        actualSettingsDAO.update(actualSettings);
    }

    //Tea
    public void deleteAllTeas() {
        teaDAO.deleteAll();
    }
}
