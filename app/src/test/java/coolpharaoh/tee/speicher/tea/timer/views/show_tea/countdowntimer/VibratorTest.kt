package coolpharaoh.tee.speicher.tea.timer.views.show_tea.countdowntimer

import android.app.Application
import android.content.Context
import android.media.AudioManager
import android.os.Build
import android.os.VibrationEffect
import android.os.VibratorManager
import coolpharaoh.tee.speicher.tea.timer.core.system.CurrentSdk.setFixedSystem
import coolpharaoh.tee.speicher.tea.timer.core.system.SystemUtility
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnit
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class VibratorTest {
    @JvmField
    @Rule
    var rule = MockitoJUnit.rule()

    @Mock
    lateinit var application: Application

    @Mock
    lateinit var timerViewModel: TimerViewModel

    @Mock
    lateinit var vibratorManager: VibratorManager

    @Mock
    lateinit var systemVibrator: android.os.Vibrator

    @Mock
    lateinit var audioManager: AudioManager

    @Mock
    lateinit var systemUtility: SystemUtility

    @Before
    @Throws(Exception::class)
    fun setUp() {
        setFixedSystem(systemUtility)
        `when`(systemUtility.sdkVersion).thenReturn(Build.VERSION_CODES.R)
    }

    @Test
    fun whenSettingVibrationIsFalseDoNothing() {
        `when`(timerViewModel.isVibration).thenReturn(false)

        val vibrator = Vibrator(application, timerViewModel)
        vibrator.vibrate()

        verify(systemVibrator, times(0)).vibrate(ArgumentMatchers.any<VibrationEffect>())
    }

    @Test
    fun whenRingerModeIsSilentDoNothing() {
        `when`(timerViewModel.isVibration).thenReturn(true)
        mockAudioManager(AudioManager.RINGER_MODE_SILENT)

        val vibrator = Vibrator(application, timerViewModel)
        vibrator.vibrate()

        verify(systemVibrator, times(0)).vibrate(ArgumentMatchers.any<VibrationEffect>())
    }

    @Test
    fun vibrate() {
        `when`(application.getSystemService(Context.VIBRATOR_SERVICE)).thenReturn(systemVibrator)
        `when`(timerViewModel.isVibration).thenReturn(true)
        mockAudioManager(AudioManager.RINGER_MODE_NORMAL)

        val vibrator = Vibrator(application, timerViewModel)
        vibrator.vibrate()

        verify(systemVibrator).vibrate(ArgumentMatchers.any(VibrationEffect::class.java))
    }

    @Test
    fun vibrateVersionAndroidSOrNewer() {
        `when`(systemUtility.sdkVersion).thenReturn(Build.VERSION_CODES.S)
        `when`(application.getSystemService(Context.VIBRATOR_MANAGER_SERVICE)).thenReturn(vibratorManager)
        `when`(vibratorManager.defaultVibrator).thenReturn(systemVibrator)
        `when`(timerViewModel.isVibration).thenReturn(true)
        mockAudioManager(AudioManager.RINGER_MODE_NORMAL)

        val vibrator = Vibrator(application, timerViewModel)
        vibrator.vibrate()

        verify(systemVibrator).vibrate(ArgumentMatchers.any(VibrationEffect::class.java))
    }

    private fun mockAudioManager(ringerMode: Int?) {
        if (ringerMode == null) {
            `when`(application.getSystemService(Context.AUDIO_SERVICE)).thenReturn(null)
        } else {
            `when`(application.getSystemService(Context.AUDIO_SERVICE)).thenReturn(audioManager)
            `when`(audioManager.ringerMode).thenReturn(ringerMode)
        }
    }
}