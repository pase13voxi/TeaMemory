package coolpharaoh.tee.speicher.tea.timer.core.tea

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TeaDao {
    @Insert
    fun insert(items: Tea): Long

    @Update
    fun update(items: Tea)

    @Query("DELETE FROM tea")
    fun deleteAll()

    @Query("SELECT * FROM tea")
    fun getTeas(): List<Tea>

    @Query("SELECT * FROM tea ORDER BY LOWER(date) DESC")
    fun getTeasOrderByActivity(): List<Tea>

    @Query("SELECT * FROM tea WHERE in_stock = 1 ORDER BY LOWER(date) DESC")
    fun getTeasInStockOrderByActivity(): List<Tea>

    @Query("SELECT * FROM tea ORDER BY LOWER(name)")
    fun getTeasOrderByAlphabetic(): List<Tea>

    @Query("SELECT * FROM tea WHERE in_stock = 1 ORDER BY LOWER(name)")
    fun getTeasInStockOrderByAlphabetic(): List<Tea>

    @Query("SELECT * FROM tea ORDER BY variety, LOWER(name)")
    fun getTeasOrderByVariety(): List<Tea>

    @Query("SELECT * FROM tea WHERE in_stock = 1 ORDER BY variety, LOWER(name)")
    fun getTeasInStockOrderByVariety(): List<Tea>

    @Query("SELECT * FROM tea ORDER BY rating DESC, LOWER(name)")
    fun getTeasOrderByRating(): List<Tea>

    @Query("SELECT * FROM tea WHERE in_stock = 1 ORDER BY rating DESC, LOWER(name)")
    fun getTeasInStockOrderByRating(): List<Tea>

    @Query("SELECT * FROM tea WHERE tea_id = :id")
    fun getTeaById(id: Long): Tea?

    @Query("SELECT * FROM tea WHERE in_stock = 1 ORDER BY RANDOM() LIMIT 1")
    fun getRandomTeaInStock(): Tea?

    @Query("DELETE FROM tea WHERE tea_id = :id")
    fun deleteTeaById(id: Long)

    @Query("SELECT * FROM tea WHERE name LIKE ('%' || :searchString || '%') ORDER BY LOWER(name)")
    fun getTeasBySearchString(searchString: String?): List<Tea?>?
}