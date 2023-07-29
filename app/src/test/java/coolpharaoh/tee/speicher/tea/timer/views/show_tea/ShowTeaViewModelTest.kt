package coolpharaoh.tee.speicher.tea.timer.views.show_tea

import android.app.Application
import android.content.res.Resources
import coolpharaoh.tee.speicher.tea.timer.R
import coolpharaoh.tee.speicher.tea.timer.core.counter.Counter
import coolpharaoh.tee.speicher.tea.timer.core.counter.CounterRepository
import coolpharaoh.tee.speicher.tea.timer.core.date.CurrentDate.setFixedDate
import coolpharaoh.tee.speicher.tea.timer.core.date.DateUtility
import coolpharaoh.tee.speicher.tea.timer.core.infusion.Infusion
import coolpharaoh.tee.speicher.tea.timer.core.infusion.InfusionRepository
import coolpharaoh.tee.speicher.tea.timer.core.settings.SharedSettings
import coolpharaoh.tee.speicher.tea.timer.core.settings.TemperatureUnit
import coolpharaoh.tee.speicher.tea.timer.core.tea.AmountKind
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea
import coolpharaoh.tee.speicher.tea.timer.core.tea.TeaRepository
import coolpharaoh.tee.speicher.tea.timer.core.tea.Variety
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.argumentCaptor
import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import java.util.Date

@ExtendWith(MockitoExtension::class)
internal class ShowTeaViewModelTest {
    private var showTeaViewModel: ShowTeaViewModel? = null

    @Mock
    lateinit var application: Application

    @Mock
    lateinit var resources: Resources

    @Mock
    lateinit var teaRepository: TeaRepository

    @Mock
    lateinit var infusionRepository: InfusionRepository

    @Mock
    lateinit var counterRepository: CounterRepository

    @Mock
    lateinit var sharedSettings: SharedSettings

    @Mock
    lateinit var dateUtility: DateUtility

    @BeforeEach
    internal fun setUp() {
        showTeaViewModel = ShowTeaViewModel(TEA_ID, application, teaRepository, infusionRepository,
            counterRepository, sharedSettings)
    }

    @Test
    fun teaExist() {
        val tea = Tea()
        `when`(teaRepository.getTeaById(TEA_ID)).thenReturn(tea)

        assertThat(showTeaViewModel!!.teaExists()).isTrue
    }

    @Test
    fun teaDoesNotExist() {
        assertThat(showTeaViewModel!!.teaExists()).isFalse
    }

    @Test
    fun getTeaId() {
        val teaIdBefore = 1L

        val tea = Tea()
        tea.id = teaIdBefore
        `when`(teaRepository.getTeaById(TEA_ID)).thenReturn(tea)

        val teaIdAfter = showTeaViewModel!!.getTeaId()

        assertThat(teaIdAfter).isEqualTo(teaIdBefore)
    }

    @Test
    fun getName() {
        val teaNameBefore = "TEA"

        val tea = Tea()
        tea.name = teaNameBefore
        `when`(teaRepository.getTeaById(TEA_ID)).thenReturn(tea)

        val teaNameAfter = showTeaViewModel!!.name

        assertThat(teaNameAfter).isEqualTo(teaNameBefore)
    }

    @Test
    fun getVariety() {
        val varietyTeas = arrayOf(
            "Black tea", "Green tea", "Yellow tea", "White tea", "Oolong tea",
            "Pu-erh tea", "Herbal tea", "Fruit tea", "Rooibus tea", "Other"
        )

        val tea = Tea()
        tea.variety = Variety.OOLONG_TEA.code
        `when`(teaRepository.getTeaById(TEA_ID)).thenReturn(tea)

        `when`(application.resources).thenReturn(resources)
        `when`(resources.getStringArray(R.array.new_tea_variety_teas)).thenReturn(varietyTeas)

        val varietyAfter = showTeaViewModel!!.variety

        assertThat(varietyAfter).isEqualTo(varietyTeas[Variety.OOLONG_TEA.choice])
    }

    @Test
    fun getUnknownVariety() {
        val varietyTeas = arrayOf(
            "Black tea", "Green tea", "Yellow tea", "White tea", "Oolong tea",
            "Pu-erh tea", "Herbal tea", "Fruit tea", "Rooibus tea", "Other"
        )

        val varietyBefore = "VARIETY"

        val tea = Tea()
        tea.variety = varietyBefore
        `when`(teaRepository.getTeaById(TEA_ID)).thenReturn(tea)

        `when`(application.resources).thenReturn(resources)
        `when`(resources.getStringArray(R.array.new_tea_variety_teas)).thenReturn(varietyTeas)

        val varietyAfter = showTeaViewModel!!.variety

        assertThat(varietyAfter).isEqualTo(varietyBefore)
    }

