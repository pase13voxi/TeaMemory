package coolpharaoh.tee.speicher.tea.timer.core.counter;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.views.exportimport.datatransfer.pojo.StatisticsPOJO;

@Dao
public interface CounterDao {
    @Insert
    void insert(Counter items);

    @Update
    void update(Counter items);

    @Query("SELECT * FROM counter")
    List<Counter> getCounters();

    @Query("SELECT * FROM counter WHERE tea_id = :id")
    Counter getCounterByTeaId(long id);

    @Query("SELECT tea.name, tea.color, counter.overall as counter FROM tea INNER JOIN counter\n" +
            "ON tea.tea_id = counter.tea_id\n" +
            "ORDER BY counter.overall DESC")
    List<StatisticsPOJO> getTeaCounterOverall();

    @Query("SELECT tea.name, tea.color, counter.month as counter FROM tea INNER JOIN counter\n" +
            "ON tea.tea_id = counter.tea_id\n" +
            "ORDER BY counter.month DESC")
    List<StatisticsPOJO> getTeaCounterMonth();

    @Query("SELECT tea.name, tea.color, counter.week as counter FROM tea INNER JOIN counter\n" +
            "ON tea.tea_id = counter.tea_id\n" +
            "ORDER BY counter.week DESC")
    List<StatisticsPOJO> getTeaCounterWeek();

    @Query("SELECT tea.name, tea.color, counter.day as counter FROM tea INNER JOIN counter\n" +
            "ON tea.tea_id = counter.tea_id\n" +
            "ORDER BY counter.day DESC")
    List<StatisticsPOJO> getTeaCounterDay();
}