package coolpharaoh.tee.speicher.tea.timer.views.showtea.countdowntimer;

import android.annotation.SuppressLint;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.core.system.CurrentSdk;
import coolpharaoh.tee.speicher.tea.timer.views.showtea.ShowTea;

import static android.content.Context.NOTIFICATION_SERVICE;

class Notifier {

    private static final String CHANNEL_ID_NOTIFY = "3422";

    private final Application application;
    private final TimerViewModel timerViewModel;
    private final long teaId;

    Notifier(final Application application, final long teaId) {
        this(application, teaId, new TimerViewModel(application));
    }

    Notifier(final Application application, final long teaId, final TimerViewModel timerViewModel) {
        this.application = application;
        this.teaId = teaId;
        this.timerViewModel = timerViewModel;
    }

    // The SDK is checked but android studio doesn't recognize it.
    @SuppressLint("NewApi")
    android.app.Notification getNotification() {
        if (CurrentSdk.getSdkVersion() >= Build.VERSION_CODES.O) {
            createChannel();
            return getNotificationAfterAndroidO();
        } else {
            return getNotificationBeforeAndroidO();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createChannel() {
        final String name = application.getString(R.string.showtea_channel_name);
        final String description = application.getString(R.string.showtea_channel_description);
        final int importance = NotificationManager.IMPORTANCE_HIGH;
        final NotificationChannel channel = new NotificationChannel(CHANNEL_ID_NOTIFY, name, importance);
        channel.setDescription(description);
        channel.setSound(null, null);

        final NotificationManager notificationManager = (NotificationManager) application.getSystemService(NOTIFICATION_SERVICE);
        if (notificationManager == null) {
            throw new AssertionError("NotificationManager is null.");
        } else {
            notificationManager.createNotificationChannel(channel);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private android.app.Notification getNotificationAfterAndroidO() {
        final android.app.Notification.Builder notification = new android.app.Notification.Builder(application, CHANNEL_ID_NOTIFY)
                .setTicker(application.getString(R.string.notification_ticker))
                .setContentTitle(application.getString(R.string.notification_title))
                .setContentText(timerViewModel.getName(teaId))
                .setSmallIcon(R.drawable.notification)
                .setContentIntent(createPendingIntent())
                .setAutoCancel(true);
        return notification.build();
    }

    //deprecated builder can be removed once the minimum sdk is greater that 25
    @SuppressWarnings("java:S1874")
    private android.app.Notification getNotificationBeforeAndroidO() {
        final android.app.Notification.Builder notification = new android.app.Notification.Builder(application)
                .setTicker(application.getString(R.string.notification_ticker))
                .setContentTitle(application.getString(R.string.notification_title))
                .setContentText(timerViewModel.getName(teaId))
                .setSmallIcon(R.drawable.notification)
                .setContentIntent(createPendingIntent())
                .setAutoCancel(true);
        return notification.build();
    }

    private PendingIntent createPendingIntent() {
        //Back to the Showtea Intent
        final Intent intentShowtea = new Intent(application, ShowTea.class);
        intentShowtea.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        return PendingIntent.getActivity(application, 0, intentShowtea, 0);
    }
}
