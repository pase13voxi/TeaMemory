package coolpharaoh.tee.speicher.tea.timer.core.infusion

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
internal class InfusionRepositoryTest {
    @MockK
    lateinit var teaMemoryDatabase: TeaMemoryDatabase
    @RelaxedMockK
    lateinit var infusionDao: InfusionDao

    private var infusionRepository: InfusionRepository? = null

    @BeforeEach
    fun setUp() {
        setMockedDatabase(teaMemoryDatabase)
        every { teaMemoryDatabase.infusionDao } returns infusionDao

        infusionRepository = InfusionRepository(Application())
    }

    @Test
    fun insertInfusion() {
        val infusion = Infusion()

        infusionRepository!!.insertInfusion(infusion)

        verify { infusionDao.insert(infusion) }
    }

    @Test
    fun getInfusions() {
        every { infusionDao.getInfusions() } returns listOf(Infusion(), Infusion())

        val infusions = infusionRepository!!.infusions

        verify { infusionDao.getInfusions() }
        assertThat(infusions).hasSize(2)
    }

    @Test
    fun getInfusionsByTeaId() {
        val teaId: Long = 2
        every { infusionDao.getInfusionsByTeaId(teaId) } returns listOf(Infusion(), Infusion())

        val infusions = infusionRepository!!.getInfusionsByTeaId(teaId)

        assertThat(infusions).hasSize(2)
    }

    @Test
    fun deleteInfusionsByTeaId() {
        val teaId: Long = 2

        infusionRepository!!.deleteInfusionsByTeaId(teaId)

        verify { infusionDao.deleteInfusionsByTeaId(teaId) }
    }
}