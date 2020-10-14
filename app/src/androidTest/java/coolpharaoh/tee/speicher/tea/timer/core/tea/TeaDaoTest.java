package coolpharaoh.tee.speicher.tea.timer.core.tea;

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

import coolpharaoh.tee.speicher.tea.timer.core.database.TeaMemoryDatabase;
import coolpharaoh.tee.speicher.tea.timer.core.date.CurrentDate;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@RunWith(AndroidJUnit4.class)
public class TeaDaoTest {
    private static final String VARIETY = "variety";

    private TeaDao teaDao;
    private TeaMemoryDatabase db;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, TeaMemoryDatabase.class).build();
        teaDao = db.getTeaDao();
    }

    @After
    public void closeDb() {
        db.close();
    }

    @Test
    public void insertTea(){
        assertThat(teaDao.getTeas()).isEmpty();

        Tea teaBefore = createTea("name", VARIETY, CurrentDate.getDate());
        teaDao.insert(teaBefore);

        assertThat(teaDao.getTeas()).hasSize(1);

        Tea teaAfter = teaDao.getTeas().get(0);
        assertThat(teaAfter).isEqualToIgnoringGivenFields(teaBefore, "id");
    }

    @Test
    public void updateTea(){
        assertThat(teaDao.getTeas()).isEmpty();

        Tea teaBefore = createTea("name", VARIETY, CurrentDate.getDate());
        teaDao.insert(teaBefore);

        assertThat(teaDao.getTeas()).hasSize(1);

        Tea teaUpdate = teaDao.getTeas().get(0);
        teaUpdate.setName("NameChanged");
        teaUpdate.setName("VarietyChanged");
        teaDao.update(teaUpdate);

        Tea teaAfter = teaDao.getTeas().get(0);
        assertThat(teaAfter).isEqualToComparingFieldByField(teaUpdate);
    }

    @Test
    public void deleteTea(){
        assertThat(teaDao.getTeas()).isEmpty();

        Tea teaBefore1 = createTea("name1", VARIETY, CurrentDate.getDate());
        teaDao.insert(teaBefore1);

        Tea teaBefore2 = createTea("name2", VARIETY, CurrentDate.getDate());
        teaDao.insert(teaBefore2);

        assertThat(teaDao.getTeas()).hasSize(2);

        teaDao.delete(teaDao.getTeas().get(0));

        assertThat(teaDao.getTeas()).hasSize(1);

        Tea teaAfter2 = teaDao.getTeas().get(0);
        assertThat(teaAfter2).isEqualToIgnoringGivenFields(teaBefore2, "id");
    }

    @Test
    public void deleteAllTeas(){
        assertThat(teaDao.getTeas()).isEmpty();

        Tea teaBefore1 = createTea("name1", VARIETY, CurrentDate.getDate());
        teaDao.insert(teaBefore1);

        Tea teaBefore2 = createTea("name2", VARIETY, CurrentDate.getDate());
        teaDao.insert(teaBefore2);

        assertThat(teaDao.getTeas()).hasSize(2);

        teaDao.deleteAll();

        assertThat(teaDao.getTeas()).isEmpty();
    }

    @Test
    public void getTeasOrderByActivity() {
        Tea teaOld = createTea("nameOld", VARIETY, new GregorianCalendar(2016, Calendar.FEBRUARY, 22).getTime());
        teaDao.insert(teaOld);
        Tea teaMiddle = createTea("nameMiddle", VARIETY, new GregorianCalendar(2018, Calendar.FEBRUARY, 11).getTime());
        teaDao.insert(teaMiddle);
        Tea teaNew = createTea("nameNew", VARIETY, new GregorianCalendar(2018, Calendar.DECEMBER, 15).getTime());
        teaDao.insert(teaNew);

        List<Tea> teaList = teaDao.getTeasOrderByActivity();

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
        Tea teaBefore1 = createTea("nameOld", VARIETY, new GregorianCalendar(2016, Calendar.FEBRUARY, 22).getTime());
        long teaId1 = teaDao.insert(teaBefore1);

        Tea teaBefore2 = createTea("nameMiddle", VARIETY, new GregorianCalendar(2018, Calendar.FEBRUARY, 11).getTime());
        long teaId2 = teaDao.insert(teaBefore2);

        Tea teaAfter1 = teaDao.getTeaById(teaId1);
        assertThat(teaAfter1).isEqualToIgnoringGivenFields(teaBefore1, "id");

        Tea teaAfter2 = teaDao.getTeaById(teaId2);
        assertThat(teaAfter2).isEqualToIgnoringGivenFields(teaBefore2, "id");
    }

    @Test
    public void getTeasOrderByAlphabetic() {
        Tea teaC = createTea("nameC", VARIETY, CurrentDate.getDate());
        teaDao.insert(teaC);
        Tea teaA = createTea("nameA", VARIETY, CurrentDate.getDate());
        teaDao.insert(teaA);
        Tea teaB = createTea("nameB", VARIETY, CurrentDate.getDate());
        teaDao.insert(teaB);

        List<Tea> teaList = teaDao.getTeasOrderByAlphabetic();

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
        Tea teaC = createTea("name", "varietyC", CurrentDate.getDate());
        teaDao.insert(teaC);
        Tea teaA = createTea("name", "varietyA", CurrentDate.getDate());
        teaDao.insert(teaA);
        Tea teaB = createTea("name", "varietyB", CurrentDate.getDate());
        teaDao.insert(teaB);

        List<Tea> teaList = teaDao.getTeasOrderByVariety();

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
        Tea teaA = createTea("A", VARIETY, CurrentDate.getDate());
        teaDao.insert(teaA);
        Tea teaC = createTea("nameC", VARIETY, CurrentDate.getDate());
        teaDao.insert(teaC);
        Tea teaB = createTea("nameB", VARIETY, CurrentDate.getDate());
        teaDao.insert(teaB);

        List<Tea> teaList = teaDao.getTeasBySearchString("name");

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
