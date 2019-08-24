package coolpharaoh.tee.speicher.tea.timer.daos;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import coolpharaoh.tee.speicher.tea.timer.entities.ActualSettings;


@Dao
public interface ActualSettingsDAO {
    @Insert
    void insert(ActualSettings... items);

    @Update
    void update(ActualSettings... items);

    @Query("SELECT * FROM settings LIMIT 1")
    ActualSettings getSettings();

    @Query("SELECT count(*) FROM settings")
    int getCountItems();
}