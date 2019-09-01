package coolpharaoh.tee.speicher.tea.timer.daos;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;

import coolpharaoh.tee.speicher.tea.timer.database.TeaMemoryDatabase;

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
}
