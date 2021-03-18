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

    @ColumnInfo(name = "darkmode")
    private String darkMode;

    @ColumnInfo(name = "mainratealert")
    private boolean mainRateAlert;

    @ColumnInfo(name = "mainratecounter")
    private int mainRateCounter;

    @ColumnInfo(name = "mainupdatealert")
    private boolean mainUpdateAlert;

    @ColumnInfo(name = "showteaalert")
    private boolean showTeaAlert;

    @ColumnInfo(name = "settingspermissionalert")
    private boolean settingsPermissionAlert;

    //0 = sort by Date, 1 = alphabethically, 2 = by variety, 3 = rating
    @ColumnInfo(name = "sort")
    private int sort;
}
