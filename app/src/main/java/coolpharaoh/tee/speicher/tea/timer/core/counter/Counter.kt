package coolpharaoh.tee.speicher.tea.timer.core.counter

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import coolpharaoh.tee.speicher.tea.timer.core.date.DateConverter
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea
import java.util.Date
import java.util.Objects
import java.util.stream.Stream

@Entity(
    tableName = "counter",
    foreignKeys = [ForeignKey(
        entity = Tea::class,
        parentColumns = arrayOf("tea_id"),
        childColumns = arrayOf("tea_id"),
        onDelete = CASCADE
    )],
    indices = [Index("tea_id")]
)
data class Counter(
    @ColumnInfo(name = "counter_id") @PrimaryKey(autoGenerate = true) var id: Long? = null,
    @ColumnInfo(name = "tea_id") var teaId: Long = 0,
    @ColumnInfo(name = "week") var week: Int = 0,
    @ColumnInfo(name = "month") var month: Int = 0,
    @ColumnInfo(name = "year") var year: Int = 0,
    @ColumnInfo(name = "overall") var overall: Long = 0,
    @ColumnInfo(name = "week_date") @field:TypeConverters(DateConverter::class)
    var weekDate: Date? = null,
    @ColumnInfo(name = "month_date") @field:TypeConverters(DateConverter::class)
    var monthDate: Date? = null,
    @ColumnInfo(name = "year_date") @field:TypeConverters(DateConverter::class)
    var yearDate: Date? = null
) {

    @Ignore
    constructor(teaId: Long, week: Int, month: Int, year: Int, overall: Long, weekDate: Date?,
                monthDate: Date?, yearDate: Date?) :
            this(null, teaId, week, month, year, overall, weekDate, monthDate, yearDate)

    fun hasEmptyFields(): Boolean {
        return Stream.of(weekDate, monthDate, yearDate)
            .anyMatch { obj: Date? -> Objects.isNull(obj) }
    }
}