package coolpharaoh.tee.speicher.tea.timer.daos;

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

import coolpharaoh.tee.speicher.tea.timer.database.TeaMemoryDatabase;
import coolpharaoh.tee.speicher.tea.timer.entities.Infusion;
import coolpharaoh.tee.speicher.tea.timer.entities.Tea;

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

        mTeaDAO.insert(createTea());
        long teaId = mTeaDAO.getTeas().get(0).getId();

        Infusion infusionBefore = new Infusion(teaId, 1, "03:00", "10:00", 70, 158);
        mInfusionDAO.insert(infusionBefore);

        assertEquals(mInfusionDAO.getInfusions().size(), 1);

        Infusion infusionAfter = mInfusionDAO.getInfusions().get(0);
        assertEquals(infusionAfter.getTeaId(), infusionBefore.getTeaId());
        assertEquals(infusionAfter.getInfusionindex(), infusionBefore.getInfusionindex());
        assertEquals(infusionAfter.getTime(), infusionBefore.getTime());
        assertEquals(infusionAfter.getCooldowntime(), infusionBefore.getCooldowntime());
        assertEquals(infusionAfter.getTemperaturecelsius(), infusionBefore.getTemperaturecelsius());
        assertEquals(infusionAfter.getTemperaturefahrenheit(), infusionBefore.getTemperaturefahrenheit());
    }

    @Test
    public void getInfusionsByTeaId(){
        assertEquals(mInfusionDAO.getInfusions().size(), 0);

        mTeaDAO.insert(createTea());
        long teaId1 = mTeaDAO.getTeas().get(0).getId();

        List<Infusion> infusionBefore1 = new ArrayList<>();
        infusionBefore1.add(new Infusion(teaId1, 1, "03:00", "10:00", 70, 158));
        infusionBefore1.add(new Infusion(teaId1, 2, "02:00", "8:00", 80, 176));

        mInfusionDAO.insert(infusionBefore1.get(0));
        mInfusionDAO.insert(infusionBefore1.get(1));

        assertEquals(mInfusionDAO.getInfusions().size(), 2);


        mTeaDAO.insert(createTea());
        long teaId2 = mTeaDAO.getTeas().get(1).getId();

        Infusion infusionBefore2 = new Infusion(teaId2, 4, "07:00", "5:00", 100, 212);
        mInfusionDAO.insert(infusionBefore2);

        assertEquals(mInfusionDAO.getInfusions().size(), 3);

        List<Infusion> infusionAfter1 = mInfusionDAO.getInfusionsByTeaId(teaId1);
        assertEquals(infusionAfter1.size(), 2);

        assertEquals(infusionAfter1.get(0).getTeaId(), infusionBefore1.get(0).getTeaId());
        assertEquals(infusionAfter1.get(0).getInfusionindex(), infusionBefore1.get(0).getInfusionindex());
        assertEquals(infusionAfter1.get(0).getTime(), infusionBefore1.get(0).getTime());
        assertEquals(infusionAfter1.get(0).getCooldowntime(), infusionBefore1.get(0).getCooldowntime());
        assertEquals(infusionAfter1.get(0).getTemperaturecelsius(), infusionBefore1.get(0).getTemperaturecelsius());
        assertEquals(infusionAfter1.get(0).getTemperaturefahrenheit(), infusionBefore1.get(0).getTemperaturefahrenheit());

        assertEquals(infusionAfter1.get(1).getTeaId(), infusionBefore1.get(1).getTeaId());
        assertEquals(infusionAfter1.get(1).getInfusionindex(), infusionBefore1.get(1).getInfusionindex());
        assertEquals(infusionAfter1.get(1).getTime(), infusionBefore1.get(1).getTime());
        assertEquals(infusionAfter1.get(1).getCooldowntime(), infusionBefore1.get(1).getCooldowntime());
        assertEquals(infusionAfter1.get(1).getTemperaturecelsius(), infusionBefore1.get(1).getTemperaturecelsius());
        assertEquals(infusionAfter1.get(1).getTemperaturefahrenheit(), infusionBefore1.get(1).getTemperaturefahrenheit());

        List<Infusion> infusionAfter2 = mInfusionDAO.getInfusionsByTeaId(teaId2);
        assertEquals(infusionAfter2.size(), 1);

        assertEquals(infusionAfter2.get(0).getTeaId(), infusionBefore2.getTeaId());
        assertEquals(infusionAfter2.get(0).getInfusionindex(), infusionBefore2.getInfusionindex());
        assertEquals(infusionAfter2.get(0).getTime(), infusionBefore2.getTime());
        assertEquals(infusionAfter2.get(0).getCooldowntime(), infusionBefore2.getCooldowntime());
        assertEquals(infusionAfter2.get(0).getTemperaturecelsius(), infusionBefore2.getTemperaturecelsius());
        assertEquals(infusionAfter2.get(0).getTemperaturefahrenheit(), infusionBefore2.getTemperaturefahrenheit());
    }

    @Test
    public void deleteInfusionsByTeaId(){
        assertEquals(mInfusionDAO.getInfusions().size(), 0);

        mTeaDAO.insert(createTea());
        long teaId1 = mTeaDAO.getTeas().get(0).getId();

        List<Infusion> infusionBefore1 = new ArrayList<>();
        infusionBefore1.add(new Infusion(teaId1, 1, "03:00", "10:00", 70, 158));
        infusionBefore1.add(new Infusion(teaId1, 2, "02:00", "8:00", 80, 176));

        mInfusionDAO.insert(infusionBefore1.get(0));
        mInfusionDAO.insert(infusionBefore1.get(1));

        assertEquals(mInfusionDAO.getInfusions().size(), 2);


        mTeaDAO.insert(createTea());
        long teaId2 = mTeaDAO.getTeas().get(1).getId();

        Infusion infusionBefore2 = new Infusion(teaId2, 4, "07:00", "5:00", 100, 212);
        mInfusionDAO.insert(infusionBefore2);

        assertEquals(mInfusionDAO.getInfusions().size(), 3);

        mInfusionDAO.deleteInfusionByTeaId(teaId1);

        assertEquals(mInfusionDAO.getInfusions().size(), 1);

        List<Infusion> infusionAfter = mInfusionDAO.getInfusions();

        assertEquals(infusionAfter.get(0).getTeaId(), infusionBefore2.getTeaId());
        assertEquals(infusionAfter.get(0).getInfusionindex(), infusionBefore2.getInfusionindex());
        assertEquals(infusionAfter.get(0).getTime(), infusionBefore2.getTime());
        assertEquals(infusionAfter.get(0).getCooldowntime(), infusionBefore2.getCooldowntime());
        assertEquals(infusionAfter.get(0).getTemperaturecelsius(), infusionBefore2.getTemperaturecelsius());
        assertEquals(infusionAfter.get(0).getTemperaturefahrenheit(), infusionBefore2.getTemperaturefahrenheit());
    }

    private Tea createTea(){
        return new Tea("name", "variety", 3, "ts", 15, 0, Calendar.getInstance().getTime());
    }
}
