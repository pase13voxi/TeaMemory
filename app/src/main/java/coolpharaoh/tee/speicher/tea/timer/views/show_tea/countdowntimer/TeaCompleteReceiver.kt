package coolpharaoh.tee.speicher.tea.timer.views.show_tea.countdowntimer

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class TeaCompleteReceiver : BroadcastReceiver() {
    // The SDK is checked but android studio doesn't recognize it.
    @SuppressLint("NewApi")
    override fun onReceive(context: Context, intent: Intent) {
        val teaId = intent.getLongExtra("teaId", 0)
        val notificationService = Intent(context, NotificationService::class.java)
        notificationService.putExtra("teaId", teaId)
        context.startForegroundService(notificationService)
    }
}