package coolpharaoh.tee.speicher.tea.timer.views.timer;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;

import java.util.Calendar;

public class ForegroundTimer {
    public static final String COUNTDOWN_BR = "paseb.teeapp.teespeicher.countdown_br";

    //general
    private final Context context;
    private final SharedTimerPreferences sharedPreferences;
    private final BackgroundTimer backgroundTimer;

    private CountDownTimer countDownTimer;
    private final Intent broadcastIntent = new Intent(COUNTDOWN_BR);

    //helper
    private long teaId;
    private long time = 0;
    private long timeToStart;
    private TimerState timerState = TimerState.STOPPED;

    public ForegroundTimer(final Context context) {
        this.context = context;
        sharedPreferences = new SharedTimerPreferences(context);
        backgroundTimer = new BackgroundTimer(context, sharedPreferences);
    }

    public void startForegroundTimer(long time, long teaId) {
        this.teaId = teaId;
        this.time = time;
        initTimer();
        startTimer();
        sharedPreferences.setStartedTime(getNow());
    }

    public void startBackgroundTimer() {
        if (timerState == TimerState.RUNNING) {
            countDownTimer.cancel();
            backgroundTimer.setAlarmManager(teaId, time);
        }
    }

    public void resumeForegroundTimer() {
        initTimer();
        backgroundTimer.removeAlarmManager();
    }

    public void reset() {
        //cancle foregroundtimer
        if (countDownTimer != null)
            countDownTimer.cancel();
        sharedPreferences.setStartedTime(0);
        timerState = TimerState.STOPPED;
        //cancle Backgroundtimer
        backgroundTimer.removeAlarmManager();
        //stop music
        context.stopService(new Intent(context, NotificationService.class));
    }

    private void initTimer() {
        long startTime = sharedPreferences.getStartedTime();
        if (startTime > 0) {
            timeToStart = (time - (getNow() - startTime));
            if (timeToStart <= 0) {
                onTimerFinish();
            } else {
                startTimer();
            }
        } else {
            timeToStart = time;
        }
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(timeToStart, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                broadcastIntent.putExtra("countdown", millisUntilFinished);
                broadcastIntent.putExtra("ready", false);
                context.sendBroadcast(broadcastIntent);
            }

            @Override
            public void onFinish() {
                startNotificationService();
                onTimerFinish();
            }
        }.start();
        timerState = TimerState.RUNNING;
    }

    private void startNotificationService(){
        Intent intent = new Intent();
        intent.putExtra("teaId", teaId);

        TeaCompleteReceiver receiver = new TeaCompleteReceiver();
        receiver.onReceive(context, intent);
    }

    private void onTimerFinish() {
        timerState = TimerState.STOPPED;
        sharedPreferences.setStartedTime(0);

        broadcastIntent.putExtra("ready", true);
        context.sendBroadcast(broadcastIntent);
    }

    private long getNow() {
        Calendar rightNow = Calendar.getInstance();
        return rightNow.getTimeInMillis();
    }

    private enum TimerState {
        STOPPED,
        RUNNING
    }
}
