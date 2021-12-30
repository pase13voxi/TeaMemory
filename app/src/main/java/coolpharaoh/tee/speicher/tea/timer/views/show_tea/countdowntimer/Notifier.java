package coolpharaoh.tee.speicher.tea.timer.views.show_tea.countdowntimer;

import static android.content.Context.NOTIFICATION_SERVICE;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.views.show_tea.ShowTea;

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

    android.app.Notification getNotification() {
        createChannel();

        final android.app.Notification.Builder notification = new android.app.Notification.Builder(application, CHANNEL_ID_NOTIFY)
                .setTicker(application.getString(R.string.show_tea_notification_ticker))
                .setContentTitle(application.getString(R.string.show_tea_notification_title))
                .setContentText(timerViewModel.getName(teaId))
                .setSmallIcon(R.drawable.notification_white)
                .setContentIntent(createPendingIntent())
                .setAutoCancel(true);
        return notification.build();
    }

    private void createChannel() {
        final String name = application.getString(R.string.show_tea_channel_name);
        final String description = application.getString(R.string.show_tea_channel_description);
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

    private PendingIntent createPendingIntent() {
        //Back to the ShowTea Intent
        final Intent intentShowTea = new Intent(application, ShowTea.class);
        intentShowTea.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        return PendingIntent.getActivity(application, 0, intentShowTea, PendingIntent.FLAG_IMMUTABLE);
    }
}
