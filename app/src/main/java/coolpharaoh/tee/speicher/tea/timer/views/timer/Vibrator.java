package coolpharaoh.tee.speicher.tea.timer.views.timer;

import android.content.Context;

import coolpharaoh.tee.speicher.tea.timer.viewmodels.TimerViewModel;

class Vibrator {

    static void vibrate(Context context){
        TimerViewModel timerViewModel = new TimerViewModel(context);
        if(timerViewModel.isVibration()) {
            android.os.Vibrator vibrator = (android.os.Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            // Vibrate for 1000 milliseconds
            long[] twice = { 0, 500, 400, 500 };
            if (vibrator==null){
                throw new AssertionError("Vibrator is null.");
            } else {
                vibrator.vibrate(twice, -1);
            }
        }
    }
}
