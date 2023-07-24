package coolpharaoh.tee.speicher.tea.timer.core.infusion;

import android.app.Application;

import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.database.TeaMemoryDatabase;

public class InfusionRepository {

    private final InfusionDao infusionDao;

    public InfusionRepository(final Application application) {
        final TeaMemoryDatabase database = TeaMemoryDatabase.getDatabaseInstance(application);
        infusionDao = database.getInfusionDao();
    }

    public void insertInfusion(final Infusion infusion) {
        infusionDao.insert(infusion);
    }

    public List<Infusion> getInfusions() {
        return infusionDao.getInfusions();
    }

    public List<Infusion> getInfusionsByTeaId(final long id) {
        return infusionDao.getInfusionsByTeaId(id);
    }

    public void deleteInfusionsByTeaId(final long id) {
        infusionDao.deleteInfusionsByTeaId(id);
    }
}
