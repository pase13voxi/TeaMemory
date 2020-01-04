package coolpharaoh.tee.speicher.tea.timer.views.timer;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;

import java.io.IOException;

import coolpharaoh.tee.speicher.tea.timer.viewmodels.TimerViewModel;

class AudioPlayer{
    private Context context;
    private MediaPlayer mediaPlayer;

    AudioPlayer(Context context){
        this.context = context;
    }

    void start(){
        TimerViewModel timerViewModel = new TimerViewModel(context);
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
                e.printStackTrace();
            }
        }
    }

    void reset() {
        if (mediaPlayer != null)
            mediaPlayer.release();
    }
}
