package coolpharaoh.tee.speicher.tea.timer.core.tea;

import android.app.Application;

import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.database.TeaMemoryDatabase;

public class TeaRepository {
    private final TeaDao teaDao;

    public TeaRepository(final Application application) {
        final TeaMemoryDatabase teaMemoryDatabase = TeaMemoryDatabase.getDatabaseInstance(application);
        teaDao = teaMemoryDatabase.getTeaDao();
    }

    public long insertTea(final Tea tea) {
        return teaDao.insert(tea);
    }

    public void updateTea(final Tea tea) {
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

    public List<Tea> getTeasOrderByActivity(final boolean inStock) {
        if (inStock) {
            return teaDao.getTeasInStockOrderByActivity();
        } else {
            return teaDao.getTeasOrderByActivity();
        }
    }

    public List<Tea> getTeasOrderByAlphabetic(final boolean inStock) {
        if (inStock) {
            return teaDao.getTeasInStockOrderByAlphabetic();
        } else {
            return teaDao.getTeasOrderByAlphabetic();
        }
    }

    public List<Tea> getTeasOrderByVariety(final boolean inStock) {
        if (inStock) {
            return teaDao.getTeasInStockOrderByVariety();
        } else {
            return teaDao.getTeasOrderByVariety();
        }
    }

    public List<Tea> getTeasOrderByRating(final boolean inStock) {
        if (inStock) {
            return teaDao.getTeasInStockOrderByRating();
        } else {
            return teaDao.getTeasOrderByRating();
        }
    }

    public Tea getTeaById(final long id) {
        return teaDao.getTeaById(id);
    }

    public Tea getRandomTeaInStock() {
        return teaDao.getRandomTeaInStock();
    }

    public List<Tea> getTeasBySearchString(final String searchString) {
        return teaDao.getTeasBySearchString(searchString);
    }
}
