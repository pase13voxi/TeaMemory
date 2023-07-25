package coolpharaoh.tee.speicher.tea.timer.core.tea

import android.app.Application
import coolpharaoh.tee.speicher.tea.timer.database.TeaMemoryDatabase
import coolpharaoh.tee.speicher.tea.timer.database.TeaMemoryDatabase.Companion.setMockedDatabase
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class TeaRepositoryTest {

    @Mock
    var teaMemoryDatabase: TeaMemoryDatabase? = null

    @Mock

    var teaDao: TeaDao? = null
    private var teaRepository: TeaRepository? = null

    @BeforeEach
    fun setUp() {
        setMockedDatabase(teaMemoryDatabase)
        `when`(teaMemoryDatabase!!.teaDao).thenReturn(teaDao)

        teaRepository = TeaRepository(Application())
    }

    @Test
    fun insertTea() {
        val tea = Tea()

        teaRepository!!.insertTea(tea)

        verify(teaDao)?.insert(tea)
    }

    @Test
    fun updateTea() {
        val tea = Tea()

        teaRepository!!.updateTea(tea)

        verify(teaDao)?.update(tea)
    }

    @Test
    fun deleteTeaById() {
        val teaId: Long = 1

        teaRepository!!.deleteTeaById(teaId)

        verify(teaDao)?.deleteTeaById(teaId)
    }

    @Test
    fun deleteAllTeas() {
        teaRepository!!.deleteAllTeas()

        verify(teaDao)?.deleteAll()
    }

    @Test
    fun getTeas() {
        `when`(teaDao!!.getTeas()).thenReturn(listOf(Tea(), Tea()))

        val teas = teaRepository!!.teas

        assertThat(teas).hasSize(2)
    }

    @Test
    fun getTeasOrderByActivity() {
        `when`(teaDao!!.getTeasOrderByActivity()).thenReturn(listOf(Tea(), Tea()))

        val teas = teaRepository!!.getTeasOrderByActivity(false)

        assertThat(teas).hasSize(2)
    }

    @Test
    fun getTeasInStockOrderByActivity() {
        `when`(teaDao!!.getTeasInStockOrderByActivity()).thenReturn(listOf(Tea(), Tea()))

        val teas = teaRepository!!.getTeasOrderByActivity(true)

        assertThat(teas).hasSize(2)
    }

    @Test
    fun getTeasOrderByAlphabetic() {
        `when`(teaDao!!.getTeasOrderByAlphabetic()).thenReturn(listOf(Tea(), Tea()))

        val teas = teaRepository!!.getTeasOrderByAlphabetic(false)

        assertThat(teas).hasSize(2)
    }

    @Test
    fun getTeasInStockOrderByAlphabetic() {
        `when`(teaDao!!.getTeasInStockOrderByAlphabetic()).thenReturn(listOf(Tea(), Tea()))

        val teas = teaRepository!!.getTeasOrderByAlphabetic(true)

        assertThat(teas).hasSize(2)
    }

    @Test
    fun getTeasOrderByVariety() {
        `when`(teaDao!!.getTeasOrderByVariety()).thenReturn(listOf(Tea(), Tea()))

        val teas = teaRepository!!.getTeasOrderByVariety(false)

        assertThat(teas).hasSize(2)
    }

    @Test
    fun getTeasStockOrderByVariety() {
        `when`(teaDao!!.getTeasInStockOrderByVariety()).thenReturn(listOf(Tea(), Tea()))

        val teas = teaRepository!!.getTeasOrderByVariety(true)

        assertThat(teas).hasSize(2)
    }

    @Test
    fun getTeasOrderByRating() {
        `when`(teaDao!!.getTeasOrderByRating()).thenReturn(listOf(Tea(), Tea()))

        val teas = teaRepository!!.getTeasOrderByRating(false)

        assertThat(teas).hasSize(2)
    }

    @Test
    fun getTeasInStockOrderByRating() {
        `when`(teaDao!!.getTeasInStockOrderByRating()).thenReturn(listOf(Tea(), Tea()))

        val teas = teaRepository!!.getTeasOrderByRating(true)

        assertThat(teas).hasSize(2)
    }

    @Test
    fun getTeaById() {
        val teaId: Long = 1
        val tea = Tea()
        `when`(teaDao!!.getTeaById(teaId)).thenReturn(tea)

        val teaById = teaRepository!!.getTeaById(teaId)

        assertThat(teaById).isEqualTo(tea)
    }

    @Test
    fun getRandomTeaInStock() {
        val tea = Tea()
        `when`(teaDao!!.getRandomTeaInStock()).thenReturn(tea)

        val randomTea = teaRepository!!.randomTeaInStock

        assertThat(randomTea).isEqualTo(tea)
    }

    @Test
    fun getTeasBySearchString() {
        val searchString = "search"
        `when`(teaDao!!.getTeasBySearchString(searchString)).thenReturn(listOf(Tea(), Tea()))

        val teas = teaRepository!!.getTeasBySearchString(searchString)

        assertThat(teas).hasSize(2)
    }
}