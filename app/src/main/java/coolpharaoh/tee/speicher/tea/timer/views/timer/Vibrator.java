package coolpharaoh.tee.speicher.tea.timer.views.timer;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import coolpharaoh.tee.speicher.tea.timer.viewmodels.TimerViewModel;

class Vibrator extends Service {

    private android.os.Vibrator vibrator = null;

    @Override
    public void onCreate() {
        super.onCreate();

        TimerViewModel timerViewModel = new TimerViewModel(getApplicationContext());
        if(timerViewModel.isVibration()) {
            vibrator = (android.os.Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            // Vibrate for 1000 milliseconds
            long[] twice = { 0, 500, 400, 500 };
            if (vibrator==null){
                throw new AssertionError("Vibrator is null.");
            } else {
                vibrator.vibrate(twice, -1);
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
