package coolpharaoh.tee.speicher.tea.timer.core.counter

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import coolpharaoh.tee.speicher.tea.timer.views.export_import.data_transform.pojo.StatisticsPOJO

@Dao
interface CounterDao {
    @Insert
    fun insert(items: Counter): Long

    @Update
    fun update(items: Counter)

    @Query("SELECT * FROM counter")
    fun getCounters(): List<Counter>

    @Query("SELECT * FROM counter WHERE tea_id = :id")
    fun getCounterByTeaId(id: Long): Counter?

    @Query("""SELECT tea.name, tea.color, counter.overall as counter FROM tea INNER JOIN counter 
              ON tea.tea_id = counter.tea_id 
              WHERE counter.overall > 0 
              ORDER BY counter.overall ASC""")
    fun getTeaCounterOverall(): List<StatisticsPOJO>

    @Query("""SELECT tea.name, tea.color, counter.year as counter FROM tea INNER JOIN counter
              ON tea.tea_id = counter.tea_id
              WHERE counter.year > 0
              ORDER BY counter.year ASC""")
    fun getTeaCounterYear(): List<StatisticsPOJO>

    @Query("""SELECT tea.name, tea.color, counter.month as counter FROM tea INNER JOIN counter
              ON tea.tea_id = counter.tea_id
              WHERE counter.month > 0
              ORDER BY counter.month ASC""")
    fun getTeaCounterMonth(): List<StatisticsPOJO>

    @Query("""SELECT tea.name, tea.color, counter.week as counter FROM tea INNER JOIN counter
              ON tea.tea_id = counter.tea_id
              WHERE counter.week > 0
              ORDER BY counter.week ASC""")
    fun getTeaCounterWeek(): List<StatisticsPOJO>
}