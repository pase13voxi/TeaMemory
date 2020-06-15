package coolpharaoh.tee.speicher.tea.timer.views.showtea.timer;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class NotificationService extends Service {
    private static final int NOTIFICATION_ID = 7684;
    AudioPlayer audioPlayer = null;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags , startId);

        startForeground(NOTIFICATION_ID, getNotification(intent.getLongExtra("teaId", 0)));
        startAudioPlayer();
        Vibrator.vibrate(getApplicationContext());

        return START_REDELIVER_INTENT;
    }

    private Notification getNotification(final long teaId){
        Notifier notifier = new Notifier(getApplicationContext(), teaId);
        return notifier.getNotification();
    }

    private void startAudioPlayer(){
        audioPlayer = new AudioPlayer(getApplicationContext());
        audioPlayer.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        audioPlayer.reset();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (notificationManager == null) {
            throw new AssertionError("NotificationManager is null.");
        } else {
            notificationManager.cancelAll();
        }
    }
}
