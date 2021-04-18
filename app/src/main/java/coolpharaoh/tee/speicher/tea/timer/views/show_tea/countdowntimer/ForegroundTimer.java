package coolpharaoh.tee.speicher.tea.timer.views.show_tea.countdowntimer;

interface ForegroundTimer {
    void start(TimerController timerController, long millisUntilFinished);

    void cancel();
}
