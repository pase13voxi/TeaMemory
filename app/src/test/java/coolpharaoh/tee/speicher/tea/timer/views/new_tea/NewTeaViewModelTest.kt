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
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.slot
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.time.Instant
import java.util.Date

@ExtendWith(MockKExtension::class, TaskExecutorExtension::class)
internal class NewTeaViewModelTest {
    private var newTeaViewModelEmpty: NewTeaViewModel? = null
    private var newTeaViewModelFilled: NewTeaViewModel? = null

    @MockK
    lateinit var application: Application
    @MockK
    lateinit var resources: Resources
    @RelaxedMockK
    lateinit var teaRepository: TeaRepository
    @RelaxedMockK
    lateinit var infusionRepository: InfusionRepository
    @MockK
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
        every { sharedSettings.temperatureUnit } returns TemperatureUnit.CELSIUS

        newTeaViewModelEmpty!!.setInfusionTemperature(90)

        assertThat(newTeaViewModelEmpty!!.getInfusionTemperature()).isEqualTo(90)
        assertThat(newTeaViewModelEmpty!!.getTemperatureUnit()).isEqualTo(TemperatureUnit.CELSIUS)
    }

    @Test
    fun setTemperatureCelsiusAndExpectFahrenheitTemperature() {
        every { sharedSettings.temperatureUnit } returns TemperatureUnit.CELSIUS

        newTeaViewModelEmpty!!.setInfusionTemperature(90)

        every { sharedSettings.temperatureUnit } returns TemperatureUnit.FAHRENHEIT
        assertThat(newTeaViewModelEmpty!!.getInfusionTemperature()).isEqualTo(194)
        assertThat(newTeaViewModelEmpty!!.getTemperatureUnit()).isEqualTo(TemperatureUnit.FAHRENHEIT)
    }

    @Test
    fun setTemperatureFahrenheitAndExpectTemperature() {
        every { sharedSettings.temperatureUnit } returns TemperatureUnit.FAHRENHEIT

        newTeaViewModelEmpty!!.setInfusionTemperature(194)

        assertThat(newTeaViewModelEmpty!!.getInfusionTemperature()).isEqualTo(194)
        assertThat(newTeaViewModelEmpty!!.getTemperatureUnit()).isEqualTo(TemperatureUnit.FAHRENHEIT)
    }

    @Test
    fun setTemperatureFahrenheitAndExpectCelsiusTemperature() {
        every { sharedSettings.temperatureUnit } returns TemperatureUnit.FAHRENHEIT

        newTeaViewModelEmpty!!.setInfusionTemperature(194)

        every { sharedSettings.temperatureUnit } returns TemperatureUnit.CELSIUS
        assertThat(newTeaViewModelEmpty!!.getInfusionTemperature()).isEqualTo(90)
        assertThat(newTeaViewModelEmpty!!.getTemperatureUnit()).isEqualTo(TemperatureUnit.CELSIUS)
    }

    @Test
    fun setTemperatureFahrenheitAndExpectStoredTemperatureFahrenheitAndTemperature() {
        every { sharedSettings.temperatureUnit } returns TemperatureUnit.CELSIUS

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
        every { sharedSettings.temperatureUnit } returns TemperatureUnit.CELSIUS
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

        val slotTea = slot<Tea>()
        verify { teaRepository.insertTea(capture(slotTea)) }
        val (_, name1) = slotTea.captured
        assertThat(name1).isEqualTo("name")
        verify { infusionRepository.insertInfusion(any()) }
    }

    @Test
    fun saveTeaAndExpectEditedExistingTea() {
        newTeaViewModelFilled!!.saveTea("name")

        val slotTea = slot<Tea>()
        verify { teaRepository.updateTea(capture(slotTea)) }
        val (_, name1) = slotTea.captured
        assertThat(name1).isEqualTo("name")
        verify { infusionRepository.deleteInfusionsByTeaId(TEA_ID_FILLED) }
        verify(exactly = 2) { infusionRepository.insertInfusion(any()) }
    }

    private fun mockStoredTea() {
        val today = Date.from(Instant.now())
        tea = Tea("TEA", Variety.YELLOW_TEA.code, 3.0, AmountKind.TEA_SPOON.text, 5, 0, today)
        tea!!.id = TEA_ID_FILLED
        every { teaRepository.getTeaById(TEA_ID_FILLED) } returns tea

        val infusions: MutableList<Infusion> = ArrayList()
        val infusion1 = Infusion(TEA_ID_FILLED, 0, "2", "0:30", 5, 5)
        infusions.add(infusion1)
        val infusion2 = Infusion(TEA_ID_FILLED, 1, "4", "1", 50, 100)
        infusions.add(infusion2)
        every { infusionRepository.getInfusionsByTeaId(TEA_ID_FILLED) } returns infusions
    }

    private fun mockStringResource() {
        every { application.resources } returns resources
        every { resources.getStringArray(R.array.new_tea_variety_teas) } returns VARIETY_TEAS
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