package coolpharaoh.tee.speicher.tea.timer.viewmodels;

import android.arch.persistence.room.Room;
import android.content.Context;

import coolpharaoh.tee.speicher.tea.timer.daos.ActualSettingsDAO;
import coolpharaoh.tee.speicher.tea.timer.daos.TeaDAO;
import coolpharaoh.tee.speicher.tea.timer.database.TeaMemoryDatabase;
import coolpharaoh.tee.speicher.tea.timer.entities.ActualSettings;

public class SettingsViewModel {

    private ActualSettingsDAO mActualSettingsDAO;
    private TeaDAO mTeaDAO;

    private ActualSettings mActualSettings;

    public SettingsViewModel(Context context) {
        TeaMemoryDatabase database = Room.databaseBuilder(context, TeaMemoryDatabase.class, "teamemory")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        mActualSettingsDAO = database.getActualSettingsDAO();
        mActualSettings = mActualSettingsDAO.getItemById(1l);

        mTeaDAO = database.getTeaDAO();
    }

    //Settings
    public void setMusicchoice(String musicchoice) {
        mActualSettings.setMusicchoice(musicchoice);
        mActualSettingsDAO.update(mActualSettings);
    }

    public String getMusicname() {
        return mActualSettings.getMusicname();
    }

    public void setMusicname(String musicname) {
        mActualSettings.setMusicchoice(musicname);
        mActualSettingsDAO.update(mActualSettings);
    }

    public boolean isVibration() {
        return mActualSettings.isVibration();
    }

    public void setVibration(boolean vibration) {
        mActualSettings.setVibration(vibration);
        mActualSettingsDAO.update(mActualSettings);
    }

    public boolean isNotification() {
        return mActualSettings.isNotification();
    }

    public void setNotification(boolean notification) {
        mActualSettings.setNotification(notification);
        mActualSettingsDAO.update(mActualSettings);
    }

    public boolean isAnimation() {
        return mActualSettings.isAnimation();
    }

    public void setAnimation(boolean animation) {
        mActualSettings.setAnimation(animation);
        mActualSettingsDAO.update(mActualSettings);
    }

    public String getTemperatureunit() {
        return mActualSettings.getTemperatureunit();
    }

    public void setTemperatureunit(String temperatureunit) {
        mActualSettings.setTemperatureunit(temperatureunit);
        mActualSettingsDAO.update(mActualSettings);
    }

    public boolean isShowteaalert() {
        return mActualSettings.isShowteaalert();
    }

    public void setShowteaalert(boolean showteaalert) {
        mActualSettings.setShowteaalert(showteaalert);
        mActualSettingsDAO.update(mActualSettings);
    }

    public boolean isMainproblemalert() {
        return mActualSettings.isMainproblemalert();
    }

    public void setMainproblemalert(boolean mainproblemalert) {
        mActualSettings.setMainproblemalert(mainproblemalert);
        mActualSettingsDAO.update(mActualSettings);
    }

    public boolean isMainratealert() {
        return mActualSettings.isMainratealert();
    }

    public void setMainratealert(boolean mainratealert) {
        mActualSettings.setMainratealert(mainratealert);
        mActualSettingsDAO.update(mActualSettings);
    }

    public void setDefaultSettings() {
        mActualSettings.setMusicchoice("content://settings/system/ringtone");
        mActualSettings.setMusicname("Default");
        mActualSettings.setVibration(false);
        mActualSettings.setNotification(true);
        mActualSettings.setAnimation(true);
        mActualSettings.setTemperatureunit("Celsius");
        mActualSettings.setShowteaalert(true);
        mActualSettings.setMainproblemalert(true);
        mActualSettings.setMainratealert(true);
        mActualSettings.setMainratecounter(0);
        mActualSettings.setSort(0);

        mActualSettingsDAO.update(mActualSettings);
    }

    //Tea
    public void deleteAllTeas() {
        mTeaDAO.deleteAll();
    }
}
