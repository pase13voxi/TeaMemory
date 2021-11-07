package coolpharaoh.tee.speicher.tea.timer.core.actual_settings;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

// Could be removed after the successful migration (In half a year 1.6.2022)
@Dao
public interface ActualSettingsDao {
    @Insert
    void insert(ActualSettings... items);

    @Update
    void update(ActualSettings... items);

    @Query("SELECT * FROM settings LIMIT 1")
    ActualSettings getSettings();

    @Query("SELECT count(*) FROM settings")
    int getCountItems();
}