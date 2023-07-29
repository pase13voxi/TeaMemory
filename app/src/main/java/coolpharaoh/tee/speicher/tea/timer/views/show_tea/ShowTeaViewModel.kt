package coolpharaoh.tee.speicher.tea.timer.views.show_tea

import android.app.Application
import androidx.annotation.VisibleForTesting
import coolpharaoh.tee.speicher.tea.timer.core.counter.Counter
import coolpharaoh.tee.speicher.tea.timer.core.counter.CounterRepository
import coolpharaoh.tee.speicher.tea.timer.core.counter.RefreshCounter.refreshCounter
import coolpharaoh.tee.speicher.tea.timer.core.date.CurrentDate.getDate
import coolpharaoh.tee.speicher.tea.timer.core.infusion.InfusionRepository
import coolpharaoh.tee.speicher.tea.timer.core.infusion.TimeConverter
import coolpharaoh.tee.speicher.tea.timer.core.settings.SharedSettings
import coolpharaoh.tee.speicher.tea.timer.core.settings.TemperatureUnit
import coolpharaoh.tee.speicher.tea.timer.core.tea.AmountKind
import coolpharaoh.tee.speicher.tea.timer.core.tea.AmountKind.Companion.fromText
import coolpharaoh.tee.speicher.tea.timer.core.tea.TeaRepository
import coolpharaoh.tee.speicher.tea.timer.core.tea.Variety.Companion.convertStoredVarietyToText

internal class ShowTeaViewModel
@VisibleForTesting constructor(
    private val teaId: Long, private val application: Application, private val teaRepository: TeaRepository,
    private val infusionRepository: InfusionRepository, private val counterRepository: CounterRepository,
    private val sharedSettings: SharedSettings
) {

    constructor(teaId: Long, application: Application) : this(
        teaId, application, TeaRepository(application), InfusionRepository(application),
        CounterRepository(application), SharedSettings(application)
    )

    var infusionIndex = 0

    // Tea
    fun teaExists(): Boolean {
        return teaRepository.getTeaById(teaId) != null
    }

    fun getTeaId(): Long {
        return teaRepository.getTeaById(teaId)!!.id!!
    }

    val name: String?
        get() = teaRepository.getTeaById(teaId)!!.name

    val variety: String?
        get() {
            val variety = teaRepository.getTeaById(teaId)!!.variety
            return if (variety == null || "" == variety) {
                "-"
            } else {
                convertStoredVarietyToText(variety, application)
            }
        }

    val amount: Double
        get() = teaRepository.getTeaById(teaId)!!.amount

    val amountKind: AmountKind
        get() {
            val amountKind = teaRepository.getTeaById(teaId)!!.amountKind
            return fromText(amountKind)
        }

    val color: Int
        get() = teaRepository.getTeaById(teaId)!!.color

    fun setCurrentDate() {
        val tea = teaRepository.getTeaById(teaId)
        tea!!.date = getDate()
        teaRepository.updateTea(tea)
    }

    val nextInfusion: Int
        get() = teaRepository.getTeaById(teaId)!!.nextInfusion

    fun updateNextInfusion() {
        val tea = teaRepository.getTeaById(teaId)
        if (infusionIndex + 1 >= getInfusionSize()) {
            tea!!.nextInfusion = 0
        } else {
            tea!!.nextInfusion = infusionIndex + 1
        }
        teaRepository.updateTea(tea)
    }

    fun resetNextInfusion() {
        val tea = teaRepository.getTeaById(teaId)
        tea!!.nextInfusion = 0
        teaRepository.updateTea(tea)
    }

    // Infusion
    val time: TimeConverter
        get() = TimeConverter(infusionRepository.getInfusionsByTeaId(teaId)[infusionIndex].time)

    val coolDownTime: TimeConverter
        get() = TimeConverter(infusionRepository.getInfusionsByTeaId(teaId)[infusionIndex].coolDownTime)

    val temperature: Int
        get() = if (TemperatureUnit.FAHRENHEIT == getTemperatureUnit()) {
            infusionRepository.getInfusionsByTeaId(teaId)[infusionIndex].temperatureFahrenheit
        } else {
            infusionRepository.getInfusionsByTeaId(teaId)[infusionIndex].temperatureCelsius
        }

    fun getInfusionSize(): Int {
        return infusionRepository.getInfusionsByTeaId(teaId).size
    }

    fun incrementInfusionIndex() {
        infusionIndex++
    }

    //Counter
    fun countCounter() {
        val counter = getOrCreateCounter()
        refreshCounter(counter)
        val currentDate = getDate()
        counter.yearDate = currentDate
        counter.monthDate = currentDate
        counter.weekDate = currentDate
        counter.overall = counter.overall + 1
        counter.year = counter.year + 1
        counter.month = counter.month + 1
        counter.week = counter.week + 1
        counterRepository.updateCounter(counter)
    }

    fun getOverallCounter(): Long {
        val (_, _, _, _, _, overall) = getOrCreateCounter()
        return overall
    }

    private fun getOrCreateCounter(): Counter {
        var counter = counterRepository.getCounterByTeaId(teaId)
        if (counter == null) {
            counter = Counter()
            counter.teaId = teaId
            counter.week = 0
            counter.month = 0
            counter.year = 0
            counter.overall = 0
            counter.weekDate = getDate()
            counter.monthDate = getDate()
            counter.yearDate = getDate()
            counter.id = counterRepository.insertCounter(counter)
        }

        return counter
    }

    // Settings
    fun isAnimation(): Boolean {
        return sharedSettings.isAnimation
    }

    fun isShowTeaAlert(): Boolean {
        return sharedSettings.isShowTeaAlert
    }

    fun setShowTeaAlert(showTeaAlert: Boolean) {
        sharedSettings.isShowTeaAlert = showTeaAlert
    }

    fun getTemperatureUnit(): TemperatureUnit {
        return sharedSettings.temperatureUnit
    }
}