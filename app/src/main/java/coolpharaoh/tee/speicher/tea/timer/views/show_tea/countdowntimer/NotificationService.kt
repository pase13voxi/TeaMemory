package coolpharaoh.tee.speicher.tea.timer.views.show_tea.countdowntimer

import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
import android.os.Build
import android.os.IBinder

class NotificationService : Service() {
    @JvmField
    var audioPlayer: AudioPlayer? = null

    @JvmField
    var vibrator: Vibrator? = null

    @JvmField
    var notifier: Notifier? = null

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        showNotification(intent)
        startAlarm()
        vibrate()

        return START_REDELIVER_INTENT
    }

    private fun showNotification(intent: Intent) {
        notifier = Notifier(application, intent.getLongExtra("teaId", 0))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            startForeground(NOTIFICATION_ID, notifier!!.notification, FOREGROUND_SERVICE_TYPE_SPECIAL_USE)
        } else {
            startForeground(NOTIFICATION_ID, notifier!!.notification)
        }
    }

    private fun startAlarm() {
        audioPlayer = AudioPlayer(application)
        audioPlayer!!.start()
    }

    private fun vibrate() {
        vibrator = Vibrator(application)
        vibrator!!.vibrate()
    }

    override fun onDestroy() {
        super.onDestroy()

        audioPlayer!!.reset()

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll()
    }

    companion object {
        private const val NOTIFICATION_ID = 7684
    }
}