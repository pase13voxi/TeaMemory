package coolpharaoh.tee.speicher.tea.timer.views.statistics

import androidx.test.core.app.ActivityScenario
import com.github.mikephil.charting.charts.HorizontalBarChart
import coolpharaoh.tee.speicher.tea.timer.R
import coolpharaoh.tee.speicher.tea.timer.core.counter.CounterDao
import coolpharaoh.tee.speicher.tea.timer.database.TeaMemoryDatabase
import coolpharaoh.tee.speicher.tea.timer.database.TeaMemoryDatabase.Companion.setMockedDatabase
import coolpharaoh.tee.speicher.tea.timer.views.export_import.data_transform.pojo.StatisticsPOJO
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import org.assertj.core.api.Assertions.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.fakes.RoboMenuItem
import org.robolectric.shadows.ShadowAlertDialog

@RunWith(RobolectricTestRunner::class)
class StatisticsTest {
    @get:Rule
    val mockkRule = MockKRule(this)
    @MockK
    lateinit var teaMemoryDatabase: TeaMemoryDatabase
    @RelaxedMockK
    lateinit var counterDao: CounterDao

    @Before
    fun setUp() {
        mockDB()
    }

    private fun mockDB() {
        setMockedDatabase(teaMemoryDatabase)
        every { teaMemoryDatabase.counterDao } returns counterDao
    }

    @Test
    fun launchActivityAndExpectListWeek() {
        val countSize = 2
        val statisticsPOJOs = getStatisticsPOJOs("LAUNCH", countSize)
        every { counterDao.getTeaCounterWeek() } returns statisticsPOJOs

        val statisticsActivityScenario = ActivityScenario.launch(Statistics::class.java)

        statisticsActivityScenario.onActivity { statistics: Statistics -> checkExpectedItems(statistics, countSize) }
    }

    @Test
    fun setPeriodWeekAndExpectListWeek() {
        val countSize = 5
        val statisticsPOJOs = getStatisticsPOJOs("WEEK", countSize)
        every { counterDao.getTeaCounterWeek() } returns statisticsPOJOs

        val statisticsActivityScenario = ActivityScenario.launch(Statistics::class.java)

        statisticsActivityScenario.onActivity { statistics: Statistics ->
            statistics.onOptionsItemSelected(RoboMenuItem(R.id.action_statistics_period))

            val shadowAlertDialog = Shadows.shadowOf(ShadowAlertDialog.getLatestAlertDialog())
            shadowAlertDialog.clickOnItem(WEEK_ITEM)

            checkExpectedItems(statistics, countSize)
        }
    }

    @Test
    fun setPeriodMonthAndExpectListMonth() {
        val countSize = 4
        val statisticsPOJOs = getStatisticsPOJOs("MONTH", countSize)
        every { counterDao.getTeaCounterMonth() } returns statisticsPOJOs

        val statisticsActivityScenario = ActivityScenario.launch(Statistics::class.java)

        statisticsActivityScenario.onActivity { statistics: Statistics ->
            statistics.onOptionsItemSelected(RoboMenuItem(R.id.action_statistics_period))

            val shadowAlertDialog = Shadows.shadowOf(ShadowAlertDialog.getLatestAlertDialog())
            shadowAlertDialog.clickOnItem(MONTH_ITEM)

            checkExpectedItems(statistics, countSize)
        }
    }

    @Test
    fun setPeriodDayAndExpectListYear() {
        val countSize = 6
        val statisticsPOJOs = getStatisticsPOJOs("YEAR", countSize)
        every { counterDao.getTeaCounterYear() } returns statisticsPOJOs

        val statisticsActivityScenario = ActivityScenario.launch(Statistics::class.java)

        statisticsActivityScenario.onActivity { statistics: Statistics ->
            statistics.onOptionsItemSelected(RoboMenuItem(R.id.action_statistics_period))

            val shadowAlertDialog = Shadows.shadowOf(ShadowAlertDialog.getLatestAlertDialog())
            shadowAlertDialog.clickOnItem(YEAR_ITEM)

            checkExpectedItems(statistics, countSize)
        }
    }

    @Test
    fun setPeriodOverallAndExpectListOverall() {
        val countSize = 3
        val statisticsPOJOs = getStatisticsPOJOs("OVERALL", countSize)
        every { counterDao.getTeaCounterOverall() } returns statisticsPOJOs

        val statisticsActivityScenario = ActivityScenario.launch(Statistics::class.java)

        statisticsActivityScenario.onActivity { statistics: Statistics ->
            statistics.onOptionsItemSelected(RoboMenuItem(R.id.action_statistics_period))

            val shadowAlertDialog = Shadows.shadowOf(ShadowAlertDialog.getLatestAlertDialog())
            shadowAlertDialog.clickOnItem(OVERALL_ITEM)

            checkExpectedItems(statistics, countSize)
        }
    }

    private fun checkExpectedItems(statistics: Statistics, itemCount: Int) {
        val horizontalBar = statistics.findViewById<HorizontalBarChart>(R.id.horizontal_graph_statistics)

        val iBarDataSet = horizontalBar.data.dataSets[0]
        assertThat(iBarDataSet.entryCount).isEqualTo(itemCount)
        for (i in 0 until itemCount) {
            assertThat(iBarDataSet.getEntryForIndex(i).y).isEqualTo((i + 1).toFloat())
        }
    }

    private fun getStatisticsPOJOs(name: String, itemCount: Int): List<StatisticsPOJO> {
        val statisticsPOJOS: MutableList<StatisticsPOJO> = ArrayList()

        for (count in 1..itemCount) {
            val statistic = StatisticsPOJO()
            statistic.teaName = name + count
            statistic.teaColor = count
            statistic.counter = count.toLong()
            statisticsPOJOS.add(statistic)
        }

        return statisticsPOJOS
    }

    companion object {
        private const val WEEK_ITEM = 0
        private const val MONTH_ITEM = 1
        private const val YEAR_ITEM = 2
        private const val OVERALL_ITEM = 3
    }
}