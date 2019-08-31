package coolpharaoh.tee.speicher.tea.timer.daos;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import coolpharaoh.tee.speicher.tea.timer.database.TeaMemoryDatabase;
import coolpharaoh.tee.speicher.tea.timer.entities.ActualSettings;

import static org.junit.Assert.assertEquals;


@RunWith(AndroidJUnit4.class)
public class ActualSettingsDAOTest {
    private ActualSettingsDAO mActualSettingsDao;
    private TeaMemoryDatabase db;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, TeaMemoryDatabase.class).build();
        mActualSettingsDao = db.getActualSettingsDAO();
    }

    @After
    public void closeDb() {
        db.close();
    }

    @Test
    public void insertSettings() {
        ActualSettings actualSettingsBefore = createSettings(false);
        mActualSettingsDao.insert(actualSettingsBefore);

        assertEquals(mActualSettingsDao.getCountItems(), 1);

        ActualSettings actualSettingsAfter = mActualSettingsDao.getSettings();
        assertEquals(actualSettingsAfter.getMusicchoice(),actualSettingsBefore.getMusicchoice());
        assertEquals(actualSettingsAfter.getMusicname(),actualSettingsBefore.getMusicname());
        assertEquals(actualSettingsAfter.isVibration(),actualSettingsBefore.isVibration());
        assertEquals(actualSettingsAfter.isNotification(),actualSettingsBefore.isNotification());
        assertEquals(actualSettingsAfter.isAnimation(),actualSettingsBefore.isAnimation());
        assertEquals(actualSettingsAfter.getTemperatureunit(),actualSettingsBefore.getTemperatureunit());
        assertEquals(actualSettingsAfter.isShowteaalert(),actualSettingsBefore.isShowteaalert());
        assertEquals(actualSettingsAfter.isMainproblemalert(),actualSettingsBefore.isMainproblemalert());
        assertEquals(actualSettingsAfter.isMainratealert(),actualSettingsBefore.isMainratealert());
        assertEquals(actualSettingsAfter.getMainratecounter(),actualSettingsBefore.getMainratecounter());
        assertEquals(actualSettingsAfter.getSort(),actualSettingsBefore.getSort());
    }

    @Test
    public void insertIncompleteSettings() {
        ActualSettings actualSettingsBefore = createSettings(true);
        mActualSettingsDao.insert(actualSettingsBefore);

        assertEquals(mActualSettingsDao.getCountItems(), 1);

        ActualSettings actualSettingsAfter = mActualSettingsDao.getSettings();
        assertEquals(actualSettingsAfter.getMusicchoice(),actualSettingsBefore.getMusicchoice());
        assertEquals(actualSettingsAfter.getMusicname(),actualSettingsBefore.getMusicname());
        assertEquals(actualSettingsAfter.isVibration(),actualSettingsBefore.isVibration());
        assertEquals(actualSettingsAfter.isNotification(),actualSettingsBefore.isNotification());
        assertEquals(actualSettingsAfter.isAnimation(),actualSettingsBefore.isAnimation());
        assertEquals(actualSettingsAfter.getTemperatureunit(),actualSettingsBefore.getTemperatureunit());
        assertEquals(actualSettingsAfter.isShowteaalert(),actualSettingsBefore.isShowteaalert());
        assertEquals(actualSettingsAfter.isMainproblemalert(),actualSettingsBefore.isMainproblemalert());
        assertEquals(actualSettingsAfter.isMainratealert(),actualSettingsBefore.isMainratealert());
        assertEquals(actualSettingsAfter.getMainratecounter(),actualSettingsBefore.getMainratecounter());
        assertEquals(actualSettingsAfter.getSort(),actualSettingsBefore.getSort());
    }

    @Test
    public void updateSettings() {
        ActualSettings actualSettingsBefore = createSettings(false);
        mActualSettingsDao.insert(actualSettingsBefore);

        ActualSettings actualSettingsUpdate = mActualSettingsDao.getSettings();
        actualSettingsUpdate.setTemperatureunit("Fahrenheit");
        actualSettingsUpdate.setVibration(false);
        mActualSettingsDao.update(actualSettingsUpdate);

        assertEquals(mActualSettingsDao.getCountItems(), 1);

        ActualSettings actualSettingsAfter = mActualSettingsDao.getSettings();
        assertEquals(actualSettingsAfter.getMusicchoice(),actualSettingsUpdate.getMusicchoice());
        assertEquals(actualSettingsAfter.getMusicname(),actualSettingsUpdate.getMusicname());
        assertEquals(actualSettingsAfter.isVibration(),actualSettingsUpdate.isVibration());
        assertEquals(actualSettingsAfter.isNotification(),actualSettingsUpdate.isNotification());
        assertEquals(actualSettingsAfter.isAnimation(),actualSettingsUpdate.isAnimation());
        assertEquals(actualSettingsAfter.getTemperatureunit(),actualSettingsUpdate.getTemperatureunit());
        assertEquals(actualSettingsAfter.isShowteaalert(),actualSettingsUpdate.isShowteaalert());
        assertEquals(actualSettingsAfter.isMainproblemalert(),actualSettingsUpdate.isMainproblemalert());
        assertEquals(actualSettingsAfter.isMainratealert(),actualSettingsUpdate.isMainratealert());
        assertEquals(actualSettingsAfter.getMainratecounter(),actualSettingsUpdate.getMainratecounter());
        assertEquals(actualSettingsAfter.getSort(),actualSettingsUpdate.getSort());
    }

    private ActualSettings createSettings(boolean incomplete){
        ActualSettings actualSettings = new ActualSettings();
        actualSettings.setMusicchoice("abc");
        actualSettings.setMusicname("abc");
        actualSettings.setVibration(true);
        actualSettings.setNotification(true);
        if(incomplete) {
            actualSettings.setAnimation(true);
            actualSettings.setTemperatureunit("Celsius");
            actualSettings.setMainratecounter(10);
        }
        actualSettings.setShowteaalert(true);
        actualSettings.setMainproblemalert(true);
        actualSettings.setMainratealert(true);
        actualSettings.setSort(3);
        return actualSettings;
    }
}
