package coolpharaoh.tee.speicher.tea.timer.core.counter;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;

import coolpharaoh.tee.speicher.tea.timer.core.converter.DateConverter;
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea;


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
    @ColumnInfo(name = "daydate")
    private Date dayDate;

    @TypeConverters(DateConverter.class)
    @ColumnInfo(name = "weekdate")
    private Date weekDate;

    @TypeConverters(DateConverter.class)
    @ColumnInfo(name = "monthdate")
    private Date monthDate;

    public Counter(){}

    // the constructor needs these parameters
    @SuppressWarnings("java:S107")
    @Ignore
    public Counter(long teaId, int day, int week, int month, long overall, Date dayDate, Date weekDate, Date monthDate) {
        this.teaId = teaId;
        this.day = day;
        this.week = week;
        this.month = month;
        this.overall = overall;
        this.dayDate = dayDate;
        this.weekDate = weekDate;
        this.monthDate = monthDate;
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

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public long getOverall() {
        return overall;
    }

    public void setOverall(long overall) {
        this.overall = overall;
    }

    public Date getDayDate() {
        return dayDate;
    }

    public void setDayDate(Date dayDate) {
        this.dayDate = dayDate;
    }

    public Date getWeekDate() {
        return weekDate;
    }

    public void setWeekDate(Date weekDate) {
        this.weekDate = weekDate;
    }

    public Date getMonthDate() {
        return monthDate;
    }

    public void setMonthDate(Date monthDate) {
        this.monthDate = monthDate;
    }
}
