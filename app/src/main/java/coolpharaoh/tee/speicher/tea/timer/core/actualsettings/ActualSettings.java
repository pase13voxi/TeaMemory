package coolpharaoh.tee.speicher.tea.timer.core.actualsettings;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
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
}