    @Test
    fun getEmptyVariety() {
        val varietyBefore = ""

        val tea = Tea()
        tea.variety = varietyBefore
        `when`(teaRepository.getTeaById(TEA_ID)).thenReturn(tea)

        val varietyAfter = showTeaViewModel!!.variety

        assertThat(varietyAfter).isEqualTo("-")
    }

    @Test
    fun getAmount() {
        val amountBefore = 1.0

        val tea = Tea()
        tea.amount = amountBefore
        `when`(teaRepository.getTeaById(TEA_ID)).thenReturn(tea)

        val amountAfter = showTeaViewModel!!.amount

        assertThat(amountAfter).isEqualTo(amountBefore)
    }

    @Test
    fun getAmountKind() {
        val tea = Tea()
        tea.amountKind = AmountKind.TEA_SPOON.text
        `when`(teaRepository.getTeaById(TEA_ID)).thenReturn(tea)

        val amountKind = showTeaViewModel!!.amountKind

        assertThat(amountKind).isEqualTo(AmountKind.TEA_SPOON)
    }

    @Test
    fun getColor() {
        val colorBefore = 1

        val tea = Tea()
        tea.color = colorBefore
        `when`(teaRepository.getTeaById(TEA_ID)).thenReturn(tea)

        val colorAfter = showTeaViewModel!!.color

        assertThat(colorAfter).isEqualTo(colorBefore)
    }

    @Test
    fun setCurrentDate() {
        val fixedDate = mockFixedDate()
        val teaBefore = Tea()
        `when`(teaRepository.getTeaById(TEA_ID)).thenReturn(teaBefore)

        showTeaViewModel!!.setCurrentDate()

        argumentCaptor<Tea>().apply {
            verify(teaRepository).updateTea(capture())
            val (_, _, _, _, _, _, _, _, _, date) = lastValue

            assertThat(date).isEqualTo(fixedDate)
        }
    }

    @Test
    fun getNextInfusion() {
        val nextInfusionBefore = 0

        val tea = Tea()
        tea.nextInfusion = nextInfusionBefore
        `when`(teaRepository.getTeaById(TEA_ID)).thenReturn(tea)

        val nextInfusionAfter = showTeaViewModel!!.nextInfusion

        assertThat(nextInfusionAfter).isEqualTo(nextInfusionBefore)
    }

    @Test
    fun updateNextInfusion() {
        val tea = Tea()
        tea.nextInfusion = 0
        `when`(teaRepository.getTeaById(TEA_ID)).thenReturn(tea)

        `when`(infusionRepository.getInfusionsByTeaId(TEA_ID)).thenReturn(listOf(Infusion(), Infusion()))

        showTeaViewModel!!.updateNextInfusion()

        argumentCaptor<Tea>().apply {
            verify(teaRepository).updateTea(capture())
            val (_, _, _, _, _, _, _, _, nextInfusion1) = lastValue

            assertThat(nextInfusion1).isEqualTo(1)
        }
    }

    @Test
    fun resetNextInfusion() {
        val tea = Tea()
        tea.nextInfusion = 2
        `when`(teaRepository.getTeaById(TEA_ID)).thenReturn(tea)

        showTeaViewModel!!.resetNextInfusion()

        argumentCaptor<Tea>().apply {
            verify(teaRepository).updateTea(capture())
            val (_, _, _, _, _, _, _, _, nextInfusion1) = lastValue

            assertThat(nextInfusion1).isZero
        }
    }

    @Test
    fun updateLastInfusionBiggerOrEqual() {
        val tea = Tea()
        tea.nextInfusion = 0
        `when`(teaRepository.getTeaById(TEA_ID)).thenReturn(tea)

        `when`(infusionRepository.getInfusionsByTeaId(TEA_ID)).thenReturn(listOf(Infusion()))

        showTeaViewModel!!.updateNextInfusion()

        argumentCaptor<Tea>().apply {
            verify(teaRepository).updateTea(capture())
            val (_, _, _, _, _, _, _, _, nextInfusion1) = lastValue

            assertThat(nextInfusion1).isZero
        }
    }

