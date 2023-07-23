package coolpharaoh.tee.speicher.tea.timer.views.export_import.data_transform.pojo

import androidx.room.ColumnInfo

class StatisticsPOJO {
    @JvmField
    @ColumnInfo(name = "name")
    var teaName: String? = null

    @JvmField
    @ColumnInfo(name = "color")
    var teaColor: Int = 0

    @JvmField
    @ColumnInfo(name = "counter")
    var counter: Long = 0
}