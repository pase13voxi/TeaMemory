package coolpharaoh.tee.speicher.tea.timer.core.note

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface NoteDao {
    @Insert
    fun insert(items: Note)

    @Update
    fun update(items: Note)

    @get:Query("SELECT * FROM note")
    val notes: List<Note>

    @Query("SELECT * FROM note WHERE tea_id = :teaId AND position = :position")
    fun getNoteByTeaIdAndPosition(teaId: Long, position: Int): Note?

    @Query("SELECT * FROM note WHERE tea_id = :teaId AND position >= 0 ORDER BY position")
    fun getNotesByTeaIdAndPositionBiggerZero(teaId: Long): List<Note>

    @Query("DELETE FROM note WHERE tea_id = :teaId AND position = :position")
    fun deleteNoteByTeaIdAndPosition(teaId: Long, position: Int)
}