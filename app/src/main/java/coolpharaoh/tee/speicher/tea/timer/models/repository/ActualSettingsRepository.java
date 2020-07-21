package coolpharaoh.tee.speicher.tea.timer.models.repository;

import android.app.Application;

import coolpharaoh.tee.speicher.tea.timer.models.daos.ActualSettingsDao;
import coolpharaoh.tee.speicher.tea.timer.models.database.TeaMemoryDatabase;
import coolpharaoh.tee.speicher.tea.timer.models.entities.ActualSettings;

public class ActualSettingsRepository {
    private ActualSettingsDao actualSettingsDao;

    public ActualSettingsRepository(Application application) {
        TeaMemoryDatabase teaMemoryDatabase = TeaMemoryDatabase.getDatabaseInstance(application);
        actualSettingsDao = teaMemoryDatabase.getActualSettingsDAO();
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
}
