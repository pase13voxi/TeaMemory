package coolpharaoh.tee.speicher.tea.timer.views.show_tea.countdowntimer;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import coolpharaoh.tee.speicher.tea.timer.core.system.CurrentSdk;

public class TeaCompleteReceiver extends BroadcastReceiver {

    // The SDK is checked but android studio doesn't recognize it.
    @SuppressLint("NewApi")
    @Override
    public void onReceive(Context context, Intent intent) {
        final long teaId = intent.getLongExtra("teaId", 0);
        Intent notificationService = new Intent(context, NotificationService.class);
        notificationService.putExtra("teaId", teaId);
        if (CurrentSdk.getSdkVersion() >= Build.VERSION_CODES.O) {
            context.startForegroundService(notificationService);
        } else {
            context.startService(notificationService);
        }
    }
}
