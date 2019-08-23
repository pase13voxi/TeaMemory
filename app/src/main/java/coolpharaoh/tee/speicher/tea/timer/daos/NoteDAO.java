package coolpharaoh.tee.speicher.tea.timer.daos;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import coolpharaoh.tee.speicher.tea.timer.entities.Note;


@Dao
public interface NoteDAO {
    @Insert
    void insert(Note... items);

    @Update
    void update(Note... items);

    @Delete
    void delete(Note item);

    @Query("SELECT * FROM note WHERE tea_id = :id LIMIT 1")
    Note getNoteByTeaId(long id);
}