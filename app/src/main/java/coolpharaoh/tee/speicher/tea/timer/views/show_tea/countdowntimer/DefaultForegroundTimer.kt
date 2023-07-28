package coolpharaoh.tee.speicher.tea.timer.views.show_tea.countdowntimer;

import android.os.CountDownTimer;

class DefaultForegroundTimer implements ForegroundTimer {

    private CountDownTimer countDownTimer;

    @Override
    public void start(final TimerController timerController, final long millisUntilFinished) {
        countDownTimer = new CountDownTimer(millisUntilFinished, 1000) {

            @Override
            public void onTick(final long millisUntilFinished) {
                timerController.onTimerTick(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                timerController.onTimerFinish();
            }
        }.start();
    }

    @Override
    public void cancel() {
        if (countDownTimer != null)
            countDownTimer.cancel();
    }
}
