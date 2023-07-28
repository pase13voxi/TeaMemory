package coolpharaoh.tee.speicher.tea.timer.views.show_tea.countdowntimer

import android.app.AlarmManager
import android.app.AlarmManager.AlarmClockInfo
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import coolpharaoh.tee.speicher.tea.timer.core.system.CurrentSdk.sdkVersion

class BackgroundTimer(private val application: Application, private val sharedPreferences: SharedTimerPreferences) {

    fun setAlarmManager(teaId: Long, time: Long) {
        val wakeUpTime = sharedPreferences.startedTime + time
        val alarmManager = application.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val teaCompleteReceiver = Intent(application, TeaCompleteReceiver::class.java)
        teaCompleteReceiver.putExtra("teaId", teaId)
        val sender = getSender(teaCompleteReceiver)
        alarmManager.setAlarmClock(AlarmClockInfo(wakeUpTime, sender), sender)
    }

    fun removeAlarmManager() {
        val teaCompleteReceiver = Intent(application, TeaCompleteReceiver::class.java)
        val sender = getSender(teaCompleteReceiver)
        val alarmManager = application.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(sender)
    }

    fun getSender(teaCompleteReceiver: Intent?): PendingIntent {
        return if (sdkVersion >= Build.VERSION_CODES.S) {
            PendingIntent.getBroadcast(application, REQUEST_CODE, teaCompleteReceiver!!,PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE)
        } else {
            PendingIntent.getBroadcast(application, REQUEST_CODE, teaCompleteReceiver!!, PendingIntent.FLAG_UPDATE_CURRENT)
        }
    }

    companion object {
        private const val REQUEST_CODE = 4356
    }
}