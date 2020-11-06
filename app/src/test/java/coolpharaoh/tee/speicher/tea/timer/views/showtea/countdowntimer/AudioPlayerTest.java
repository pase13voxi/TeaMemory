package coolpharaoh.tee.speicher.tea.timer.views.showtea.countdowntimer;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest(value = Uri.class, fullyQualifiedNames = "coolpharaoh.tee.speicher.tea.timer.views.showtea.timer.*")
public class AudioPlayerTest {

    @Mock
    TimerViewModel timerViewModel;
    @Mock
    MediaPlayer mediaPlayer;
    @Mock
    Uri uri;

    @Test
    public void startAndResetAudioPlayer() throws Exception {
        String musicChoice = "/music/choice";

        whenNew(TimerViewModel.class).withAnyArguments().thenReturn(timerViewModel);
        when(timerViewModel.getMusicchoice()).thenReturn(musicChoice);

        whenNew(MediaPlayer.class).withAnyArguments().thenReturn(mediaPlayer);

        PowerMockito.mockStatic(Uri.class);
        PowerMockito.when(Uri.parse(musicChoice)).thenReturn(uri);

        AudioPlayer audioPlayer = new AudioPlayer(null);
        audioPlayer.start();

        verify(mediaPlayer).setAudioStreamType(AudioManager.STREAM_RING);
        verify(mediaPlayer).setDataSource(null, uri);
        verify(mediaPlayer).prepare();
        verify(mediaPlayer).start();

        audioPlayer.reset();

        verify(mediaPlayer).release();
    }
}
