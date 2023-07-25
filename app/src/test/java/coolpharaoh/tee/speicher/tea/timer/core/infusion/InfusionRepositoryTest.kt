package coolpharaoh.tee.speicher.tea.timer.core.infusion

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
internal class InfusionRepositoryTest {
    @Mock
    var teaMemoryDatabase: TeaMemoryDatabase? = null
    @Mock
    var infusionDao: InfusionDao? = null

    private var infusionRepository: InfusionRepository? = null

    @BeforeEach
    fun setUp() {
        setMockedDatabase(teaMemoryDatabase)
        `when`(teaMemoryDatabase!!.infusionDao).thenReturn(infusionDao)

        infusionRepository = InfusionRepository(Application())
    }

    @Test
    fun insertInfusion() {
        val infusion = Infusion()

        infusionRepository!!.insertInfusion(infusion)

        verify(infusionDao)?.insert(infusion)
    }

    @Test
    fun getInfusions() {
        `when`(infusionDao!!.getInfusions()).thenReturn(listOf(Infusion(), Infusion()))

        val infusions = infusionRepository!!.infusions

        verify(infusionDao)?.getInfusions()
        assertThat(infusions).hasSize(2)
    }

    @Test
    fun getInfusionsByTeaId() {
        val teaId: Long = 2
        `when`(infusionDao!!.getInfusionsByTeaId(teaId)).thenReturn(listOf(Infusion(), Infusion()))

        val infusions = infusionRepository!!.getInfusionsByTeaId(teaId)

        assertThat(infusions).hasSize(2)
    }

    @Test
    fun deleteInfusionsByTeaId() {
        val teaId: Long = 2

        infusionRepository!!.deleteInfusionsByTeaId(teaId)

        verify(infusionDao)?.deleteInfusionsByTeaId(teaId)
    }
}