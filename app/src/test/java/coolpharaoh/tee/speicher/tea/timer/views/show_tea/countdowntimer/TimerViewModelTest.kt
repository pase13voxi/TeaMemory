package coolpharaoh.tee.speicher.tea.timer.views.show_tea.countdowntimer

import coolpharaoh.tee.speicher.tea.timer.core.settings.SharedSettings
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea
import coolpharaoh.tee.speicher.tea.timer.core.tea.TeaRepository
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class TimerViewModelTest {
    @Mock
    lateinit var sharedSettings: SharedSettings

    @Mock
    lateinit var teaRepository: TeaRepository

    @InjectMocks
    lateinit var timerViewModel: TimerViewModel

    @Test
    fun isVibration() {
        `when`(sharedSettings.isVibration).thenReturn(true)

        assertThat(timerViewModel.isVibration).isTrue

        `when`(sharedSettings.isVibration).thenReturn(false)

        assertThat(timerViewModel.isVibration).isFalse
    }

    @Test
    fun getMusicChoice() {
        val musicChoice = "MUSICPATH"

        `when`(sharedSettings.musicChoice).thenReturn(musicChoice)

        assertThat(timerViewModel.musicChoice).isEqualTo(musicChoice)
    }

    @Test
    fun getName() {
        val teaName = "TEANAME"

        val tea = Tea()
        tea.name = teaName

        `when`(teaRepository.getTeaById(1L)).thenReturn(tea)

        assertThat(timerViewModel.getName(0L)).isEqualTo("Default Tea")
        assertThat(timerViewModel.getName(1L)).isEqualTo(teaName)
    }
}