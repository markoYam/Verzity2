package com.dwmedios.uniconekt.data.service.broadcast;

import android.content.Context;
import android.database.ContentObserver;
import android.media.AudioManager;
import android.os.Handler;

public class audioController extends ContentObserver {
    int previousVolume;
    Context context;

    public audioController(Context c, Handler handler) {
        super(handler);
        context = c;

        AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        previousVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC);
    }

    @Override
    public boolean deliverSelfNotifications() {
        return super.deliverSelfNotifications();
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);

        AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int currentVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC);

        int delta = previousVolume - currentVolume;

        if (delta > 0) {
            ///Logger.d("Decreased");
            previousVolume = currentVolume;
        } else if (delta < 0) {
            // Logger.d("Increased");
            previousVolume = currentVolume;
        }
    }

}
