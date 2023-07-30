package coolpharaoh.tee.speicher.tea.timer.views.overview

import android.app.Application
import coolpharaoh.tee.speicher.tea.timer.TaskExecutorExtension
import coolpharaoh.tee.speicher.tea.timer.core.infusion.InfusionRepository
import coolpharaoh.tee.speicher.tea.timer.core.settings.SharedSettings
import coolpharaoh.tee.speicher.tea.timer.core.settings.SortMode
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea
import coolpharaoh.tee.speicher.tea.timer.core.tea.TeaRepository
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class, TaskExecutorExtension::class)
internal class OverviewViewModelTest {
    private var overviewViewModel: OverviewViewModel? = null

    @MockK
    lateinit var application: Application

    @RelaxedMockK
    lateinit var teaRepository: TeaRepository

    @MockK
    lateinit var infusionRepository: InfusionRepository

    @RelaxedMockK
    lateinit var sharedSettings: SharedSettings


    @BeforeEach
    fun setUp() {
        mockSettings()
    }

    private fun mockSettings() {
        every { sharedSettings.isFirstStart } returns false
        every { sharedSettings.sortMode } returns SortMode.LAST_USED
    }

    @Test
    fun getTeas() {
        val teas = mockTeas(SortMode.LAST_USED)

        overviewViewModel = OverviewViewModel(application, teaRepository, infusionRepository, sharedSettings)

        val teasAfter = overviewViewModel!!.getTeas().value!!
        assertThat(teasAfter).isEqualTo(teas)
    }

    @Test
    fun getTeaById() {
        val teaId: Long = 1

        overviewViewModel = OverviewViewModel(application, teaRepository, infusionRepository, sharedSettings)
        overviewViewModel!!.getTeaBy(teaId)

        verify { teaRepository.getTeaById(teaId) }
    }

    @Test
    fun deleteTea() {
        val teaId: Long = 1

        overviewViewModel = OverviewViewModel(application, teaRepository, infusionRepository, sharedSettings)
        overviewViewModel!!.deleteTea(teaId)

        verify {teaRepository.deleteTeaById(teaId) }
    }

    @Test
    fun isTeaInStock() {
        val teaId: Long = 1
        val tea = Tea()
        tea.inStock = true
        every { teaRepository.getTeaById(teaId) } returns tea

        overviewViewModel = OverviewViewModel(application, teaRepository, infusionRepository, sharedSettings)

        assertThat(overviewViewModel!!.isTeaInStock(teaId)).isTrue
    }

    @Test
    fun updateInStockOfTea() {
        val teaId: Long = 1
        every { teaRepository.getTeaById(teaId) } returns Tea()

        overviewViewModel = OverviewViewModel(application, teaRepository, infusionRepository, sharedSettings)
        overviewViewModel!!.updateInStockOfTea(teaId, true)

        verify { teaRepository.updateTea(any()) }
    }

    @Test
    fun showTeasBySearchString() {
        val searchString = "search"

        overviewViewModel = OverviewViewModel(application, teaRepository, infusionRepository, sharedSettings)
        overviewViewModel!!.visualizeTeasBySearchString(searchString)

        verify { teaRepository.getTeasBySearchString(searchString) }
    }

    @Test
    fun showTeasByEmptySearchString() {
        val searchString = ""

        overviewViewModel = OverviewViewModel(application, teaRepository, infusionRepository, sharedSettings)
        overviewViewModel!!.visualizeTeasBySearchString(searchString)

        verify (exactly = 0) { teaRepository.getTeasBySearchString(any()) }
        verify (atLeast = 1) { teaRepository.getTeasOrderByActivity(false) }
    }

    @Test
    fun getSort() {
            overviewViewModel = OverviewViewModel(application, teaRepository, infusionRepository, sharedSettings)
            val sort = overviewViewModel!!.sort

            assertThat(sort).isEqualTo(SortMode.LAST_USED)
        }

    @Test
    fun setSort() {
        overviewViewModel = OverviewViewModel(application, teaRepository, infusionRepository, sharedSettings)
        overviewViewModel!!.sort = SortMode.BY_VARIETY

        verify { sharedSettings.sortMode = SortMode.BY_VARIETY }
    }

    @Test
    fun isOverviewHeader() {
        every { sharedSettings.isOverviewHeader } returns false

        overviewViewModel = OverviewViewModel(application, teaRepository, infusionRepository, sharedSettings)
        val isOverviewHeader = overviewViewModel!!.isOverviewHeader

        assertThat(isOverviewHeader).isFalse
    }

    @Test
    fun setOverviewFavorites() {
        val overviewFavorites = true

        overviewViewModel = OverviewViewModel(application, teaRepository, infusionRepository, sharedSettings)
        overviewViewModel!!.isOverviewInStock = overviewFavorites

        verify { sharedSettings.isOverviewInStock = overviewFavorites }
    }

    @Test
    fun isOverviewFavorites() {
        every { sharedSettings.isOverviewInStock } returns false

        overviewViewModel = OverviewViewModel(application, teaRepository, infusionRepository, sharedSettings)
        val overViewFavorites = overviewViewModel!!.isOverviewInStock

        assertThat(overViewFavorites).isFalse
    }

    @Test
    fun isMainUpdateAlert() {
        val overviewUpdateAlert = true
        every { sharedSettings.isOverviewUpdateAlert } returns overviewUpdateAlert

        overviewViewModel = OverviewViewModel(application, teaRepository, infusionRepository, sharedSettings)

        assertThat(overviewViewModel!!.isOverviewUpdateAlert).isEqualTo(overviewUpdateAlert)
    }

    @Test
    fun setOverviewUpdateAlert() {
        val alert = true

        overviewViewModel = OverviewViewModel(application, teaRepository, infusionRepository, sharedSettings)
        overviewViewModel!!.isOverviewUpdateAlert = alert

        verify { sharedSettings.isOverviewUpdateAlert = alert }
    }

