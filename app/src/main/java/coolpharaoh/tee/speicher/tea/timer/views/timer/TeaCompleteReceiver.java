package coolpharaoh.tee.speicher.tea.timer.views.timer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class TeaCompleteReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        final long teaId = intent.getLongExtra("teaName", 0);
        Notifier.notify(context, teaId);
        context.startService(new Intent(context, VibrationService.class));
        context.startService(new Intent(context, MediaService.class));
    }
}
