package coolpharaoh.tee.speicher.tea.timer.views.show_tea.countdowntimer

import android.app.Application
import android.content.Context
import android.media.AudioManager
import android.os.Build
import android.os.VibrationEffect
import android.os.VibratorManager
import coolpharaoh.tee.speicher.tea.timer.core.system.CurrentSdk.setFixedSystem
import coolpharaoh.tee.speicher.tea.timer.core.system.SystemUtility
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class VibratorTest {
    @get:Rule
    val mockkRule = MockKRule(this)
    @MockK
    lateinit var application: Application
    @MockK
    lateinit var timerViewModel: TimerViewModel
    @MockK
    lateinit var vibratorManager: VibratorManager
    @RelaxedMockK
    lateinit var systemVibrator: android.os.Vibrator
    @MockK
    lateinit var audioManager: AudioManager
    @MockK
    lateinit var systemUtility: SystemUtility

    @Before
    @Throws(Exception::class)
    fun setUp() {
        setFixedSystem(systemUtility)
        every { systemUtility.sdkVersion } returns Build.VERSION_CODES.R
    }

    @Test
    fun whenSettingVibrationIsFalseDoNothing() {
        every { timerViewModel.isVibration } returns false

        val vibrator = Vibrator(application, timerViewModel)
        vibrator.vibrate()

        verify (exactly = 0) { systemVibrator.vibrate(any<VibrationEffect>()) }
    }

    @Test
    fun whenRingerModeIsSilentDoNothing() {
        every { timerViewModel.isVibration } returns true
        mockAudioManager(AudioManager.RINGER_MODE_SILENT)

        val vibrator = Vibrator(application, timerViewModel)
        vibrator.vibrate()

        verify (exactly=0) { systemVibrator.vibrate(any<VibrationEffect>()) }
    }

    @Test
    fun vibrate() {
        every { application.getSystemService(Context.VIBRATOR_SERVICE) } returns systemVibrator
        every { timerViewModel.isVibration } returns true
        mockAudioManager(AudioManager.RINGER_MODE_NORMAL)

        val vibrator = Vibrator(application, timerViewModel)
        vibrator.vibrate()

        verify { systemVibrator.vibrate(any<VibrationEffect>()) }
    }

    @Test
    fun vibrateVersionAndroidSOrNewer() {
        every { systemUtility.sdkVersion } returns Build.VERSION_CODES.S
        every { application.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) } returns vibratorManager
        every { vibratorManager.defaultVibrator } returns systemVibrator
        every { timerViewModel.isVibration } returns true
        mockAudioManager(AudioManager.RINGER_MODE_NORMAL)

        val vibrator = Vibrator(application, timerViewModel)
        vibrator.vibrate()

        verify { systemVibrator.vibrate(any<VibrationEffect>()) }
    }

    private fun mockAudioManager(ringerMode: Int?) {
        if (ringerMode == null) {
            every { application.getSystemService(Context.AUDIO_SERVICE) } returns null
        } else {
            every { application.getSystemService(Context.AUDIO_SERVICE) } returns audioManager
            every { audioManager.ringerMode } returns ringerMode
        }
    }
}