package coolpharaoh.tee.speicher.tea.timer.core.tea;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

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

import coolpharaoh.tee.speicher.tea.timer.core.date.CurrentDate;
import coolpharaoh.tee.speicher.tea.timer.database.TeaMemoryDatabase;

@RunWith(AndroidJUnit4.class)
public class TeaDaoTest {
    private static final String VARIETY = "variety";

    private TeaDao teaDao;
    private TeaMemoryDatabase db;

    @Before
    public void createDb() {
        final Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, TeaMemoryDatabase.class).build();
        teaDao = db.getTeaDao();
    }

    @After
    public void closeDb() {
        db.close();
    }

    @Test
    public void insertTea() {
        assertThat(teaDao.getTeas()).isEmpty();

        final Tea teaBefore = createTea("name", VARIETY, CurrentDate.getDate());
        teaDao.insert(teaBefore);

        assertThat(teaDao.getTeas()).hasSize(1);

        final Tea teaAfter = teaDao.getTeas().get(0);
        assertThat(teaAfter).usingRecursiveComparison().ignoringFields("id").isEqualTo(teaBefore);
    }

    @Test
    public void updateTea() {
        assertThat(teaDao.getTeas()).isEmpty();

        final Tea teaBefore = createTea("name", VARIETY, CurrentDate.getDate());
        teaDao.insert(teaBefore);

        assertThat(teaDao.getTeas()).hasSize(1);

        final Tea teaUpdate = teaDao.getTeas().get(0);
        teaUpdate.setName("NameChanged");
        teaUpdate.setName("VarietyChanged");
        teaDao.update(teaUpdate);

        final Tea teaAfter = teaDao.getTeas().get(0);
        assertThat(teaAfter).usingRecursiveComparison().isEqualTo(teaUpdate);
    }

    @Test
    public void deleteTeaById() {
        assertThat(teaDao.getTeas()).isEmpty();

        final Tea teaBefore1 = createTea("name1", VARIETY, CurrentDate.getDate());
        teaDao.insert(teaBefore1);

        final Tea teaBefore2 = createTea("name2", VARIETY, CurrentDate.getDate());
        teaDao.insert(teaBefore2);

        assertThat(teaDao.getTeas()).hasSize(2);

        teaDao.deleteTeaById(teaDao.getTeas().get(0).getId());

        assertThat(teaDao.getTeas()).hasSize(1);

        final Tea teaAfter2 = teaDao.getTeas().get(0);
        assertThat(teaAfter2).usingRecursiveComparison().ignoringFields("id").isEqualTo(teaBefore2);
    }

    @Test
    public void deleteAllTeas() {
        assertThat(teaDao.getTeas()).isEmpty();

        final Tea teaBefore1 = createTea("name1", VARIETY, CurrentDate.getDate());
        teaDao.insert(teaBefore1);

        final Tea teaBefore2 = createTea("name2", VARIETY, CurrentDate.getDate());
        teaDao.insert(teaBefore2);

        assertThat(teaDao.getTeas()).hasSize(2);

        teaDao.deleteAll();

        assertThat(teaDao.getTeas()).isEmpty();
    }

    @Test
    public void getTeaById() {
        final Tea teaBefore1 = createTea("nameOld", VARIETY, new GregorianCalendar(2016, Calendar.FEBRUARY, 22).getTime());
        final long teaId1 = teaDao.insert(teaBefore1);

        final Tea teaBefore2 = createTea("nameMiddle", VARIETY, new GregorianCalendar(2018, Calendar.FEBRUARY, 11).getTime());
        final long teaId2 = teaDao.insert(teaBefore2);

        final Tea teaAfter1 = teaDao.getTeaById(teaId1);
        assertThat(teaAfter1).usingRecursiveComparison().ignoringFields("id").isEqualTo(teaBefore1);

        final Tea teaAfter2 = teaDao.getTeaById(teaId2);
        assertThat(teaAfter2).usingRecursiveComparison().ignoringFields("id").isEqualTo(teaBefore2);
    }

    @Test
    public void getTeasOrderByActivity() {
        final Tea teaOld = createTea("nameOld", VARIETY, new GregorianCalendar(2016, Calendar.FEBRUARY, 22).getTime());
        teaDao.insert(teaOld);
        final Tea teaMiddle = createTea("nameMiddle", VARIETY, new GregorianCalendar(2018, Calendar.FEBRUARY, 11).getTime());
        teaDao.insert(teaMiddle);
        final Tea teaNew = createTea("nameNew", VARIETY, new GregorianCalendar(2018, Calendar.DECEMBER, 15).getTime());
        teaDao.insert(teaNew);

        final List<Tea> teaList = teaDao.getTeasOrderByActivity();

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
    public void getTeasStockOrderByActivity() {
        final Tea teaOld = createTea("nameOld", VARIETY, new GregorianCalendar(2016, Calendar.FEBRUARY, 22).getTime());
        teaDao.insert(teaOld);
        final Tea teaMiddle = createTea("nameMiddle", VARIETY, new GregorianCalendar(2018, Calendar.FEBRUARY, 11).getTime());
        teaMiddle.setInStock(true);
        teaDao.insert(teaMiddle);
        final Tea teaNew = createTea("nameNew", VARIETY, new GregorianCalendar(2018, Calendar.DECEMBER, 15).getTime());
        teaDao.insert(teaNew);

        final List<Tea> teaList = teaDao.getTeasInStockOrderByActivity();

        assertThat(teaList).hasSize(1);
    }

    @Test
    public void getTeasOrderByAlphabetic() {
        final Tea teaC = createTea("nameC", VARIETY, CurrentDate.getDate());
        teaDao.insert(teaC);
        final Tea teaA = createTea("nameA", VARIETY, CurrentDate.getDate());
        teaDao.insert(teaA);
        final Tea teaB = createTea("nameB", VARIETY, CurrentDate.getDate());
        teaDao.insert(teaB);

        final List<Tea> teaList = teaDao.getTeasOrderByAlphabetic();

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
    public void getTeasInStockOrderByAlphabetic() {
        final Tea teaC = createTea("nameC", VARIETY, CurrentDate.getDate());
        teaDao.insert(teaC);
        final Tea teaA = createTea("nameA", VARIETY, CurrentDate.getDate());
        teaA.setInStock(true);
        teaDao.insert(teaA);
        final Tea teaB = createTea("nameB", VARIETY, CurrentDate.getDate());
        teaDao.insert(teaB);

        final List<Tea> teaList = teaDao.getTeasInStockOrderByAlphabetic();

        assertThat(teaList).hasSize(1);
    }

    @Test
    public void getTeasOrderByVariety() {
        final Tea teaC = createTea("name", "varietyC", CurrentDate.getDate());
        teaDao.insert(teaC);
        final Tea teaA = createTea("name", "varietyA", CurrentDate.getDate());
        teaDao.insert(teaA);
        final Tea teaB = createTea("name", "varietyB", CurrentDate.getDate());
        teaDao.insert(teaB);

        final List<Tea> teaList = teaDao.getTeasOrderByVariety();

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
    public void getTeasInStockOrderByVariety() {
        final Tea teaC = createTea("name", "varietyC", CurrentDate.getDate());
        teaDao.insert(teaC);
        final Tea teaA = createTea("name", "varietyA", CurrentDate.getDate());
        teaA.setInStock(true);
        teaDao.insert(teaA);
        final Tea teaB = createTea("name", "varietyB", CurrentDate.getDate());
        teaDao.insert(teaB);

        final List<Tea> teaList = teaDao.getTeasInStockOrderByVariety();

        assertThat(teaList).hasSize(1);
    }

    @Test
    public void getTeasOrderByRating() {
        final Tea tea3 = createTea("rating3", VARIETY, CurrentDate.getDate(), 4);
        teaDao.insert(tea3);
        final Tea tea5 = createTea("rating5", VARIETY, CurrentDate.getDate(), 5);
        teaDao.insert(tea5);
        final Tea tea1 = createTea("rating1", VARIETY, CurrentDate.getDate(), 1);
        teaDao.insert(tea1);

        final List<Tea> teaList = teaDao.getTeasOrderByRating();

        assertThat(teaList)
                .extracting(
                        Tea::getName,
                        Tea::getRating)
                .containsSequence(
                        Tuple.tuple(
                                tea5.getName(),
                                tea5.getRating()
                        ),
                        Tuple.tuple(
                                tea3.getName(),
                                tea3.getRating()
                        ),
                        Tuple.tuple(
                                tea1.getName(),
                                tea1.getRating()
                        )
                );
    }

    @Test
    public void getTeasInStockOrderByRating() {
        final Tea tea3 = createTea("rating3", VARIETY, CurrentDate.getDate(), 4);
        teaDao.insert(tea3);
        final Tea tea5 = createTea("rating5", VARIETY, CurrentDate.getDate(), 5);
        tea5.setInStock(true);
        teaDao.insert(tea5);
        final Tea tea1 = createTea("rating1", VARIETY, CurrentDate.getDate(), 1);
        teaDao.insert(tea1);

        final List<Tea> teaList = teaDao.getTeasInStockOrderByRating();

        assertThat(teaList).hasSize(1);
    }

    @Test
    public void getRandomTeaInStock() {
        final Tea tea1 = createTea("tea1", VARIETY, CurrentDate.getDate());
        teaDao.insert(tea1);
        final Tea tea2 = createTea("tea2", VARIETY, CurrentDate.getDate());
        tea2.setInStock(true);
        teaDao.insert(tea2);
        final Tea tea3 = createTea("tea3", VARIETY, CurrentDate.getDate());
        tea3.setInStock(true);
        teaDao.insert(tea3);

        final Tea randomTea = teaDao.getRandomTeaInStock();

        assertThat(randomTea.isInStock()).isTrue();
        assertThat(randomTea.getName())
                .satisfiesAnyOf(
                        s -> assertThat(s).isEqualTo(tea2.getName()),
                        s -> assertThat(s).isEqualTo(tea3.getName())
                );
    }

    @Test
    public void getNoRandomTeaInStock() {
        final Tea tea1 = createTea("tea1", VARIETY, CurrentDate.getDate());
        teaDao.insert(tea1);
        final Tea tea2 = createTea("tea2", VARIETY, CurrentDate.getDate());
        teaDao.insert(tea2);
        final Tea tea3 = createTea("tea3", VARIETY, CurrentDate.getDate());
        teaDao.insert(tea3);

        final Tea randomTea = teaDao.getRandomTeaInStock();

        assertThat(randomTea).isNull();
    }

    @Test
    public void getTeasBySearchString() {
        final Tea teaA = createTea("A", VARIETY, CurrentDate.getDate());
        teaDao.insert(teaA);
        final Tea teaC = createTea("nameC", VARIETY, CurrentDate.getDate());
        teaDao.insert(teaC);
        final Tea teaB = createTea("nameB", VARIETY, CurrentDate.getDate());
        teaDao.insert(teaB);

        final List<Tea> teaList = teaDao.getTeasBySearchString("name");

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

    private Tea createTea(final String name, final String variety, final Date date) {
        return createTea(name, variety, date, 0);
    }

    private Tea createTea(final String name, final String variety, final Date date, final int rating) {
        final Tea tea = new Tea(name, variety, 3, "ts", 15, 0, date);
        tea.setRating(rating);
        return tea;
    }
}
