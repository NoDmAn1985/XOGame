package ru.academits.nodman.support;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;

import ru.academits.nodman.xogame.R;

public class Sound extends Activity {
    private Context context;
    private MediaPlayer newPlay;
    private MediaPlayer lostPlay;
    private MediaPlayer nobodyPlay;
    private MediaPlayer winsPlay;
    private MediaPlayer backPlay;
    private MediaPlayer playerPlay;

    public Sound(Context context) {
        this.context = context;
    }

    public void initBack() {
        this.backPlay = MediaPlayer.create(context, R.raw.back);
    }

    public void initSounds() {
        this.newPlay = MediaPlayer.create(context, R.raw.new_game);
        this.lostPlay = MediaPlayer.create(context, R.raw.lost);
        this.nobodyPlay = MediaPlayer.create(context, R.raw.nobody);
        this.winsPlay = MediaPlayer.create(context, R.raw.victory);
        this.playerPlay = MediaPlayer.create(context, R.raw.player_move);
    }

    public void play(String act) {
        switch (act) {
            case "new":
                newPlay.start();
                break;
            case "lost":
                lostPlay.start();
                break;
            case "nobody":
                nobodyPlay.start();
                break;
            case "wins":
                winsPlay.start();
                break;
            case "back":
                backPlay.start();
                break;
            default:
                playerPlay.start();
                break;
        }
    }

    public void pause() {
        backPlay.pause();
    }

    public void stopSounds() {
        newPlay.release();
        lostPlay.release();
        nobodyPlay.release();
        winsPlay.release();
        playerPlay.release();
    }

    public void stopBack() {
        backPlay.release();
    }
}