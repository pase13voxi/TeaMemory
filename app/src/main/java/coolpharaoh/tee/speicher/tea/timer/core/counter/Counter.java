package coolpharaoh.tee.speicher.tea.timer.core.counter;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;
import java.util.Objects;
import java.util.stream.Stream;

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

    @ColumnInfo(name = "week")
    private int week;

    @ColumnInfo(name = "month")
    private int month;

    @ColumnInfo(name = "year")
    private int year;

    @ColumnInfo(name = "overall")
    private long overall;

    @TypeConverters(DateConverter.class)
    @ColumnInfo(name = "week_date")
    private Date weekDate;

    @TypeConverters(DateConverter.class)
    @ColumnInfo(name = "month_date")
    private Date monthDate;

    @TypeConverters(DateConverter.class)
    @ColumnInfo(name = "year_date")
    private Date yearDate;

    // the constructor needs these parameters
    @SuppressWarnings("java:S107")
    @Ignore
    public Counter(final long teaId, final int week, final int month, final  int year, final long overall,
                   final Date weekDate, final Date monthDate, final Date yearDate) {
        this.teaId = teaId;
        this.week = week;
        this.month = month;
        this.year = year;
        this.overall = overall;
        this.weekDate = weekDate;
        this.monthDate = monthDate;
        this.yearDate = yearDate;
    }

    public boolean hasEmptyFields() {
        return Stream.of(weekDate, monthDate, yearDate).anyMatch(Objects::isNull);
    }
}
