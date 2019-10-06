package coolpharaoh.tee.speicher.tea.timer.viewmodels;

import android.content.Context;

import androidx.room.Room;

import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.daos.CounterDAO;
import coolpharaoh.tee.speicher.tea.timer.database.TeaMemoryDatabase;
import coolpharaoh.tee.speicher.tea.timer.entities.Counter;
import coolpharaoh.tee.speicher.tea.timer.pojos.StatisticsPOJO;
import coolpharaoh.tee.speicher.tea.timer.viewmodels.helper.RefreshCounter;

public class StatisticsViewModel {
    private CounterDAO counterDAO;

    public StatisticsViewModel(Context context) {
        TeaMemoryDatabase database = Room.databaseBuilder(context, TeaMemoryDatabase.class, "teamemory")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();


        counterDAO = database.getCounterDAO();

        refreshAllCounter();
    }

    public List<StatisticsPOJO> getStatisticsOverall() {
        return counterDAO.getTeaCounterOverall();
    }

    public List<StatisticsPOJO> getStatisticsMonth() {
        return counterDAO.getTeaCounterMonth();
    }

    public List<StatisticsPOJO> getStatisticsWeek() {
        return counterDAO.getTeaCounterWeek();
    }

    public List<StatisticsPOJO> getStatisticsDay() {
        return counterDAO.getTeaCounterDay();
    }

    private void refreshAllCounter() {
        for (Counter counter : RefreshCounter.refreshCounters(counterDAO.getCounters())) {
            counterDAO.update(counter);
        }
    }
}
