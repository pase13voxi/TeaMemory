package coolpharaoh.tee.speicher.tea.timer.views.show_tea.countdowntimer;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class TeaCompleteReceiver extends BroadcastReceiver {

    // The SDK is checked but android studio doesn't recognize it.
    @SuppressLint("NewApi")
    @Override
    public void onReceive(final Context context, final Intent intent) {
        final long teaId = intent.getLongExtra("teaId", 0);
        final Intent notificationService = new Intent(context, NotificationService.class);
        notificationService.putExtra("teaId", teaId);
        context.startForegroundService(notificationService);
    }
}
