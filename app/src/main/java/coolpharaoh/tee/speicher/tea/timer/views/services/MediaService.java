package coolpharaoh.tee.speicher.tea.timer.views.services;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;

import java.io.IOException;

import coolpharaoh.tee.speicher.tea.timer.viewmodels.MediaServiceViewModel;

/**
 * Created by CoolPharaoh on 10.02.2016.
 */
public class MediaService extends Service {

    private MediaPlayer mediaPlayer = null;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        MediaServiceViewModel mediaServiceViewModel = new MediaServiceViewModel(getApplicationContext());
        //Musikstück initialisieren
        if(mediaServiceViewModel.getMusicchoice()!=null) {
            mediaPlayer = new MediaPlayer();
            Uri uri = Uri.parse(mediaServiceViewModel.getMusicchoice());
            try {
                //synchronisiere Musikstreams
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);
                mediaPlayer.setDataSource(getApplicationContext(), uri);
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //mediaPlayer = MediaPlayer.create(this, uri);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(mediaPlayer != null)
            mediaPlayer.start();
        return START_STICKY;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mediaPlayer != null)
            mediaPlayer.release();
    }
}
