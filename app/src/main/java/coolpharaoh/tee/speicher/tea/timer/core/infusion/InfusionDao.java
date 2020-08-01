package coolpharaoh.tee.speicher.tea.timer.core.infusion;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface InfusionDao {
    @Insert
    void insert(Infusion items);

    @Query("SELECT * FROM infusion")
    List<Infusion> getInfusions();

    @Query("SELECT * FROM infusion WHERE tea_id = :id")
    List<Infusion> getInfusionsByTeaId(long id);

    @Query("DELETE FROM infusion WHERE tea_id = :id")
    void deleteInfusionsByTeaId(long id);
}