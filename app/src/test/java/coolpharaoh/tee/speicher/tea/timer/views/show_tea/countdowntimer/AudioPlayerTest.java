package coolpharaoh.tee.speicher.tea.timer.views.show_tea.countdowntimer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RobolectricTestRunner;

import java.io.IOException;

@RunWith(RobolectricTestRunner.class)
public class AudioPlayerTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();
    @Mock
    TimerViewModel timerViewModel;
    @Mock
    MediaPlayer mediaPlayer;

    @Test
    public void startAudioPlayer() throws IOException {
        final String musicChoice = "/music/choice";
        final Uri uri = Uri.parse(musicChoice);
        when(timerViewModel.getMusicChoice()).thenReturn(musicChoice);

        final AudioPlayer audioPlayer = new AudioPlayer(null, timerViewModel, mediaPlayer);
        audioPlayer.start();

        verify(mediaPlayer).setAudioAttributes(any(AudioAttributes.class));
        verify(mediaPlayer).setDataSource(null, uri);
        verify(mediaPlayer).prepare();
        verify(mediaPlayer).start();
    }

    @Test
    public void startAudioPlayerButDoNothingWhenMusicChoiceIsNull() {
        when(timerViewModel.getMusicChoice()).thenReturn(null);

        final AudioPlayer audioPlayer = new AudioPlayer(null, timerViewModel, mediaPlayer);
        audioPlayer.start();

        verify(mediaPlayer, times(0)).start();
    }

    @Test
    public void startAudioPlayerButDoNothingWhenIOExceptionIsThrown() throws IOException {
        final String musicChoice = "/music/choice";
        when(timerViewModel.getMusicChoice()).thenReturn(musicChoice);

        doThrow(IOException.class).when(mediaPlayer).setDataSource(any(), any());

        final AudioPlayer audioPlayer = new AudioPlayer(null, timerViewModel, mediaPlayer);
        audioPlayer.start();

        verify(mediaPlayer, times(0)).start();
    }

    @Test
    public void restAudioPlayer() throws Exception {
        final AudioPlayer audioPlayer = new AudioPlayer(null, timerViewModel, mediaPlayer);

        audioPlayer.reset();

        verify(mediaPlayer).release();
    }
}
