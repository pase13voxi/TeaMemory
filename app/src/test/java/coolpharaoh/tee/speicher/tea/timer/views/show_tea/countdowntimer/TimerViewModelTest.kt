package coolpharaoh.tee.speicher.tea.timer.views.show_tea.countdowntimer

import coolpharaoh.tee.speicher.tea.timer.core.settings.SharedSettings
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea
import coolpharaoh.tee.speicher.tea.timer.core.tea.TeaRepository
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class TimerViewModelTest {
    @MockK
    lateinit var sharedSettings: SharedSettings

    @MockK
    lateinit var teaRepository: TeaRepository

    @InjectMockKs
    lateinit var timerViewModel: TimerViewModel

    @Test
    fun isVibration() {
        every { sharedSettings.isVibration } returns true

        assertThat(timerViewModel.isVibration).isTrue

        every { sharedSettings.isVibration } returns false

        assertThat(timerViewModel.isVibration).isFalse
    }

    @Test
    fun getMusicChoice() {
        val musicChoice = "MUSICPATH"

        every { sharedSettings.musicChoice } returns musicChoice

        assertThat(timerViewModel.musicChoice).isEqualTo(musicChoice)
    }

    @Test
    fun getName() {
        val teaName = "TEANAME"

        val tea = Tea()
        tea.name = teaName

        every { teaRepository.getTeaById(1L) } returns tea

        assertThat(timerViewModel.getName(0L)).isEqualTo("Default Tea")
        assertThat(timerViewModel.getName(1L)).isEqualTo(teaName)
    }
}