package coolpharaoh.tee.speicher.tea.timer.core.actualsettings;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "settings")
public class ActualSettings {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "settings_id")
    private Long id;

    @ColumnInfo(name = "musicchoice")
    private String musicChoice;

    @ColumnInfo(name = "musicname")
    private String musicName;

    @ColumnInfo(name = "vibration")
    private boolean vibration;

    @ColumnInfo(name = "animation")
    private boolean animation;

    @ColumnInfo(name = "temperatureunit")
    private String temperatureUnit;

    @ColumnInfo(name = "showteaalert")
    private boolean showTeaAlert;

    @ColumnInfo(name = "mainratealert")
    private boolean mainRateAlert;

    @ColumnInfo(name = "mainratecounter")
    private int mainRateCounter;

    @ColumnInfo(name = "settingspermissionalert")
    private boolean settingsPermissionAlert;

    //0 = sort by Date, 1 = sort alphabethically, 2 = sort by variety
    @ColumnInfo(name = "sort")
    private int sort;

    @NonNull
    public Long getId() {
        return id;
    }

    public void setId(@NonNull Long id) {
        this.id = id;
    }

    public String getMusicChoice() {
        return musicChoice;
    }

    public void setMusicChoice(String musicChoice) {
        this.musicChoice = musicChoice;
    }

    public String getMusicName() {
        return musicName;
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }

    public boolean isVibration() {
        return vibration;
    }

    public void setVibration(boolean vibration) {
        this.vibration = vibration;
    }

    public boolean isAnimation() {
        return animation;
    }

    public void setAnimation(boolean animation) {
        this.animation = animation;
    }

    public String getTemperatureUnit() {
        return temperatureUnit;
    }

    public void setTemperatureUnit(String temperatureUnit) {
        this.temperatureUnit = temperatureUnit;
    }

    public boolean isShowTeaAlert() {
        return showTeaAlert;
    }

    public void setShowTeaAlert(boolean showTeaAlert) {
        this.showTeaAlert = showTeaAlert;
    }

    public boolean isMainRateAlert() {
        return mainRateAlert;
    }

    public void setMainRateAlert(boolean mainRateAlert) {
        this.mainRateAlert = mainRateAlert;
    }

    public int getMainRateCounter() {
        return mainRateCounter;
    }

    public void setMainRateCounter(int mainRateCounter) {
        this.mainRateCounter = mainRateCounter;
    }

    public boolean isSettingsPermissionAlert() {
        return settingsPermissionAlert;
    }

    public void setSettingsPermissionAlert(boolean settingsPermissionAlert) {
        this.settingsPermissionAlert = settingsPermissionAlert;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }
}
