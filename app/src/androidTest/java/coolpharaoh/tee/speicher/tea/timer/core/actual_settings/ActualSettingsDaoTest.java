package coolpharaoh.tee.speicher.tea.timer.core.actual_settings;

import static org.assertj.core.api.Assertions.assertThat;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import coolpharaoh.tee.speicher.tea.timer.database.TeaMemoryDatabase;

// Could be removed after the successful migration (In half a year 1.6.2022)
@RunWith(AndroidJUnit4.class)
public class ActualSettingsDaoTest {
    private ActualSettingsDao mActualSettingsDAO;
    private TeaMemoryDatabase db;

    @Before
    public void createDb() {
        final Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, TeaMemoryDatabase.class).build();
        mActualSettingsDAO = db.getActualSettingsDao();
    }

    @After
    public void closeDb() {
        db.close();
    }

    @Test
    public void insertSettings() {
        final ActualSettings actualSettingsBefore = createSettings(false);
        mActualSettingsDAO.insert(actualSettingsBefore);

        assertThat(mActualSettingsDAO.getCountItems()).isEqualTo(1);

        final ActualSettings actualSettingsAfter = mActualSettingsDAO.getSettings();
        assertThat(actualSettingsAfter).isEqualToIgnoringGivenFields(actualSettingsBefore, "id");
    }

    @Test
    public void insertIncompleteSettings() {
        final ActualSettings actualSettingsBefore = createSettings(true);
        mActualSettingsDAO.insert(actualSettingsBefore);

        assertThat(mActualSettingsDAO.getCountItems()).isEqualTo(1);

        final ActualSettings actualSettingsAfter = mActualSettingsDAO.getSettings();
        assertThat(actualSettingsAfter).isEqualToIgnoringGivenFields(actualSettingsBefore, "id");
    }

    @Test
    public void updateSettings() {
        final ActualSettings actualSettingsBefore = createSettings(false);
        mActualSettingsDAO.insert(actualSettingsBefore);

        assertThat(mActualSettingsDAO.getCountItems()).isEqualTo(1);

        final ActualSettings actualSettingsUpdate = mActualSettingsDAO.getSettings();
        actualSettingsUpdate.setTemperatureUnit("Fahrenheit");
        actualSettingsUpdate.setVibration(false);
        mActualSettingsDAO.update(actualSettingsUpdate);

        assertThat(mActualSettingsDAO.getCountItems()).isEqualTo(1);

        final ActualSettings actualSettingsAfter = mActualSettingsDAO.getSettings();
        assertThat(actualSettingsAfter).isEqualToComparingFieldByField(actualSettingsUpdate);
    }

    @Test
    public void countSettings() {
        assertThat(mActualSettingsDAO.getCountItems()).isZero();

        mActualSettingsDAO.insert(createSettings(false));

        assertThat(mActualSettingsDAO.getCountItems()).isEqualTo(1);

        mActualSettingsDAO.insert(createSettings(false));
        mActualSettingsDAO.insert(createSettings(false));

        assertThat(mActualSettingsDAO.getCountItems()).isEqualTo(3);
    }

    private ActualSettings createSettings(final boolean incomplete) {
        final ActualSettings actualSettings = new ActualSettings();
        actualSettings.setMusicChoice("choice");
        actualSettings.setMusicName("name");
        actualSettings.setVibration(true);
        if (incomplete) {
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
