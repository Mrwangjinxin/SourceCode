package sundy.android.demo.processthread;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

public class MemoryCheckActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		
		//handlerTest();
		taskTest();
				
	}
	
	private void handlerTest() {
		Handler handler = new Handler();
		handler.postDelayed(mBlockAction, 100);
	}
	
	private void taskTest() {
		AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... arg0) {
				mBlockAction.run();
				return null;
			}
			
		};		
		task.execute();		
	}
	
	
	
	private Runnable mBlockAction = new Runnable() {
		public void run() {
			try{Thread.sleep(20000);}catch(Exception e){}
		}
	};

}
