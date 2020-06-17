package coolpharaoh.tee.speicher.tea.timer.views.showtea.timer;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;

import coolpharaoh.tee.speicher.tea.timer.models.database.TeaMemoryDatabase;

class AudioPlayer {
    private static final String LOG_TAG = AudioPlayer.class.getSimpleName();

    private final Context context;
    private MediaPlayer mediaPlayer;

    AudioPlayer(Context context) {
        this.context = context;
    }

    void start() {
        TimerViewModel timerViewModel = new TimerViewModel(TeaMemoryDatabase.getDatabaseInstance(context));
        //initial music track
        if (timerViewModel.getMusicchoice() != null) {
            mediaPlayer = new MediaPlayer();
            Uri uri = Uri.parse(timerViewModel.getMusicchoice());
            try {
                //synchronize musicstreams
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);
                mediaPlayer.setDataSource(context, uri);
                mediaPlayer.prepare();
                mediaPlayer.start();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Something went wrong when start playing the Ringtone.", e);
            }
        }
    }

    void reset() {
        if (mediaPlayer != null)
            mediaPlayer.release();
    }
}
