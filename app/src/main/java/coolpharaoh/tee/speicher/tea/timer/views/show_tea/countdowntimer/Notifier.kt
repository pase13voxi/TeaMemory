package coolpharaoh.tee.speicher.tea.timer.views.show_tea.countdowntimer

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import coolpharaoh.tee.speicher.tea.timer.R
import coolpharaoh.tee.speicher.tea.timer.views.show_tea.ShowTea

class Notifier
@JvmOverloads constructor(
    private val application: Application,
    private val teaId: Long,
    private val timerViewModel: TimerViewModel = TimerViewModel(application)
) {
    val notification: Notification
        get() {
            createChannel()

            val notification = Notification.Builder(application, CHANNEL_ID_NOTIFY)
                .setTicker(application.getString(R.string.show_tea_notification_ticker))
                .setContentTitle(application.getString(R.string.show_tea_notification_title))
                .setContentText(timerViewModel.getName(teaId))
                .setSmallIcon(R.drawable.notification_white)
                .setContentIntent(createPendingIntent())
                .setAutoCancel(true)
            return notification.build()
        }

    private fun createChannel() {
        val name = application.getString(R.string.show_tea_channel_name)
        val description = application.getString(R.string.show_tea_channel_description)
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(CHANNEL_ID_NOTIFY, name, importance)
        channel.description = description
        channel.setSound(null, null)

        val notificationManager = application.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun createPendingIntent(): PendingIntent {
        //Back to the ShowTea Intent
        val intentShowTea = Intent(application, ShowTea::class.java)
        intentShowTea.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP

        return PendingIntent.getActivity(application, 0, intentShowTea, PendingIntent.FLAG_IMMUTABLE)
    }

    companion object {
        private const val CHANNEL_ID_NOTIFY = "3422"
    }
}