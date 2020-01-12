package coolpharaoh.tee.speicher.tea.timer.views.timer;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.models.database.TeaMemoryDatabase;
import coolpharaoh.tee.speicher.tea.timer.viewmodels.TimerViewModel;
import coolpharaoh.tee.speicher.tea.timer.views.ShowTea;

import static android.content.Context.NOTIFICATION_SERVICE;

class Notifier {

    private static final String CHANNEL_ID_NOTIFY = "3422";

    private final Context context;
    private final TimerViewModel timerViewModel;
    private final long teaId;

    Notifier(final Context context, final long teaId){
        this.context = context;
        this.teaId = teaId;
        timerViewModel = new TimerViewModel(TeaMemoryDatabase.getDatabaseInstance(context));
    }

    android.app.Notification getNotification() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createChannel();
                return getNotificationAfterAndroidO();
            } else {
                return getNotificationBeforeAndroidO();
            }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createChannel(){
        String name = context.getString(R.string.showtea_channel_name);
        String description = context.getString(R.string.showtea_channel_description);
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID_NOTIFY, name, importance);
        channel.setDescription(description);
        channel.setSound(null, null);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        if (notificationManager == null) {
            throw new AssertionError("NotificationManager is null.");
        } else {
            notificationManager.createNotificationChannel(channel);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private android.app.Notification getNotificationAfterAndroidO(){
        android.app.Notification.Builder notification = new android.app.Notification.Builder(context, CHANNEL_ID_NOTIFY)
                .setTicker(context.getString(R.string.notification_ticker))
                .setContentTitle(context.getString(R.string.notification_title))
                .setContentText(timerViewModel.getName(teaId))
                .setSmallIcon(R.drawable.iconnotification)
                .setContentIntent(createPendingIntent())
                .setAutoCancel(true);
        return notification.build();
    }

    private android.app.Notification getNotificationBeforeAndroidO(){
        android.app.Notification.Builder notification = new android.app.Notification.Builder(context)
                .setTicker(context.getString(R.string.notification_ticker))
                .setContentTitle(context.getString(R.string.notification_title))
                .setContentText(timerViewModel.getName(teaId))
                .setSmallIcon(R.drawable.iconnotification)
                .setContentIntent(createPendingIntent())
                .setAutoCancel(true);
        return notification.build();
    }

    private PendingIntent createPendingIntent(){
        //Back to the Showtea Intent
        Intent intent_showtea = new Intent(context, ShowTea.class);
        intent_showtea.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        return PendingIntent.getActivity(context, 0, intent_showtea, 0);
    }
}
