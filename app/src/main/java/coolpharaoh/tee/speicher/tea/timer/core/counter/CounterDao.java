package coolpharaoh.tee.speicher.tea.timer.core.counter;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.views.export_import.data_transform.pojo.StatisticsPOJO;

@Dao
public interface CounterDao {
    @Insert
    Long insert(Counter items);

    @Update
    void update(Counter items);

    @Query("SELECT * FROM counter")
    List<Counter> getCounters();

    @Query("SELECT * FROM counter WHERE tea_id = :id")
    Counter getCounterByTeaId(long id);

    @Query("SELECT tea.name, tea.color, counter.overall as counter FROM tea INNER JOIN counter\n" +
            "ON tea.tea_id = counter.tea_id\n" +
            "WHERE counter.overall > 0\n" +
            "ORDER BY counter.overall ASC")
    List<StatisticsPOJO> getTeaCounterOverall();

    @Query("SELECT tea.name, tea.color, counter.year as counter FROM tea INNER JOIN counter\n" +
            "ON tea.tea_id = counter.tea_id\n" +
            "WHERE counter.year > 0\n" +
            "ORDER BY counter.year ASC")
    List<StatisticsPOJO> getTeaCounterYear();

    @Query("SELECT tea.name, tea.color, counter.month as counter FROM tea INNER JOIN counter\n" +
            "ON tea.tea_id = counter.tea_id\n" +
            "WHERE counter.month > 0\n" +
            "ORDER BY counter.month ASC")
    List<StatisticsPOJO> getTeaCounterMonth();

    @Query("SELECT tea.name, tea.color, counter.week as counter FROM tea INNER JOIN counter\n" +
            "ON tea.tea_id = counter.tea_id\n" +
            "WHERE counter.week > 0\n" +
            "ORDER BY counter.week ASC")
    List<StatisticsPOJO> getTeaCounterWeek();
}