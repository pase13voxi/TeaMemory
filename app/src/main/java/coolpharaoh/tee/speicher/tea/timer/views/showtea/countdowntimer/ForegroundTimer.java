package coolpharaoh.tee.speicher.tea.timer.views.showtea.countdowntimer;

interface ForegroundTimer {
    void start(TimerController timerController, long millisUntilFinished);

    void cancel();
}
