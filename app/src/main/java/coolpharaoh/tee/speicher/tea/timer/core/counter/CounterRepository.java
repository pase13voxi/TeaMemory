package coolpharaoh.tee.speicher.tea.timer.core.counter;

import android.app.Application;

import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.database.TeaMemoryDatabase;
import coolpharaoh.tee.speicher.tea.timer.views.export_import.data_transform.pojo.StatisticsPOJO;

public class CounterRepository {
    private final CounterDao counterDao;

    public CounterRepository(final Application application) {
        final TeaMemoryDatabase teaMemoryDatabase = TeaMemoryDatabase.getDatabaseInstance(application);
        counterDao = teaMemoryDatabase.getCounterDao();
    }

    public Long insertCounter(final Counter counter) {
        return counterDao.insert(counter);
    }

    public void updateCounter(final Counter counter) {
        counterDao.update(counter);
    }

    public List<Counter> getCounters() {
        return counterDao.getCounters();
    }

    public Counter getCounterByTeaId(final long id) {
        return counterDao.getCounterByTeaId(id);
    }

    public List<StatisticsPOJO> getTeaCounterOverall() {
        return counterDao.getTeaCounterOverall();
    }

    public List<StatisticsPOJO> getTeaCounterMonth() {
        return counterDao.getTeaCounterMonth();
    }

    public List<StatisticsPOJO> getTeaCounterWeek() {
        return counterDao.getTeaCounterWeek();
    }

    public List<StatisticsPOJO> getTeaCounterDay() {
        return counterDao.getTeaCounterDay();
    }
}
