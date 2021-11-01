package coolpharaoh.tee.speicher.tea.timer.core.counter;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;

import coolpharaoh.tee.speicher.tea.timer.core.date.DateConverter;
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity(tableName = "counter", foreignKeys = @ForeignKey(entity = Tea.class, parentColumns = "tea_id", childColumns = "tea_id", onDelete = ForeignKey.CASCADE), indices = {@Index("tea_id")})
public class Counter {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "counter_id")
    private Long id;

    @ColumnInfo(name = "tea_id")
    private long teaId;

    @ColumnInfo(name = "day")
    private int day;

    @ColumnInfo(name = "week")
    private int week;

    @ColumnInfo(name = "month")
    private int month;

    @ColumnInfo(name = "overall")
    private long overall;

    @TypeConverters(DateConverter.class)
    @ColumnInfo(name = "day_date")
    private Date dayDate;

    @TypeConverters(DateConverter.class)
    @ColumnInfo(name = "week_date")
    private Date weekDate;

    @TypeConverters(DateConverter.class)
    @ColumnInfo(name = "month_date")
    private Date monthDate;

    // the constructor needs these parameters
    @SuppressWarnings("java:S107")
    @Ignore
    public Counter(final long teaId, final int day, final int week, final int month, final long overall,
                   final Date dayDate, final Date weekDate, final Date monthDate) {
        this.teaId = teaId;
        this.day = day;
        this.week = week;
        this.month = month;
        this.overall = overall;
        this.dayDate = dayDate;
        this.weekDate = weekDate;
        this.monthDate = monthDate;
    }
}
