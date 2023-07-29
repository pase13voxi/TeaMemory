package coolpharaoh.tee.speicher.tea.timer.views.new_tea

import android.app.Application
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import coolpharaoh.tee.speicher.tea.timer.core.date.CurrentDate.getDate
import coolpharaoh.tee.speicher.tea.timer.core.infusion.Infusion
import coolpharaoh.tee.speicher.tea.timer.core.infusion.InfusionRepository
import coolpharaoh.tee.speicher.tea.timer.core.infusion.TemperatureConversation.celsiusToFahrenheit
import coolpharaoh.tee.speicher.tea.timer.core.infusion.TemperatureConversation.fahrenheitToCelsius
import coolpharaoh.tee.speicher.tea.timer.core.settings.SharedSettings
import coolpharaoh.tee.speicher.tea.timer.core.settings.TemperatureUnit
import coolpharaoh.tee.speicher.tea.timer.core.tea.AmountKind
import coolpharaoh.tee.speicher.tea.timer.core.tea.AmountKind.Companion.fromText
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea
import coolpharaoh.tee.speicher.tea.timer.core.tea.TeaRepository
import coolpharaoh.tee.speicher.tea.timer.core.tea.Variety
import coolpharaoh.tee.speicher.tea.timer.core.tea.Variety.Companion.convertStoredVarietyToText
import coolpharaoh.tee.speicher.tea.timer.core.tea.Variety.Companion.convertTextToStoredVariety
import coolpharaoh.tee.speicher.tea.timer.core.tea.Variety.Companion.fromStoredText

