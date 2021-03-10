package coolpharaoh.tee.speicher.tea.timer.views.showtea.countdowntimer;

import android.app.Application;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;

class AudioPlayer {
    private static final String LOG_TAG = AudioPlayer.class.getSimpleName();

    private final Application application;
    private final TimerViewModel timerViewModel;
    private final MediaPlayer mediaPlayer;

    AudioPlayer(Application application) {
        this(application, new TimerViewModel(application), new MediaPlayer());
    }

    AudioPlayer(Application application, TimerViewModel timerViewModel, MediaPlayer mediaPlayer) {
        this.application = application;
        this.timerViewModel = timerViewModel;
        this.mediaPlayer = mediaPlayer;
    }

    void start() {
        //initial music track
        if (timerViewModel.getMusicchoice() != null) {
            Uri uri = Uri.parse(timerViewModel.getMusicchoice());
            try {
                mediaPlayer.setAudioAttributes(new AudioAttributes
                        .Builder()
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .build());
                mediaPlayer.setDataSource(application, uri);
                mediaPlayer.prepare();
                mediaPlayer.start();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Something went wrong when start playing the Ringtone.", e);
            }
        }
    }

    void reset() {
        mediaPlayer.release();
    }
}
