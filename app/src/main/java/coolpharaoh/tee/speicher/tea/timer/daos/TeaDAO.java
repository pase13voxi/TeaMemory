package coolpharaoh.tee.speicher.tea.timer.daos;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.entities.Tea;

@Dao
public interface TeaDAO {
    @Insert
    void insert(Tea... items);

    @Update
    void update(Tea... items);

    @Delete
    void delete(Tea item);

    @Query("DELETE FROM tea")
    void deleteAll();

    @Query("SELECT * from tea")
    List<Tea> getTeas();

    @Query("SELECT * from tea ORDER BY LOWER(date) DESC")
    List<Tea> getTeasOrderByActivity();

    @Query("SELECT * from tea ORDER BY LOWER(name) ASC")
    List<Tea> getTeasOrderByAlphabetic();

    @Query("SELECT * from tea ORDER BY variety ASC")
    List<Tea> getTeasOrderByVariety();

    @Query("SELECT * FROM tea WHERE tea_id = :id")
    Tea getTeaById(Long id);

    @Query("SELECT * from tea ORDER BY LOWER(date) DESC LIMIT 1")
    Tea getLastEditedTea();
}
