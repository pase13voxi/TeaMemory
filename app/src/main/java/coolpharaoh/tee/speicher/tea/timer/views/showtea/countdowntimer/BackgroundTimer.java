package coolpharaoh.tee.speicher.tea.timer.views.showtea.countdowntimer;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

class BackgroundTimer {
    private static final int REQUEST_CODE = 4356;

    private final Application application;
    private final SharedTimerPreferences sharedPreferences;

    BackgroundTimer(final Application application, final SharedTimerPreferences sharedPreferences) {
        this.application = application;
        this.sharedPreferences = sharedPreferences;
    }

    void setAlarmManager(final long teaId, final long time) {
        long wakeUpTime = sharedPreferences.getStartedTime() + time;
        AlarmManager alarmManager = (AlarmManager) application.getSystemService(Context.ALARM_SERVICE);
        Intent teaCompleteReceiver = new Intent(application, TeaCompleteReceiver.class);
        teaCompleteReceiver.putExtra("teaId", teaId);
        PendingIntent sender = PendingIntent.getBroadcast(application, REQUEST_CODE, teaCompleteReceiver, PendingIntent.FLAG_CANCEL_CURRENT);
        if (alarmManager != null) {
            alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(wakeUpTime, sender), sender);
        } else {
            throw new NullPointerException("AlarmManager is not available");
        }

    }

    void removeAlarmManager() {
        Intent teaCompleteReceiver = new Intent(application, TeaCompleteReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(application, REQUEST_CODE, teaCompleteReceiver, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager) application.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.cancel(sender);
        } else {
            throw new NullPointerException("AlarmManager is not available");
        }
    }
}
