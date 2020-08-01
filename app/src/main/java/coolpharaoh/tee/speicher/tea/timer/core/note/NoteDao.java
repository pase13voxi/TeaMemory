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

    @Query("SELECT * FROM note WHERE tea_id = :id LIMIT 1")
    Note getNoteByTeaId(long id);
}