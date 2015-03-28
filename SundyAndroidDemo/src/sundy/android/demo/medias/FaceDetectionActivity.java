package sundy.android.demo.medias;

import sundy.android.demo.R;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.media.FaceDetector;
import android.media.FaceDetector.Face;
import android.os.Bundle;
import android.view.View;

public class FaceDetectionActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(new FaceDetectionView(this));
	}

	public class FaceDetectionView extends View {

		private static final int FACS_RES_ID = R.drawable.faces3;
		private static final int MAX_FACE_COUNT = 10;
		private final Face[] mFaces = new Face[MAX_FACE_COUNT];

		private Bitmap mBitmap = null;
		private int mFaceCount = 0;

		public FaceDetectionView(Context context) {
			super(context);
			initBitmap();
		}

		public void initBitmap() {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inPreferredConfig = Bitmap.Config.RGB_565;
			Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
					FACS_RES_ID, options);
			FaceDetector detector = new FaceDetector(bitmap.getWidth(),
					bitmap.getHeight(), MAX_FACE_COUNT);
			mFaceCount = detector.findFaces(bitmap, mFaces);
			mBitmap = bitmap;
		}

		@Override
		protected void onDraw(Canvas canvas) {
			canvas.drawBitmap(mBitmap, 0, 0, null);

			Paint paint = new Paint();
			paint.setColor(Color.BLUE);
			paint.setStyle(Paint.Style.STROKE);
			paint.setStrokeWidth(3);

			for (int i = 0; i < mFaceCount; i++) {
				Face face = mFaces[i];
				PointF faceMidPoint = new PointF();
				face.getMidPoint(faceMidPoint);
				canvas.drawPoint(faceMidPoint.x, faceMidPoint.y, paint);
				canvas.drawCircle(faceMidPoint.x, faceMidPoint.y,
						face.eyesDistance(), paint);
			}
			super.onDraw(canvas);
		}

	}

}
