package coolpharaoh.tee.speicher.tea.timer.views.statistics

import coolpharaoh.tee.speicher.tea.timer.core.counter.Counter
import coolpharaoh.tee.speicher.tea.timer.core.counter.CounterRepository
import coolpharaoh.tee.speicher.tea.timer.core.date.CurrentDate.setFixedDate
import coolpharaoh.tee.speicher.tea.timer.core.date.DateUtility
import coolpharaoh.tee.speicher.tea.timer.views.export_import.data_transform.pojo.StatisticsPOJO
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.argumentCaptor
import java.time.Clock
import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.util.Date

@ExtendWith(MockitoExtension::class)
internal class StatisticsViewModelTest {

    private var statisticsViewModel: StatisticsViewModel? = null

    @Mock
    var counterRepository: CounterRepository? = null

    @Mock
    var fixedDate: DateUtility? = null

    @BeforeEach
    fun setUp() {
        statisticsViewModel = StatisticsViewModel(counterRepository!!)
    }

    @Test
    fun getStatisticsOverall() {
        val counterOverallBefore: MutableList<StatisticsPOJO> = ArrayList()

        val statisticsPOJO1 = StatisticsPOJO()
        statisticsPOJO1.teaName = "TEA1"
        statisticsPOJO1.teaColor = 15
        statisticsPOJO1.counter = 15
        counterOverallBefore.add(statisticsPOJO1)

        val statisticsPOJO2 = StatisticsPOJO()
        statisticsPOJO2.teaName = "TEA2"
        statisticsPOJO2.teaColor = 18
        statisticsPOJO2.counter = 18
        counterOverallBefore.add(statisticsPOJO2)

        `when`(counterRepository!!.teaCounterOverall).thenReturn(counterOverallBefore)

        val counterOverallAfter = statisticsViewModel!!.statisticsOverall

        assertThat(counterOverallAfter).isEqualTo(counterOverallBefore)
    }

    @Test
    fun getStatisticsYear() {
        val counterYearBefore: MutableList<StatisticsPOJO> = ArrayList()

        val statisticsPOJO1 = StatisticsPOJO()
        statisticsPOJO1.teaName = "TEA1"
        statisticsPOJO1.teaColor = 15
        statisticsPOJO1.counter = 15
        counterYearBefore.add(statisticsPOJO1)

        val statisticsPOJO2 = StatisticsPOJO()
        statisticsPOJO2.teaName = "TEA2"
        statisticsPOJO2.teaColor = 18
        statisticsPOJO2.counter = 18
        counterYearBefore.add(statisticsPOJO2)

        `when`(counterRepository!!.teaCounterYear).thenReturn(counterYearBefore)

        val counterDayAfter = statisticsViewModel!!.statisticsYear

        assertThat(counterDayAfter).isEqualTo(counterYearBefore)
    }

    @Test
    fun getStatisticsMonth() {
        val counterMonthBefore: MutableList<StatisticsPOJO> = ArrayList()

        val statisticsPOJO1 = StatisticsPOJO()
        statisticsPOJO1.teaName = "TEA1"
        statisticsPOJO1.teaColor = 15
        statisticsPOJO1.counter = 15
        counterMonthBefore.add(statisticsPOJO1)

        val statisticsPOJO2 = StatisticsPOJO()
        statisticsPOJO2.teaName = "TEA2"
        statisticsPOJO2.teaColor = 18
        statisticsPOJO2.counter = 18
        counterMonthBefore.add(statisticsPOJO2)

        `when`(counterRepository!!.teaCounterMonth).thenReturn(counterMonthBefore)

        val counterMonthAfter = statisticsViewModel!!.statisticsMonth

        assertThat(counterMonthAfter).isEqualTo(counterMonthBefore)
    }

    @Test
    fun getStatisticsWeek() {
        val counterWeekBefore: MutableList<StatisticsPOJO> = ArrayList()

        val statisticsPOJO1 = StatisticsPOJO()
        statisticsPOJO1.teaName = "TEA1"
        statisticsPOJO1.teaColor = 15
        statisticsPOJO1.counter = 15
        counterWeekBefore.add(statisticsPOJO1)

        val statisticsPOJO2 = StatisticsPOJO()
        statisticsPOJO2.teaName = "TEA2"
        statisticsPOJO2.teaColor = 18
        statisticsPOJO2.counter = 18
        counterWeekBefore.add(statisticsPOJO2)

        `when`(counterRepository!!.teaCounterWeek).thenReturn(counterWeekBefore)

        val counterWeekAfter = statisticsViewModel!!.statisticsWeek

        assertThat(counterWeekAfter).isEqualTo(counterWeekBefore)
    }

    @Test
    fun refreshAllCounter() {
        val now = getFixedDate()
        val today = Date.from(now)
        val weekBefore = Date.from(now.minus(Duration.ofDays(7)))
        val monthBefore = Date.from(now.minus(Duration.ofDays(31)))
        val yearBefore = Date.from(now.minus(Duration.ofDays(370)))

        `when`(fixedDate!!.date).thenReturn(today)
        setFixedDate(fixedDate!!)

        val countersBefore: MutableList<Counter> = ArrayList()

        val noRefresh = Counter(1L, 4, 7, 9, 15L, today, today, today)
        countersBefore.add(noRefresh)
        val refreshWeek = Counter(1L, 4, 7, 9, 15L, weekBefore, today, today)
        countersBefore.add(refreshWeek)
        val refreshMonth = Counter(1L, 4, 7, 9, 15L, today, monthBefore, today)
        countersBefore.add(refreshMonth)
        val refreshDay = Counter(1L, 4, 7, 9, 15L, today, today, yearBefore)
        countersBefore.add(refreshDay)
        val refreshAll = Counter(1L, 4, 7, 9, 15L, yearBefore, yearBefore, yearBefore)
        countersBefore.add(refreshAll)

        `when`(counterRepository!!.counters).thenReturn(countersBefore)

        statisticsViewModel!!.refreshAllCounter()

        argumentCaptor<Counter>().apply {
            verify(counterRepository!!, times(5)).updateCounter(capture())

            val counterAfter = allValues

            assertThat(counterAfter[0]).isEqualTo(noRefresh)

            assertThat(counterAfter[1].week).isZero
            assertThat(counterAfter[1].month).isEqualTo(7)
            assertThat(counterAfter[1].year).isEqualTo(9)
            assertThat(counterAfter[1].overall).isEqualTo(15L)

            assertThat(counterAfter[2].week).isEqualTo(4)
            assertThat(counterAfter[2].month).isZero
            assertThat(counterAfter[2].year).isEqualTo(9)
            assertThat(counterAfter[2].overall).isEqualTo(15L)

            assertThat(counterAfter[3].week).isEqualTo(4)
            assertThat(counterAfter[3].month).isEqualTo(7)
            assertThat(counterAfter[3].year).isZero
            assertThat(counterAfter[3].overall).isEqualTo(15L)

            assertThat(counterAfter[4].week).isZero
            assertThat(counterAfter[4].month).isZero
            assertThat(counterAfter[4].year).isZero
            assertThat(counterAfter[4].overall).isEqualTo(15L)
        }
    }

    private fun getFixedDate(): Instant {
        val clock = Clock.fixed(Instant.parse(CURRENT_DATE), ZoneId.of("UTC"))
        return Instant.now(clock)
    }

    companion object {
        const val CURRENT_DATE = "2020-08-19T10:15:30Z"
    }
}