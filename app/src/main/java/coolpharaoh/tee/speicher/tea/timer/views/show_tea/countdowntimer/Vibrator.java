package coolpharaoh.tee.speicher.tea.timer.views.show_tea.countdowntimer;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.media.AudioManager;
import android.os.Build;
import android.os.VibrationEffect;
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

    //deprecated vibrate can be removed once the minimum sdk is greater that 25
    @SuppressWarnings("java:S1874")
    // The SDK is checked but android studio doesn't recognize it.
    @SuppressLint("NewApi")
    void vibrate() {
        if (timerViewModel.isVibration() && !isSilent(application)) {
            final android.os.Vibrator vibrator = (android.os.Vibrator) application.getSystemService(Context.VIBRATOR_SERVICE);
            if (vibrator == null) {
                throw new AssertionError("Vibrator is null.");
            } else {
                // Vibrate for 1000 milliseconds
                long[] twice = {0, 500, 400, 500};
                if (CurrentSdk.getSdkVersion() >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createWaveform(twice, -1));
                } else {
                    vibrator.vibrate(twice, -1);
                }
            }
        }
    }

    private boolean isSilent(final Application application) {
        AudioManager audio = (AudioManager) application.getSystemService(Context.AUDIO_SERVICE);
        if (audio == null) {
            Log.w(LOG_TAG, "Cannot discover ringer mode because audio manager is not available");
            return false;
        } else {
            return audio.getRingerMode() == AudioManager.RINGER_MODE_SILENT;
        }
    }
}
