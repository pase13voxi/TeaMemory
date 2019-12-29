package coolpharaoh.tee.speicher.tea.timer.views.timer;

import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

import coolpharaoh.tee.speicher.tea.timer.viewmodels.TimerViewModel;

class Vibrate {

    static void vibrate(Context context) {
        TimerViewModel timerViewModel = new TimerViewModel(context);

        if (timerViewModel.isVibration()) {
            Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
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
}
