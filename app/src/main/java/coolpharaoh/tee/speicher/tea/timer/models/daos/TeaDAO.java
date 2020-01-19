package coolpharaoh.tee.speicher.tea.timer.models.daos;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.models.entities.Tea;

@Dao
public interface TeaDAO {
    @Insert
    long insert(Tea items);

    @Update
    void update(Tea items);

    @Delete
    void delete(Tea item);

    @Query("DELETE FROM tea")
    void deleteAll();

    @Query("SELECT * FROM tea")
    List<Tea> getTeas();

    @Query("SELECT * FROM tea ORDER BY LOWER(date) DESC")
    List<Tea> getTeasOrderByActivity();

    @Query("SELECT * FROM tea ORDER BY LOWER(name) ASC")
    List<Tea> getTeasOrderByAlphabetic();

    @Query("SELECT * FROM tea ORDER BY variety ASC")
    List<Tea> getTeasOrderByVariety();

    @Query("SELECT * FROM tea WHERE tea_id = :id")
    Tea getTeaById(long id);
}
