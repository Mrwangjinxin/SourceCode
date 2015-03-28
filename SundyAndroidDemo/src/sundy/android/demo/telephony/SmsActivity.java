package sundy.android.demo.telephony;

import sundy.android.demo.R;
import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class SmsActivity extends ListActivity {
	
	private static final String SMS_TARGET = "5556";
	private static final String ACTION_SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
	
	private SMSReceiver mSMSReceiver = new SMSReceiver();
	private SMSBlocker mSMSBlocker = new SMSBlocker();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_sms);

		findViewById(R.id.btn_send_sms_intent).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						Uri uri = Uri.parse("smsto:" + SMS_TARGET);
						Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
						intent.putExtra("sms_body", "SMS Test Message");
						startActivity(intent);
					}
				});

		findViewById(R.id.btn_send_sms).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						SmsManager smsMgr = SmsManager.getDefault();
						smsMgr.sendTextMessage(SMS_TARGET, null,
								"SMS Test Message", null, null);
					}
				});

		findViewById(R.id.btn_list_sms_inbox).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						String[] projection = new String[] { "_id", "address",
								"person", "body" };
						Cursor c = managedQuery(
								Uri.parse("content://sms/inbox"), projection,
								null, null, null);
						ListAdapter adapter = new SimpleCursorAdapter(
								SmsActivity.this,
								android.R.layout.simple_list_item_1, c,
								new String[] { "body" },
								new int[] { android.R.id.text1 });
						setListAdapter(adapter);
					}
				});

		findViewById(R.id.btn_clear_sms).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						getContentResolver().delete(Uri.parse("content://sms"), null, null);
					}
				});

		findViewById(R.id.btn_start_monitor).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						registerReceiver(mSMSReceiver, new IntentFilter(ACTION_SMS_RECEIVED));
					}
				});

		findViewById(R.id.btn_stop_monitor).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						unregisterReceiver(mSMSReceiver);
					}
				});

		findViewById(R.id.btn_start_block).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						IntentFilter filter = new IntentFilter(ACTION_SMS_RECEIVED);
						filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
						registerReceiver(mSMSBlocker, filter);
					}
				});

		findViewById(R.id.btn_stop_block).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						unregisterReceiver(mSMSBlocker);
					}
				});
		
	}	
	
		
	private class SMSReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			Object[] pdus = (Object[]) intent.getExtras().get("pdus");
			SmsMessage[] message = new SmsMessage[pdus.length];
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < pdus.length; i++) {
				message[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
				sb.append("Recv SMS From:\n");
				sb.append(message[i].getDisplayOriginatingAddress() + "\n");
				sb.append("Body:" + message[i].getDisplayMessageBody());
			}

			Toast.makeText(context, sb.toString(),
					Toast.LENGTH_LONG).show();
		}
	}
	

	private class SMSBlocker extends SMSReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			super.onReceive(context, intent);
			abortBroadcast();
		}
	}
	
	
	
}
