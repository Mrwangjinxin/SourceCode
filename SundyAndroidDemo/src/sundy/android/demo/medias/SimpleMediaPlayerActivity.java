package sundy.android.demo.medias;

import java.io.IOException;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

public class SimpleMediaPlayerActivity extends Activity {
	
	//private static final Uri mMusicUri = Uri.fromFile(new File("/sdcard/sound_file_1.mp3"));
	private static final Uri mMusicUri = Uri.parse("http://czanxi.azone.artron.net/users_info/88/czanxi/2009121322260351292.mp3");
	private MediaPlayer mMediaPlayer = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		playMusic1();
	}
	
	private void playMusic1() {
		stopCurrentMediaPlayer();
		long t1 = System.currentTimeMillis();
		mMediaPlayer = MediaPlayer.create(this, mMusicUri);  //prepared
		long t = System.currentTimeMillis() - t1;
		Log.d("SimpleMediaPlayerActivity", "Create Media Player Cost: " + t + "ms");
		mMediaPlayer.start(); // no need to call prepare(); create() does that for you
	}

	private void playMusic2() {
		stopCurrentMediaPlayer();
		mMediaPlayer = new MediaPlayer();  //idle
		mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		try {
			mMediaPlayer.setDataSource(getApplicationContext(), mMusicUri);
			mMediaPlayer.prepare();			
		} catch(IOException e) {}
		mMediaPlayer.start();
	}
	
	@Override
	protected void onDestroy() {
		stopCurrentMediaPlayer();
		super.onDestroy();
	}
	
	private void stopCurrentMediaPlayer() {
		if (mMediaPlayer != null) {
			mMediaPlayer.stop();
			mMediaPlayer.release();
			mMediaPlayer = null;
		}
	}
	
	
	
	
	

}
