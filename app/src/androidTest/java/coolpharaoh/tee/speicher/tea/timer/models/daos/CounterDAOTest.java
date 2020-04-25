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

import static org.assertj.core.api.Java6Assertions.assertThat;


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

        assertThat(mCounterDAO.getCounters()).hasSize(0);

        Counter counterBefore = new Counter(teaId, 1, 2, 3, 4, Calendar.getInstance().getTime(), Calendar.getInstance().getTime(), Calendar.getInstance().getTime());
        mCounterDAO.insert(counterBefore);

        assertThat(mCounterDAO.getCounters()).hasSize(1);

        Counter counterAfter = mCounterDAO.getCounterByTeaId(teaId);
        assertThat(counterAfter).isEqualToIgnoringGivenFields(counterBefore, "id");
    }

    @Test
    public void updateCounter(){
        long teaId = mTeaDAO.insert(createTea("name"));

        Counter counterBefore = new Counter(teaId, 1, 2, 3, 4, Calendar.getInstance().getTime(), Calendar.getInstance().getTime(), Calendar.getInstance().getTime());
        mCounterDAO.insert(counterBefore);

        assertThat(mCounterDAO.getCounters()).hasSize(1);

        Counter counterUpdate = mCounterDAO.getCounterByTeaId(teaId);
        counterUpdate.setDay(5);
        counterUpdate.setWeek(4);
        counterUpdate.setMonth(3);
        mCounterDAO.update(counterUpdate);

        Counter counterAfter = mCounterDAO.getCounterByTeaId(teaId);
        assertThat(counterAfter).isEqualToComparingFieldByField(counterUpdate);
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
        assertThat(counterDay.get(0).counter).isEqualTo(counterD.getOverall());
        assertThat(counterDay.get(0).teaname).isEqualTo(teaD.getName());
        assertThat(counterDay.get(0).teacolor).isEqualTo(teaD.getColor());
        assertThat(counterDay.get(1).counter).isEqualTo(counterC.getOverall());
        assertThat(counterDay.get(1).teaname).isEqualTo(teaC.getName());
        assertThat(counterDay.get(1).teacolor).isEqualTo(teaC.getColor());
        assertThat(counterDay.get(2).counter).isEqualTo(counterB.getOverall());
        assertThat(counterDay.get(2).teaname).isEqualTo(teaB.getName());
        assertThat(counterDay.get(2).teacolor).isEqualTo(teaB.getColor());
        assertThat(counterDay.get(3).counter).isEqualTo(counterA.getOverall());
        assertThat(counterDay.get(3).teaname).isEqualTo(teaA.getName());
        assertThat(counterDay.get(3).teacolor).isEqualTo(teaA.getColor());
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
        assertThat(counterDay.get(0).counter).isEqualTo(counterD.getMonth());
        assertThat(counterDay.get(0).teaname).isEqualTo(teaD.getName());
        assertThat(counterDay.get(0).teacolor).isEqualTo(teaD.getColor());
        assertThat(counterDay.get(1).counter).isEqualTo(counterB.getMonth());
        assertThat(counterDay.get(1).teaname).isEqualTo(teaB.getName());
        assertThat(counterDay.get(1).teacolor).isEqualTo(teaB.getColor());
        assertThat(counterDay.get(2).counter).isEqualTo(counterC.getMonth());
        assertThat(counterDay.get(2).teaname).isEqualTo(teaC.getName());
        assertThat(counterDay.get(2).teacolor).isEqualTo(teaC.getColor());
        assertThat(counterDay.get(3).counter).isEqualTo(counterA.getMonth());
        assertThat(counterDay.get(3).teaname).isEqualTo(teaA.getName());
        assertThat(counterDay.get(3).teacolor).isEqualTo(teaA.getColor());
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
        assertThat(counterDay.get(0).counter).isEqualTo(counterD.getWeek());
        assertThat(counterDay.get(0).teaname).isEqualTo(teaD.getName());
        assertThat(counterDay.get(0).teacolor).isEqualTo(teaD.getColor());
        assertThat(counterDay.get(1).counter).isEqualTo(counterA.getWeek());
        assertThat(counterDay.get(1).teaname).isEqualTo(teaA.getName());
        assertThat(counterDay.get(1).teacolor).isEqualTo(teaA.getColor());
        assertThat(counterDay.get(2).counter).isEqualTo(counterB.getWeek());
        assertThat(counterDay.get(2).teaname).isEqualTo(teaB.getName());
        assertThat(counterDay.get(2).teacolor).isEqualTo(teaB.getColor());
        assertThat(counterDay.get(3).counter).isEqualTo(counterC.getWeek());
        assertThat(counterDay.get(3).teaname).isEqualTo(teaC.getName());
        assertThat(counterDay.get(3).teacolor).isEqualTo(teaC.getColor());
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
        assertThat(counterDay.get(0).counter).isEqualTo(counterB.getDay());
        assertThat(counterDay.get(0).teaname).isEqualTo(teaB.getName());
        assertThat(counterDay.get(0).teacolor).isEqualTo(teaB.getColor());
        assertThat(counterDay.get(1).counter).isEqualTo(counterC.getDay());
        assertThat(counterDay.get(1).teaname).isEqualTo(teaC.getName());
        assertThat(counterDay.get(1).teacolor).isEqualTo(teaC.getColor());
        assertThat(counterDay.get(2).counter).isEqualTo(counterA.getDay());
        assertThat(counterDay.get(2).teaname).isEqualTo(teaA.getName());
        assertThat(counterDay.get(2).teacolor).isEqualTo(teaA.getColor());
        assertThat(counterDay.get(3).counter).isEqualTo(counterD.getDay());
        assertThat(counterDay.get(3).teaname).isEqualTo(teaD.getName());
        assertThat(counterDay.get(3).teacolor).isEqualTo(teaD.getColor());
    }

    private Tea createTea(String name){
        return new Tea(name, "variety", 3, "ts", 15, 0, Calendar.getInstance().getTime());
    }

}
