package sundy.android.demo.medias;


import sundy.android.demo.R;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Images.ImageColumns;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class MediaStoreActivity extends Activity {

    private static final String EXTERNAL_SD_DIRECTORY = Environment.getExternalStorageDirectory()
            .toString();

    private static final int MAX_RECORDS = 10;

    private static final String[] PROJECTION = {
            ImageColumns._ID, // 0
            ImageColumns.TITLE, // 1
            ImageColumns.MIME_TYPE, // 2
            ImageColumns.LATITUDE, // 3
            ImageColumns.LONGITUDE, // 4
            ImageColumns.DATE_TAKEN, // 5
            ImageColumns.DATE_ADDED, // 6
            ImageColumns.DATE_MODIFIED, // 7
            ImageColumns.DATA, // 8
    };

    private static final int INDEX_ID = 0;
    private static final int INDEX_CAPTION = 1;
    private static final int INDEX_MIME_TYPE = 2;
    private static final int INDEX_LATITUDE = 3;
    private static final int INDEX_LONGITUDE = 4;
    private static final int INDEX_DATE_TAKEN = 5;
    private static final int INDEX_DATE_ADDED = 6;
    private static final int INDEX_DATE_MODIFIED = 7;
    private static final int INDEX_DATA = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_media_store);

        final TextView textView = (TextView) findViewById(R.id.text);

        findViewById(R.id.btn_images).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                textView.setText(getMediaInfo(Images.Media.EXTERNAL_CONTENT_URI));
            }
        });

        findViewById(R.id.btn_media_scan).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                runAllDataMediaScan();
            }
        });
    }


    private String getMediaInfo(Uri uri) {
        uri = uri.buildUpon().appendQueryParameter("limit", 0 + "," + MAX_RECORDS).build();
        Cursor cursor = null;
        try {
            cursor = getContentResolver().query(uri, PROJECTION, null, null, null);
            StringBuilder builder = new StringBuilder();
            builder.append("============\n");
            while(cursor.moveToNext()) {
                builder.append(cursor.getString(INDEX_CAPTION)).append("\n");
            }
            builder.append("===========\n");
            return builder.toString();

        } finally {
            try{cursor.close();}catch(Exception e){}
            cursor = null;
        }
    }



    private void runAllDataMediaScan() {
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"
                +  EXTERNAL_SD_DIRECTORY)));
    }

    private void runMediaScanner(String filePath) {
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri
                .parse("file://" + filePath)));
    }


    
    
}
