package coolpharaoh.tee.speicher.tea.timer.models.daos;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;
import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.models.database.TeaMemoryDatabase;
import coolpharaoh.tee.speicher.tea.timer.models.datatransfer.pojo.StatisticsPOJO;
import coolpharaoh.tee.speicher.tea.timer.models.entities.Counter;
import coolpharaoh.tee.speicher.tea.timer.models.entities.Tea;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class CounterDAOTest {
    private CounterDAO mCounterDAO;
    private TeaDAO mTeaDAO;
    private TeaMemoryDatabase db;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, TeaMemoryDatabase.class).build();
        mCounterDAO = db.getCounterDAO();
        mTeaDAO = db.getTeaDAO();
    }

    @After
    public void closeDb() {
        db.close();
    }

    @Test
    public void insertCounter(){
        long teaId = mTeaDAO.insert(createTea("name"));

        assertEquals(mCounterDAO.getCounters().size(), 0);

        Counter counterBefore = new Counter(teaId, 1, 2, 3, 4, Calendar.getInstance().getTime(), Calendar.getInstance().getTime(), Calendar.getInstance().getTime());
        mCounterDAO.insert(counterBefore);

        assertEquals(mCounterDAO.getCounters().size(), 1);

        Counter counterAfter = mCounterDAO.getCounterByTeaId(teaId);
        assertEquals(counterAfter.getDay(), counterBefore.getDay());
        assertEquals(counterAfter.getWeek(), counterBefore.getWeek());
        assertEquals(counterAfter.getMonth(), counterBefore.getMonth());
        assertEquals(counterAfter.getOverall(), counterBefore.getOverall());
        assertEquals(counterAfter.getDayDate(), counterBefore.getDayDate());
        assertEquals(counterAfter.getWeekDate(), counterBefore.getWeekDate());
        assertEquals(counterAfter.getMonthDate(), counterBefore.getMonthDate());
    }

    @Test
    public void updateCounter(){
        long teaId = mTeaDAO.insert(createTea("name"));

        Counter counterBefore = new Counter(teaId, 1, 2, 3, 4, Calendar.getInstance().getTime(), Calendar.getInstance().getTime(), Calendar.getInstance().getTime());
        mCounterDAO.insert(counterBefore);

        assertEquals(mCounterDAO.getCounters().size(), 1);

        Counter counterUpdate = mCounterDAO.getCounterByTeaId(teaId);
        counterUpdate.setDay(5);
        counterUpdate.setWeek(4);
        counterUpdate.setMonth(3);
        mCounterDAO.update(counterUpdate);

        Counter counterAfter = mCounterDAO.getCounterByTeaId(teaId);
        assertEquals(counterAfter.getDay(), counterUpdate.getDay());
        assertEquals(counterAfter.getWeek(), counterUpdate.getWeek());
        assertEquals(counterAfter.getMonth(), counterUpdate.getMonth());
        assertEquals(counterAfter.getOverall(), counterUpdate.getOverall());
        assertEquals(counterAfter.getDayDate(), counterUpdate.getDayDate());
        assertEquals(counterAfter.getWeekDate(), counterUpdate.getWeekDate());
        assertEquals(counterAfter.getMonthDate(), counterUpdate.getMonthDate());
    }

    @Test
    public void getTeaCounterOverall(){
        mTeaDAO.insert(createTea("A"));
        mTeaDAO.insert(createTea("B"));
        mTeaDAO.insert(createTea("C"));
        mTeaDAO.insert(createTea("D"));

        List<Tea> teas = mTeaDAO.getTeas();
        Tea teaA = teas.get(0);
        Tea teaB = teas.get(1);
        Tea teaC = teas.get(2);
        Tea teaD = teas.get(3);

        Counter counterA = new Counter(teaA.getId(), 4, 4, 4, 1, Calendar.getInstance().getTime(), Calendar.getInstance().getTime(), Calendar.getInstance().getTime());
        mCounterDAO.insert(counterA);
        Counter counterB = new Counter(teaB.getId(), 3, 3, 3, 2, Calendar.getInstance().getTime(), Calendar.getInstance().getTime(), Calendar.getInstance().getTime());
        mCounterDAO.insert(counterB);
        Counter counterC = new Counter(teaC.getId(), 2, 2, 2, 3, Calendar.getInstance().getTime(), Calendar.getInstance().getTime(), Calendar.getInstance().getTime());
        mCounterDAO.insert(counterC);
        Counter counterD = new Counter(teaD.getId(), 1, 1, 1, 4, Calendar.getInstance().getTime(), Calendar.getInstance().getTime(), Calendar.getInstance().getTime());
        mCounterDAO.insert(counterD);

        List<StatisticsPOJO> counterDay = mCounterDAO.getTeaCounterOverall();
        assertEquals(counterDay.get(0).counter, counterD.getOverall());
        assertEquals(counterDay.get(0).teaname, teaD.getName());
        assertEquals(counterDay.get(0).teacolor, teaD.getColor());
        assertEquals(counterDay.get(1).counter, counterC.getOverall());
        assertEquals(counterDay.get(1).teaname, teaC.getName());
        assertEquals(counterDay.get(1).teacolor, teaC.getColor());
        assertEquals(counterDay.get(2).counter, counterB.getOverall());
        assertEquals(counterDay.get(2).teaname, teaB.getName());
        assertEquals(counterDay.get(2).teacolor, teaB.getColor());
        assertEquals(counterDay.get(3).counter, counterA.getOverall());
        assertEquals(counterDay.get(3).teaname, teaA.getName());
        assertEquals(counterDay.get(3).teacolor, teaA.getColor());
    }

    @Test
    public void getTeaCounterMonth(){

        mTeaDAO.insert(createTea("A"));
        mTeaDAO.insert(createTea("B"));
        mTeaDAO.insert(createTea("C"));
        mTeaDAO.insert(createTea("D"));

        List<Tea> teas = mTeaDAO.getTeas();
        Tea teaA = teas.get(0);
        Tea teaB = teas.get(1);
        Tea teaC = teas.get(2);
        Tea teaD = teas.get(3);

        Counter counterA = new Counter(teaA.getId(), 4, 4, 1, 4, Calendar.getInstance().getTime(), Calendar.getInstance().getTime(), Calendar.getInstance().getTime());
        mCounterDAO.insert(counterA);
        Counter counterB = new Counter(teaB.getId(), 3, 3, 3, 3, Calendar.getInstance().getTime(), Calendar.getInstance().getTime(), Calendar.getInstance().getTime());
        mCounterDAO.insert(counterB);
        Counter counterC = new Counter(teaC.getId(), 2, 2, 2, 2, Calendar.getInstance().getTime(), Calendar.getInstance().getTime(), Calendar.getInstance().getTime());
        mCounterDAO.insert(counterC);
        Counter counterD = new Counter(teaD.getId(), 1, 1, 4, 1, Calendar.getInstance().getTime(), Calendar.getInstance().getTime(), Calendar.getInstance().getTime());
        mCounterDAO.insert(counterD);

        List<StatisticsPOJO> counterDay = mCounterDAO.getTeaCounterMonth();
        assertEquals(counterDay.get(0).counter, counterD.getMonth());
        assertEquals(counterDay.get(0).teaname, teaD.getName());
        assertEquals(counterDay.get(0).teacolor, teaD.getColor());
        assertEquals(counterDay.get(1).counter, counterB.getMonth());
        assertEquals(counterDay.get(1).teaname, teaB.getName());
        assertEquals(counterDay.get(1).teacolor, teaB.getColor());
        assertEquals(counterDay.get(2).counter, counterC.getMonth());
        assertEquals(counterDay.get(2).teaname, teaC.getName());
        assertEquals(counterDay.get(2).teacolor, teaC.getColor());
        assertEquals(counterDay.get(3).counter, counterA.getMonth());
        assertEquals(counterDay.get(3).teaname, teaA.getName());
        assertEquals(counterDay.get(3).teacolor, teaA.getColor());
    }

    @Test
    public void getTeaCounterWeek(){

        mTeaDAO.insert(createTea("A"));
        mTeaDAO.insert(createTea("B"));
        mTeaDAO.insert(createTea("C"));
        mTeaDAO.insert(createTea("D"));

        List<Tea> teas = mTeaDAO.getTeas();
        Tea teaA = teas.get(0);
        Tea teaB = teas.get(1);
        Tea teaC = teas.get(2);
        Tea teaD = teas.get(3);

        Counter counterA = new Counter(teaA.getId(), 4, 3, 4, 4, Calendar.getInstance().getTime(), Calendar.getInstance().getTime(), Calendar.getInstance().getTime());
        mCounterDAO.insert(counterA);
        Counter counterB = new Counter(teaB.getId(), 3, 2, 3, 3, Calendar.getInstance().getTime(), Calendar.getInstance().getTime(), Calendar.getInstance().getTime());
        mCounterDAO.insert(counterB);
        Counter counterC = new Counter(teaC.getId(), 2, 1, 2, 2, Calendar.getInstance().getTime(), Calendar.getInstance().getTime(), Calendar.getInstance().getTime());
        mCounterDAO.insert(counterC);
        Counter counterD = new Counter(teaD.getId(), 1, 4, 1, 1, Calendar.getInstance().getTime(), Calendar.getInstance().getTime(), Calendar.getInstance().getTime());
        mCounterDAO.insert(counterD);

        List<StatisticsPOJO> counterDay = mCounterDAO.getTeaCounterWeek();
        assertEquals(counterDay.get(0).counter, counterD.getWeek());
        assertEquals(counterDay.get(0).teaname, teaD.getName());
        assertEquals(counterDay.get(0).teacolor, teaD.getColor());
        assertEquals(counterDay.get(1).counter, counterA.getWeek());
        assertEquals(counterDay.get(1).teaname, teaA.getName());
        assertEquals(counterDay.get(1).teacolor, teaA.getColor());
        assertEquals(counterDay.get(2).counter, counterB.getWeek());
        assertEquals(counterDay.get(2).teaname, teaB.getName());
        assertEquals(counterDay.get(2).teacolor, teaB.getColor());
        assertEquals(counterDay.get(3).counter, counterC.getWeek());
        assertEquals(counterDay.get(3).teaname, teaC.getName());
        assertEquals(counterDay.get(3).teacolor, teaC.getColor());
    }

    @Test
    public void getTeaCounterDay(){

        mTeaDAO.insert(createTea("A"));
        mTeaDAO.insert(createTea("B"));
        mTeaDAO.insert(createTea("C"));
        mTeaDAO.insert(createTea("D"));

        List<Tea> teas = mTeaDAO.getTeas();
        Tea teaA = teas.get(0);
        Tea teaB = teas.get(1);
        Tea teaC = teas.get(2);
        Tea teaD = teas.get(3);

        Counter counterA = new Counter(teaA.getId(), 2, 4, 4, 4, Calendar.getInstance().getTime(), Calendar.getInstance().getTime(), Calendar.getInstance().getTime());
        mCounterDAO.insert(counterA);
        Counter counterB = new Counter(teaB.getId(), 4, 3, 3, 3, Calendar.getInstance().getTime(), Calendar.getInstance().getTime(), Calendar.getInstance().getTime());
        mCounterDAO.insert(counterB);
        Counter counterC = new Counter(teaC.getId(), 3, 2, 2, 2, Calendar.getInstance().getTime(), Calendar.getInstance().getTime(), Calendar.getInstance().getTime());
        mCounterDAO.insert(counterC);
        Counter counterD = new Counter(teaD.getId(), 1, 1, 1, 1, Calendar.getInstance().getTime(), Calendar.getInstance().getTime(), Calendar.getInstance().getTime());
        mCounterDAO.insert(counterD);

        List<StatisticsPOJO> counterDay = mCounterDAO.getTeaCounterDay();
        assertEquals(counterDay.get(0).counter, counterB.getDay());
        assertEquals(counterDay.get(0).teaname, teaB.getName());
        assertEquals(counterDay.get(0).teacolor, teaB.getColor());
        assertEquals(counterDay.get(1).counter, counterC.getDay());
        assertEquals(counterDay.get(1).teaname, teaC.getName());
        assertEquals(counterDay.get(1).teacolor, teaC.getColor());
        assertEquals(counterDay.get(2).counter, counterA.getDay());
        assertEquals(counterDay.get(2).teaname, teaA.getName());
        assertEquals(counterDay.get(2).teacolor, teaA.getColor());
        assertEquals(counterDay.get(3).counter, counterD.getDay());
        assertEquals(counterDay.get(3).teaname, teaD.getName());
        assertEquals(counterDay.get(3).teacolor, teaD.getColor());
    }

    private Tea createTea(String name){
        return new Tea(name, "variety", 3, "ts", 15, 0, Calendar.getInstance().getTime());
    }

}
