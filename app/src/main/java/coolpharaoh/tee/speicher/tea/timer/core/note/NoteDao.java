package coolpharaoh.tee.speicher.tea.timer.core.note;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;


@Dao
public interface NoteDao {
    @Insert
    void insert(Note items);

    @Update
    void update(Note items);

    @Query("SELECT * FROM note")
    List<Note> getNotes();

    @Query("SELECT * FROM note WHERE tea_id = :teaId")
    Note getNotesByTeaId(long teaId);

    @Query("SELECT * FROM note WHERE tea_id = :teaId AND position = :position")
    Note getNoteByTeaIdAndPosition(long teaId, int position);

    @Query("SELECT * FROM note WHERE tea_id = :teaId AND position >= 0 ORDER BY position")
    List<Note> getNotesByTeaIdAndPositionBiggerZero(long teaId);

    @Query("DELETE FROM note WHERE tea_id = :teaId AND position = :position")
    void deleteNoteByTeaIdAndPosition(long teaId, int position);
}