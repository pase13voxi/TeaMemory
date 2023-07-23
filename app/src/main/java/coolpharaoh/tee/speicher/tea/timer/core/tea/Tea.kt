package coolpharaoh.tee.speicher.tea.timer.core.tea

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import coolpharaoh.tee.speicher.tea.timer.core.date.DateConverter
import java.util.Date

@Entity(tableName = "tea")
data class Tea(
    @ColumnInfo(name = "tea_id") @PrimaryKey(autoGenerate = true) var id: Long? = null,
    @ColumnInfo(name = "name") var name: String? = null,
    @ColumnInfo(name = "variety") var variety: String? = null,
    @ColumnInfo(name = "amount") var amount: Double = 0.0,
    @ColumnInfo(name = "amount_kind") var amountKind: String? = null,
    @ColumnInfo(name = "color") var color: Int = 0,
    @ColumnInfo(name = "rating") var rating: Int = 0,
    @ColumnInfo(name = "in_stock") var inStock: Boolean = false,
    @ColumnInfo(name = "next_infusion") var nextInfusion: Int = 0,
    @ColumnInfo(name = "date") @field:TypeConverters(DateConverter::class) var date: Date? = null
) {
    @Ignore
    constructor(name: String?, variety: String?, amount: Double, amountKind: String?, color: Int,
                nextInfusion: Int, date: Date?) :
            this(null, name, variety, amount, amountKind, color, 0, false,
                nextInfusion, date)
}