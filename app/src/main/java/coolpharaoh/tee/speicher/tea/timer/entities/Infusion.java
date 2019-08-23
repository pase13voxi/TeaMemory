package coolpharaoh.tee.speicher.tea.timer.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity(tableName = "infusion",foreignKeys =
@ForeignKey(entity = Tea.class,parentColumns = "tea_id",childColumns = "tea_id", onDelete = ForeignKey.CASCADE), indices = {@Index("tea_id")})
public class Infusion {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "infusion_id")
    private Long id;
    @ColumnInfo(name = "tea_id")
    private long teaId;

    private int infusion;
    private String time;
    private String cooldowntime;
    private int temperaturecelsius;
    private int temperaturefahrenheit;

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

    public int getInfusion() {
        return infusion;
    }

    public void setInfusion(int infusion) {
        this.infusion = infusion;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCooldowntime() {
        return cooldowntime;
    }

    public void setCooldowntime(String cooldowntime) {
        this.cooldowntime = cooldowntime;
    }

    public int getTemperaturecelsius() {
        return temperaturecelsius;
    }

    public void setTemperaturecelsius(int temperaturecelsius) {
        this.temperaturecelsius = temperaturecelsius;
    }

    public int getTemperaturefahrenheit() {
        return temperaturefahrenheit;
    }

    public void setTemperaturefahrenheit(int temperaturefahrenheit) {
        this.temperaturefahrenheit = temperaturefahrenheit;
    }
}
