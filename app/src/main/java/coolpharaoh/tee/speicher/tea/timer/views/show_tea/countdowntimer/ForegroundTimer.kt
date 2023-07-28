package coolpharaoh.tee.speicher.tea.timer.views.show_tea.countdowntimer

interface ForegroundTimer {

    fun start(timerController: TimerController, millisUntilFinished: Long)

    fun cancel()
}