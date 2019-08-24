package coolpharaoh.tee.speicher.tea.timer.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import androidx.annotation.NonNull;

import java.util.Date;

import coolpharaoh.tee.speicher.tea.timer.entities.converter.DateConverter;


@Entity(tableName = "counter", foreignKeys = @ForeignKey(entity = Tea.class, parentColumns = "tea_id", childColumns = "tea_id", onDelete = ForeignKey.CASCADE), indices = {@Index("tea_id")})
public class Counter {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "counter_id")
    private Long id;
    @ColumnInfo(name = "tea_id")
    private long teaId;

    private int day;
    private int week;
    private int month;
    private long overall;
    @TypeConverters(DateConverter.class)
    private Date daydate;
    @TypeConverters(DateConverter.class)
    private Date weekdate;
    @TypeConverters(DateConverter.class)
    private Date monthdate;

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

    public Date getDaydate() {
        return daydate;
    }

    public void setDaydate(Date daydate) {
        this.daydate = daydate;
    }

    public Date getWeekdate() {
        return weekdate;
    }

    public void setWeekdate(Date weekdate) {
        this.weekdate = weekdate;
    }

    public Date getMonthdate() {
        return monthdate;
    }

    public void setMonthdate(Date monthdate) {
        this.monthdate = monthdate;
    }
}
