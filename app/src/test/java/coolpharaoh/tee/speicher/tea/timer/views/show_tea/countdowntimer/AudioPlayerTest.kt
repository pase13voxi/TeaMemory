package coolpharaoh.tee.speicher.tea.timer.views.show_tea.countdowntimer

import android.app.Application
import android.media.MediaPlayer
import android.net.Uri
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import io.mockk.verify
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.IOException

@RunWith(RobolectricTestRunner::class)
class AudioPlayerTest {
    @get:Rule
    val mockkRule = MockKRule(this)
    @MockK
    lateinit var timerViewModel: TimerViewModel
    @RelaxedMockK
    lateinit var mediaPlayer: MediaPlayer

    @Test
    @Throws(IOException::class)
    fun startAudioPlayer() {
        val application = Application()
        val musicChoice = "/music/choice"
        val uri = Uri.parse(musicChoice)
        every { timerViewModel.musicChoice } returns musicChoice

        val audioPlayer = AudioPlayer(application, timerViewModel, mediaPlayer)
        audioPlayer.start()

        verify { mediaPlayer.setAudioAttributes(any()) }
        verify { mediaPlayer.setDataSource(application, uri) }
        verify { mediaPlayer.prepare() }
        verify { mediaPlayer.start() }
    }

    @Test
    fun startAudioPlayerButDoNothingWhenMusicChoiceIsNull() {
        every { timerViewModel.musicChoice } returns null

        val audioPlayer = AudioPlayer(Application(), timerViewModel, mediaPlayer)
        audioPlayer.start()

        verify (exactly = 0) { mediaPlayer.start() }
    }

    @Test
    @Throws(IOException::class)
    fun startAudioPlayerButDoNothingWhenIOExceptionIsThrown() {
        val musicChoice = "/music/choice"
        every { timerViewModel.musicChoice } returns musicChoice

       every { mediaPlayer.setDataSource(any(), any()) } throws IOException()

        val audioPlayer = AudioPlayer(Application(), timerViewModel, mediaPlayer)
        audioPlayer.start()

        verify (exactly = 0) { mediaPlayer.start() }
    }

    @Test
    fun restAudioPlayer() {
        val audioPlayer = AudioPlayer(Application(), timerViewModel, mediaPlayer)

        audioPlayer.reset()

        verify { mediaPlayer.release() }
    }
}