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
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.models.database.TeaMemoryDatabase;
import coolpharaoh.tee.speicher.tea.timer.models.entities.Tea;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class TeaDAOTest {
    private TeaDAO teaDAO;
    private TeaMemoryDatabase db;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, TeaMemoryDatabase.class).build();
        teaDAO = db.getTeaDAO();
    }

    @After
    public void closeDb() {
        db.close();
    }

    @Test
    public void insertTea(){
        assertEquals(teaDAO.getTeas().size(), 0);

        Tea teaBefore = createTea("name", "variety", Calendar.getInstance().getTime());
        teaDAO.insert(teaBefore);

        assertEquals(teaDAO.getTeas().size(), 1);

        Tea teaAfter = teaDAO.getTeas().get(0);
        assertEquals(teaAfter.getName(), teaBefore.getName());
        assertEquals(teaAfter.getVariety(), teaBefore.getVariety());
        assertEquals(teaAfter.getAmount(), teaBefore.getAmount());
        assertEquals(teaAfter.getAmountKind(), teaBefore.getAmountKind());
        assertEquals(teaAfter.getColor(), teaBefore.getColor());
        assertEquals(teaAfter.getNextInfusion(), teaBefore.getNextInfusion());
        assertEquals(teaAfter.getDate(), teaBefore.getDate());
    }

    @Test
    public void updateTea(){
        assertEquals(teaDAO.getTeas().size(), 0);

        Tea teaBefore = createTea("name", "variety", Calendar.getInstance().getTime());
        teaDAO.insert(teaBefore);

        assertEquals(teaDAO.getTeas().size(), 1);

        Tea teaUpdate = teaDAO.getTeas().get(0);
        teaUpdate.setName("NameChanged");
        teaUpdate.setName("VarietyChanged");
        teaDAO.update(teaUpdate);

        Tea teaAfter = teaDAO.getTeas().get(0);
        assertEquals(teaAfter.getName(), teaUpdate.getName());
        assertEquals(teaAfter.getVariety(), teaUpdate.getVariety());
        assertEquals(teaAfter.getAmount(), teaUpdate.getAmount());
        assertEquals(teaAfter.getAmountKind(), teaUpdate.getAmountKind());
        assertEquals(teaAfter.getColor(), teaUpdate.getColor());
        assertEquals(teaAfter.getNextInfusion(), teaUpdate.getNextInfusion());
        assertEquals(teaAfter.getDate(), teaUpdate.getDate());
    }

    @Test
    public void deleteTea(){
        assertEquals(teaDAO.getTeas().size(), 0);

        Tea teaBefore1 = createTea("name1", "variety1", Calendar.getInstance().getTime());
        teaDAO.insert(teaBefore1);

        Tea teaBefore2 = createTea("name2", "variety2", Calendar.getInstance().getTime());
        teaDAO.insert(teaBefore2);

        assertEquals(teaDAO.getTeas().size(), 2);

        teaDAO.delete(teaDAO.getTeas().get(0));

        assertEquals(teaDAO.getTeas().size(), 1);

        Tea teaAfter2 = teaDAO.getTeas().get(0);
        assertEquals(teaAfter2.getName(), teaBefore2.getName());
        assertEquals(teaAfter2.getVariety(), teaBefore2.getVariety());
        assertEquals(teaAfter2.getAmount(), teaBefore2.getAmount());
        assertEquals(teaAfter2.getAmountKind(), teaBefore2.getAmountKind());
        assertEquals(teaAfter2.getColor(), teaBefore2.getColor());
        assertEquals(teaAfter2.getNextInfusion(), teaBefore2.getNextInfusion());
        assertEquals(teaAfter2.getDate(), teaBefore2.getDate());
    }

    @Test
    public void deleteAllTeas(){
        assertEquals(teaDAO.getTeas().size(), 0);

        Tea teaBefore1 = createTea("name1", "variety1", Calendar.getInstance().getTime());
        teaDAO.insert(teaBefore1);

        Tea teaBefore2 = createTea("name2", "variety2", Calendar.getInstance().getTime());
        teaDAO.insert(teaBefore2);

        assertEquals(teaDAO.getTeas().size(), 2);

        teaDAO.deleteAll();

        assertEquals(teaDAO.getTeas().size(), 0);
    }

    @Test
    public void getTeaOrderByActivity(){
        Tea teaOld = createTea("nameOld", "varietyOld", new GregorianCalendar(2016, Calendar.FEBRUARY, 36).getTime());
        teaDAO.insert(teaOld);
        Tea teaMiddle = createTea("nameMiddle", "varietyMiddle", new GregorianCalendar(2018, Calendar.FEBRUARY, 11).getTime());
        teaDAO.insert(teaMiddle);
        Tea teaNew = createTea("nameNew", "varietyNew", new GregorianCalendar(2018, Calendar.DECEMBER, 15).getTime());
        teaDAO.insert(teaNew);

        List<Tea> teaList = teaDAO.getTeasOrderByActivity();

        assertEquals(teaList.get(0).getName(), teaNew.getName());
        assertEquals(teaList.get(0).getVariety(), teaNew.getVariety());
        assertEquals(teaList.get(0).getDate(), teaNew.getDate());

        assertEquals(teaList.get(1).getName(), teaMiddle.getName());
        assertEquals(teaList.get(1).getVariety(), teaMiddle.getVariety());
        assertEquals(teaList.get(1).getDate(), teaMiddle.getDate());

        assertEquals(teaList.get(2).getName(), teaOld.getName());
        assertEquals(teaList.get(2).getVariety(), teaOld.getVariety());
        assertEquals(teaList.get(2).getDate(), teaOld.getDate());
    }

    @Test
    public void getTeaById(){
        Tea teaBefore1 = createTea("nameOld", "varietyOld", new GregorianCalendar(2016, Calendar.FEBRUARY, 36).getTime());
        long teaId1 = teaDAO.insert(teaBefore1);

        Tea teaBefore2 = createTea("nameMiddle", "varietyMiddle", new GregorianCalendar(2018, Calendar.FEBRUARY, 11).getTime());
        long teaId2 = teaDAO.insert(teaBefore2);

        Tea teaAfter1 = teaDAO.getTeaById(teaId1);
        assertEquals(teaAfter1.getName(), teaBefore1.getName());
        assertEquals(teaAfter1.getVariety(), teaBefore1.getVariety());
        assertEquals(teaAfter1.getAmount(), teaBefore1.getAmount());
        assertEquals(teaAfter1.getAmountKind(), teaBefore1.getAmountKind());
        assertEquals(teaAfter1.getColor(), teaBefore1.getColor());
        assertEquals(teaAfter1.getNextInfusion(), teaBefore1.getNextInfusion());
        assertEquals(teaAfter1.getDate(), teaBefore1.getDate());

        Tea teaAfter2 = teaDAO.getTeaById(teaId2);
        assertEquals(teaAfter2.getName(), teaBefore2.getName());
        assertEquals(teaAfter2.getVariety(), teaBefore2.getVariety());
        assertEquals(teaAfter2.getAmount(), teaBefore2.getAmount());
        assertEquals(teaAfter2.getAmountKind(), teaBefore2.getAmountKind());
        assertEquals(teaAfter2.getColor(), teaBefore2.getColor());
        assertEquals(teaAfter2.getNextInfusion(), teaBefore2.getNextInfusion());
        assertEquals(teaAfter2.getDate(), teaBefore2.getDate());
    }

    @Test
    public void getTeaOrderByAlphabetic(){
        Tea teaC = createTea("nameC", "varietyC", Calendar.getInstance().getTime());
        teaDAO.insert(teaC);
        Tea teaA = createTea("nameA", "varietyA", Calendar.getInstance().getTime());
        teaDAO.insert(teaA);
        Tea teaB = createTea("nameB", "varietyB", Calendar.getInstance().getTime());
        teaDAO.insert(teaB);

        List<Tea> teaList = teaDAO.getTeasOrderByAlphabetic();

        assertEquals(teaList.get(0).getName(), teaA.getName());
        assertEquals(teaList.get(0).getVariety(), teaA.getVariety());
        assertEquals(teaList.get(0).getDate(), teaA.getDate());

        assertEquals(teaList.get(1).getName(), teaB.getName());
        assertEquals(teaList.get(1).getVariety(), teaB.getVariety());
        assertEquals(teaList.get(1).getDate(), teaB.getDate());

        assertEquals(teaList.get(2).getName(), teaC.getName());
        assertEquals(teaList.get(2).getVariety(), teaC.getVariety());
        assertEquals(teaList.get(2).getDate(), teaC.getDate());
    }

    @Test
    public void getTeaOrderByVariety(){
        Tea teaC = createTea("nameC", "varietyC", Calendar.getInstance().getTime());
        teaDAO.insert(teaC);
        Tea teaA = createTea("nameA", "varietyA", Calendar.getInstance().getTime());
        teaDAO.insert(teaA);
        Tea teaB = createTea("nameB", "varietyB", Calendar.getInstance().getTime());
        teaDAO.insert(teaB);

        List<Tea> teaList = teaDAO.getTeasOrderByVariety();

        assertEquals(teaList.get(0).getName(), teaA.getName());
        assertEquals(teaList.get(0).getVariety(), teaA.getVariety());
        assertEquals(teaList.get(0).getDate(), teaA.getDate());

        assertEquals(teaList.get(1).getName(), teaB.getName());
        assertEquals(teaList.get(1).getVariety(), teaB.getVariety());
        assertEquals(teaList.get(1).getDate(), teaB.getDate());

        assertEquals(teaList.get(2).getName(), teaC.getName());
        assertEquals(teaList.get(2).getVariety(), teaC.getVariety());
        assertEquals(teaList.get(2).getDate(), teaC.getDate());
    }

    private Tea createTea(String name, String variety, Date date){
        return new Tea(name, variety, 3, "ts", 15, 0, date);
    }
}
