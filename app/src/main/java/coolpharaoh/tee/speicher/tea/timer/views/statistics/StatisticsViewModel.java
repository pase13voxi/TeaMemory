package coolpharaoh.tee.speicher.tea.timer.views.statistics;

import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.models.daos.CounterDAO;
import coolpharaoh.tee.speicher.tea.timer.models.database.TeaMemoryDatabase;
import coolpharaoh.tee.speicher.tea.timer.models.entities.Counter;
import coolpharaoh.tee.speicher.tea.timer.views.exportimport.datatransfer.pojo.StatisticsPOJO;
import coolpharaoh.tee.speicher.tea.timer.views.utils.RefreshCounter;

public class StatisticsViewModel {
    private final CounterDAO counterDAO;

    public StatisticsViewModel(TeaMemoryDatabase database) {

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

    void refreshAllCounter() {
        for (Counter counter : RefreshCounter.refreshCounters(counterDAO.getCounters())) {
            counterDAO.update(counter);
        }
    }
}
