package coolpharaoh.tee.speicher.tea.timer.daos;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

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