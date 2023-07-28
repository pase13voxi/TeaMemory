package coolpharaoh.tee.speicher.tea.timer.views.overview

import android.app.Application
import androidx.annotation.VisibleForTesting
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import coolpharaoh.tee.speicher.tea.timer.core.date.CurrentDate.getDate
import coolpharaoh.tee.speicher.tea.timer.core.infusion.Infusion
import coolpharaoh.tee.speicher.tea.timer.core.infusion.InfusionRepository
import coolpharaoh.tee.speicher.tea.timer.core.infusion.TemperatureConversation.celsiusToCoolDownTime
import coolpharaoh.tee.speicher.tea.timer.core.infusion.TemperatureConversation.celsiusToFahrenheit
import coolpharaoh.tee.speicher.tea.timer.core.settings.SharedSettings
import coolpharaoh.tee.speicher.tea.timer.core.settings.SortMode
import coolpharaoh.tee.speicher.tea.timer.core.tea.AmountKind
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea
import coolpharaoh.tee.speicher.tea.timer.core.tea.TeaRepository
import coolpharaoh.tee.speicher.tea.timer.core.tea.Variety

class OverviewViewModel @VisibleForTesting constructor(
    application: Application, private val teaRepository: TeaRepository,
    private val infusionRepository: InfusionRepository,
    private val sharedSettings: SharedSettings) : ViewModel() {

    private val teas: MutableLiveData<List<Tea>>
    private var searchMode = false

    constructor(application: Application) : this(
        application, TeaRepository(application),
        InfusionRepository(application), SharedSettings(application)
    )

    init {
        if (sharedSettings.isFirstStart) {
            createDefaultTeas(application)
            sharedSettings.isFirstStart = false
            sharedSettings.isOverviewUpdateAlert = false
        }

        teas = MutableLiveData()

        refreshTeas()
    }

    // Defaults
    private fun createDefaultTeas(application: Application) {
        val tea1 = Tea("Earl Grey", Variety.BLACK_TEA.code, 5.0, AmountKind.TEA_SPOON.text, ContextCompat.getColor(application, Variety.BLACK_TEA.color), 0, getDate())
        val teaId1 = teaRepository.insertTea(tea1)
        val infusion1 = Infusion(teaId1, 0, "3:30", celsiusToCoolDownTime(100), 100, celsiusToFahrenheit(100))
        infusionRepository.insertInfusion(infusion1)

        val tea2 = Tea("Pai Mu Tan", Variety.WHITE_TEA.code, 4.0, AmountKind.TEA_SPOON.text, ContextCompat.getColor(application, Variety.WHITE_TEA.color), 0, getDate())
        val teaId2 = teaRepository.insertTea(tea2)
        val infusion2 = Infusion(teaId2, 0, "2", celsiusToCoolDownTime(85), 85, celsiusToFahrenheit(85))
        infusionRepository.insertInfusion(infusion2)

        val tea3 = Tea("Sencha", Variety.GREEN_TEA.code, 4.0, AmountKind.TEA_SPOON.text, ContextCompat.getColor(application, Variety.GREEN_TEA.color), 0, getDate())
        val teaId3 = teaRepository.insertTea(tea3)
        val infusion3 = Infusion(teaId3, 0, "1:30", celsiusToCoolDownTime(80), 80, celsiusToFahrenheit(80))
        infusionRepository.insertInfusion(infusion3)
    }

    // Teas
    fun getTeas(): LiveData<List<Tea>> {
        return teas
    }

    fun getTeaBy(id: Long): Tea? {
        return teaRepository.getTeaById(id)
    }

    fun deleteTea(id: Long) {
        teaRepository.deleteTeaById(id)
        refreshTeas()
    }

    fun isTeaInStock(id: Long): Boolean {
        val tea = teaRepository.getTeaById(id)
        return tea!!.inStock
    }

    fun updateInStockOfTea(id: Long, inStock: Boolean) {
        val tea = teaRepository.getTeaById(id)
        tea!!.inStock = inStock
        teaRepository.updateTea(tea)
        refreshTeas()
    }

    val randomTeaInStock: Tea?
        get() = teaRepository.randomTeaInStock

    fun visualizeTeasBySearchString(searchString: String) {
        if ("" == searchString) {
            refreshTeas()
        } else {
            searchMode = true
            teas.setValue(teaRepository.getTeasBySearchString(searchString))
        }
    }

    // Settings
    var sort: SortMode
        get() = sharedSettings.sortMode
        set(sortMode) {
            sharedSettings.sortMode = sortMode
            refreshTeas()
        }

    val isOverviewHeader: Boolean
        get() = sharedSettings.isOverviewHeader

    var isOverviewInStock: Boolean
        get() = sharedSettings.isOverviewInStock
        set(inStock) {
            sharedSettings.isOverviewInStock = inStock
            refreshTeas()
        }

    val sortWithHeader: Int
        get() = if (isOverviewHeader && !searchMode) sort.choice else -1

    var isOverviewUpdateAlert: Boolean
        get() = sharedSettings.isOverviewUpdateAlert
        set(updateAlert) {
            sharedSettings.isOverviewUpdateAlert = updateAlert
        }

    fun refreshTeas() {
        searchMode = false
        when (sharedSettings.sortMode) {
            SortMode.LAST_USED -> teas.setValue(
                teaRepository.getTeasOrderByActivity(
                    this.isOverviewInStock
                )
            )

            SortMode.ALPHABETICAL -> teas.setValue(teaRepository.getTeasOrderByAlphabetic(this.isOverviewInStock))
            SortMode.BY_VARIETY -> teas.setValue(teaRepository.getTeasOrderByVariety(this.isOverviewInStock))
            SortMode.RATING -> teas.setValue(teaRepository.getTeasOrderByRating(this.isOverviewInStock))
        }
    }
}