package sundy.android.demo.medias;

import java.io.File;

import sundy.android.demo.R;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;

public class ForgroundService extends Service {
	
	private static final int NOTIFICATION_ID = 100;
	private static final Uri mMusicUri = Uri.fromFile(new File("/sdcard/sound_file_1.mp3"));
	private MediaPlayer mMediaPlayer = null;
	
	public class ForgroundServiceBinder extends Binder {

		public void playMusic() {

			stopCurrentMediaPlayer();
			mMediaPlayer = MediaPlayer.create(getApplicationContext(), mMusicUri);
			mMediaPlayer.start();		
			
			String songName = "Test Music";
			// assign the song name to songName
			PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0, new Intent(getApplicationContext(), ForgroundServiceActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
			Notification notification = new Notification();
			notification.tickerText = "Forground Service";
			notification.icon = R.drawable.icon;
			notification.flags |= Notification.FLAG_ONGOING_EVENT;
			notification.setLatestEventInfo(getApplicationContext(), "MusicPlayerSample", "Playing: " + songName, pi);
			startForeground(NOTIFICATION_ID, notification);
			
		}

		public void stopMusic() {
			stopCurrentMediaPlayer();
			stopForeground(true);	
		}
	}
	
	private ForgroundServiceBinder mBinder = new ForgroundServiceBinder();
	
	@Override
	public IBinder onBind(Intent arg0) {		
		return mBinder;
	}
		

	@Override
	public void onDestroy() {
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
