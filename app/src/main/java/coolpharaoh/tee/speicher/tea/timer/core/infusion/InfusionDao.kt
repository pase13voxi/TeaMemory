package coolpharaoh.tee.speicher.tea.timer.core.infusion

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface InfusionDao {
    @Insert
    fun insert(items: Infusion)

    @Query("SELECT * FROM infusion")
    fun getInfusions(): List<Infusion>

    @Query("SELECT * FROM infusion WHERE tea_id = :id")
    fun getInfusionsByTeaId(id: Long): List<Infusion>

    @Query("DELETE FROM infusion WHERE tea_id = :id")
    fun deleteInfusionsByTeaId(id: Long)
}