package coolpharaoh.tee.speicher.tea.timer.core.infusion;

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

import coolpharaoh.tee.speicher.tea.timer.core.database.TeaMemoryDatabase;
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea;
import coolpharaoh.tee.speicher.tea.timer.core.tea.TeaDao;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@RunWith(AndroidJUnit4.class)
public class InfusionDAOTest {
    private InfusionDao mInfusionDAO;
    private TeaDao mTeaDAO;
    private TeaMemoryDatabase db;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, TeaMemoryDatabase.class).build();
        mInfusionDAO = db.getInfusionDao();
        mTeaDAO = db.getTeaDao();
    }

    @After
    public void closeDb() {
        db.close();
    }

    @Test
    public void insertInfusion(){
        assertThat(mInfusionDAO.getInfusions()).hasSize(0);

        long teaId = mTeaDAO.insert(createTea());

        Infusion infusionBefore = new Infusion(teaId, 1, "03:00", "10:00", 70, 158);
        mInfusionDAO.insert(infusionBefore);

        assertThat(mInfusionDAO.getInfusions()).hasSize(1);

        Infusion infusionAfter = mInfusionDAO.getInfusions().get(0);
        assertThat(infusionAfter).isEqualToIgnoringGivenFields(infusionBefore, "id");
    }

    @Test
    public void getInfusionsByTeaId(){
        assertThat(mInfusionDAO.getInfusions()).hasSize(0);

        long teaId1 = mTeaDAO.insert(createTea());

        List<Infusion> infusionBefore1 = new ArrayList<>();
        infusionBefore1.add(new Infusion(teaId1, 1, "03:00", "10:00", 70, 158));
        infusionBefore1.add(new Infusion(teaId1, 2, "02:00", "8:00", 80, 176));

        mInfusionDAO.insert(infusionBefore1.get(0));
        mInfusionDAO.insert(infusionBefore1.get(1));

        assertThat(mInfusionDAO.getInfusions()).hasSize(2);

        long teaId2 = mTeaDAO.insert(createTea());

        Infusion infusionBefore2 = new Infusion(teaId2, 4, "07:00", "5:00", 100, 212);
        mInfusionDAO.insert(infusionBefore2);

        assertThat(mInfusionDAO.getInfusions()).hasSize(3);

        List<Infusion> infusionAfter1 = mInfusionDAO.getInfusionsByTeaId(teaId1);
        assertThat(infusionAfter1).hasSize(2);

        assertThat(infusionAfter1.get(0)).isEqualToIgnoringGivenFields(infusionBefore1.get(0), "id");

        assertThat(infusionAfter1.get(1)).isEqualToIgnoringGivenFields(infusionBefore1.get(1), "id");

        List<Infusion> infusionAfter2 = mInfusionDAO.getInfusionsByTeaId(teaId2);
        assertThat(infusionAfter2).hasSize(1);

        assertThat(infusionAfter2.get(0)).isEqualToIgnoringGivenFields(infusionBefore2, "id");
    }

    @Test
    public void deleteInfusionsByTeaId(){
        assertThat(mInfusionDAO.getInfusions()).hasSize(0);

        long teaId1 = mTeaDAO.insert(createTea());

        List<Infusion> infusionBefore1 = new ArrayList<>();
        infusionBefore1.add(new Infusion(teaId1, 1, "03:00", "10:00", 70, 158));
        infusionBefore1.add(new Infusion(teaId1, 2, "02:00", "8:00", 80, 176));

        mInfusionDAO.insert(infusionBefore1.get(0));
        mInfusionDAO.insert(infusionBefore1.get(1));

        assertThat(mInfusionDAO.getInfusions()).hasSize(2);

        long teaId2 = mTeaDAO.insert(createTea());

        Infusion infusionBefore2 = new Infusion(teaId2, 4, "07:00", "5:00", 100, 212);
        mInfusionDAO.insert(infusionBefore2);

        assertThat(mInfusionDAO.getInfusions()).hasSize(3);

        mInfusionDAO.deleteInfusionsByTeaId(teaId1);

        assertThat(mInfusionDAO.getInfusions()).hasSize(1);

        List<Infusion> infusionAfter = mInfusionDAO.getInfusions();

        assertThat(infusionAfter.get(0)).isEqualToIgnoringGivenFields(infusionBefore2, "id");
    }

    private Tea createTea(){
        return new Tea("name", "variety", 3, "ts", 15, 0, Calendar.getInstance().getTime());
    }
}
