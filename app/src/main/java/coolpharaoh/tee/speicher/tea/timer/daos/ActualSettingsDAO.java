package coolpharaoh.tee.speicher.tea.timer.daos;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
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

    @Query("SELECT count(*) FROM settings")
    int getCountItems();
}