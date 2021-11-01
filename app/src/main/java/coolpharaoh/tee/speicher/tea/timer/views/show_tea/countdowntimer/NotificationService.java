package coolpharaoh.tee.speicher.tea.timer.views.show_tea.countdowntimer;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class NotificationService extends Service {
    private static final int NOTIFICATION_ID = 7684;
    AudioPlayer audioPlayer = null;
    Vibrator vibrator = null;
    Notifier notifier = null;

    @Nullable
    @Override
    public IBinder onBind(final Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(final Intent intent, final int flags, final int startId) {
        super.onStartCommand(intent, flags, startId);

        showNotification(intent);
        startAlarm();
        vibrate();

        return START_REDELIVER_INTENT;
    }

    private void showNotification(final Intent intent) {
        notifier = new Notifier(getApplication(), intent.getLongExtra("teaId", 0));
        startForeground(NOTIFICATION_ID, notifier.getNotification());
    }

    private void startAlarm() {
        audioPlayer = new AudioPlayer(getApplication());
        audioPlayer.start();
    }

    private void vibrate() {
        vibrator = new Vibrator(getApplication());
        vibrator.vibrate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        audioPlayer.reset();

        final NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (notificationManager == null) {
            throw new AssertionError("NotificationManager is null.");
        } else {
            notificationManager.cancelAll();
        }
    }
}
