/**
 * @author Sundy
 * @description Multimedai application demo .
 */
package sundy.android.demo.medias;

import java.io.IOException;

import sundy.android.demo.R;
import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * @author cninsuzh
 *
 */
public class AudioHelloworld extends Activity {

	private final String EXTENAL_FILE_PATH = "file:///sdcard/sound_file_1.mp3"  ;
	private final String INITENT_FILE_PATH = "http://czanxi.azone.artron.net/users_info/88/czanxi/2009121322260351292.mp3";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_audiohelloworld) ;
		//set the first audio playing listener . 
		Button buttonSimple = (Button)findViewById(R.id.buttonFirstAudio)  ;
		buttonSimple.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				//MediaPlayer mp = MediaPlayer.create(AudioHelloworld.this, sundy.android.demo.R.raw.maria) ;
				//mp.start() ; // no need to call prepare(); create() does that for you
			}
			
		}) ;
		
		//play the audio file from extental card  . 
		//Notification: before this demo  , please push one mp3 file into the sdcard path
		Button buttonExtenalCard = (Button)findViewById(R.id.ButtonAudioDatasource)  ;
		buttonExtenalCard.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Uri myUri = Uri.parse(EXTENAL_FILE_PATH)  ;
				MediaPlayer mediaPlayer = new MediaPlayer()  ;
				mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)  ;
				
				try {
					mediaPlayer.setDataSource(AudioHelloworld.this, myUri)  ;
					mediaPlayer.prepare()  ;
					mediaPlayer.start()  ;
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
				
			}
			
		}) ;
		
		
		//Play the audio file from internet  .
		Button buttonAudioUri = (Button)findViewById(R.id.ButtonAudioUri)  ;
		buttonAudioUri.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Uri myUri = Uri.parse(INITENT_FILE_PATH)  ;
				MediaPlayer mediaPlayer = new MediaPlayer()  ;
				mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)  ;
				
				try {
					mediaPlayer.setDataSource(AudioHelloworld.this, myUri)  ;
					//might take long time ! (for buffering , etc) 
					mediaPlayer.prepare()  ;
					mediaPlayer.start()  ;
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		})  ;
		
		//Play the audio file from AsyncPrepare
		Button buttonAsyncPrepare = (Button)findViewById(R.id.ButtonAsynchPrepare)  ;
		buttonAsyncPrepare.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Uri myUri = Uri.parse(INITENT_FILE_PATH)  ;
				MediaPlayer mediaPlayer = new MediaPlayer()  ;
				mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)  ;
				mediaPlayer.setOnPreparedListener(new OnPreparedListener()
				{
					@Override
					public void onPrepared(MediaPlayer arg0) {
						arg0.start()  ;
					}
					
				}) ;
				try {
					mediaPlayer.setDataSource(getApplicationContext(), myUri);
					mediaPlayer.prepareAsync()  ;
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		})  ;				
	}

}
