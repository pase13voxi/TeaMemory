package coolpharaoh.tee.speicher.tea.timer.datatransfer.pojo;

import androidx.room.TypeConverters;

import java.util.Date;

import coolpharaoh.tee.speicher.tea.timer.entities.converter.DateConverter;

public class CounterPOJO {

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
