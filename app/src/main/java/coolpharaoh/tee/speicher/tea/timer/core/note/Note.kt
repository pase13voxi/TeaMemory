package coolpharaoh.tee.speicher.tea.timer.core.note

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea

@Entity(
    tableName = "note",
    foreignKeys = [ForeignKey(
        entity = Tea::class,
        parentColumns = arrayOf("tea_id"),
        childColumns = arrayOf("tea_id"),
        onDelete = CASCADE
    )],
    indices = [Index("tea_id")]
)
data class Note(
    @ColumnInfo(name = "note_id") @PrimaryKey(autoGenerate = true) var id: Long? = null,
    @ColumnInfo(name = "tea_id") var teaId: Long = 0,
    @ColumnInfo(name = "position") var position: Int = 0,
    @ColumnInfo(name = "header") var header: String? = null,
    @ColumnInfo(name = "description") var description: String? = null
) {
    @Ignore
    constructor(teaId: Long, position: Int, header: String?, description: String?) :
            this(null, teaId, position, header, description)
}