package coolpharaoh.tee.speicher.tea.timer.views.new_tea

import android.app.Application
import android.content.res.Resources
import coolpharaoh.tee.speicher.tea.timer.R
import coolpharaoh.tee.speicher.tea.timer.TaskExecutorExtension
import coolpharaoh.tee.speicher.tea.timer.core.infusion.Infusion
import coolpharaoh.tee.speicher.tea.timer.core.infusion.InfusionRepository
import coolpharaoh.tee.speicher.tea.timer.core.settings.SharedSettings
import coolpharaoh.tee.speicher.tea.timer.core.settings.TemperatureUnit
import coolpharaoh.tee.speicher.tea.timer.core.tea.AmountKind
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea
import coolpharaoh.tee.speicher.tea.timer.core.tea.TeaRepository
import coolpharaoh.tee.speicher.tea.timer.core.tea.Variety
import coolpharaoh.tee.speicher.tea.timer.core.tea.Variety.Companion.convertStoredVarietyToText
import coolpharaoh.tee.speicher.tea.timer.core.tea.Variety.Companion.fromStoredText
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import java.time.Instant
import java.util.Date

@ExtendWith(MockitoExtension::class, TaskExecutorExtension::class)
internal class NewTeaViewModelTest {
    private var newTeaViewModelEmpty: NewTeaViewModel? = null
    private var newTeaViewModelFilled: NewTeaViewModel? = null

    @Mock
    lateinit var application: Application

    @Mock
    lateinit var resources: Resources

    @Mock
    lateinit var teaRepository: TeaRepository

    @Mock
    lateinit var infusionRepository: InfusionRepository

    @Mock
    lateinit var sharedSettings: SharedSettings

    private var tea: Tea? = null

    @BeforeEach
    fun setUp() {
        newTeaViewModelEmpty = NewTeaViewModel(null, application, teaRepository, infusionRepository, sharedSettings)
        mockStoredTea()
        newTeaViewModelFilled = NewTeaViewModel(TEA_ID_FILLED, application, teaRepository, infusionRepository, sharedSettings)
    }

    @Test
    fun getTeaId() {
        assertThat(newTeaViewModelFilled!!.teaId).isEqualTo(tea!!.id)
    }

    @Test
    fun getName() {
        assertThat(newTeaViewModelFilled!!.name).isEqualTo(tea!!.name)
    }

    @Test
    fun getVarietyAsText() {
        mockStringResource()

        assertThat(newTeaViewModelFilled!!.varietyAsText).isEqualTo(convertStoredVarietyToText(tea!!.variety, application))
    }

    @Test
    fun getVariety() {
        assertThat(newTeaViewModelFilled!!.variety).isEqualTo(fromStoredText(tea!!.variety))
    }

    @Test
    fun setVariety() {
        mockStringResource()

        newTeaViewModelEmpty!!.setVariety("VARIETY")

        assertThat(newTeaViewModelEmpty!!.varietyAsText).isEqualTo("VARIETY")
    }

    @Test
    fun setAmountAndExpectAmountAndAmountKind() {
        newTeaViewModelEmpty!!.setAmount(5.5, AmountKind.TEA_SPOON)

        assertThat(newTeaViewModelEmpty!!.amount).isEqualTo(5.5)
        assertThat(newTeaViewModelEmpty!!.amountKind).isEqualTo(AmountKind.TEA_SPOON)
    }

    @Test
    fun setColorAndExpectColor() {
        newTeaViewModelEmpty!!.color = 1234

        assertThat(newTeaViewModelEmpty!!.color).isEqualTo(1234)
    }

    @Test
    fun setTemperatureCelsiusAndExpectTemperature() {
        `when`(sharedSettings.temperatureUnit).thenReturn(TemperatureUnit.CELSIUS)

        newTeaViewModelEmpty!!.setInfusionTemperature(90)

        assertThat(newTeaViewModelEmpty!!.getInfusionTemperature()).isEqualTo(90)
        assertThat(newTeaViewModelEmpty!!.getTemperatureUnit()).isEqualTo(TemperatureUnit.CELSIUS)
    }

