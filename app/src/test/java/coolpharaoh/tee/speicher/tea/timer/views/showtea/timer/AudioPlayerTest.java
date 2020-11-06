//package coolpharaoh.tee.speicher.tea.timer.views.showtea.timer;
//
//import android.media.AudioManager;
//import android.media.MediaPlayer;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.powermock.core.classloader.annotations.PrepareForTest;
//import org.powermock.modules.junit4.PowerMockRunner;
//
//import static org.mockito.Mockito.verify;
//import static org.powermock.api.mockito.PowerMockito.when;
//import static org.powermock.api.mockito.PowerMockito.whenNew;
//
//@RunWith(PowerMockRunner.class)
//@PrepareForTest(fullyQualifiedNames = "coolpharaoh.tee.speicher.tea.timer.views.showtea.timer.*")
//public class AudioPlayerTest {
//
//    @Mock
//    TimerViewModel timerViewModel;
//    @Mock
//    MediaPlayer mediaPlayer;
//
//    @Test
//    public void startAndResetAudioPlayer() throws Exception {
//        whenNew(TimerViewModel.class).withAnyArguments().thenReturn(timerViewModel);
//        when(timerViewModel.getMusicchoice()).thenReturn("/abc/cde");
//        whenNew(MediaPlayer.class).withAnyArguments().thenReturn(mediaPlayer);
//
//        AudioPlayer audioPlayer = new AudioPlayer(null);
//        audioPlayer.start();
//
//        verify(mediaPlayer).setAudioStreamType(AudioManager.STREAM_RING);
//        verify(mediaPlayer).setDataSource(null, null);
//        verify(mediaPlayer).prepare();
//        verify(mediaPlayer).start();
//
//        audioPlayer.reset();
//
//        verify(mediaPlayer).release();
//    }
//}
