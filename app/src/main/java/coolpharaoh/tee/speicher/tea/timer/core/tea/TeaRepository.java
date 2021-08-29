package coolpharaoh.tee.speicher.tea.timer.core.tea;

import android.app.Application;

import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.database.TeaMemoryDatabase;

public class TeaRepository {
    private final TeaDao teaDao;

    public TeaRepository(Application application) {
        TeaMemoryDatabase teaMemoryDatabase = TeaMemoryDatabase.getDatabaseInstance(application);
        teaDao = teaMemoryDatabase.getTeaDao();
    }

    public long insertTea(Tea tea) {
        return teaDao.insert(tea);
    }

    public void updateTea(Tea tea) {
        teaDao.update(tea);
    }

    public void deleteTeaById(final long id) {
        teaDao.deleteTeaById(id);
    }

    public void deleteAllTeas() {
        teaDao.deleteAll();
    }

    public List<Tea> getTeas() {
        return teaDao.getTeas();
    }

    public List<Tea> getTeasOrderByActivity(final boolean favorite) {
        if (favorite) {
            return teaDao.getFavoriteTeasOrderByActivity();
        } else {
            return teaDao.getTeasOrderByActivity();
        }
    }

    public List<Tea> getTeasOrderByAlphabetic(final boolean favorite) {
        if (favorite) {
            return teaDao.getFavoriteTeasOrderByAlphabetic();
        } else {
            return teaDao.getTeasOrderByAlphabetic();
        }
    }

    public List<Tea> getTeasOrderByVariety(final boolean favorite) {
        if (favorite) {
            return teaDao.getFavoriteTeasOrderByVariety();
        } else {
            return teaDao.getTeasOrderByVariety();
        }
    }

    public List<Tea> getTeasOrderByRating(final boolean favorite) {
        if (favorite) {
            return teaDao.getFavoriteTeasOrderByRating();
        } else {
            return teaDao.getTeasOrderByRating();
        }
    }

    public Tea getTeaById(long id) {
        return teaDao.getTeaById(id);
    }

    public List<Tea> getTeasBySearchString(String searchString) {
        return teaDao.getTeasBySearchString(searchString);
    }
}
