package coolpharaoh.tee.speicher.tea.timer.views.show_tea.countdowntimer

import android.os.CountDownTimer

class DefaultForegroundTimer : ForegroundTimer {

    private var countDownTimer: CountDownTimer? = null

    override fun start(timerController: TimerController, millisUntilFinished: Long) {
        countDownTimer = object : CountDownTimer(millisUntilFinished, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                timerController.onTimerTick(millisUntilFinished)
            }

            override fun onFinish() {
                timerController.onTimerFinish()
            }
        }.start()
    }

    override fun cancel() {
        if (countDownTimer != null) countDownTimer!!.cancel()
    }
}