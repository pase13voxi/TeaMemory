package coolpharaoh.tee.speicher.tea.timer.core.tea;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TeaDao {
    @Insert
    long insert(Tea items);

    @Update
    void update(Tea items);

    @Query("DELETE FROM tea")
    void deleteAll();

    @Query("SELECT * FROM tea")
    List<Tea> getTeas();

    @Query("SELECT * FROM tea ORDER BY LOWER(date) DESC")
    List<Tea> getTeasOrderByActivity();

    @Query("SELECT * FROM tea WHERE in_stock = 1 ORDER BY LOWER(date) DESC")
    List<Tea> getTeasInStockOrderByActivity();

    @Query("SELECT * FROM tea ORDER BY LOWER(name)")
    List<Tea> getTeasOrderByAlphabetic();

    @Query("SELECT * FROM tea WHERE in_stock = 1 ORDER BY LOWER(name)")
    List<Tea> getTeasInStockOrderByAlphabetic();

    @Query("SELECT * FROM tea ORDER BY variety, LOWER(name)")
    List<Tea> getTeasOrderByVariety();

    @Query("SELECT * FROM tea WHERE in_stock = 1 ORDER BY variety, LOWER(name)")
    List<Tea> getTeasInStockOrderByVariety();

    @Query("SELECT * FROM tea ORDER BY rating DESC, LOWER(name)")
    List<Tea> getTeasOrderByRating();

    @Query("SELECT * FROM tea WHERE in_stock = 1 ORDER BY rating DESC, LOWER(name)")
    List<Tea> getTeasInStockOrderByRating();

    @Query("SELECT * FROM tea WHERE tea_id = :id")
    Tea getTeaById(long id);

    @Query("SELECT * FROM tea WHERE in_stock = 1 ORDER BY RANDOM() LIMIT 1")
    Tea getRandomTeaInStock();

    @Query("DELETE FROM tea WHERE tea_id = :id")
    void deleteTeaById(long id);

    @Query("SELECT * FROM tea WHERE name LIKE ('%' || :searchString || '%') ORDER BY LOWER(name)")
    List<Tea> getTeasBySearchString(String searchString);
}
