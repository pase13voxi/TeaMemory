package coolpharaoh.tee.speicher.tea.timer.views.overview.recycler_view

import android.app.Application
import android.content.res.Resources
import coolpharaoh.tee.speicher.tea.timer.R
import coolpharaoh.tee.speicher.tea.timer.core.date.CurrentDate.setFixedDate
import coolpharaoh.tee.speicher.tea.timer.core.date.DateUtility
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.assertj.core.api.Assertions
import org.assertj.core.groups.Tuple
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.time.Clock
import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.util.Date

@ExtendWith(MockKExtension::class)
internal class RecyclerItemsHeaderStrategyLastUsedTest {
    @MockK
    lateinit var application: Application
    @MockK
    lateinit var resources: Resources
    @MockK
    lateinit var dateUtility: DateUtility

    @BeforeEach
    fun setUp() {
        every { resources.getStringArray(R.array.new_tea_variety_teas) } returns VARIETIES
        every { resources.getStringArray(R.array.overview_sort_last_used_month) } returns MONTH_NAMES
        every { resources.getStringArray(R.array.overview_sort_last_used_month_short) } returns MONTH_NAMES_SHORT
        every { application.resources } returns resources
        every { application.getString(R.string.overview_sort_last_used_this_week) } returns "This week"
        every { application.getString(R.string.overview_sort_last_used_this_month) } returns "This month"
        every { dateUtility.date } returns Date.from(fixedDate)
        setFixedDate(dateUtility)
    }

    private val fixedDate: Instant
        get() {
            val clock = Clock.fixed(Instant.parse(CURRENT_DATE), ZoneId.of("UTC"))
            return Instant.now(clock)
        }

    @Test
    fun generateRecyclerItemsHeader() {
        val teas = createTeas()

        val recyclerItemsHeader: RecyclerItemsHeaderStrategy = RecyclerItemsHeaderStrategyLastUsed(application)
        val recyclerItems = recyclerItemsHeader.generateFrom(teas)

        Assertions.assertThat(recyclerItems)
            .extracting(
                RecyclerItemOverview::teaId,
                RecyclerItemOverview::teaName,
                RecyclerItemOverview::variety,
                RecyclerItemOverview::color,
                RecyclerItemOverview::favorite,
                RecyclerItemOverview::category
            ).contains(
                Tuple.tuple(null, null, null, null, false, "- This week -"),
                Tuple.tuple(teas[0].id, teas[0].name, teas[0].variety, teas[0].color, true, null),
                Tuple.tuple(null, null, null, null, false, "- This month -"),
                Tuple.tuple(teas[1].id, teas[1].name, teas[1].variety, teas[1].color, true, null),
                Tuple.tuple(null, null, null, null, false, "- June -"),
                Tuple.tuple(teas[2].id, teas[2].name, teas[2].variety, teas[2].color, true, null),
                Tuple.tuple(null, null, null, null, false, "- Oct. 2019 -"),
                Tuple.tuple(teas[3].id, teas[3].name, teas[3].variety, teas[3].color, true, null),
                Tuple.tuple(null, null, null, null, false, "- 2019 -"),
                Tuple.tuple(teas[4].id, teas[4].name, teas[4].variety, teas[4].color, true, null)
            )
    }

    private fun createTeas(): ArrayList<Tea> {
        val dates = generateDifferentDates()
        val teas = ArrayList<Tea>()
        for (i in dates.indices) {
            val tea = Tea()
            tea.id = i.toLong()
            tea.name = "TEA" + i + 1
            tea.variety = "VARIETY" + i + 1
            tea.color = i
            tea.inStock = true
            tea.date = dates[i]
            teas.add(tea)
        }
        return teas
    }

    private fun generateDifferentDates(): List<Date> {
        val now = fixedDate

        val dates = ArrayList<Date>()

        val thisWeek = Date.from(now)
        dates.add(thisWeek)
        val thisMonth = Date.from(now.minus(Duration.ofDays(8)))
        dates.add(thisMonth)
        val thisYear = Date.from(now.minus(Duration.ofDays(50)))
        dates.add(thisYear)
        val lastYearBetweenLastTwelveMonth = Date.from(now.minus(Duration.ofDays(300)))
        dates.add(lastYearBetweenLastTwelveMonth)
        val lastYear = Date.from(now.minus(Duration.ofDays(500)))
        dates.add(lastYear)

        return dates
    }

    companion object {
        const val CURRENT_DATE = "2020-08-19T10:15:30Z"
        private val VARIETIES = arrayOf(
            "Black tea", "Green tea", "Yellow tea", "White tea",
            "Oolong tea", "Pu-erh tea", "Herbal tea", "Fruit tea", "Rooibus tea", "Other"
        )
        private val MONTH_NAMES = arrayOf(
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        )
        private val MONTH_NAMES_SHORT = arrayOf(
            "Jan.", "Feb.", "Mar.", "Apr.", "May", "Jun.",
            "Jul.", "Aug.", "Sep.", "Oct.", "Nov.", "Dec."
        )
    }
}