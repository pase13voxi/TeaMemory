package coolpharaoh.tee.speicher.tea.timer.daos;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;
import coolpharaoh.tee.speicher.tea.timer.entities.ActualSettings;


@Dao
public interface ActualSettingsDAO {
    @Insert
    void insert(ActualSettings... items);
    @Update
    void update(ActualSettings... items);
    @Delete
    void delete(ActualSettings item);

    @Query("SELECT * FROM settings")
    List<ActualSettings> getItems();

    @Query("SELECT * FROM settings WHERE id = :id")
    ActualSettings getItemById(Long id);

    @Query("SELECT * FROM settings LIMIT 1")
    ActualSettings getSettings();

    @Query("SELECT count(*) FROM settings")
    int getCountItems();
}