    @Test
    fun setTemperatureCelsiusAndExpectFahrenheitTemperature() {
        `when`(sharedSettings.temperatureUnit).thenReturn(TemperatureUnit.CELSIUS)

        newTeaViewModelEmpty!!.setInfusionTemperature(90)

        `when`(sharedSettings.temperatureUnit).thenReturn(TemperatureUnit.FAHRENHEIT)
        assertThat(newTeaViewModelEmpty!!.getInfusionTemperature()).isEqualTo(194)
        assertThat(newTeaViewModelEmpty!!.getTemperatureUnit()).isEqualTo(TemperatureUnit.FAHRENHEIT)
    }

    @Test
    fun setTemperatureFahrenheitAndExpectTemperature() {
        `when`(sharedSettings.temperatureUnit).thenReturn(TemperatureUnit.FAHRENHEIT)

        newTeaViewModelEmpty!!.setInfusionTemperature(194)

        assertThat(newTeaViewModelEmpty!!.getInfusionTemperature()).isEqualTo(194)
        assertThat(newTeaViewModelEmpty!!.getTemperatureUnit()).isEqualTo(TemperatureUnit.FAHRENHEIT)
    }

    @Test
    fun setTemperatureFahrenheitAndExpectCelsiusTemperature() {
        `when`(sharedSettings.temperatureUnit).thenReturn(TemperatureUnit.FAHRENHEIT)

        newTeaViewModelEmpty!!.setInfusionTemperature(194)

        `when`(sharedSettings.temperatureUnit).thenReturn(TemperatureUnit.CELSIUS)
        assertThat(newTeaViewModelEmpty!!.getInfusionTemperature()).isEqualTo(90)
        assertThat(newTeaViewModelEmpty!!.getTemperatureUnit()).isEqualTo(TemperatureUnit.CELSIUS)
    }

    @Test
    fun setTemperatureFahrenheitAndExpectStoredTemperatureFahrenheitAndTemperature() {
        `when`(sharedSettings.temperatureUnit).thenReturn(TemperatureUnit.CELSIUS)

        newTeaViewModelEmpty!!.setInfusionTemperature(90)

        assertThat(newTeaViewModelEmpty!!.getInfusionTemperature()).isEqualTo(90)
        assertThat(newTeaViewModelEmpty!!.getTemperatureUnit()).isEqualTo(TemperatureUnit.CELSIUS)
    }

    @Test
    fun setCoolDownTimeAndExpectCoolDownTime() {
        newTeaViewModelEmpty!!.setInfusionCoolDownTime(TIME_1)

        assertThat(newTeaViewModelEmpty!!.getInfusionCoolDownTime()).isEqualTo(TIME_1)
    }

    @Test
    fun resetCoolDownTimeAndExpectNull() {
        newTeaViewModelEmpty!!.resetInfusionCoolDownTime()

        assertThat(newTeaViewModelEmpty!!.getInfusionCoolDownTime()).isNull()
    }

    @Test
    fun setTimeAndExpectTime() {
        newTeaViewModelEmpty!!.setInfusionTime(TIME_1)

        assertThat(newTeaViewModelEmpty!!.getInfusionTime()).isEqualTo(TIME_1)
    }

    @Test
    fun addInfusion() {
        `when`(sharedSettings.temperatureUnit).thenReturn(TemperatureUnit.CELSIUS)
        assertThat(newTeaViewModelEmpty!!.getInfusionSize()).isEqualTo(1)

        newTeaViewModelEmpty!!.addInfusion()

        assertThat(newTeaViewModelEmpty!!.dataChanges()!!.value).isEqualTo(1)
        assertThat(newTeaViewModelEmpty!!.getInfusionSize()).isEqualTo(2)
        assertThat(newTeaViewModelEmpty!!.getInfusionTime()).isNull()
        assertThat(newTeaViewModelEmpty!!.getInfusionCoolDownTime()).isNull()
        assertThat(newTeaViewModelEmpty!!.getInfusionTemperature()).isEqualTo(-500)
    }

