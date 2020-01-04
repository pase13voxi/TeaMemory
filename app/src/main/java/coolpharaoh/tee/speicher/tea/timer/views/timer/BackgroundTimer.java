package coolpharaoh.tee.speicher.tea.timer.views.timer;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

class BackgroundTimer {
    private final int REQUEST_CODE = 4356;

    private final Context context;
    private final SharedTimerPreferences sharedPreferences;

    BackgroundTimer(final Context context, final SharedTimerPreferences sharedPreferences) {
        this.context = context;
        this.sharedPreferences = sharedPreferences;
    }

    void setAlarmManager(final long teaId, final long time) {
        long wakeUpTime = sharedPreferences.getStartedTime() + time;
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent teaCompleteReceiver = new Intent(context, TeaCompleteReceiver.class);
        teaCompleteReceiver.putExtra("teaId", teaId);
        PendingIntent sender = PendingIntent.getBroadcast(context, REQUEST_CODE, teaCompleteReceiver, PendingIntent.FLAG_CANCEL_CURRENT);
        if (alarmManager != null) {
            alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(wakeUpTime, sender), sender);
        } else {
            throw new NullPointerException("AlarmManager is not available");
        }

    }

    void removeAlarmManager() {
        Intent teaCompleteReceiver = new Intent(context, TeaCompleteReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, REQUEST_CODE, teaCompleteReceiver, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.cancel(sender);
        } else {
            throw new NullPointerException("AlarmManager is not available");
        }
    }
}
