package coolpharaoh.tee.speicher.tea.timer.models.daos;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.models.database.TeaMemoryDatabase;
import coolpharaoh.tee.speicher.tea.timer.models.entities.Infusion;
import coolpharaoh.tee.speicher.tea.timer.models.entities.Tea;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class InfusionDAOTest {
    private InfusionDAO mInfusionDAO;
    private TeaDAO mTeaDAO;
    private TeaMemoryDatabase db;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, TeaMemoryDatabase.class).build();
        mInfusionDAO = db.getInfusionDAO();
        mTeaDAO = db.getTeaDAO();
    }

    @After
    public void closeDb() {
        db.close();
    }

    @Test
    public void insertInfusion(){
        assertEquals(mInfusionDAO.getInfusions().size(), 0);

        long teaId = mTeaDAO.insert(createTea());

        Infusion infusionBefore = new Infusion(teaId, 1, "03:00", "10:00", 70, 158);
        mInfusionDAO.insert(infusionBefore);

        assertEquals(mInfusionDAO.getInfusions().size(), 1);

        Infusion infusionAfter = mInfusionDAO.getInfusions().get(0);
        assertEquals(infusionAfter.getTeaId(), infusionBefore.getTeaId());
        assertEquals(infusionAfter.getInfusionIndex(), infusionBefore.getInfusionIndex());
        assertEquals(infusionAfter.getTime(), infusionBefore.getTime());
        assertEquals(infusionAfter.getCoolDownTime(), infusionBefore.getCoolDownTime());
        assertEquals(infusionAfter.getTemperatureCelsius(), infusionBefore.getTemperatureCelsius());
        assertEquals(infusionAfter.getTemperatureFahrenheit(), infusionBefore.getTemperatureFahrenheit());
    }

    @Test
    public void getInfusionsByTeaId(){
        assertEquals(mInfusionDAO.getInfusions().size(), 0);

        long teaId1 = mTeaDAO.insert(createTea());

        List<Infusion> infusionBefore1 = new ArrayList<>();
        infusionBefore1.add(new Infusion(teaId1, 1, "03:00", "10:00", 70, 158));
        infusionBefore1.add(new Infusion(teaId1, 2, "02:00", "8:00", 80, 176));

        mInfusionDAO.insert(infusionBefore1.get(0));
        mInfusionDAO.insert(infusionBefore1.get(1));

        assertEquals(mInfusionDAO.getInfusions().size(), 2);


        long teaId2 = mTeaDAO.insert(createTea());

        Infusion infusionBefore2 = new Infusion(teaId2, 4, "07:00", "5:00", 100, 212);
        mInfusionDAO.insert(infusionBefore2);

        assertEquals(mInfusionDAO.getInfusions().size(), 3);

        List<Infusion> infusionAfter1 = mInfusionDAO.getInfusionsByTeaId(teaId1);
        assertEquals(infusionAfter1.size(), 2);

        assertEquals(infusionAfter1.get(0).getTeaId(), infusionBefore1.get(0).getTeaId());
        assertEquals(infusionAfter1.get(0).getInfusionIndex(), infusionBefore1.get(0).getInfusionIndex());
        assertEquals(infusionAfter1.get(0).getTime(), infusionBefore1.get(0).getTime());
        assertEquals(infusionAfter1.get(0).getCoolDownTime(), infusionBefore1.get(0).getCoolDownTime());
        assertEquals(infusionAfter1.get(0).getTemperatureCelsius(), infusionBefore1.get(0).getTemperatureCelsius());
        assertEquals(infusionAfter1.get(0).getTemperatureFahrenheit(), infusionBefore1.get(0).getTemperatureFahrenheit());

        assertEquals(infusionAfter1.get(1).getTeaId(), infusionBefore1.get(1).getTeaId());
        assertEquals(infusionAfter1.get(1).getInfusionIndex(), infusionBefore1.get(1).getInfusionIndex());
        assertEquals(infusionAfter1.get(1).getTime(), infusionBefore1.get(1).getTime());
        assertEquals(infusionAfter1.get(1).getCoolDownTime(), infusionBefore1.get(1).getCoolDownTime());
        assertEquals(infusionAfter1.get(1).getTemperatureCelsius(), infusionBefore1.get(1).getTemperatureCelsius());
        assertEquals(infusionAfter1.get(1).getTemperatureFahrenheit(), infusionBefore1.get(1).getTemperatureFahrenheit());

        List<Infusion> infusionAfter2 = mInfusionDAO.getInfusionsByTeaId(teaId2);
        assertEquals(infusionAfter2.size(), 1);

        assertEquals(infusionAfter2.get(0).getTeaId(), infusionBefore2.getTeaId());
        assertEquals(infusionAfter2.get(0).getInfusionIndex(), infusionBefore2.getInfusionIndex());
        assertEquals(infusionAfter2.get(0).getTime(), infusionBefore2.getTime());
        assertEquals(infusionAfter2.get(0).getCoolDownTime(), infusionBefore2.getCoolDownTime());
        assertEquals(infusionAfter2.get(0).getTemperatureCelsius(), infusionBefore2.getTemperatureCelsius());
        assertEquals(infusionAfter2.get(0).getTemperatureFahrenheit(), infusionBefore2.getTemperatureFahrenheit());
    }

    @Test
    public void deleteInfusionsByTeaId(){
        assertEquals(mInfusionDAO.getInfusions().size(), 0);

        long teaId1 = mTeaDAO.insert(createTea());

        List<Infusion> infusionBefore1 = new ArrayList<>();
        infusionBefore1.add(new Infusion(teaId1, 1, "03:00", "10:00", 70, 158));
        infusionBefore1.add(new Infusion(teaId1, 2, "02:00", "8:00", 80, 176));

        mInfusionDAO.insert(infusionBefore1.get(0));
        mInfusionDAO.insert(infusionBefore1.get(1));

        assertEquals(mInfusionDAO.getInfusions().size(), 2);


        long teaId2 = mTeaDAO.insert(createTea());

        Infusion infusionBefore2 = new Infusion(teaId2, 4, "07:00", "5:00", 100, 212);
        mInfusionDAO.insert(infusionBefore2);

        assertEquals(mInfusionDAO.getInfusions().size(), 3);

        mInfusionDAO.deleteInfusionByTeaId(teaId1);

        assertEquals(mInfusionDAO.getInfusions().size(), 1);

        List<Infusion> infusionAfter = mInfusionDAO.getInfusions();

        assertEquals(infusionAfter.get(0).getTeaId(), infusionBefore2.getTeaId());
        assertEquals(infusionAfter.get(0).getInfusionIndex(), infusionBefore2.getInfusionIndex());
        assertEquals(infusionAfter.get(0).getTime(), infusionBefore2.getTime());
        assertEquals(infusionAfter.get(0).getCoolDownTime(), infusionBefore2.getCoolDownTime());
        assertEquals(infusionAfter.get(0).getTemperatureCelsius(), infusionBefore2.getTemperatureCelsius());
        assertEquals(infusionAfter.get(0).getTemperatureFahrenheit(), infusionBefore2.getTemperatureFahrenheit());
    }

    private Tea createTea(){
        return new Tea("name", "variety", 3, "ts", 15, 0, Calendar.getInstance().getTime());
    }
}
