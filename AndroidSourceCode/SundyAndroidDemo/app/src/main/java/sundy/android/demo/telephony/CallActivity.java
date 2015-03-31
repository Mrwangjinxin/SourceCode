package sundy.android.demo.telephony;

import java.io.File;
import java.lang.reflect.Method;

import sundy.android.demo.R;
import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class CallActivity extends ListActivity {

	private static final String CALL_TARGET = "5556";
	private static final String CALL_PREFIX = "17951";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_call);

		findViewById(R.id.btn_make_call).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + CALL_TARGET)); 
						startActivity(intent); 
					}
				});

		findViewById(R.id.btn_list_call_logs).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						Cursor c = managedQuery(
								CallLog.Calls.CONTENT_URI, null,
								null, null, CallLog.Calls.DEFAULT_SORT_ORDER);
						ListAdapter adapter = new SimpleCursorAdapter(
								CallActivity.this,
								android.R.layout.simple_list_item_1, c,
								new String[] { CallLog.Calls.NUMBER },
								new int[] { android.R.id.text1 });
						setListAdapter(adapter);
					}
				});
		
		final TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		final CallListener callListener = new CallListener();
		  
		findViewById(R.id.btn_start_monitor).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						tm.listen(callListener, PhoneStateListener.LISTEN_CALL_STATE);
					}
				});
		
		findViewById(R.id.btn_stop_monitor).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						tm.listen(callListener, PhoneStateListener.LISTEN_NONE);
					}
				});		
		
		final CallPrefixHandler   callPrefixHandler   = new CallPrefixHandler();	
		findViewById(R.id.btn_add_call_prefix).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						registerReceiver(callPrefixHandler, new IntentFilter(Intent.ACTION_NEW_OUTGOING_CALL));
					}
				});
		
		findViewById(R.id.btn_remove_call_prefix).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						unregisterReceiver(callPrefixHandler);
					}
				});
	}
	
	
	private void showToast(String toast) {
		Toast.makeText(this, toast,
				Toast.LENGTH_LONG).show();
	}

	private MediaRecorder startCallRecorder(File file) {
		MediaRecorder recorder = new MediaRecorder();
		recorder.setAudioSource(MediaRecorder.AudioSource.MIC); // VOICE_CALL?
		recorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
		recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
		recorder.setOutputFile(file.getAbsolutePath());
		try {
			file.createNewFile();
			recorder.prepare();
			recorder.start();
		} catch (Exception e) {
			e.printStackTrace();
			recorder.release();
			return null;
		}
		
		return recorder;
	}
	
	private void endCurrentCall() {
		try {
			TelephonyManager tm = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
			Method getITelephonyMethod = tm.getClass().getDeclaredMethod("getITelephony", (Class[])null);
			getITelephonyMethod.setAccessible(true);
			Object iTelephony = getITelephonyMethod.invoke(tm, (Object[])null);
			Method endCallMethod = iTelephony.getClass().getDeclaredMethod("endCall", (Class[])null);
			endCallMethod.invoke(iTelephony, (Object[])null);			
		} catch(Exception e) {
			e.printStackTrace();
			showToast("Failed to end call");
		}
		
	}
		
	class CallListener extends PhoneStateListener {
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			super.onCallStateChanged(state, incomingNumber);
			switch (state) {
			case TelephonyManager.CALL_STATE_IDLE: {
				showToast("CALL_STATE_IDLE");
				break;
			}
			case TelephonyManager.CALL_STATE_OFFHOOK: {
				showToast("CALL_STATE_OFFHOOK");
				break;
			}
			case TelephonyManager.CALL_STATE_RINGING: {
				showToast("CALL_STATE_RINGING");
				endCurrentCall();
				break;
			}
			default:
				break;
			}
		}
	}
	
	private class CallPrefixHandler extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			 setResultData(CALL_PREFIX + getResultData());
		}
	}
	
}
