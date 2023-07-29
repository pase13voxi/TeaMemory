package coolpharaoh.tee.speicher.tea.timer.views.show_tea.countdowntimer

import android.app.Application
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnit
import org.robolectric.RobolectricTestRunner
import java.io.IOException

@RunWith(RobolectricTestRunner::class)
class AudioPlayerTest {
    @JvmField
    @Rule
    var rule = MockitoJUnit.rule()

    @Mock
    lateinit var timerViewModel: TimerViewModel

    @Mock
    lateinit var mediaPlayer: MediaPlayer

    @Test
    @Throws(IOException::class)
    fun startAudioPlayer() {
        val application = Application()
        val musicChoice = "/music/choice"
        val uri = Uri.parse(musicChoice)
        `when`(timerViewModel.musicChoice).thenReturn(musicChoice)

        val audioPlayer = AudioPlayer(application, timerViewModel, mediaPlayer)
        audioPlayer.start()

        verify(mediaPlayer).setAudioAttributes(ArgumentMatchers.any(AudioAttributes::class.java))
        verify(mediaPlayer).setDataSource(application, uri)
        verify(mediaPlayer).prepare()
        verify(mediaPlayer).start()
    }

    @Test
    fun startAudioPlayerButDoNothingWhenMusicChoiceIsNull() {
        `when`(timerViewModel.musicChoice).thenReturn(null)

        val audioPlayer = AudioPlayer(Application(), timerViewModel, mediaPlayer)
        audioPlayer.start()

        verify(mediaPlayer, times(0)).start()
    }

    @Test
    @Throws(IOException::class)
    fun startAudioPlayerButDoNothingWhenIOExceptionIsThrown() {
        val musicChoice = "/music/choice"
        `when`(timerViewModel.musicChoice).thenReturn(musicChoice)

        doThrow(IOException::class.java).`when`(mediaPlayer).setDataSource(ArgumentMatchers.any(), ArgumentMatchers.any())

        val audioPlayer = AudioPlayer(Application(), timerViewModel, mediaPlayer)
        audioPlayer.start()

        verify(mediaPlayer, times(0)).start()
    }

    @Test
    fun restAudioPlayer() {
        val audioPlayer = AudioPlayer(Application(), timerViewModel, mediaPlayer)

        audioPlayer.reset()

        verify(mediaPlayer).release()
    }
}