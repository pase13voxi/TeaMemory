package coolpharaoh.tee.speicher.tea.timer.views.timer;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;

import java.io.IOException;

import coolpharaoh.tee.speicher.tea.timer.viewmodels.TimerViewModel;

public class MusicPlayer extends Service {

    private MediaPlayer mediaPlayer = null;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        TimerViewModel timerViewModel = new TimerViewModel(getApplicationContext());
        //initial music track
        if (timerViewModel.getMusicchoice() != null) {
            mediaPlayer = new MediaPlayer();
            Uri uri = Uri.parse(timerViewModel.getMusicchoice());
            try {
                //synchronize musicstreams
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);
                mediaPlayer.setDataSource(getApplicationContext(), uri);
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mediaPlayer != null)
            mediaPlayer.start();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null)
            mediaPlayer.release();
    }
}
