package coolpharaoh.tee.speicher.tea.timer.core.infusion;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.core.date.CurrentDate;
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea;
import coolpharaoh.tee.speicher.tea.timer.core.tea.TeaDao;
import coolpharaoh.tee.speicher.tea.timer.database.TeaMemoryDatabase;

@RunWith(AndroidJUnit4.class)
public class InfusionDaoTest {
    private InfusionDao mInfusionDAO;
    private TeaDao mTeaDAO;
    private TeaMemoryDatabase db;

    @Before
    public void createDb() {
        final Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, TeaMemoryDatabase.class).build();
        mInfusionDAO = db.getInfusionDao();
        mTeaDAO = db.getTeaDao();
    }

    @After
    public void closeDb() {
        db.close();
    }

    @Test
    public void insertInfusion() {
        assertThat(mInfusionDAO.getInfusions()).isEmpty();

        final long teaId = insertTea();

        final List<Infusion> infusionBefore = insertInfusions(teaId, 1);

        assertThat(mInfusionDAO.getInfusions()).hasSize(1);

        final Infusion infusionAfter = mInfusionDAO.getInfusions().get(0);
        assertThat(infusionAfter).isEqualToIgnoringGivenFields(infusionBefore.get(0), "id");
    }

    @Test
    public void getInfusionsByTeaId() {
        assertThat(mInfusionDAO.getInfusions()).isEmpty();

        final long teaId1 = insertTea();

        final List<Infusion> infusionsBefore1 = insertInfusions(teaId1, 2);

        assertThat(mInfusionDAO.getInfusions()).hasSize(2);

        final long teaId2 = insertTea();

        final List<Infusion> infusionBefore2 = insertInfusions(teaId2, 1);

        assertThat(mInfusionDAO.getInfusions()).hasSize(3);

        final List<Infusion> infusionAfter1 = mInfusionDAO.getInfusionsByTeaId(teaId1);
        assertThat(infusionAfter1).hasSize(2);

        assertThat(infusionAfter1.get(0)).isEqualToIgnoringGivenFields(infusionsBefore1.get(0), "id");

        assertThat(infusionAfter1.get(1)).isEqualToIgnoringGivenFields(infusionsBefore1.get(1), "id");

        final List<Infusion> infusionAfter2 = mInfusionDAO.getInfusionsByTeaId(teaId2);
        assertThat(infusionAfter2).hasSize(1);

        assertThat(infusionAfter2.get(0)).isEqualToIgnoringGivenFields(infusionBefore2.get(0), "id");
    }

    @Test
    public void deleteInfusionsByTeaId() {
        assertThat(mInfusionDAO.getInfusions()).isEmpty();

        final long teaId1 = insertTea();

        insertInfusions(teaId1, 2);

        assertThat(mInfusionDAO.getInfusions()).hasSize(2);

        final long teaId2 = insertTea();

        final List<Infusion> infusionBefore2 = insertInfusions(teaId2, 1);

        assertThat(mInfusionDAO.getInfusions()).hasSize(3);

        mInfusionDAO.deleteInfusionsByTeaId(teaId1);

        assertThat(mInfusionDAO.getInfusions()).hasSize(1);

        final List<Infusion> infusionAfter = mInfusionDAO.getInfusions();

        assertThat(infusionAfter.get(0)).isEqualToIgnoringGivenFields(infusionBefore2.get(0), "id");
    }

    private long insertTea() {
        return mTeaDAO.insert(new Tea("name", "variety", 3, "ts", 15, 0, CurrentDate.getDate()));
    }

    private List<Infusion> insertInfusions(final long teaId1, final int count) {
        final List<Infusion> infusionBefore1 = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            infusionBefore1.add(new Infusion(teaId1, i + 1, "03:00", "10:00", 70, 158));
            mInfusionDAO.insert(infusionBefore1.get(i));
        }
        return infusionBefore1;
    }
}
