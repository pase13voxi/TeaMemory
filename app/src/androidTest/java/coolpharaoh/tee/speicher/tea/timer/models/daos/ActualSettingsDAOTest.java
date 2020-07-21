package coolpharaoh.tee.speicher.tea.timer.models.daos;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import coolpharaoh.tee.speicher.tea.timer.models.database.TeaMemoryDatabase;
import coolpharaoh.tee.speicher.tea.timer.models.entities.ActualSettings;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(AndroidJUnit4.class)
public class ActualSettingsDAOTest {
    private ActualSettingsDao mActualSettingsDAO;
    private TeaMemoryDatabase db;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, TeaMemoryDatabase.class).build();
        mActualSettingsDAO = db.getActualSettingsDao();
    }

    @After
    public void closeDb() {
        db.close();
    }

    @Test
    public void insertSettings() {
        ActualSettings actualSettingsBefore = createSettings(false);
        mActualSettingsDAO.insert(actualSettingsBefore);

        assertThat(mActualSettingsDAO.getCountItems()).isEqualTo(1);

        ActualSettings actualSettingsAfter = mActualSettingsDAO.getSettings();
        assertThat(actualSettingsAfter).isEqualToIgnoringGivenFields(actualSettingsBefore, "id");
    }

    @Test
    public void insertIncompleteSettings() {
        ActualSettings actualSettingsBefore = createSettings(true);
        mActualSettingsDAO.insert(actualSettingsBefore);

        assertThat(mActualSettingsDAO.getCountItems()).isEqualTo(1);

        ActualSettings actualSettingsAfter = mActualSettingsDAO.getSettings();
        assertThat(actualSettingsAfter).isEqualToIgnoringGivenFields(actualSettingsBefore, "id");
    }

    @Test
    public void updateSettings() {
        ActualSettings actualSettingsBefore = createSettings(false);
        mActualSettingsDAO.insert(actualSettingsBefore);

        assertThat(mActualSettingsDAO.getCountItems()).isEqualTo(1);

        ActualSettings actualSettingsUpdate = mActualSettingsDAO.getSettings();
        actualSettingsUpdate.setTemperatureUnit("Fahrenheit");
        actualSettingsUpdate.setVibration(false);
        mActualSettingsDAO.update(actualSettingsUpdate);

        assertThat(mActualSettingsDAO.getCountItems()).isEqualTo(1);

        ActualSettings actualSettingsAfter = mActualSettingsDAO.getSettings();
        assertThat(actualSettingsAfter).isEqualToComparingFieldByField(actualSettingsUpdate);
    }

    @Test
    public void countSettings() {
        assertThat(mActualSettingsDAO.getCountItems()).isEqualTo(0);

        mActualSettingsDAO.insert(createSettings(false));

        assertThat(mActualSettingsDAO.getCountItems()).isEqualTo(1);

        mActualSettingsDAO.insert(createSettings(false));
        mActualSettingsDAO.insert(createSettings(false));

        assertThat(mActualSettingsDAO.getCountItems()).isEqualTo(3);
    }

    private ActualSettings createSettings(boolean incomplete){
        ActualSettings actualSettings = new ActualSettings();
        actualSettings.setMusicChoice("choice");
        actualSettings.setMusicName("name");
        actualSettings.setVibration(true);
        if(incomplete) {
            actualSettings.setAnimation(true);
            actualSettings.setTemperatureUnit("Celsius");
            actualSettings.setMainRateCounter(10);
        }
        actualSettings.setShowTeaAlert(true);
        actualSettings.setMainRateAlert(true);
        actualSettings.setSort(3);
        return actualSettings;
    }
}
