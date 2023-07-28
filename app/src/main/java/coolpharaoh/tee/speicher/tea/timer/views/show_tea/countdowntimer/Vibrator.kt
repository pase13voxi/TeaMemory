package coolpharaoh.tee.speicher.tea.timer.views.show_tea.countdowntimer

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.media.AudioManager
import android.os.Build.VERSION_CODES
import android.os.VibrationEffect
import android.os.VibratorManager
import android.util.Log
import coolpharaoh.tee.speicher.tea.timer.core.system.CurrentSdk.sdkVersion

class Vibrator
@JvmOverloads
constructor(
    private val application: Application,
    private val timerViewModel: TimerViewModel = TimerViewModel(application)
) {

    fun vibrate() {
        if (timerViewModel.isVibration && !isSilent(application)) {
            val vibrator = vibrator
            // Vibrate for 1000 milliseconds
            val twice = longArrayOf(0, 500, 400, 500)
            vibrator.vibrate(VibrationEffect.createWaveform(twice, -1))
            Log.i(LOG_TAG, "Vibration is triggered.")
        }
    }

    @get:SuppressLint("NewApi")
    private val vibrator: android.os.Vibrator
        get() {
            val vibrator: android.os.Vibrator = if (sdkVersion >= VERSION_CODES.S) {
                val vibratorManager = application.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                vibratorManager.defaultVibrator
            } else {
                application.getSystemService(Context.VIBRATOR_SERVICE) as android.os.Vibrator
            }
            return vibrator
        }

    private fun isSilent(application: Application): Boolean {
        val audio = application.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        return audio.ringerMode == AudioManager.RINGER_MODE_SILENT
    }

    companion object {
        private val LOG_TAG = Vibrator::class.java.simpleName
    }
}