class NewTeaViewModel
@VisibleForTesting constructor(
    teaId: Long?, private val application: Application, private val teaRepository: TeaRepository,
    private val infusionRepository: InfusionRepository, private val sharedSettings: SharedSettings
) {

    private var infusionIndex: MutableLiveData<Int>? = null
    private var tea: Tea? = null
    private var infusions: MutableList<Infusion> = ArrayList()

    constructor(application: Application) : this(null, application, TeaRepository(application),
        InfusionRepository(application), SharedSettings(application))

    constructor(teaId: Long, application: Application) : this(teaId, application, TeaRepository(application),
        InfusionRepository(application), SharedSettings(application))

    init {
        initializeTeaAndInfusions(teaId)
    }

    private fun initializeTeaAndInfusions(teaId: Long?) {
        infusionIndex = MutableLiveData(0)
        if (teaId == null) {
            tea = Tea()
            tea!!.variety = "01_black"
            tea!!.color = -15461296
            tea!!.amount = (-500).toDouble()
            tea!!.amountKind = "Ts"
            tea!!.rating = 0
            tea!!.inStock = true
            infusions = ArrayList()
            addInfusion()
        } else {
            tea = teaRepository.getTeaById(teaId)
            infusions.addAll(infusionRepository.getInfusionsByTeaId(teaId))
        }
    }

    // Tea
    val teaId: Long
        get() = tea!!.id!!

    val name: String?
        get() = tea!!.name

    val varietyAsText: String?
        get() = convertStoredVarietyToText(tea!!.variety, application)

    fun setVariety(variety: String) {
        tea!!.variety = convertTextToStoredVariety(variety, application)
        signalDataChanged()
    }

    val variety: Variety
        get() = fromStoredText(tea!!.variety)

    fun setAmount(amount: Double, amountKind: AmountKind) {
        tea!!.amount = amount
        tea!!.amountKind = amountKind.text
        signalDataChanged()
    }

    val amount: Double
        get() = tea!!.amount

    val amountKind: AmountKind
        get() = fromText(tea!!.amountKind)

    var color: Int
        get() = tea!!.color
        set(color) {
            tea!!.color = color
            signalDataChanged()
        }

    // Infusion
    fun dataChanges(): LiveData<Int>? {
        return infusionIndex
    }

    fun getInfusionIndex(): Int {
        return if (infusionIndex != null) {
            infusionIndex!!.value!!
        } else {
            infusionIndex = MutableLiveData(0)
            infusionIndex!!.value!!
        }
    }

    fun addInfusion() {
        val infusion = Infusion()
        infusion.temperatureCelsius = -500
        infusion.temperatureFahrenheit = -500
        infusions.add(infusion)
        if (getInfusionSize() > 1) {
            infusionIndex!!.value = getInfusionIndex() + 1
        }
        signalDataChanged()
    }

    fun deleteInfusion() {
        if (getInfusionSize() > 1) {
            infusions.removeAt(getInfusionIndex())
            if (getInfusionIndex() == getInfusionSize()) {
                infusionIndex!!.value = getInfusionIndex() - 1
            }
            signalDataChanged()
        }
    }

    fun previousInfusion() {
        if (getInfusionIndex() - 1 >= 0) {
            infusionIndex!!.value = getInfusionIndex() - 1
        }
    }

    fun nextInfusion() {
        if (getInfusionIndex() + 1 < getInfusionSize()) {
            infusionIndex!!.value = getInfusionIndex() + 1
        }
    }

    fun getInfusionSize(): Int {
        return infusions.size
    }

    fun setInfusionTemperature(temperature: Int) {
        if (isFahrenheit()) {
            infusions[getInfusionIndex()].temperatureFahrenheit = temperature
            infusions[getInfusionIndex()].temperatureCelsius = fahrenheitToCelsius(temperature)
        } else {
            infusions[getInfusionIndex()].temperatureCelsius = temperature
            infusions[getInfusionIndex()].temperatureFahrenheit = celsiusToFahrenheit(temperature)
        }
        signalDataChanged()
    }

    fun getInfusionTemperature(): Int {
        return if (isFahrenheit()) {
            infusions[getInfusionIndex()].temperatureFahrenheit
        } else {
            infusions[getInfusionIndex()].temperatureCelsius
        }
    }

    fun setInfusionTime(time: String?) {
        infusions[getInfusionIndex()].time = time
        signalDataChanged()
    }

    fun getInfusionTime(): String? {
        return infusions[getInfusionIndex()].time
    }

    fun setInfusionCoolDownTime(time: String?) {
        infusions[getInfusionIndex()].coolDownTime = time
        signalDataChanged()
    }

    fun resetInfusionCoolDownTime() {
        infusions[getInfusionIndex()].coolDownTime = null
    }

    fun getInfusionCoolDownTime(): String? {
        return infusions[getInfusionIndex()].coolDownTime
    }

    // Settings
    fun getTemperatureUnit(): TemperatureUnit {
        return sharedSettings.temperatureUnit
    }

    // Overall
    fun saveTea(name: String) {
        // if id is null a new Tea will be created
        if (tea!!.id == null) {
            insertTea(name)
        } else {
            updateTea(name)
        }
    }

    private fun updateTea(name: String) {
        setTeaInformation(name)

        teaRepository.updateTea(tea!!)

        saveInfusions(tea!!.id!!)
    }

    private fun insertTea(name: String) {
        setTeaInformation(name)
        tea!!.nextInfusion = 0

        val teaId = teaRepository.insertTea(tea!!)

        saveInfusions(teaId)
    }

    private fun setTeaInformation(name: String) {
        tea!!.name = name
        tea!!.date = getDate()
    }

    private fun saveInfusions(teaId: Long) {
        infusionRepository.deleteInfusionsByTeaId(teaId)
        for (i in 0 until getInfusionSize()) {
            infusions[i].teaId = teaId
            infusions[i].infusionIndex = i
            infusionRepository.insertInfusion(infusions[i])
        }
    }

    private fun isFahrenheit(): Boolean {
        return TemperatureUnit.FAHRENHEIT == getTemperatureUnit()
    }

    private fun signalDataChanged() {
        infusionIndex!!.value = getInfusionIndex()
    }
}