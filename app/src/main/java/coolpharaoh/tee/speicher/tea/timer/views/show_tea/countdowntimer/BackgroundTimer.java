package coolpharaoh.tee.speicher.tea.timer.views.show_tea.countdowntimer;

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
        final long wakeUpTime = sharedPreferences.getStartedTime() + time;
        final AlarmManager alarmManager = (AlarmManager) application.getSystemService(Context.ALARM_SERVICE);
        final Intent teaCompleteReceiver = new Intent(application, TeaCompleteReceiver.class);
        teaCompleteReceiver.putExtra("teaId", teaId);
        final PendingIntent sender = PendingIntent.getBroadcast(application, REQUEST_CODE, teaCompleteReceiver, PendingIntent.FLAG_IMMUTABLE);
        if (alarmManager != null) {
            alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(wakeUpTime, sender), sender);
        } else {
            throw new NullPointerException("AlarmManager is not available");
        }

    }

    void removeAlarmManager() {
        final Intent teaCompleteReceiver = new Intent(application, TeaCompleteReceiver.class);
        final PendingIntent sender = PendingIntent.getBroadcast(application, REQUEST_CODE, teaCompleteReceiver, PendingIntent.FLAG_IMMUTABLE);
        final AlarmManager alarmManager = (AlarmManager) application.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.cancel(sender);
        } else {
            throw new NullPointerException("AlarmManager is not available");
        }
    }
}
