package coolpharaoh.tee.speicher.tea.timer.views.showtea.timer;

import android.content.Context;
import android.media.AudioManager;
import android.os.Build;
import android.os.VibrationEffect;

import coolpharaoh.tee.speicher.tea.timer.models.database.TeaMemoryDatabase;

class Vibrator {

    static void vibrate(Context context) {

        TimerViewModel timerViewModel = new TimerViewModel(TeaMemoryDatabase.getDatabaseInstance(context));

        if(!isSilent(context) && timerViewModel.isVibration()) {
            android.os.Vibrator vibrator = (android.os.Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
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

    private static boolean isSilent(Context context){
        AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        try {
            // TODO May Produce a NUllpointer Exception
            return audio.getRingerMode() == AudioManager.RINGER_MODE_SILENT;
        } catch (NullPointerException e){
            return false;
        }
    }
}
