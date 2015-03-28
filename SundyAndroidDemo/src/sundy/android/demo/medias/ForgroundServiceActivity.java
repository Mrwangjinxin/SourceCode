package sundy.android.demo.medias;

import sundy.android.demo.R;
import sundy.android.demo.medias.ForgroundService.ForgroundServiceBinder;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;

public class ForgroundServiceActivity extends Activity {
	
	@Override
	protected void onDestroy() {
		unbindService(mServiceConnection);
		super.onDestroy();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_forground_service);
		
		final Intent serviceIntent = new Intent(this, ForgroundService.class);
		
		//startService(serviceIntent);
		bindService(serviceIntent, mServiceConnection, BIND_AUTO_CREATE);
		
	}
	
	private ServiceConnection mServiceConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName name, IBinder binder) {

			final ForgroundServiceBinder service = (ForgroundServiceBinder) binder;
					
			findViewById(R.id.start).setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					service.playMusic();
				}
				
			});

			findViewById(R.id.stop).setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					service.stopMusic();
				}
				
			});
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {				
		}
		
	};		

}
