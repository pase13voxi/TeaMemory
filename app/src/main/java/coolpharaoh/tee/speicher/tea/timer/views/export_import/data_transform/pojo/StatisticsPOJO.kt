package coolpharaoh.tee.speicher.tea.timer.views.export_import.data_transform.pojo;

import androidx.room.ColumnInfo;

public class StatisticsPOJO {
    @ColumnInfo(name = "name")
    public String teaName;

    @ColumnInfo(name = "color")
    public int teaColor;

    @ColumnInfo(name = "counter")
    public long counter;
}