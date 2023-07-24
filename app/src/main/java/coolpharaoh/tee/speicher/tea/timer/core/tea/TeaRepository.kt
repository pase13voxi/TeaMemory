package coolpharaoh.tee.speicher.tea.timer.core.tea

import android.app.Application
import coolpharaoh.tee.speicher.tea.timer.database.TeaMemoryDatabase

class TeaRepository(application: Application?) {
    private val teaDao: TeaDao

    init {
        val teaMemoryDatabase = TeaMemoryDatabase.getDatabaseInstance(application)
        teaDao = teaMemoryDatabase.teaDao
    }

    fun insertTea(tea: Tea): Long {
        return teaDao.insert(tea)
    }

    fun updateTea(tea: Tea) {
        teaDao.update(tea)
    }

    fun deleteTeaById(id: Long) {
        teaDao.deleteTeaById(id)
    }

    fun deleteAllTeas() {
        teaDao.deleteAll()
    }

    val teas: List<Tea>
        get() = teaDao.getTeas()

    fun getTeasOrderByActivity(inStock: Boolean): List<Tea> {
        return if (inStock) {
            teaDao.getTeasInStockOrderByActivity()
        } else {
            teaDao.getTeasOrderByActivity()
        }
    }

    fun getTeasOrderByAlphabetic(inStock: Boolean): List<Tea> {
        return if (inStock) {
            teaDao.getTeasInStockOrderByAlphabetic()
        } else {
            teaDao.getTeasOrderByAlphabetic()
        }
    }

    fun getTeasOrderByVariety(inStock: Boolean): List<Tea> {
        return if (inStock) {
            teaDao.getTeasInStockOrderByVariety()
        } else {
            teaDao.getTeasOrderByVariety()
        }
    }

    fun getTeasOrderByRating(inStock: Boolean): List<Tea> {
        return if (inStock) {
            teaDao.getTeasInStockOrderByRating()
        } else {
            teaDao.getTeasOrderByRating()
        }
    }

    fun getTeaById(id: Long): Tea? {
        return teaDao.getTeaById(id)
    }

    val randomTeaInStock: Tea?
        get() = teaDao.getRandomTeaInStock()

    fun getTeasBySearchString(searchString: String): List<Tea> {
        return teaDao.getTeasBySearchString(searchString)
    }
}