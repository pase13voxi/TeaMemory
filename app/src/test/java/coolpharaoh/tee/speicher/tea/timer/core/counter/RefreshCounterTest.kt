package coolpharaoh.tee.speicher.tea.timer.core.counter

import coolpharaoh.tee.speicher.tea.timer.core.counter.RefreshCounter.refreshCounter
import coolpharaoh.tee.speicher.tea.timer.core.counter.RefreshCounter.refreshCounters
import coolpharaoh.tee.speicher.tea.timer.core.date.CurrentDate.setFixedDate
import coolpharaoh.tee.speicher.tea.timer.core.date.DateUtility
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.time.Clock
import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.util.Date

@ExtendWith(MockKExtension::class)
internal class RefreshCounterTest {

    var weekBefore: Date? = null
    var monthBefore: Date? = null
    var yearBefore: Date? = null
    var today: Date? = null

    @MockK
    lateinit var fixedDate: DateUtility

    @BeforeEach
    fun setUp() {
        val now = getFixedDate()

        today = Date.from(now)
        weekBefore = Date.from(now.minus(Duration.ofDays(7)))
        monthBefore = Date.from(now.minus(Duration.ofDays(31)))
        yearBefore = Date.from(now.minus(Duration.ofDays(370)))

        every { fixedDate.date } returns today!!
        setFixedDate(fixedDate)
    }

    private fun getFixedDate(): Instant {
        val clock = Clock.fixed(Instant.parse(CURRENT_DATE), ZoneId.of("UTC"))
        return Instant.now(clock)
    }

    @Test
    fun resetIncompleteCounter() {
        val counter = Counter(0, WEEK, MONTH, YEAR, OVERALL, null, today, today)

        refreshCounter(counter)

        assertThat(counter)
            .extracting(
                Counter::week,
                Counter::month,
                Counter::year,
                Counter::overall,
                Counter::weekDate,
                Counter::monthDate,
                Counter::yearDate
            )
            .containsExactly(0, 0, 0, 0L, today, today, today)
    }

    @Test
    fun refreshCounterNothingChanged() {
        val counter = Counter(0, WEEK, MONTH, YEAR, OVERALL, today, today, today)

        refreshCounter(counter)

        assertThat(counter)
            .extracting(
                Counter::week,
                Counter::month,
                Counter::year,
                Counter::overall
            )
            .containsExactly(WEEK, MONTH, YEAR, OVERALL)
    }

    @Test
    fun refreshCounterDayWeekRefreshed() {
        val counter = Counter(0, WEEK, MONTH, YEAR, OVERALL, weekBefore, weekBefore, weekBefore)

        refreshCounter(counter)

        assertThat(counter)
            .extracting(
                Counter::week,
                Counter::month,
                Counter::year,
                Counter::overall
            )
            .containsExactly(0, MONTH, YEAR, OVERALL)
    }

    @Test
    fun refreshCounterDayWeekMonthRefreshed() {
        val counter = Counter(0, WEEK, MONTH, YEAR, OVERALL, monthBefore, monthBefore, monthBefore)

        refreshCounter(counter)

        assertThat(counter)
            .extracting(
                Counter::week,
                Counter::month,
                Counter::year,
                Counter::overall
            )
            .containsExactly(0, 0, YEAR, OVERALL)
    }

    @Test
    fun refreshCounterYearRefreshed() {
        val counter = Counter(0, WEEK, MONTH, YEAR, OVERALL, yearBefore, yearBefore, yearBefore)

        refreshCounter(counter)

        assertThat(counter)
            .extracting(
                Counter::week,
                Counter::month,
                Counter::year,
                Counter::overall
            )
            .containsExactly(0, 0, 0, OVERALL)
    }

    @Test
    fun refreshCounters() {
        val counter1 = Counter(0, WEEK, MONTH, YEAR, OVERALL, today, today, today)
        val counter2 = Counter(0, WEEK, MONTH, YEAR, OVERALL, yearBefore, yearBefore, yearBefore)

        val counters = listOf(counter1, counter2)

        refreshCounters(counters)

        assertThat(counter1)
            .extracting(
                Counter::week,
                Counter::month,
                Counter::year,
                Counter::overall
            )
            .containsExactly(WEEK, MONTH, YEAR, OVERALL)

        assertThat(counter2)
            .extracting(
                Counter::week,
                Counter::month,
                Counter::year,
                Counter::overall
            )
            .containsExactly(0, 0, 0, OVERALL)
    }

    companion object {
        const val CURRENT_DATE = "2020-08-19T10:15:30Z"
        const val WEEK = 1
        const val MONTH = 2
        const val YEAR = 3
        const val OVERALL = 4L
    }
}