package coolpharaoh.tee.speicher.tea.timer.core.infusion;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
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

    @Ignore
    public Infusion(long teaId, int infusionIndex, String time, String coolDownTime, int temperatureCelsius, int temperatureFahrenheit) {
        this.teaId = teaId;
        this.infusionIndex = infusionIndex;
        this.time = time;
        this.coolDownTime = coolDownTime;
        this.temperatureCelsius = temperatureCelsius;
        this.temperatureFahrenheit = temperatureFahrenheit;
    }
}
