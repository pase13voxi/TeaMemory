package coolpharaoh.tee.speicher.tea.timer.views.showtea.countdowntimer;

import android.media.AudioManager;
import android.media.MediaPlayer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AudioPlayerTest {

    @Mock
    TimerViewModel timerViewModel;
    @Mock
    MediaPlayer mediaPlayer;

    @Test
    public void startAudioPlayer() throws IOException {
        String musicChoice = "/music/choice";
        when(timerViewModel.getMusicchoice()).thenReturn(musicChoice);

        AudioPlayer audioPlayer = new AudioPlayer(null, timerViewModel, mediaPlayer);
        audioPlayer.start();

        verify(mediaPlayer).setAudioStreamType(AudioManager.STREAM_RING);
        verify(mediaPlayer).setDataSource(null, null);
        verify(mediaPlayer).prepare();
        verify(mediaPlayer).start();
    }

    @Test
    public void startAudioPlayerButDoNothingWhenMusicChoiceIsNull() {
        when(timerViewModel.getMusicchoice()).thenReturn(null);

        AudioPlayer audioPlayer = new AudioPlayer(null, timerViewModel, mediaPlayer);
        audioPlayer.start();

        verify(mediaPlayer, times(0)).start();
    }

    @Test
    public void startAudioPlayerButDoNothingWhenIOExceptionIsThrown() throws IOException {
        String musicChoice = "/music/choice";
        when(timerViewModel.getMusicchoice()).thenReturn(musicChoice);

        doThrow(IOException.class).when(mediaPlayer).setDataSource(any(), any());

        AudioPlayer audioPlayer = new AudioPlayer(null, timerViewModel, mediaPlayer);
        audioPlayer.start();

        verify(mediaPlayer, times(0)).start();
    }

    @Test
    public void restAudioPlayer() throws Exception {
        AudioPlayer audioPlayer = new AudioPlayer(null, timerViewModel, mediaPlayer);

        audioPlayer.reset();

        verify(mediaPlayer).release();
    }
}
