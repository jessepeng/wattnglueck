package org.fu.swphcc.wattnglueck;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.googlecode.tesseract.android.TessBaseAPI;

public class ZaehlerstandKamera extends WattnActivity {
	
	protected String file;
	protected String path;
	protected boolean taken;
		
	protected static final String PHOTO_TAKEN = "photo_taken";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zaehlerstand_kamera);
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		if (!taken) {
			
			path = Environment.getExternalStorageDirectory() + "/wattnglueck/";
			file = path + "photo.jpg";
			
			File imageFile = new File(file);
			Uri outputFileUri = Uri.fromFile(imageFile);
			
			Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
			
			startActivityForResult(intent, 0);
		}
		
		initViews();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{	
	    switch(resultCode)
	    {
	    	case 0:
	    		NavUtils.navigateUpFromSameTask(this);
	    		break;
	    	case -1:
	    		BitmapFactory.Options options = new BitmapFactory.Options();
	    	    options.inSampleSize = 4;
	    	    	
	    	    Bitmap bitmap = BitmapFactory.decodeFile(file, options);
	    		
				ExifInterface exif = null;
				try {
					exif = new ExifInterface(file);
				} catch (IOException e) {
					NavUtils.navigateUpFromSameTask(this);
					break;
				}
	    		int exifOrientation = exif.getAttributeInt(
	    		        ExifInterface.TAG_ORIENTATION,
	    		        ExifInterface.ORIENTATION_NORMAL);

	    		int rotate = 0;

	    		switch (exifOrientation) {
	    		case ExifInterface.ORIENTATION_ROTATE_90:
	    		    rotate = 90;
	    		    break;
	    		case ExifInterface.ORIENTATION_ROTATE_180:
	    		    rotate = 180;
	    		    break;
	    		case ExifInterface.ORIENTATION_ROTATE_270:
	    		    rotate = 270;
	    		    break;
	    		}

	    		if (rotate != 0) {
	    		    int w = bitmap.getWidth();
	    		    int h = bitmap.getHeight();

	    		    // Setting pre rotate
	    		    Matrix mtx = new Matrix();
	    		    mtx.preRotate(rotate);

	    		    // Rotating Bitmap & convert to ARGB_8888, required by tess
	    		    bitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, false);
	    		}
	    		
	    		ColorMatrix bwMatrix = new ColorMatrix();
	    		bwMatrix.setSaturation(0);
	    		final ColorMatrixColorFilter colorFilter = new ColorMatrixColorFilter(bwMatrix);
	    		
	    		bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
	    		
	    		Paint paint = new Paint();
	    		paint.setColorFilter(colorFilter);
	    		
	    		Canvas myCanvas = new Canvas(bitmap);
	    		myCanvas.drawBitmap(bitmap, 0, 0, paint);
	    		
	    		TessBaseAPI baseApi = new TessBaseAPI();
	    		// DATA_PATH = Path to the storage
	    		// lang for which the language data exists, usually "eng"
	    		baseApi.init(path, "eng"); baseApi.setImage(bitmap);
	    		baseApi.setDebug(true);
	    		String recognizedText = baseApi.getUTF8Text();
	    		baseApi.end();
	    		
	    		TextView textView = (TextView) findViewById(R.id.textKameraZaehlerstand);
	    		textView.setText(recognizedText);
	    		
	    		break;
	    }
	}
	
	@Override
	protected List<TextView> getTextViewsForFont() {
		return null;
	}

	@Override
	protected boolean showOptionsMenu() {
		return false;
	}

	@Override
	protected List<TextView> getButtonTextViews() {
		return Arrays.asList((TextView) findViewById(R.id.textKameraJa), (TextView) findViewById(R.id.textKameraEingeben), (TextView) findViewById(R.id.textKameraNeuesFoto));
	}

	@Override
	public boolean onClick(View arg0, MotionEvent arg1) {
		return false;
	}

}
