package coolpharaoh.tee.speicher.tea.timer.models.repository;

import android.app.Application;

import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.models.daos.CounterDao;
import coolpharaoh.tee.speicher.tea.timer.models.database.TeaMemoryDatabase;
import coolpharaoh.tee.speicher.tea.timer.models.entities.Counter;
import coolpharaoh.tee.speicher.tea.timer.views.exportimport.datatransfer.pojo.StatisticsPOJO;

public class CounterRepository {
    private CounterDao counterDao;

    public CounterRepository(Application application) {
        TeaMemoryDatabase teaMemoryDatabase = TeaMemoryDatabase.getDatabaseInstance(application);
        counterDao = teaMemoryDatabase.getCounterDao();
    }

    public void insertCounter(Counter counter) {
        counterDao.insert(counter);
    }

    public void updateCounter(Counter counter) {
        counterDao.update(counter);
    }

    public List<Counter> getCounters() {
        return counterDao.getCounters();
    }

    public Counter getCounterByTeaId(long id) {
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
