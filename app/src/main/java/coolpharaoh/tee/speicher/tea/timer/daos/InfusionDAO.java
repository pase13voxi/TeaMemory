package coolpharaoh.tee.speicher.tea.timer.daos;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.entities.Infusion;

@Dao
public interface InfusionDAO {
    @Insert
    void insert(Infusion... items);

    @Update
    void update(Infusion... items);

    @Delete
    void delete(Infusion item);

    @Query("SELECT * FROM infusion")
    List<Infusion> getInfusions();

    @Query("SELECT * FROM infusion WHERE infusion_id = :id")
    Infusion getInfusionById(Long id);

    @Query("SELECT * FROM infusion WHERE tea_id = :id")
    List<Infusion> getInfusionsByTeaId(Long id);
}