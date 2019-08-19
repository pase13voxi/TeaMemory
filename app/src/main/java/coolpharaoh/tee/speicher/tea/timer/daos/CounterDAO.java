package coolpharaoh.tee.speicher.tea.timer.daos;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import java.util.List;
import coolpharaoh.tee.speicher.tea.timer.entities.Counter;

@Dao
public interface CounterDAO {
    @Insert
    void insert(Counter... items);
    @Update
    void update(Counter... items);
    @Delete
    void delete(Counter item);
    @Query("SELECT * FROM counter")
    List<Counter> getItems();
    @Query("SELECT * FROM counter WHERE counter_id = :id")
    Counter getItemById(Long id);
}