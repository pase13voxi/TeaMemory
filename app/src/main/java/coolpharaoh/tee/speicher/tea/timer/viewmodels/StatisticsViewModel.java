package coolpharaoh.tee.speicher.tea.timer.viewmodels;

import androidx.room.Room;
import android.content.Context;

import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.daos.CounterDAO;
import coolpharaoh.tee.speicher.tea.timer.database.TeaMemoryDatabase;
import coolpharaoh.tee.speicher.tea.timer.entities.Counter;
import coolpharaoh.tee.speicher.tea.timer.pojos.StatisticsPOJO;
import coolpharaoh.tee.speicher.tea.timer.viewmodels.helper.RefreshCounter;

public class StatisticsViewModel {
    private CounterDAO mCounterDAO;

    public StatisticsViewModel(Context context) {
        TeaMemoryDatabase database = Room.databaseBuilder(context, TeaMemoryDatabase.class, "teamemory")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();


        mCounterDAO = database.getCounterDAO();

        refreshAllCounter();
    }

    public List<StatisticsPOJO> getStatisticsOverall() {
        return mCounterDAO.getTeaCounterOverall();
    }

    public List<StatisticsPOJO> getStatisticsMonth() {
        return mCounterDAO.getTeaCounterMonth();
    }

    public List<StatisticsPOJO> getStatisticsWeek() {
        return mCounterDAO.getTeaCounterWeek();
    }

    public List<StatisticsPOJO> getStatisticsDay() {
        return mCounterDAO.getTeaCounterDay();
    }

    private void refreshAllCounter() {
        for (Counter counter : RefreshCounter.refreshCounters(mCounterDAO.getCounters())) {
            mCounterDAO.update(counter);
        }
    }
}
