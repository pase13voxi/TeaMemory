package coolpharaoh.tee.speicher.tea.timer.views.statistics

import android.app.Application
import androidx.annotation.VisibleForTesting
import coolpharaoh.tee.speicher.tea.timer.core.counter.CounterRepository
import coolpharaoh.tee.speicher.tea.timer.core.counter.RefreshCounter.refreshCounters
import coolpharaoh.tee.speicher.tea.timer.views.export_import.data_transform.pojo.StatisticsPOJO

class StatisticsViewModel @VisibleForTesting constructor(private val counterRepository: CounterRepository) {
    constructor(application: Application?) : this(CounterRepository(application!!))

    init {
        refreshAllCounter()
    }

    val statisticsOverall: List<StatisticsPOJO>
        get() = counterRepository.teaCounterOverall

    val statisticsYear: List<StatisticsPOJO>
        get() = counterRepository.teaCounterYear


    val statisticsMonth: List<StatisticsPOJO>
        get() = counterRepository.teaCounterMonth


    val statisticsWeek: List<StatisticsPOJO>
        get() = counterRepository.teaCounterWeek


    fun refreshAllCounter() {
        for (counter in refreshCounters(counterRepository.counters)) {
            counterRepository.updateCounter(counter)
        }
    }
}