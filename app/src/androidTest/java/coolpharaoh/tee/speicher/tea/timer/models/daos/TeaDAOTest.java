package coolpharaoh.tee.speicher.tea.timer.models.daos;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.runner.AndroidJUnit4;

import org.assertj.core.groups.Tuple;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.models.database.TeaMemoryDatabase;
import coolpharaoh.tee.speicher.tea.timer.models.entities.Tea;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@RunWith(AndroidJUnit4.class)
public class TeaDAOTest {
    private TeaDAO teaDAO;
    private TeaMemoryDatabase db;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, TeaMemoryDatabase.class).build();
        teaDAO = db.getTeaDao();
    }

    @After
    public void closeDb() {
        db.close();
    }

    @Test
    public void insertTea(){
        assertThat(teaDAO.getTeas()).hasSize(0);

        Tea teaBefore = createTea("name", "variety", Calendar.getInstance().getTime());
        teaDAO.insert(teaBefore);

        assertThat(teaDAO.getTeas()).hasSize(1);

        Tea teaAfter = teaDAO.getTeas().get(0);
        assertThat(teaAfter).isEqualToIgnoringGivenFields(teaBefore, "id");
    }

    @Test
    public void updateTea(){
        assertThat(teaDAO.getTeas()).hasSize(0);

        Tea teaBefore = createTea("name", "variety", Calendar.getInstance().getTime());
        teaDAO.insert(teaBefore);

        assertThat(teaDAO.getTeas()).hasSize(1);

        Tea teaUpdate = teaDAO.getTeas().get(0);
        teaUpdate.setName("NameChanged");
        teaUpdate.setName("VarietyChanged");
        teaDAO.update(teaUpdate);

        Tea teaAfter = teaDAO.getTeas().get(0);
        assertThat(teaAfter).isEqualToComparingFieldByField(teaUpdate);
    }

    @Test
    public void deleteTea(){
        assertThat(teaDAO.getTeas()).hasSize(0);

        Tea teaBefore1 = createTea("name1", "variety1", Calendar.getInstance().getTime());
        teaDAO.insert(teaBefore1);

        Tea teaBefore2 = createTea("name2", "variety2", Calendar.getInstance().getTime());
        teaDAO.insert(teaBefore2);

        assertThat(teaDAO.getTeas()).hasSize(2);

        teaDAO.delete(teaDAO.getTeas().get(0));

        assertThat(teaDAO.getTeas()).hasSize(1);

        Tea teaAfter2 = teaDAO.getTeas().get(0);
        assertThat(teaAfter2).isEqualToIgnoringGivenFields(teaBefore2, "id");
    }

    @Test
    public void deleteAllTeas(){
        assertThat(teaDAO.getTeas()).hasSize(0);

        Tea teaBefore1 = createTea("name1", "variety1", Calendar.getInstance().getTime());
        teaDAO.insert(teaBefore1);

        Tea teaBefore2 = createTea("name2", "variety2", Calendar.getInstance().getTime());
        teaDAO.insert(teaBefore2);

        assertThat(teaDAO.getTeas()).hasSize(2);

        teaDAO.deleteAll();

        assertThat(teaDAO.getTeas()).hasSize(0);
    }

    @Test
    public void getTeasOrderByActivity() {
        Tea teaOld = createTea("nameOld", "varietyOld", new GregorianCalendar(2016, Calendar.FEBRUARY, 36).getTime());
        teaDAO.insert(teaOld);
        Tea teaMiddle = createTea("nameMiddle", "varietyMiddle", new GregorianCalendar(2018, Calendar.FEBRUARY, 11).getTime());
        teaDAO.insert(teaMiddle);
        Tea teaNew = createTea("nameNew", "varietyNew", new GregorianCalendar(2018, Calendar.DECEMBER, 15).getTime());
        teaDAO.insert(teaNew);

        List<Tea> teaList = teaDAO.getTeasOrderByActivity();

        assertThat(teaList)
                .extracting(
                        Tea::getName,
                        Tea::getVariety,
                        Tea::getDate)
                .containsSequence(
                        Tuple.tuple(
                                teaNew.getName(),
                                teaNew.getVariety(),
                                teaNew.getDate()
                        ),
                        Tuple.tuple(
                                teaMiddle.getName(),
                                teaMiddle.getVariety(),
                                teaMiddle.getDate()
                        ),
                        Tuple.tuple(
                                teaOld.getName(),
                                teaOld.getVariety(),
                                teaOld.getDate()
                        )
                );
    }

    @Test
    public void getTeaById(){
        Tea teaBefore1 = createTea("nameOld", "varietyOld", new GregorianCalendar(2016, Calendar.FEBRUARY, 36).getTime());
        long teaId1 = teaDAO.insert(teaBefore1);

        Tea teaBefore2 = createTea("nameMiddle", "varietyMiddle", new GregorianCalendar(2018, Calendar.FEBRUARY, 11).getTime());
        long teaId2 = teaDAO.insert(teaBefore2);

        Tea teaAfter1 = teaDAO.getTeaById(teaId1);
        assertThat(teaAfter1).isEqualToIgnoringGivenFields(teaBefore1, "id");

        Tea teaAfter2 = teaDAO.getTeaById(teaId2);
        assertThat(teaAfter2).isEqualToIgnoringGivenFields(teaBefore2, "id");
    }

    @Test
    public void getTeasOrderByAlphabetic() {
        Tea teaC = createTea("nameC", "variety", Calendar.getInstance().getTime());
        teaDAO.insert(teaC);
        Tea teaA = createTea("nameA", "variety", Calendar.getInstance().getTime());
        teaDAO.insert(teaA);
        Tea teaB = createTea("nameB", "variety", Calendar.getInstance().getTime());
        teaDAO.insert(teaB);

        List<Tea> teaList = teaDAO.getTeasOrderByAlphabetic();

        assertThat(teaList)
                .extracting(
                        Tea::getName,
                        Tea::getVariety,
                        Tea::getDate)
                .containsSequence(
                        Tuple.tuple(
                                teaA.getName(),
                                teaA.getVariety(),
                                teaA.getDate()
                        ),
                        Tuple.tuple(
                                teaB.getName(),
                                teaB.getVariety(),
                                teaB.getDate()
                        ),
                        Tuple.tuple(
                                teaC.getName(),
                                teaC.getVariety(),
                                teaC.getDate()
                        )
                );
    }

    @Test
    public void getTeasOrderByVariety() {
        Tea teaC = createTea("name", "varietyC", Calendar.getInstance().getTime());
        teaDAO.insert(teaC);
        Tea teaA = createTea("name", "varietyA", Calendar.getInstance().getTime());
        teaDAO.insert(teaA);
        Tea teaB = createTea("name", "varietyB", Calendar.getInstance().getTime());
        teaDAO.insert(teaB);

        List<Tea> teaList = teaDAO.getTeasOrderByVariety();

        assertThat(teaList)
                .extracting(
                        Tea::getName,
                        Tea::getVariety,
                        Tea::getDate)
                .containsSequence(
                        Tuple.tuple(
                                teaA.getName(),
                                teaA.getVariety(),
                                teaA.getDate()
                        ),
                        Tuple.tuple(
                                teaB.getName(),
                                teaB.getVariety(),
                                teaB.getDate()
                        ),
                        Tuple.tuple(
                                teaC.getName(),
                                teaC.getVariety(),
                                teaC.getDate()
                        )
                );
    }

    @Test
    public void getTeasBySearchString() {
        Tea teaA = createTea("A", "variety", Calendar.getInstance().getTime());
        teaDAO.insert(teaA);
        Tea teaC = createTea("nameC", "variety", Calendar.getInstance().getTime());
        teaDAO.insert(teaC);
        Tea teaB = createTea("nameB", "variety", Calendar.getInstance().getTime());
        teaDAO.insert(teaB);

        List<Tea> teaList = teaDAO.getTeasBySearchString("name");

        assertThat(teaList)
                .extracting(
                        Tea::getName,
                        Tea::getVariety,
                        Tea::getDate)
                .containsSequence(
                        Tuple.tuple(
                                teaB.getName(),
                                teaB.getVariety(),
                                teaB.getDate()
                        ),
                        Tuple.tuple(
                                teaC.getName(),
                                teaC.getVariety(),
                                teaC.getDate()
                        )
                );
    }

    private Tea createTea(String name, String variety, Date date) {
        return new Tea(name, variety, 3, "ts", 15, 0, date);
    }
}
