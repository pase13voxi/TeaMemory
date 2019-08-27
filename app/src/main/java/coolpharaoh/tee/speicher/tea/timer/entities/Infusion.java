package coolpharaoh.tee.speicher.tea.timer.entities;

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

    private int infusionindex;
    private String time;
    private String cooldowntime;
    private int temperaturecelsius;
    private int temperaturefahrenheit;

    public Infusion(){}

    @Ignore
    public Infusion(long teaId, int infusionindex, String time, String cooldowntime, int temperaturecelsius, int temperaturefahrenheit) {
        this.teaId = teaId;
        this.infusionindex = infusionindex;
        this.time = time;
        this.cooldowntime = cooldowntime;
        this.temperaturecelsius = temperaturecelsius;
        this.temperaturefahrenheit = temperaturefahrenheit;
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

    public int getInfusionindex() {
        return infusionindex;
    }

    public void setInfusionindex(int infusionindex) {
        this.infusionindex = infusionindex;
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
