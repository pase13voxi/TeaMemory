package coolpharaoh.tee.speicher.tea.timer.core.counter

import android.app.Application
import coolpharaoh.tee.speicher.tea.timer.database.TeaMemoryDatabase
import coolpharaoh.tee.speicher.tea.timer.database.TeaMemoryDatabase.Companion.setMockedDatabase
import coolpharaoh.tee.speicher.tea.timer.views.export_import.data_transform.pojo.StatisticsPOJO
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class CounterRepositoryTest {
    @MockK
    lateinit var teaMemoryDatabase: TeaMemoryDatabase
    @RelaxedMockK
    lateinit var counterDao: CounterDao

    private var counterRepository: CounterRepository? = null

    @BeforeEach
    fun setUp() {
        setMockedDatabase(teaMemoryDatabase)
        every { teaMemoryDatabase.counterDao } returns counterDao

        counterRepository = CounterRepository(Application())
    }

    @Test
    fun insertCounter() {
        val counter = Counter()

        counterRepository!!.insertCounter(counter)

        verify { counterDao.insert(counter) }
    }

    @Test
    fun updateCounter() {
        val counter = Counter()

        counterRepository!!.updateCounter(counter)

        verify { counterDao.update(counter) }
    }

    @Test
    fun counters() {
        every { counterDao.getCounters() } returns listOf(Counter(), Counter())

        val counters = counterRepository!!.counters

        verify { counterDao.getCounters() }
        assertThat(counters).hasSize(2)
    }

    @Test
    fun counterByTeaId() {
        val teaId = 2
        val counter = Counter()
        every { counterDao.getCounterByTeaId(teaId.toLong()) } returns counter

        val counterByTeaId = counterRepository!!.getCounterByTeaId(teaId.toLong())

        assertThat(counterByTeaId).isEqualTo(counter)
    }

    @Test
    fun teaCounterOverall() {
        every { counterDao.getTeaCounterOverall() } returns listOf(StatisticsPOJO(), StatisticsPOJO())

        val counters = counterRepository!!.teaCounterOverall

        verify { counterDao.getTeaCounterOverall() }
        assertThat(counters).hasSize(2)
    }

    @Test
    fun teaCounterYear() {
        every { counterDao.getTeaCounterYear() } returns listOf(StatisticsPOJO(), StatisticsPOJO())

        val counters = counterRepository!!.teaCounterYear

        verify { counterDao.getTeaCounterYear() }
        assertThat(counters).hasSize(2)
    }

    @Test
    fun teaCounterMonth() {
        every { counterDao.getTeaCounterMonth() } returns listOf(StatisticsPOJO(), StatisticsPOJO())

        val counters = counterRepository!!.teaCounterMonth

        verify { counterDao.getTeaCounterMonth() }
        assertThat(counters).hasSize(2)
    }

    @Test
    fun teaCounterWeek() {
        every { counterDao.getTeaCounterWeek() } returns listOf(StatisticsPOJO(), StatisticsPOJO())

        val counters = counterRepository!!.teaCounterWeek

        verify { counterDao.getTeaCounterWeek() }
        assertThat(counters).hasSize(2)
    }
}