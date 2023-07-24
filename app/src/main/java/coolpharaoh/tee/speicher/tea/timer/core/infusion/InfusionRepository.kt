package coolpharaoh.tee.speicher.tea.timer.core.infusion

import android.app.Application
import coolpharaoh.tee.speicher.tea.timer.database.TeaMemoryDatabase

class InfusionRepository(application: Application?) {
    private val infusionDao: InfusionDao

    init {
        val database = TeaMemoryDatabase.getDatabaseInstance(application)
        infusionDao = database.infusionDao
    }

    fun insertInfusion(infusion: Infusion) {
        infusionDao.insert(infusion)
    }

    val infusions: List<Infusion>
        get() = infusionDao.getInfusions()

    fun getInfusionsByTeaId(id: Long): List<Infusion> {
        return infusionDao.getInfusionsByTeaId(id)
    }

    fun deleteInfusionsByTeaId(id: Long) {
        infusionDao.deleteInfusionsByTeaId(id)
    }
}