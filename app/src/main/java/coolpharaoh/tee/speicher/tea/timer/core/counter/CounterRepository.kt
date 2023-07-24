package coolpharaoh.tee.speicher.tea.timer.core.counter

import android.app.Application
import coolpharaoh.tee.speicher.tea.timer.database.TeaMemoryDatabase
import coolpharaoh.tee.speicher.tea.timer.views.export_import.data_transform.pojo.StatisticsPOJO

class CounterRepository(application: Application?) {
    private val counterDao: CounterDao

    init {
        val teaMemoryDatabase = TeaMemoryDatabase.getDatabaseInstance(application)
        counterDao = teaMemoryDatabase.counterDao
    }

    fun insertCounter(counter: Counter): Long {
        return counterDao.insert(counter)
    }

    fun updateCounter(counter: Counter) {
        counterDao.update(counter)
    }

    val counters: List<Counter>
        get() = counterDao.getCounters()

    fun getCounterByTeaId(id: Long): Counter? {
        return counterDao.getCounterByTeaId(id)
    }

    val teaCounterOverall: List<StatisticsPOJO>
        get() = counterDao.getTeaCounterOverall()

    val teaCounterYear: List<StatisticsPOJO>
        get() = counterDao.getTeaCounterYear()

    val teaCounterMonth: List<StatisticsPOJO>
        get() = counterDao.getTeaCounterMonth()

    val teaCounterWeek: List<StatisticsPOJO>
        get() = counterDao.getTeaCounterWeek()
}