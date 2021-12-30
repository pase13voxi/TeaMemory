package coolpharaoh.tee.speicher.tea.timer.views.show_tea.countdowntimer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

    @Test
    public void whenSettingVibrationIsFalseDoNothing() {
        when(timerViewModel.isVibration()).thenReturn(false);

        final Vibrator vibrator = new Vibrator(application, timerViewModel);
        vibrator.vibrate();

        verify(systemVibrator, times(0)).vibrate(any());
        verify(systemVibrator, times(0)).vibrate(any(), any());
    }

    @Test
    public void whenRingerModeIsSilentDoNothing() {
        when(timerViewModel.isVibration()).thenReturn(true);
        mockAudioManager(AudioManager.RINGER_MODE_SILENT);

        final Vibrator vibrator = new Vibrator(application, timerViewModel);
        vibrator.vibrate();

        verify(systemVibrator, times(0)).vibrate(any());
        verify(systemVibrator, times(0)).vibrate(any(), any());
    }

    @Test(expected = AssertionError.class)
    public void whenVibratorIsNullThrowException() {
        when(application.getSystemService(Context.VIBRATOR_SERVICE)).thenReturn(null);
        when(timerViewModel.isVibration()).thenReturn(true);
        mockAudioManager(AudioManager.RINGER_MODE_NORMAL);

        final Vibrator vibrator = new Vibrator(application, timerViewModel);
        vibrator.vibrate();
    }

    @Test
    public void vibrate() {
        when(application.getSystemService(Context.VIBRATOR_SERVICE)).thenReturn(systemVibrator);
        when(timerViewModel.isVibration()).thenReturn(true);
        mockAudioManager(AudioManager.RINGER_MODE_NORMAL);

        final Vibrator vibrator = new Vibrator(application, timerViewModel);
        vibrator.vibrate();

        verify(systemVibrator).vibrate(any(VibrationEffect.class));
    }

    @Test
    public void whenAudioManagerIsNullVibrateAnyway() {
        when(application.getSystemService(Context.VIBRATOR_SERVICE)).thenReturn(systemVibrator);
        when(timerViewModel.isVibration()).thenReturn(true);
        mockAudioManager(null);

        final Vibrator vibrator = new Vibrator(application, timerViewModel);
        vibrator.vibrate();

        verify(systemVibrator).vibrate(any(VibrationEffect.class));
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
