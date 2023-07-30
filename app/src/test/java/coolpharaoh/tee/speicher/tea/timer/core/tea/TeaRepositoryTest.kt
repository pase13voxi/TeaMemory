package coolpharaoh.tee.speicher.tea.timer.core.tea

import android.app.Application
import coolpharaoh.tee.speicher.tea.timer.database.TeaMemoryDatabase
import coolpharaoh.tee.speicher.tea.timer.database.TeaMemoryDatabase.Companion.setMockedDatabase
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
internal class TeaRepositoryTest {
    @MockK
    lateinit var teaMemoryDatabase: TeaMemoryDatabase
    @RelaxedMockK
    lateinit var teaDao: TeaDao

    private var teaRepository: TeaRepository? = null

    @BeforeEach
    fun setUp() {
        setMockedDatabase(teaMemoryDatabase)
        every { teaMemoryDatabase.teaDao } returns teaDao

        teaRepository = TeaRepository(Application())
    }

    @Test
    fun insertTea() {
        val tea = Tea()

        teaRepository!!.insertTea(tea)

        verify { teaDao.insert(tea) }
    }

    @Test
    fun updateTea() {
        val tea = Tea()

        teaRepository!!.updateTea(tea)

        verify { teaDao.update(tea) }
    }

    @Test
    fun deleteTeaById() {
        val teaId: Long = 1

        teaRepository!!.deleteTeaById(teaId)

        verify { teaDao.deleteTeaById(teaId) }
    }

    @Test
    fun deleteAllTeas() {
        teaRepository!!.deleteAllTeas()

        verify { teaDao.deleteAll() }
    }

    @Test
    fun getTeas() {
        every { teaDao.getTeas() } returns listOf(Tea(), Tea())

        val teas = teaRepository!!.teas

        assertThat(teas).hasSize(2)
    }

    @Test
    fun getTeasOrderByActivity() {
        every { teaDao.getTeasOrderByActivity() } returns listOf(Tea(), Tea())

        val teas = teaRepository!!.getTeasOrderByActivity(false)

        assertThat(teas).hasSize(2)
    }

    @Test
    fun getTeasInStockOrderByActivity() {
        every { teaDao.getTeasInStockOrderByActivity() } returns listOf(Tea(), Tea())

        val teas = teaRepository!!.getTeasOrderByActivity(true)

        assertThat(teas).hasSize(2)
    }

    @Test
    fun getTeasOrderByAlphabetic() {
        every { teaDao.getTeasOrderByAlphabetic() } returns listOf(Tea(), Tea())

        val teas = teaRepository!!.getTeasOrderByAlphabetic(false)

        assertThat(teas).hasSize(2)
    }

    @Test
    fun getTeasInStockOrderByAlphabetic() {
        every { teaDao.getTeasInStockOrderByAlphabetic() } returns listOf(Tea(), Tea())

        val teas = teaRepository!!.getTeasOrderByAlphabetic(true)

        assertThat(teas).hasSize(2)
    }

    @Test
    fun getTeasOrderByVariety() {
        every { teaDao.getTeasOrderByVariety() } returns listOf(Tea(), Tea())

        val teas = teaRepository!!.getTeasOrderByVariety(false)

        assertThat(teas).hasSize(2)
    }

    @Test
    fun getTeasStockOrderByVariety() {
        every { teaDao.getTeasInStockOrderByVariety() } returns listOf(Tea(), Tea())

        val teas = teaRepository!!.getTeasOrderByVariety(true)

        assertThat(teas).hasSize(2)
    }

    @Test
    fun getTeasOrderByRating() {
        every { teaDao.getTeasOrderByRating() } returns listOf(Tea(), Tea())

        val teas = teaRepository!!.getTeasOrderByRating(false)

        assertThat(teas).hasSize(2)
    }

    @Test
    fun getTeasInStockOrderByRating() {
        every { teaDao.getTeasInStockOrderByRating() } returns listOf(Tea(), Tea())

        val teas = teaRepository!!.getTeasOrderByRating(true)

        assertThat(teas).hasSize(2)
    }

    @Test
    fun getTeaById() {
        val teaId: Long = 1
        val tea = Tea()
        every { teaDao.getTeaById(teaId) } returns tea

        val teaById = teaRepository!!.getTeaById(teaId)

        assertThat(teaById).isEqualTo(tea)
    }

    @Test
    fun getRandomTeaInStock() {
        val tea = Tea()
        every { teaDao.getRandomTeaInStock() } returns tea

        val randomTea = teaRepository!!.randomTeaInStock

        assertThat(randomTea).isEqualTo(tea)
    }

    @Test
    fun getTeasBySearchString() {
        val searchString = "search"
        every { teaDao.getTeasBySearchString(searchString) } returns listOf(Tea(), Tea())

        val teas = teaRepository!!.getTeasBySearchString(searchString)

        assertThat(teas).hasSize(2)
    }
}