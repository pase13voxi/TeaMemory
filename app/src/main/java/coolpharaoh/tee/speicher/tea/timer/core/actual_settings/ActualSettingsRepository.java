package coolpharaoh.tee.speicher.tea.timer.core.actual_settings;

import android.app.Application;

import coolpharaoh.tee.speicher.tea.timer.database.TeaMemoryDatabase;

public class ActualSettingsRepository {
    private final ActualSettingsDao actualSettingsDao;

    public ActualSettingsRepository(final Application application) {
        final TeaMemoryDatabase teaMemoryDatabase = TeaMemoryDatabase.getDatabaseInstance(application);
        actualSettingsDao = teaMemoryDatabase.getActualSettingsDao();
    }

    public void insertSettings(final ActualSettings actualSettings) {
        actualSettingsDao.insert(actualSettings);
    }

    public void updateSettings(final ActualSettings actualSettings) {
        actualSettingsDao.update(actualSettings);
    }

    public ActualSettings getSettings() {
        return actualSettingsDao.getSettings();
    }

    public int getCountItems() {
        return actualSettingsDao.getCountItems();
    }
}
