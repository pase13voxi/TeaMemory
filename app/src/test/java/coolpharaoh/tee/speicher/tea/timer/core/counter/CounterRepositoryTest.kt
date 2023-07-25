package coolpharaoh.tee.speicher.tea.timer.core.counter

import android.app.Application
import coolpharaoh.tee.speicher.tea.timer.database.TeaMemoryDatabase
import coolpharaoh.tee.speicher.tea.timer.database.TeaMemoryDatabase.Companion.setMockedDatabase
import coolpharaoh.tee.speicher.tea.timer.views.export_import.data_transform.pojo.StatisticsPOJO
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class CounterRepositoryTest {
    @Mock
    var teaMemoryDatabase: TeaMemoryDatabase? = null
    @Mock
    var counterDao: CounterDao? = null

    private var counterRepository: CounterRepository? = null

    @BeforeEach
    fun setUp() {
        setMockedDatabase(teaMemoryDatabase)
        `when`(teaMemoryDatabase!!.counterDao).thenReturn(counterDao)

        counterRepository = CounterRepository(Application())
    }

    @Test
    fun insertCounter() {
        val counter = Counter()

        counterRepository!!.insertCounter(counter)

        verify(counterDao)?.insert(counter)
    }

    @Test
    fun updateCounter() {
        val counter = Counter()

        counterRepository!!.updateCounter(counter)

        verify(counterDao)?.update(counter)
    }

    @Test
    fun counters() {
        `when`(counterDao!!.getCounters()).thenReturn(listOf(Counter(), Counter()))

        val counters = counterRepository!!.counters

        verify(counterDao)?.getCounters()
        assertThat(counters).hasSize(2)
    }

    @Test
    fun counterByTeaId() {
        val teaId = 2
        val counter = Counter()
        `when`(counterDao!!.getCounterByTeaId(teaId.toLong())).thenReturn(counter)

        val counterByTeaId = counterRepository!!.getCounterByTeaId(teaId.toLong())

        assertThat(counterByTeaId).isEqualTo(counter)
    }

    @Test
    fun teaCounterOverall() {
        `when`(counterDao!!.getTeaCounterOverall())
            .thenReturn(listOf(StatisticsPOJO(), StatisticsPOJO()))

        val counters = counterRepository!!.teaCounterOverall

        verify(counterDao)?.getTeaCounterOverall()
        assertThat(counters).hasSize(2)
    }

    @Test
    fun teaCounterYear() {
        `when`(counterDao!!.getTeaCounterYear())
            .thenReturn(listOf(StatisticsPOJO(), StatisticsPOJO()))

        val counters = counterRepository!!.teaCounterYear

        verify(counterDao)?.getTeaCounterYear()
        assertThat(counters).hasSize(2)
    }

    @Test
    fun teaCounterMonth() {
        `when`(counterDao!!.getTeaCounterMonth())
            .thenReturn(listOf(StatisticsPOJO(), StatisticsPOJO()))

        val counters = counterRepository!!.teaCounterMonth

        verify(counterDao)?.getTeaCounterMonth()
        assertThat(counters).hasSize(2)
    }

    @Test
    fun teaCounterWeek() {
        `when`(counterDao!!.getTeaCounterWeek())
            .thenReturn(listOf(StatisticsPOJO(), StatisticsPOJO()))

        val counters = counterRepository!!.teaCounterWeek

        verify(counterDao)?.getTeaCounterWeek()
        assertThat(counters).hasSize(2)
    }
}