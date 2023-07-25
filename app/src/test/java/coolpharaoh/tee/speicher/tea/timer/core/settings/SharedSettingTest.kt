package coolpharaoh.tee.speicher.tea.timer.core.settings

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@RunWith(RobolectricTestRunner::class)
class SharedSettingTest {
    @Test
    fun isFirstStartIsTrue() {
        val sharedSettings = SharedSettings(RuntimeEnvironment.getApplication())

        assertThat(sharedSettings.isFirstStart).isTrue
    }

    @Test
    fun setFirstStartFalse() {
        val sharedSettings = SharedSettings(RuntimeEnvironment.getApplication())

        sharedSettings.isFirstStart = false

        assertThat(sharedSettings.isFirstStart).isFalse
    }

    @Test
    fun setSettingsVersionOne() {
        val settingsVersion = 1
        val sharedSettings = SharedSettings(RuntimeEnvironment.getApplication())

        sharedSettings.settingsVersion = settingsVersion

        assertThat(sharedSettings.settingsVersion).isEqualTo(settingsVersion)
    }

    @Test
    fun setMusicChoice() {
        val musicChoice = "MUSIC_CHOICE"
        val sharedSettings = SharedSettings(RuntimeEnvironment.getApplication())

        sharedSettings.musicChoice = musicChoice

        assertThat(sharedSettings.musicChoice).isEqualTo(musicChoice)
    }

    @Test
    fun setMusicName() {
        val musicName = "MUSIC_NAME"
        val sharedSettings = SharedSettings(RuntimeEnvironment.getApplication())

        sharedSettings.musicName = musicName

        assertThat(sharedSettings.musicName).isEqualTo(musicName)
    }

    @Test
    fun setVibrationFalse() {
        val sharedSettings = SharedSettings(RuntimeEnvironment.getApplication())

        sharedSettings.isVibration = false

        assertThat(sharedSettings.isVibration).isFalse
    }

    @Test
    fun setAnimationFalse() {
        val sharedSettings = SharedSettings(RuntimeEnvironment.getApplication())

        sharedSettings.isAnimation = false

        assertThat(sharedSettings.isAnimation).isFalse
    }

    @Test
    fun setTemperatureFahrenheit() {
        val sharedSettings = SharedSettings(RuntimeEnvironment.getApplication())

        sharedSettings.temperatureUnit = TemperatureUnit.FAHRENHEIT

        assertThat(sharedSettings.temperatureUnit).isEqualTo(TemperatureUnit.FAHRENHEIT)
    }

    @Test
    fun setDarkModeDisabled() {
        val sharedSettings = SharedSettings(RuntimeEnvironment.getApplication())

        sharedSettings.darkMode = DarkMode.DISABLED

        assertThat(sharedSettings.darkMode).isEqualTo(DarkMode.DISABLED)
    }

    @Test
    fun setOverviewUpdateAlertTrue() {
        val sharedSettings = SharedSettings(RuntimeEnvironment.getApplication())

        sharedSettings.isOverviewUpdateAlert = true

        assertThat(sharedSettings.isOverviewUpdateAlert).isTrue
    }

    @Test
    fun setShowTeaAlertTrue() {
        val sharedSettings = SharedSettings(RuntimeEnvironment.getApplication())

        sharedSettings.isShowTeaAlert = true

        assertThat(sharedSettings.isShowTeaAlert).isTrue
    }

    @Test
    fun setSortModeAlphabetical() {
        val sharedSettings = SharedSettings(RuntimeEnvironment.getApplication())

        sharedSettings.sortMode = SortMode.ALPHABETICAL

        assertThat(sharedSettings.sortMode).isEqualTo(SortMode.ALPHABETICAL)
    }

    @Test
    fun setOverviewHeaderTrue() {
        val sharedSettings = SharedSettings(RuntimeEnvironment.getApplication())

        sharedSettings.isOverviewHeader = true

        assertThat(sharedSettings.isOverviewHeader).isTrue
    }

    @Test
    fun setOverviewInStockTrue() {
        val sharedSettings = SharedSettings(RuntimeEnvironment.getApplication())

        sharedSettings.isOverviewInStock = true

        assertThat(sharedSettings.isOverviewInStock).isTrue
    }

    @Test
    fun setFactorySettings() {
        val sharedSettings = SharedSettings(RuntimeEnvironment.getApplication())

        sharedSettings.setFactorySettings()

        assertThat(sharedSettings.musicChoice).isEqualTo("content://settings/system/ringtone")
        assertThat(sharedSettings.musicName).isEqualTo("Default")
        assertThat(sharedSettings.isVibration).isTrue
        assertThat(sharedSettings.isAnimation).isTrue
        assertThat(sharedSettings.temperatureUnit).isEqualTo(TemperatureUnit.CELSIUS)
        assertThat(sharedSettings.darkMode).isEqualTo(DarkMode.SYSTEM)
        assertThat(sharedSettings.isOverviewUpdateAlert).isFalse
        assertThat(sharedSettings.isShowTeaAlert).isTrue
        assertThat(sharedSettings.sortMode).isEqualTo(SortMode.LAST_USED)
        assertThat(sharedSettings.isOverviewHeader).isFalse
        assertThat(sharedSettings.isOverviewInStock).isFalse
    }
}