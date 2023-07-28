package coolpharaoh.tee.speicher.tea.timer.views.show_tea.countdowntimer

import android.app.Application
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import java.io.IOException

class AudioPlayer
@JvmOverloads constructor(
    private val application: Application,
    private val timerViewModel: TimerViewModel = TimerViewModel(application),
    private val mediaPlayer: MediaPlayer = MediaPlayer()
) {
    fun start() {
        //initial music track
        if (timerViewModel.musicChoice != null) {
            val uri = Uri.parse(timerViewModel.musicChoice)
            try {
                mediaPlayer.setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                        .build()
                )
                mediaPlayer.setDataSource(application, uri)
                mediaPlayer.prepare()
                mediaPlayer.start()
            } catch (e: IOException) {
                Log.e(LOG_TAG, "Something went wrong when start playing the Ringtone.", e)
            }
        }
    }

    fun reset() {
        mediaPlayer.release()
    }

    companion object {
        private val LOG_TAG = AudioPlayer::class.java.simpleName
    }
}