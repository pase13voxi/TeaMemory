package coolpharaoh.tee.speicher.tea.timer.views.show_tea.countdowntimer;

import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

//could be removed when Robolectric supports Java 8 for API 29
@Config(sdk = Build.VERSION_CODES.O_MR1)
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
        String musicChoice = "/music/choice";
        Uri uri = Uri.parse(musicChoice);
        when(timerViewModel.getMusicchoice()).thenReturn(musicChoice);

        AudioPlayer audioPlayer = new AudioPlayer(null, timerViewModel, mediaPlayer);
        audioPlayer.start();

        verify(mediaPlayer).setAudioAttributes(any(AudioAttributes.class));
        verify(mediaPlayer).setDataSource(null, uri);
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
