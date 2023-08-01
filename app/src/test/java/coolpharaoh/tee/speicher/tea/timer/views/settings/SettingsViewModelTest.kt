package coolpharaoh.tee.speicher.tea.timer.views.settings

import coolpharaoh.tee.speicher.tea.timer.core.settings.DarkMode
import coolpharaoh.tee.speicher.tea.timer.core.settings.SharedSettings
import coolpharaoh.tee.speicher.tea.timer.core.settings.TemperatureUnit
import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea
import coolpharaoh.tee.speicher.tea.timer.core.tea.TeaRepository
import coolpharaoh.tee.speicher.tea.timer.views.utils.image_controller.ImageController
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.util.Arrays

@ExtendWith(MockKExtension::class)
internal class SettingsViewModelTest {
    @RelaxedMockK
    lateinit var teaRepository: TeaRepository
    @RelaxedMockK
    lateinit var sharedSettings: SharedSettings
    @RelaxedMockK
    lateinit var imageController: ImageController
    @InjectMockKs
    lateinit var settingsViewModel: SettingsViewModel

    @Test
    fun setMusicChoice() {
        val musicChoice = "MUSIC_CHOICE"
        settingsViewModel.setMusicChoice(musicChoice)

        verify { sharedSettings.musicChoice = musicChoice }
    }

    @Test
    fun setMusicName() {
        val musicName = "MUSIC_NAME"
        settingsViewModel.musicName = musicName

        verify { sharedSettings.musicName = musicName } 
    }

    @Test
    fun getMusicName() {
        val musicName = "MUSIC_NAME"
        every { sharedSettings.musicName } returns musicName

        assertThat(settingsViewModel.musicName).isEqualTo(musicName)
    }

    @Test
    fun setVibration() {
        val vibration = true
        settingsViewModel.isVibration = vibration

        verify { sharedSettings.isVibration = vibration }
    }

    @Test
    fun isVibration() {
        val vibration = true
        every { sharedSettings.isVibration } returns vibration

        assertThat(settingsViewModel.isVibration).isEqualTo(vibration)
    }

    @Test
    fun setAnimation() {
        val animation = true
        settingsViewModel.isAnimation = animation

        verify { sharedSettings.isAnimation = animation }
    }

    @Test
    fun isAnimation() {
        val animation = true
        every { sharedSettings.isAnimation } returns animation

        assertThat(settingsViewModel.isAnimation).isEqualTo(animation)
    }

    @Test
    fun setTemperatureUnit() {
        settingsViewModel.temperatureUnit = TemperatureUnit.CELSIUS

        verify { sharedSettings.temperatureUnit = TemperatureUnit.CELSIUS }
    }

    @Test
    fun getTemperatureUnit() {
        every { sharedSettings.temperatureUnit } returns TemperatureUnit.CELSIUS

        assertThat(settingsViewModel.temperatureUnit).isEqualTo(TemperatureUnit.CELSIUS)
    }

    @Test
    fun setOverviewHeader() {
        val overviewHeader = true
        settingsViewModel.isOverviewHeader = overviewHeader

        verify { sharedSettings.isOverviewHeader = overviewHeader }
    }

    @Test
    fun isOverviewHeader() {
        every { sharedSettings.isOverviewHeader } returns false
        val isOverviewHeader = settingsViewModel.isOverviewHeader

        assertThat(isOverviewHeader).isFalse
    }

    @Test
    fun setDarkMode() {
        val darkMode = DarkMode.ENABLED
        settingsViewModel.darkMode = darkMode

        verify { sharedSettings.darkMode = darkMode }
    }

    @Test
    fun getDarkMode() {
        every { sharedSettings.darkMode } returns DarkMode.ENABLED
        val darkMode = settingsViewModel.darkMode

        assertThat(darkMode).isEqualTo(DarkMode.ENABLED)
    }

    @Test
    fun setShowTeaAlert() {
        val showTeaAlert = true
        settingsViewModel.isShowTeaAlert = showTeaAlert

        verify { sharedSettings.isShowTeaAlert = showTeaAlert }
    }

    @Test
    fun isShowTeaAlert() {
        val showTeaAlert = true
        every { sharedSettings.isShowTeaAlert } returns showTeaAlert

        assertThat(settingsViewModel.isShowTeaAlert).isEqualTo(showTeaAlert)
    }

    @Test
    fun setMainUpdateAlert() {
        val overviewUpdateAlert = true
        every { sharedSettings.isOverviewUpdateAlert } returns overviewUpdateAlert

        assertThat(settingsViewModel.overviewUpdateAlert).isEqualTo(overviewUpdateAlert)
    }

    @Test
    fun isMainUpdateAlert() {
        val overviewUpdateAlert = true
        settingsViewModel.overviewUpdateAlert = overviewUpdateAlert

        verify { sharedSettings.isOverviewUpdateAlert = overviewUpdateAlert }
    }

    @Test
    fun setDefaultSettings() {
        settingsViewModel.setDefaultSettings()

        verify { sharedSettings.setFactorySettings() }
    }

    @Test
    fun deleteAllTeaImages() {
        val tea1 = Tea()
        tea1.id = 1L
        val tea2 = Tea()
        tea2.id = 2L
        val teas = Arrays.asList(tea1, tea2)
        every { teaRepository.teas } returns teas

        settingsViewModel.deleteAllTeaImages()

        verify { imageController.removeImageByTeaId(1L) }
        verify { imageController.removeImageByTeaId(2L) }
    }

    @Test
    fun deleteAllTeas() {
        settingsViewModel.deleteAllTeas()
        verify { teaRepository.deleteAllTeas() }
    }
}