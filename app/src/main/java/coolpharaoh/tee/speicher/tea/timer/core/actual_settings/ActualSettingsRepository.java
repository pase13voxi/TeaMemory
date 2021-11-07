package coolpharaoh.tee.speicher.tea.timer.core.actual_settings;

import android.app.Application;

import coolpharaoh.tee.speicher.tea.timer.database.TeaMemoryDatabase;

// Could be removed after the successful migration (In half a year 1.6.2022)
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
