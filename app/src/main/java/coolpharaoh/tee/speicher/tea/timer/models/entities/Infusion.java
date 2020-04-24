package coolpharaoh.tee.speicher.tea.timer.models.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "infusion", foreignKeys =
@ForeignKey(entity = Tea.class, parentColumns = "tea_id", childColumns = "tea_id", onDelete = ForeignKey.CASCADE), indices = {@Index("tea_id")})
public class Infusion {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "infusion_id")
    private Long id;

    @ColumnInfo(name = "tea_id")
    private long teaId;

    @ColumnInfo(name = "infusionindex")
    private int infusionIndex;

    @ColumnInfo(name = "time")
    private String time;

    @ColumnInfo(name = "cooldowntime")
    private String coolDownTime;

    @ColumnInfo(name = "temperaturecelsius")
    private int temperatureCelsius;

    @ColumnInfo(name = "temperaturefahrenheit")
    private int temperatureFahrenheit;

    public Infusion(){}

    @Ignore
    public Infusion(long teaId, int infusionIndex, String time, String coolDownTime, int temperatureCelsius, int temperatureFahrenheit) {
        this.teaId = teaId;
        this.infusionIndex = infusionIndex;
        this.time = time;
        this.coolDownTime = coolDownTime;
        this.temperatureCelsius = temperatureCelsius;
        this.temperatureFahrenheit = temperatureFahrenheit;
    }

    @NonNull
    public Long getId() {
        return id;
    }

    public void setId(@NonNull Long id) {
        this.id = id;
    }

    public long getTeaId() {
        return teaId;
    }

    public void setTeaId(long teaId) {
        this.teaId = teaId;
    }

    public int getInfusionIndex() {
        return infusionIndex;
    }

    public void setInfusionIndex(int infusionIndex) {
        this.infusionIndex = infusionIndex;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCoolDownTime() {
        return coolDownTime;
    }

    public void setCoolDownTime(String coolDownTime) {
        this.coolDownTime = coolDownTime;
    }

    public int getTemperatureCelsius() {
        return temperatureCelsius;
    }

    public void setTemperatureCelsius(int temperatureCelsius) {
        this.temperatureCelsius = temperatureCelsius;
    }

    public int getTemperatureFahrenheit() {
        return temperatureFahrenheit;
    }

    public void setTemperatureFahrenheit(int temperatureFahrenheit) {
        this.temperatureFahrenheit = temperatureFahrenheit;
    }
}
