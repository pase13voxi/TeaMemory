package coolpharaoh.tee.speicher.tea.timer.core.actualsettings;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


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