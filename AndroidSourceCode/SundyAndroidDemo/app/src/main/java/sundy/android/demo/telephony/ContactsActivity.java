package sundy.android.demo.telephony;

import sundy.android.demo.R;
import android.app.ListActivity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;

public class ContactsActivity extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_contacts);

		findViewById(R.id.btn_list).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Cursor c = getContentResolver().query(Contacts.CONTENT_URI,
						new String[] { Contacts._ID, Contacts.DISPLAY_NAME, }, null, null,
						null);
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(
						ContactsActivity.this,
						android.R.layout.simple_list_item_1, android.R.id.text1);

				while (c != null && c.moveToNext()) {
					String contactName = c.getString(1);	
					String extraData = getContactEmail(c.getLong(0));
					adapter.add(contactName + ": " + extraData);
				}
				if (c != null) {
					c.close();
				}				
				
				setListAdapter(adapter);			
			}			
		});

		findViewById(R.id.btn_add).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ContentValues values = new ContentValues();
				Uri rawContactUri = getContentResolver().insert(
						RawContacts.CONTENT_URI, values);
				long rawContactId = ContentUris.parseId(rawContactUri);

				values.clear();
				values.put(RawContacts.Data.RAW_CONTACT_ID, rawContactId);
				values.put(RawContacts.Data.MIMETYPE,
						StructuredName.CONTENT_ITEM_TYPE);
				values.put(StructuredName.DISPLAY_NAME, "Test");
				getContentResolver().insert(Data.CONTENT_URI, values);
			}
		});
		
		findViewById(R.id.btn_del).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Cursor c = getContentResolver().query(Data.CONTENT_URI,
						new String[] { Data.RAW_CONTACT_ID },
						Contacts.DISPLAY_NAME + "=?", new String[] { "Test" },
						null);
				if (c != null && c.moveToFirst()) {
					Uri uri = ContentUris.withAppendedId(RawContacts.CONTENT_URI, c.getLong(0));
					getContentResolver().delete(uri, null, null);
				}
				closeCursor(c);
			}			
		});
	}
	
	private String getContactPhoneNumber(long contactId) {
		Cursor c = null;
		try {
			c = getContentResolver().query(Phone.CONTENT_URI,  
	                new String[]{Phone.NUMBER},  
	                Phone.CONTACT_ID +" = "+ contactId,  
	                null, null);  
			if (c != null && c.moveToFirst()) {
				return c.getString(0);
			}			
		} finally {
			closeCursor(c);
		}
		
		return null;
	}
	
	private String getContactEmail(long contactId) {
		Cursor c = null;
		try {
			c = getContentResolver().query(Email.CONTENT_URI,  
	                new String[]{Email.DATA},  
	                Email.CONTACT_ID +" = "+ contactId,  
	                null, null);  
			if (c != null && c.moveToFirst()) {
				return c.getString(0);
			}			
		} finally {
			closeCursor(c);
		}
		
		return null;
	}
	
	private void closeCursor(Cursor c) {
		if (c != null) {
			c.close();
		}
	}

}
