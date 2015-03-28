package sundy.android.demo.processthread;

import java.io.OutputStream;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;

public class DetectSlowActionsActivity extends Activity {
	
	private static final boolean DEVELOPER_MODE = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {		
	     if (DEVELOPER_MODE) {
	         StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
	                 .detectDiskReads()
	                 .detectDiskWrites()
	                 .detectNetwork()   // or .detectAll() for all detectable problems
	                 .penaltyDialog()
	                 .build());
	     }
		super.onCreate(savedInstanceState);
		
		processIoAction();
	}
	
	private void processIoAction() {
		OutputStream out = null;
		try {
			out = openFileOutput("Test", MODE_WORLD_WRITEABLE);
			out.write(0);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try{out.close();}catch(Exception e){}
		}
	}

}
