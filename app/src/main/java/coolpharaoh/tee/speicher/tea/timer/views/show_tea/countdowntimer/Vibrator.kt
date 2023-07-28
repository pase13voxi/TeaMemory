package coolpharaoh.tee.speicher.tea.timer.views.show_tea.countdowntimer;

import static android.os.Build.VERSION_CODES.S;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.media.AudioManager;
import android.os.VibrationEffect;
import android.os.VibratorManager;
import android.util.Log;

import coolpharaoh.tee.speicher.tea.timer.core.system.CurrentSdk;

class Vibrator {
    private static final String LOG_TAG = Vibrator.class.getSimpleName();

    private final Application application;
    private final TimerViewModel timerViewModel;

    Vibrator(final Application application) {
        this(application, new TimerViewModel(application));
    }

    Vibrator(final Application application, final TimerViewModel timerViewModel) {
        this.application = application;
        this.timerViewModel = timerViewModel;
    }

    void vibrate() {
        if (timerViewModel.isVibration() && !isSilent(application)) {
            getVibrator();

            final android.os.Vibrator vibrator = getVibrator();
            if (vibrator == null) {
                throw new AssertionError("Vibrator is null.");
            } else {
                // Vibrate for 1000 milliseconds
                final long[] twice = {0, 500, 400, 500};
                vibrator.vibrate(VibrationEffect.createWaveform(twice, -1));

                Log.i(LOG_TAG, "Vibration is triggered.");
            }
        }
    }

    @SuppressLint("NewApi")
    private android.os.Vibrator getVibrator() {
        final android.os.Vibrator vibrator;
        if (CurrentSdk.getSdkVersion() >= S) {
            final VibratorManager vibratorManager = (VibratorManager) application.getSystemService(Context.VIBRATOR_MANAGER_SERVICE);

            vibrator = vibratorManager.getDefaultVibrator();
        } else {
            vibrator = (android.os.Vibrator) application.getSystemService(Context.VIBRATOR_SERVICE);
        }

        return vibrator;
    }

    private boolean isSilent(final Application application) {
        final AudioManager audio = (AudioManager) application.getSystemService(Context.AUDIO_SERVICE);
        if (audio == null) {
            Log.w(LOG_TAG, "Cannot discover ringer mode because audio manager is not available");
            return false;
        } else {
            return audio.getRingerMode() == AudioManager.RINGER_MODE_SILENT;
        }
    }
}