    @Test
    fun refreshTeasWithSort0() {
        every { sharedSettings.sortMode } returns SortMode.LAST_USED

        overviewViewModel = OverviewViewModel(application, teaRepository, infusionRepository, sharedSettings)
        overviewViewModel!!.refreshTeas()

        verify (atLeast = 1) { teaRepository.getTeasOrderByActivity(false) }
    }

    @Test
    fun refreshTeasWithSort0OnlyFavorites() {
        every { sharedSettings.isOverviewInStock } returns true
        every { sharedSettings.sortMode } returns SortMode.LAST_USED

        overviewViewModel = OverviewViewModel(application, teaRepository, infusionRepository, sharedSettings)
        overviewViewModel!!.refreshTeas()

        verify (atLeast = 1) { teaRepository.getTeasOrderByActivity(true) }
    }

    @Test
    fun refreshTeasWithSort1() {
        every { sharedSettings.sortMode} returns SortMode.ALPHABETICAL

        overviewViewModel = OverviewViewModel(application, teaRepository, infusionRepository, sharedSettings)
        overviewViewModel!!.refreshTeas()

        verify (atLeast = 1) { teaRepository.getTeasOrderByAlphabetic(false) }
    }

    @Test
    fun refreshTeasWithSort1OnlyFavorites() {
        every { sharedSettings.isOverviewInStock } returns true
        every { sharedSettings.sortMode } returns SortMode.ALPHABETICAL

        overviewViewModel = OverviewViewModel(application, teaRepository, infusionRepository, sharedSettings)
        overviewViewModel!!.refreshTeas()

        verify (atLeast = 1) { teaRepository.getTeasOrderByAlphabetic(true) }
    }

    @Test
    fun refreshTeasWithSort2() {
        every { sharedSettings.sortMode } returns SortMode.BY_VARIETY

        overviewViewModel = OverviewViewModel(application, teaRepository, infusionRepository, sharedSettings)
        overviewViewModel!!.refreshTeas()

        verify (atLeast = 1) { teaRepository.getTeasOrderByVariety(false) }
    }

    @Test
    fun refreshTeasWithSort2OnlyFavorites() {
        every { sharedSettings.isOverviewInStock } returns true
        every { sharedSettings.sortMode } returns SortMode.BY_VARIETY

        overviewViewModel = OverviewViewModel(application, teaRepository, infusionRepository, sharedSettings)
        overviewViewModel!!.refreshTeas()

        verify (atLeast = 1) { teaRepository.getTeasOrderByVariety(true) }
    }

    @Test
    fun refreshTeasWithSort3() {
        every { sharedSettings.sortMode } returns SortMode.RATING

        overviewViewModel = OverviewViewModel(application, teaRepository, infusionRepository, sharedSettings)
        overviewViewModel!!.refreshTeas()

        verify (atLeast = 1) { teaRepository.getTeasOrderByRating(false) }
    }

    @Test
    fun refreshTeasWithSort3OnlyFavorites() {
        every { sharedSettings.isOverviewInStock } returns true
        every { sharedSettings.sortMode } returns SortMode.RATING

        overviewViewModel = OverviewViewModel(application, teaRepository, infusionRepository, sharedSettings)
        overviewViewModel!!.refreshTeas()

        verify (atLeast = 1) { teaRepository.getTeasOrderByRating(true) }
    }

    @Test
    fun getSortWithHeaderIsSort3() {
        mockTeas(SortMode.RATING)
        every { sharedSettings.isOverviewHeader } returns true
        every { sharedSettings.sortMode } returns SortMode.RATING

        overviewViewModel = OverviewViewModel(application, teaRepository, infusionRepository, sharedSettings)
        overviewViewModel!!.refreshTeas()

        assertThat(overviewViewModel!!.sortWithHeader).isEqualTo(3)
    }

    @Test
    fun getSortWithHeaderIsFalse() {
        every { sharedSettings.isOverviewHeader } returns false

        overviewViewModel = OverviewViewModel(application, teaRepository, infusionRepository, sharedSettings)
        assertThat(overviewViewModel!!.sortWithHeader).isEqualTo(-1)
    }

    @Test
    fun getRandomTeaInStock() {
        val tea = Tea()
        every { teaRepository.randomTeaInStock } returns tea

        overviewViewModel = OverviewViewModel(application, teaRepository, infusionRepository, sharedSettings)
        val randomTea = overviewViewModel!!.randomTeaInStock

        assertThat(randomTea).isEqualTo(tea)
    }

    @Test
    fun getSortWithHeaderIsInSearchMode() {
        val searchString = "search"

        overviewViewModel = OverviewViewModel(application, teaRepository, infusionRepository, sharedSettings)
        overviewViewModel!!.visualizeTeasBySearchString(searchString)

        assertThat(overviewViewModel!!.sortWithHeader).isEqualTo(-1)
    }

    private fun mockTeas(sortMode: SortMode): List<Tea> {
        val teas: MutableList<Tea> = ArrayList()
        teas.add(Tea("name", "variety", 5.0, "amount", 5, 0, null))
        teas.add(Tea("name", "variety", 5.0, "amount", 5, 0, null))
        when (sortMode) {
            SortMode.LAST_USED -> every { teaRepository.getTeasOrderByActivity(false) } returns teas
            SortMode.BY_VARIETY -> every { teaRepository.getTeasOrderByVariety(false) } returns teas
            SortMode.ALPHABETICAL -> every { teaRepository.getTeasOrderByAlphabetic(false) } returns teas
            SortMode.RATING -> every { teaRepository.getTeasOrderByRating(false) } returns teas
        }
        return teas
    }
}