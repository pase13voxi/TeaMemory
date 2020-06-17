package coolpharaoh.tee.speicher.tea.timer.views.showtea.timer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class TeaCompleteReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        final long teaId = intent.getLongExtra("teaId", 0);
        Intent notificationService = new Intent(context, NotificationService.class);
        notificationService.putExtra("teaId", teaId);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(notificationService);
        } else {
            context.startService(notificationService);
        }
    }
}
