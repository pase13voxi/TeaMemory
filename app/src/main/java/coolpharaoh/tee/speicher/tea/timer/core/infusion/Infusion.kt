package coolpharaoh.tee.speicher.tea.timer.core.infusion

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea

@Entity(
    tableName = "infusion",
    foreignKeys = [ForeignKey(
        entity = Tea::class,
        parentColumns = arrayOf("tea_id"),
        childColumns = arrayOf("tea_id"),
        onDelete = CASCADE
    )],
    indices = [Index("tea_id")]
)
data class Infusion(
    @ColumnInfo(name = "infusion_id") @PrimaryKey(autoGenerate = true) var id: Long? = null,
    @ColumnInfo(name = "tea_id") var teaId: Long = 0,
    @ColumnInfo(name = "infusion_index") var infusionIndex: Int = 0,
    @ColumnInfo(name = "time") var time: String? = null,
    @ColumnInfo(name = "cool_down_time") var coolDownTime: String? = null,
    @ColumnInfo(name = "temperature_celsius") var temperatureCelsius: Int = 0,
    @ColumnInfo(name = "temperature_fahrenheit") var temperatureFahrenheit: Int = 0
) {
    @Ignore
    constructor(teaId: Long, infusionIndex: Int, time: String?, coolDownTime: String?,
                temperatureCelsius: Int, temperatureFahrenheit: Int) :
            this(null, teaId, infusionIndex, time, coolDownTime, temperatureCelsius,
                temperatureFahrenheit)
}