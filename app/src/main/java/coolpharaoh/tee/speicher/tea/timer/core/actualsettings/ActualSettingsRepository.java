package coolpharaoh.tee.speicher.tea.timer.core.actualsettings;

import android.app.Application;

import coolpharaoh.tee.speicher.tea.timer.core.database.TeaMemoryDatabase;

public class ActualSettingsRepository {
    private ActualSettingsDao actualSettingsDao;

    public ActualSettingsRepository(Application application) {
        TeaMemoryDatabase teaMemoryDatabase = TeaMemoryDatabase.getDatabaseInstance(application);
        actualSettingsDao = teaMemoryDatabase.getActualSettingsDao();
    }

    public void insertSettings(ActualSettings actualSettings) {
        actualSettingsDao.insert(actualSettings);
    }

    public void updateSettings(ActualSettings actualSettings) {
        actualSettingsDao.update(actualSettings);
    }

    public ActualSettings getSettings() {
        return actualSettingsDao.getSettings();
    }

    public int getCountItems() {
        return actualSettingsDao.getCountItems();
    }

    // Only for testing
    ActualSettingsRepository(ActualSettingsDao actualSettingsDao) {
        this.actualSettingsDao = actualSettingsDao;
    }
}
