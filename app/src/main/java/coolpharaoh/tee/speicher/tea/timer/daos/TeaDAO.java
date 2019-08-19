package coolpharaoh.tee.speicher.tea.timer.daos;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import coolpharaoh.tee.speicher.tea.timer.entities.Tea;
import java.util.List;

@Dao
public interface TeaDAO {
    @Insert
    void insert(Tea... items);
    @Update
    void update(Tea... items);
    @Delete
    void delete(Tea item);

    @Query("DELETE FROM tea")
    public void deleteAll();

    @Query("SELECT * from tea")
    List<Tea> getItems();

    @Query("SELECT * from tea ORDER BY LOWER(date) ASC")
    List<Tea> getItemsActivity();

    @Query("SELECT * from tea ORDER BY LOWER(name) ASC")
    List<Tea> getItemsAlphabetic();

    @Query("SELECT * from tea ORDER BY LOWER(variety) ASC")
    List<Tea> getItemsVariety();

    @Query("SELECT * FROM tea WHERE tea_id = :id")
    Tea getItemById(Long id);
}