    @Test
    fun navigateBetweenInfusions() {
        `when`(sharedSettings.temperatureUnit).thenReturn(TemperatureUnit.CELSIUS)

        val infusions: MutableList<Infusion> = ArrayList()
        val infusion1 = Infusion(1L, 1, "1", "2", 1, 32)
        infusions.add(infusion1)

        val infusion2 = Infusion(1L, 2, "2:30", "5:30", 2, 33)
        infusions.add(infusion2)

        `when`(infusionRepository.getInfusionsByTeaId(TEA_ID)).thenReturn(infusions)

        assertThat(showTeaViewModel!!.getInfusionSize()).isEqualTo(infusions.size)
        assertThat(showTeaViewModel!!.infusionIndex).isZero

        val time1 = showTeaViewModel!!.time
        val cooldownTime1 = showTeaViewModel!!.coolDownTime
        val temperature1 = showTeaViewModel!!.temperature

        assertThat(time1.time).isEqualTo(infusions[0].time)
        assertThat(time1.minutes).isEqualTo(1)
        assertThat(time1.seconds).isZero
        assertThat(cooldownTime1.time).isEqualTo(infusions[0].coolDownTime)
        assertThat(cooldownTime1.minutes).isEqualTo(2)
        assertThat(cooldownTime1.seconds).isZero
        assertThat(temperature1).isEqualTo(infusions[0].temperatureCelsius)

        showTeaViewModel!!.incrementInfusionIndex()
        assertThat(showTeaViewModel!!.infusionIndex).isEqualTo(1)

        val time2 = showTeaViewModel!!.time
        val coolDownTime2 = showTeaViewModel!!.coolDownTime
        val temperature2 = showTeaViewModel!!.temperature

        assertThat(time2.time).isEqualTo(infusions[1].time)
        assertThat(time2.minutes).isEqualTo(2)
        assertThat(time2.seconds).isEqualTo(30)
        assertThat(coolDownTime2.time).isEqualTo(infusions[1].coolDownTime)
        assertThat(coolDownTime2.minutes).isEqualTo(5)
        assertThat(coolDownTime2.seconds).isEqualTo(30)
        assertThat(temperature2).isEqualTo(infusions[1].temperatureCelsius)

        `when`(sharedSettings.temperatureUnit).thenReturn(TemperatureUnit.FAHRENHEIT)
        showTeaViewModel!!.infusionIndex = 0
        assertThat(showTeaViewModel!!.infusionIndex).isZero

        val temperature3 = showTeaViewModel!!.temperature
        assertThat(temperature3).isEqualTo(infusions[0].temperatureFahrenheit)
    }

    @Test
    fun getEmptyTime() {
        val infusions: MutableList<Infusion> = ArrayList()
        val infusion1 = Infusion(1L, 1, null, null, 1, 1)
        infusions.add(infusion1)

        `when`(infusionRepository.getInfusionsByTeaId(TEA_ID)).thenReturn(infusions)

        val timeAfter = showTeaViewModel!!.time

        assertThat(timeAfter.time).isNull()
        assertThat(timeAfter.minutes).isZero
        assertThat(timeAfter.seconds).isZero
    }

    @Test
    fun countCounter() {
        val currentDate = mockFixedDate()
        val counterBefore = Counter(1L, 1, 1, 1, 1, currentDate, currentDate, currentDate)
        `when`(counterRepository.getCounterByTeaId(TEA_ID)).thenReturn(counterBefore)

        showTeaViewModel!!.countCounter()

        argumentCaptor<Counter>().apply {
            verify(counterRepository).updateCounter(capture())

            assertThat(lastValue)
                .extracting(Counter::week, Counter::month, Counter::year, Counter::overall)
                .containsExactly(2, 2, 2, 2L)
        }
    }

    @Test
    fun countCounterAndCounterIsNull() {
        showTeaViewModel!!.countCounter()

        argumentCaptor<Counter>().apply {
            verify(counterRepository).updateCounter(capture())

            assertThat(lastValue)
                .extracting(Counter::week, Counter::month, Counter::year, Counter::overall)
                .containsExactly(1, 1, 1, 1L)
        }
    }

    @Test
    fun getOverallCounter() {
        val currentDate = Date.from(Instant.now())
        val counterBefore = Counter(1L, 1, 1, 1, 1, currentDate, currentDate, currentDate)
        `when`(counterRepository.getCounterByTeaId(TEA_ID)).thenReturn(counterBefore)

        val overallCounter = showTeaViewModel!!.getOverallCounter()

        assertThat(overallCounter).isEqualTo(1L)
    }

    @Test
    fun isAnimation() {
        val animation = true

        `when`(sharedSettings.isAnimation).thenReturn(animation)

        assertThat(showTeaViewModel!!.isAnimation()).isEqualTo(animation)
    }

    @Test
    fun isShowTeaAlert() {
        val showTeaAlert = true

        `when`(sharedSettings.isShowTeaAlert).thenReturn(showTeaAlert)

        assertThat(showTeaViewModel!!.isShowTeaAlert()).isEqualTo(showTeaAlert)
    }

    @Test
    fun setShowTeaAlert() {
        val showTeaAlert = true

        showTeaViewModel!!.setShowTeaAlert(showTeaAlert)

        verify(sharedSettings).isShowTeaAlert = showTeaAlert
    }

    private fun mockFixedDate(): Date {
        val clock = Clock.fixed(Instant.parse(CURRENT_DATE), ZoneId.of("UTC"))
        val now = Instant.now(clock)
        val fixedDate = Date.from(now)

        setFixedDate(dateUtility)
        `when`(dateUtility.date).thenReturn(fixedDate)
        return fixedDate
    }

    companion object {
        const val CURRENT_DATE = "2020-08-19T10:15:30Z"
        private const val TEA_ID = 1L
    }
}