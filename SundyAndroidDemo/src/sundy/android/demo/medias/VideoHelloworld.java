package sundy.android.demo.medias;

import java.io.File;

import sundy.android.demo.R;
import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.net.Uri;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class VideoHelloworld extends Activity {
	
	private final Uri mVideoUri = Uri.fromFile(new File("/sdcard/video_test.3gp")); 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_videohelloworld);


		final MediaPlayer player = new MediaPlayer();
		
		player.setOnErrorListener(new OnErrorListener() {

			@Override
			public boolean onError(MediaPlayer arg0, int arg1, int arg2) {
				return false;
			}
			
		});
		

		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.surface);

		surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
			
			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {	
				player.stop();
			}
			
			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				try {
					player.setDataSource(getApplicationContext(), mVideoUri);
					player.setAudioStreamType(AudioManager.STREAM_MUSIC); 
					player.setDisplay(holder);
					player.prepare();	
					player.start();		
				} catch(Exception e) {			
				}
				
			}
			
			@Override
			public void surfaceChanged(SurfaceHolder holder, int format, int width,
					int height) {				
			}
		});
	}

}
