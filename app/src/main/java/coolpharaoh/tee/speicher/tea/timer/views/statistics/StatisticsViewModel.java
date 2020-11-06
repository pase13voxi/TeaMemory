package coolpharaoh.tee.speicher.tea.timer.views.statistics;

import android.app.Application;

import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.core.counter.Counter;
import coolpharaoh.tee.speicher.tea.timer.core.counter.CounterRepository;
import coolpharaoh.tee.speicher.tea.timer.core.counter.RefreshCounter;
import coolpharaoh.tee.speicher.tea.timer.views.exportimport.datatransfer.pojo.StatisticsPOJO;

public class StatisticsViewModel {
    private final CounterRepository counterRepository;

    public StatisticsViewModel(Application application) {
        this(new CounterRepository(application));
    }

    public StatisticsViewModel(CounterRepository counterRepository) {
        this.counterRepository = counterRepository;
        refreshAllCounter();
    }

    public List<StatisticsPOJO> getStatisticsOverall() {
        return counterRepository.getTeaCounterOverall();
    }

    public List<StatisticsPOJO> getStatisticsMonth() {
        return counterRepository.getTeaCounterMonth();
    }

    public List<StatisticsPOJO> getStatisticsWeek() {
        return counterRepository.getTeaCounterWeek();
    }

    public List<StatisticsPOJO> getStatisticsDay() {
        return counterRepository.getTeaCounterDay();
    }

    void refreshAllCounter() {
        for (Counter counter : RefreshCounter.refreshCounters(counterRepository.getCounters())) {
            counterRepository.updateCounter(counter);
        }
    }
}
