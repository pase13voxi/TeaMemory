package coolpharaoh.tee.speicher.tea.timer.models.datatransfer.pojo;

import androidx.room.ColumnInfo;

public class StatisticsPOJO {
    @ColumnInfo(name = "name")
    public String teaname;

    @ColumnInfo(name = "color")
    public int teacolor;

    @ColumnInfo(name = "counter")
    public long counter;
}