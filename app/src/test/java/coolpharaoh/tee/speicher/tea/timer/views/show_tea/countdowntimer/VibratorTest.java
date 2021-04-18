package coolpharaoh.tee.speicher.tea.timer.views.show_tea.countdowntimer;

import android.app.Application;
import android.content.Context;
import android.media.AudioManager;
import android.os.Build;
import android.os.VibrationEffect;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import coolpharaoh.tee.speicher.tea.timer.core.system.CurrentSdk;
import coolpharaoh.tee.speicher.tea.timer.core.system.SystemUtility;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

//could be removed when Robolectric supports Java 8 for API 29
@Config(sdk = Build.VERSION_CODES.O_MR1)
@RunWith(RobolectricTestRunner.class)
public class VibratorTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();
    @Mock
    Application application;
    @Mock
    TimerViewModel timerViewModel;
    @Mock
    android.os.Vibrator systemVibrator;
    @Mock
    AudioManager audioManager;
    @Mock
    SystemUtility systemUtility;

    @Test
    public void whenSettingVibrationIsFalseDoNothing() {
        when(timerViewModel.isVibration()).thenReturn(false);

        Vibrator vibrator = new Vibrator(application, timerViewModel);
        vibrator.vibrate();

        verify(systemVibrator, times(0)).vibrate(any());
        verify(systemVibrator, times(0)).vibrate(any(), any());
    }

    @Test
    public void whenRingerModeIsSilentDoNothing() {
        when(timerViewModel.isVibration()).thenReturn(true);
        mockAudioManager(AudioManager.RINGER_MODE_SILENT);

        Vibrator vibrator = new Vibrator(application, timerViewModel);
        vibrator.vibrate();

        verify(systemVibrator, times(0)).vibrate(any());
        verify(systemVibrator, times(0)).vibrate(any(), any());
    }

    @Test(expected = AssertionError.class)
    public void whenVibratorIsNullThrowException() {
        when(application.getSystemService(Context.VIBRATOR_SERVICE)).thenReturn(null);
        when(timerViewModel.isVibration()).thenReturn(true);
        mockAudioManager(AudioManager.RINGER_MODE_NORMAL);

        Vibrator vibrator = new Vibrator(application, timerViewModel);
        vibrator.vibrate();
    }

    @Test
    public void vibrateAfterAndroidO() {
        when(application.getSystemService(Context.VIBRATOR_SERVICE)).thenReturn(systemVibrator);
        when(timerViewModel.isVibration()).thenReturn(true);
        mockAudioManager(AudioManager.RINGER_MODE_NORMAL);
        CurrentSdk.setFixedSystem(systemUtility);
        when(systemUtility.getSdkVersion()).thenReturn(Build.VERSION_CODES.O_MR1);

        Vibrator vibrator = new Vibrator(application, timerViewModel);
        vibrator.vibrate();

        verify(systemVibrator).vibrate(any(VibrationEffect.class));
    }

    @Test
    public void whenAudioManagerIsNullVibrateAnyway() {
        when(application.getSystemService(Context.VIBRATOR_SERVICE)).thenReturn(systemVibrator);
        when(timerViewModel.isVibration()).thenReturn(true);
        mockAudioManager(null);
        CurrentSdk.setFixedSystem(systemUtility);
        when(systemUtility.getSdkVersion()).thenReturn(Build.VERSION_CODES.O_MR1);

        Vibrator vibrator = new Vibrator(application, timerViewModel);
        vibrator.vibrate();

        verify(systemVibrator).vibrate(any(VibrationEffect.class));
    }

    @Test
    public void vibrateBeforeAndroidO() {
        when(application.getSystemService(Context.VIBRATOR_SERVICE)).thenReturn(systemVibrator);
        when(timerViewModel.isVibration()).thenReturn(true);
        mockAudioManager(AudioManager.RINGER_MODE_NORMAL);
        CurrentSdk.setFixedSystem(systemUtility);
        when(systemUtility.getSdkVersion()).thenReturn(Build.VERSION_CODES.N_MR1);

        Vibrator vibrator = new Vibrator(application, timerViewModel);
        vibrator.vibrate();

        verify(systemVibrator).vibrate(any(long[].class), eq(-1));
    }

    private void mockAudioManager(final Integer ringerMode) {
        if (ringerMode == null) {
            when(application.getSystemService(Context.AUDIO_SERVICE)).thenReturn(null);
        } else {
            when(application.getSystemService(Context.AUDIO_SERVICE)).thenReturn(audioManager);
            when(audioManager.getRingerMode()).thenReturn(ringerMode);
        }
    }

}