    @Test
    fun deleteInfusionAndExpectTwoInfusions() {
        assertThat(newTeaViewModelFilled!!.getInfusionSize()).isEqualTo(2)

        newTeaViewModelFilled!!.deleteInfusion()

        assertThat(newTeaViewModelFilled!!.getInfusionSize()).isEqualTo(1)
    }

    @Test
    fun deleteFirstInfusionAndExpectIndexZero() {
        newTeaViewModelFilled!!.deleteInfusion()

        assertThat(newTeaViewModelFilled!!.dataChanges()!!.value).isZero
    }

    @Test
    fun deleteLastInfusionAndExpectIndexOne() {
        newTeaViewModelFilled!!.nextInfusion()

        newTeaViewModelFilled!!.deleteInfusion()

        assertThat(newTeaViewModelFilled!!.dataChanges()!!.value).isZero
    }

    @Test
    fun navigateToNextAndPreviousInfusion() {
        newTeaViewModelFilled!!.nextInfusion()
        assertThat(newTeaViewModelFilled!!.dataChanges()!!.value).isEqualTo(1)

        newTeaViewModelFilled!!.previousInfusion()

        assertThat(newTeaViewModelFilled!!.dataChanges()!!.value).isZero
    }

    @Test
    fun couldNotNavigateToNextInfusion() {
        newTeaViewModelEmpty!!.nextInfusion()

        assertThat(newTeaViewModelFilled!!.dataChanges()!!.value).isZero
    }

    @Test
    fun couldNotNavigateToPreviousInfusion() {
        newTeaViewModelEmpty!!.previousInfusion()

        assertThat(newTeaViewModelEmpty!!.dataChanges()!!.value).isZero
    }

    @Test
    fun saveTeaAndExpectNewTea() {
        newTeaViewModelEmpty!!.saveTea("name")
        argumentCaptor<Tea>().apply {
            verify(teaRepository).insertTea(capture())
            val (_, name1) = lastValue
            assertThat(name1).isEqualTo("name")
            verify(infusionRepository).insertInfusion(any())
        }
    }

    @Test
    fun saveTeaAndExpectEditedExistingTea() {
        newTeaViewModelFilled!!.saveTea("name")

        argumentCaptor<Tea>().apply {
            verify(teaRepository).updateTea(capture())
            val (_, name1) = lastValue
            assertThat(name1).isEqualTo("name")
            verify(infusionRepository).deleteInfusionsByTeaId(TEA_ID_FILLED)
            verify(infusionRepository, times(2)).insertInfusion(any())
        }
    }

    private fun mockStoredTea() {
        val today = Date.from(Instant.now())
        tea = Tea("TEA", Variety.YELLOW_TEA.code, 3.0, AmountKind.TEA_SPOON.text, 5, 0, today)
        tea!!.id = TEA_ID_FILLED
        `when`(teaRepository.getTeaById(TEA_ID_FILLED)).thenReturn(tea)

        val infusions: MutableList<Infusion> = ArrayList()
        val infusion1 = Infusion(TEA_ID_FILLED, 0, "2", "0:30", 5, 5)
        infusions.add(infusion1)
        val infusion2 = Infusion(TEA_ID_FILLED, 1, "4", "1", 50, 100)
        infusions.add(infusion2)
        `when`(infusionRepository.getInfusionsByTeaId(TEA_ID_FILLED)).thenReturn(infusions)
    }

    private fun mockStringResource() {
        `when`(application.resources).thenReturn(resources)
        `when`(resources.getStringArray(R.array.new_tea_variety_teas)).thenReturn(VARIETY_TEAS)
    }

    companion object {
        private const val TIME_1 = "05:45"
        private val VARIETY_TEAS = arrayOf(
            "Black tea", "Green tea", "Yellow tea", "White tea", "Oolong tea",
            "Pu-erh tea", "Herbal tea", "Fruit tea", "Rooibus tea", "Other"
        )
        private const val TEA_ID_FILLED = 1L
    }
}