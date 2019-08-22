package coolpharaoh.tee.speicher.tea.timer.daos;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.entities.Counter;
import coolpharaoh.tee.speicher.tea.timer.pojos.StatisticsPOJO;

@Dao
public interface CounterDAO {
    @Insert
    void insert(Counter... items);

    @Update
    void update(Counter... items);

    @Delete
    void delete(Counter item);

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