package coolpharaoh.tee.speicher.tea.timer.views.timer;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.viewmodels.TimerViewModel;
import coolpharaoh.tee.speicher.tea.timer.views.ShowTea;

import static android.content.Context.NOTIFICATION_SERVICE;

class Notifier {

    private static final String CHANNEL_ID_NOTIFY = "3422";

    static void notify(Context context, final long teaId) {
            TimerViewModel timerViewModel = new TimerViewModel(context);
        if (timerViewModel.isNotification()) {
            //Back to the Showtea Intent
            Intent intent_showtea = new Intent(context, ShowTea.class);
            intent_showtea.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

            PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent_showtea, 0);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                String name = context.getString(R.string.showtea_channel_name);
                String description = context.getString(R.string.showtea_channel_description);
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID_NOTIFY, name, importance);
                channel.setDescription(description);
                channel.setSound(null, null);

                // Build notification
                Notification.Builder notification = new Notification.Builder(context, CHANNEL_ID_NOTIFY)
                        .setTicker(context.getString(R.string.notification_ticker))
                        .setContentTitle(context.getString(R.string.notification_title))
                        .setContentText(timerViewModel.getName(teaId))
                        .setSmallIcon(R.drawable.iconnotification)
                        .setContentIntent(pIntent)
                        .setAutoCancel(true);
                // hide the notification after its selected

                NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
                if (notificationManager == null) {
                    throw new AssertionError("NotificationManager is null.");
                } else {
                    notificationManager.createNotificationChannel(channel);
                    notificationManager.notify(0, notification.build());
                }
            } else {
                // Build notification
                Notification.Builder notification = new Notification.Builder(context)
                        .setTicker(context.getString(R.string.notification_ticker))
                        .setContentTitle(context.getString(R.string.notification_title))
                        .setContentText(timerViewModel.getName(teaId))
                        .setSmallIcon(R.drawable.iconnotification)
                        .setContentIntent(pIntent)
                        .setAutoCancel(true);
                // hide the notification after its selected

                NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
                if (notificationManager == null) {
                    throw new AssertionError("NotificationManager is null.");
                } else {
                    notificationManager.notify(0, notification.build());
                }
            }

        }
    }
}
