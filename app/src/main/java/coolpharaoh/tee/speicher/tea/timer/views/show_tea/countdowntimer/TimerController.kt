package coolpharaoh.tee.speicher.tea.timer.views.show_tea.countdowntimer

import android.app.Application
import android.content.Intent
import coolpharaoh.tee.speicher.tea.timer.core.date.CurrentDate.getDate

class TimerController(
    private val application: Application, private val foregroundTimer: ForegroundTimer,
    private val sharedPreferences: SharedTimerPreferences, private val backgroundTimer: BackgroundTimer
) {

    private val broadcastIntent = Intent(COUNTDOWN_BR)

    //helper
    private var teaId: Long = 0
    private var time: Long = 0
    private var timeToStart: Long = 0
    private var timerState = TimerState.STOPPED

    constructor(application: Application, sharedPreferences: SharedTimerPreferences) : this(
        application, DefaultForegroundTimer(), sharedPreferences,
        BackgroundTimer(application, sharedPreferences)
    )

    fun startForegroundTimer(time: Long, teaId: Long) {
        this.teaId = teaId
        this.time = time
        initTimer()
        startTimer()
        sharedPreferences.startedTime = now
    }

    fun startBackgroundTimer() {
        if (timerState == TimerState.RUNNING) {
            foregroundTimer.cancel()
            backgroundTimer.setAlarmManager(teaId, time)
        }
    }

    fun resumeForegroundTimer() {
        initTimer()
        backgroundTimer.removeAlarmManager()
    }

    fun reset() {
        //cancel foregroundtimer
        foregroundTimer.cancel()
        sharedPreferences.startedTime = 0
        timerState = TimerState.STOPPED
        //cancel Backgroundtimer
        backgroundTimer.removeAlarmManager()
        //stop music
        application.stopService(Intent(application, NotificationService::class.java))
    }

    private fun initTimer() {
        val startTime = sharedPreferences.startedTime
        if (startTime > 0) {
            timeToStart = time - (now - startTime)
            if (timeToStart <= 0) {
                showTimerFinished()
            } else {
                startTimer()
            }
        } else {
            timeToStart = time
        }
    }

    private fun startTimer() {
        foregroundTimer.start(this, timeToStart)
        timerState = TimerState.RUNNING
    }

    fun onTimerTick(millisUntilFinished: Long) {
        broadcastIntent.putExtra("countdown", millisUntilFinished)
        broadcastIntent.putExtra("ready", false)
        broadcastIntent.setPackage(application.packageName)
        application.sendBroadcast(broadcastIntent)
    }

    fun onTimerFinish() {
        val intent = Intent()
        intent.putExtra("teaId", teaId)

        val receiver = TeaCompleteReceiver()
        receiver.onReceive(application, intent)

        showTimerFinished()
    }

    private fun showTimerFinished() {
        timerState = TimerState.STOPPED
        sharedPreferences.startedTime = 0

        broadcastIntent.putExtra("ready", true)
        application.sendBroadcast(broadcastIntent)
    }

    private val now: Long
        get() = getDate().time

    private enum class TimerState {
        STOPPED, RUNNING
    }

    companion object {
        const val COUNTDOWN_BR = "paseb.teeapp.teespeicher.countdown_br"
    }
}