package coolpharaoh.tee.speicher.tea.timer.views.show_tea.countdowntimer;

import android.app.Application;
import android.content.Intent;

import coolpharaoh.tee.speicher.tea.timer.core.date.CurrentDate;

public class TimerController {
    public static final String COUNTDOWN_BR = "paseb.teeapp.teespeicher.countdown_br";
    private final Intent broadcastIntent = new Intent(COUNTDOWN_BR);

    //general
    private final Application application;
    private final ForegroundTimer foregroundTimer;
    private final SharedTimerPreferences sharedPreferences;
    private final BackgroundTimer backgroundTimer;

    //helper
    private long teaId;
    private long time = 0;
    private long timeToStart;
    private TimerState timerState = TimerState.STOPPED;

    public TimerController(final Application application, final SharedTimerPreferences sharedPreferences) {
        this(application, new DefaultForegroundTimer(), sharedPreferences,
                new BackgroundTimer(application, sharedPreferences));
    }

    public TimerController(final Application application, final ForegroundTimer foregroundTimer,
                           final SharedTimerPreferences sharedPreferences, final BackgroundTimer backgroundTimer) {
        this.application = application;
        this.foregroundTimer = foregroundTimer;
        this.sharedPreferences = sharedPreferences;
        this.backgroundTimer = backgroundTimer;
    }

    public void startForegroundTimer(final long time, final long teaId) {
        this.teaId = teaId;
        this.time = time;
        initTimer();
        startTimer();
        sharedPreferences.setStartedTime(getNow());
    }

    public void startBackgroundTimer() {
        if (timerState == TimerState.RUNNING) {
            foregroundTimer.cancel();
            backgroundTimer.setAlarmManager(teaId, time);
        }
    }

    public void resumeForegroundTimer() {
        initTimer();
        backgroundTimer.removeAlarmManager();
    }

    public void reset() {
        //cancel foregroundtimer
        foregroundTimer.cancel();
        sharedPreferences.setStartedTime(0);
        timerState = TimerState.STOPPED;
        //cancel Backgroundtimer
        backgroundTimer.removeAlarmManager();
        //stop music
        application.stopService(new Intent(application, NotificationService.class));
    }

    private void initTimer() {
        final long startTime = sharedPreferences.getStartedTime();
        if (startTime > 0) {
            timeToStart = (time - (getNow() - startTime));
            if (timeToStart <= 0) {
                showTimerFinished();
            } else {
                startTimer();
            }
        } else {
            timeToStart = time;
        }
    }

    private void startTimer() {
        foregroundTimer.start(this, timeToStart);
        timerState = TimerState.RUNNING;
    }

    void onTimerTick(final long millisUntilFinished) {
        broadcastIntent.putExtra("countdown", millisUntilFinished);
        broadcastIntent.putExtra("ready", false);
        application.sendBroadcast(broadcastIntent);
    }

    void onTimerFinish() {
        final Intent intent = new Intent();
        intent.putExtra("teaId", teaId);

        final TeaCompleteReceiver receiver = new TeaCompleteReceiver();
        receiver.onReceive(application, intent);

        showTimerFinished();
    }

    private void showTimerFinished() {
        timerState = TimerState.STOPPED;
        sharedPreferences.setStartedTime(0);

        broadcastIntent.putExtra("ready", true);
        application.sendBroadcast(broadcastIntent);
    }

    private long getNow() {
        return CurrentDate.getDate().getTime();
    }

    private enum TimerState {
        STOPPED,
        RUNNING
    }
}
