package coolpharaoh.tee.speicher.tea.timer.views.showtea.countdowntimer;

import android.app.Application;
import android.content.Context;
import android.media.AudioManager;
import android.os.Build;
import android.os.VibrationEffect;
import android.util.Log;

class Vibrator {
    private static final String LOG_TAG = Vibrator.class.getSimpleName();

    private Vibrator() {
    }

    static void vibrate(Application application) {

        TimerViewModel timerViewModel = new TimerViewModel(application);

        if (!isSilent(application) && timerViewModel.isVibration()) {
            android.os.Vibrator vibrator = (android.os.Vibrator) application.getSystemService(Context.VIBRATOR_SERVICE);
            if (vibrator == null) {
                throw new AssertionError("Vibrator is null.");
            } else {
                // Vibrate for 1000 milliseconds
                long[] twice = {0, 500, 400, 500};
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createWaveform(twice, -1));
                } else {
                    vibrator.vibrate(twice, -1);
                }
            }
        }
    }

    private static boolean isSilent(Context context) {
        AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if (audio == null) {
            Log.w(LOG_TAG, "Cannot discover ringer mode because audio manager is not available");
            return false;
        } else {
            return audio.getRingerMode() == AudioManager.RINGER_MODE_SILENT;
        }
    }
}
