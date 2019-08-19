package coolpharaoh.tee.speicher.tea.timer.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "settings")
public class ActualSettings {
    @PrimaryKey
    @NonNull
    private Long id;

    private String musicchoice;
    private String musicname;
    private boolean vibration;
    private boolean notification;
    private boolean animation;
    private String temperatureunit;
    private boolean showteaalert;
    private boolean mainProblemAlert;
    private boolean mainRateAlert;
    private int mainRatecounter;
    //0 = sort by Date, 1 = sort alphabethically, 2 = sort by variety
    private int sort;

    @NonNull
    public Long getId() {
        return id;
    }

    public void setId(@NonNull Long id) {
        this.id = id;
    }

    public String getMusicchoice() {
        return musicchoice;
    }

    public void setMusicchoice(String musicchoice) {
        this.musicchoice = musicchoice;
    }

    public String getMusicname() {
        return musicname;
    }

    public void setMusicname(String musicname) {
        this.musicname = musicname;
    }

    public boolean isVibration() {
        return vibration;
    }

    public void setVibration(boolean vibration) {
        this.vibration = vibration;
    }

    public boolean isNotification() {
        return notification;
    }

    public void setNotification(boolean notification) {
        this.notification = notification;
    }

    public boolean isAnimation() {
        return animation;
    }

    public void setAnimation(boolean animation) {
        this.animation = animation;
    }

    public String getTemperatureunit() {
        return temperatureunit;
    }

    public void setTemperatureunit(String temperatureunit) {
        this.temperatureunit = temperatureunit;
    }

    public boolean isShowteaalert() {
        return showteaalert;
    }

    public void setShowteaalert(boolean showteaalert) {
        this.showteaalert = showteaalert;
    }

    public boolean isMainProblemAlert() {
        return mainProblemAlert;
    }

    public void setMainProblemAlert(boolean mainProblemAlert) {
        this.mainProblemAlert = mainProblemAlert;
    }

    public boolean isMainRateAlert() {
        return mainRateAlert;
    }

    public void setMainRateAlert(boolean mainRateAlert) {
        this.mainRateAlert = mainRateAlert;
    }

    public int getMainRatecounter() {
        return mainRatecounter;
    }

    public void setMainRatecounter(int mainRatecounter) {
        this.mainRatecounter = mainRatecounter;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }
}
