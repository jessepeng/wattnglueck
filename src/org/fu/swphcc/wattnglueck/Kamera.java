package org.fu.swphcc.wattnglueck;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.abbyy.mobile.ocr4.AssetDataSource;
import com.abbyy.mobile.ocr4.DataSource;
import com.abbyy.mobile.ocr4.Engine;
import com.abbyy.mobile.ocr4.Engine.DataFilesExtensions;
import com.abbyy.mobile.ocr4.FileLicense;
import com.abbyy.mobile.ocr4.License;
import com.abbyy.mobile.ocr4.License.BadLicenseException;
import com.abbyy.mobile.ocr4.RecognitionConfiguration;
import com.abbyy.mobile.ocr4.RecognitionFailedException;
import com.abbyy.mobile.ocr4.RecognitionLanguage;
import com.abbyy.mobile.ocr4.RecognitionManager;
import com.abbyy.mobile.ocr4.RecognitionManager.RecognitionCallback;
import com.abbyy.mobile.ocr4.RecognitionManager.RotationType;
import com.abbyy.mobile.ocr4.UserRecognitionLanguage;
import com.abbyy.mobile.ocr4.layout.MocrLayout;
import com.abbyy.mobile.ocr4.layout.MocrPrebuiltLayoutInfo;

public class Kamera extends WattnActivity implements KameraPreview.KameraTapAction {
	
	private KameraPreview preview;
	private ProgressDialog progress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        Toast.makeText(getApplicationContext(), "Tippe auf den Bildschirm, um ein Foto zu machen.", Toast.LENGTH_LONG).show();
        
        setContentView(R.layout.view_kamera);
        
        findViewById(android.R.id.content).setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        findViewById(android.R.id.content).setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
		
        preview = (KameraPreview) findViewById(R.id.kamera_preview);
		
        preview.setKameraTap(this);
        
		initViews();
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
		return null;
	}

	@Override
	public boolean onClick(View arg0, MotionEvent arg1) {
		return false;
	}
	

	@Override
	public void kameraTap() {
		progress = new ProgressDialog(this, ProgressDialog.THEME_HOLO_LIGHT);
		progress.setMessage("Z�hlerstand wird erkannt...");
		progress.setCancelable(false);
		progress.show();
		
		new Thread(new pictureThread()).start();
	}
	
	private class pictureThread implements RecognitionCallback, Runnable {
		
		@Override
		public void run() {
			String result = "Fehler";
			
			Bitmap bitmap = preview.mBitmap;
			preview.mBitmap = null;
			System.gc();

			int origHeight = bitmap.getHeight();
			int origWidth = bitmap.getWidth();
			
			/**
			 * Im Kamerainterface werden 5/12 des Bildes von oben und unten abgeschnitten
			 * sowie 1/8 links und rechts.
			 * 
			 * 
			 */
			
			int top = (int)(origHeight * (0.36));
			int left = (int)(origWidth * (1.0/4.0));
			int right = (int)(origWidth * (3.0/4.0));
			int bottom = (int)(origHeight * (0.64));
			
			/*Matrix matrix = new Matrix();
			matrix.postScale((float)(500.0 / origWidth), (float)(220.0 / origHeight));
			
			Bitmap newBitmap = Bitmap.createBitmap(bitmap, left, top, width, height);
			bitmap = null;
			System.gc();
			newBitmap = Bitmap.createScaledBitmap(newBitmap, 500, 220, false);
			newBitmap = invert(newBitmap);*/
			
			Rect src = new Rect(left, top, right, bottom);
			Rect dst = new Rect(100, 400, (int) (100 + (right - left) * 0.2), (int) (400 + (bottom - top) * 0.2));
			
			float invert[] =
				{
				-1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 
				0.0f, -1.0f, 0.0f, 1.0f, 0.0f,
				0.0f, 0.0f, -1.0f, 1.0f, 0.0f, 
				1.0f, 1.0f, 1.0f, 1.0f, 0.0f
				};
			
			ColorMatrix cm = new ColorMatrix(invert);
			//cm.setSaturation(0);
			Paint invertPaint = new Paint();
			invertPaint.setColorFilter(new ColorMatrixColorFilter(cm));
			Paint whitePaint = new Paint();
			whitePaint.setColor(Color.WHITE);
			
			Bitmap whiteBitmap = Bitmap.createBitmap(700, 1000, Config.ARGB_8888);
			Canvas whiteCanvas = new Canvas(whiteBitmap);
			
			whiteCanvas.drawPaint(whitePaint);
			whiteCanvas.drawBitmap(bitmap, src, dst, invertPaint);
			whiteCanvas = null;
			bitmap = null;
			System.gc();
			
			try {
				/*String root = Environment.getExternalStorageDirectory().toString();
				FileOutputStream fos = new FileOutputStream(root + "/wattnglueck/picture_crop.jpg");
				whiteBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
				fos.close();*/
				
				Engine.loadNativeLibrary();
				
				DataSource dataSource = new AssetDataSource(getAssets());
				License license = new FileLicense(dataSource, "SMRT42000003000137604180.ABBYY.License", "org.fu.swphcc.wattnglueck");
				
				Engine ocrEngine = Engine.createInstance(Arrays.asList(dataSource), license, new DataFilesExtensions(".mp3", ".mp3", ".mp3"));
				
				RecognitionConfiguration config = new RecognitionConfiguration();
				config.setRecognitionLanguages(EnumSet.noneOf(RecognitionLanguage.class));
				
				List<UserRecognitionLanguage> userLanguages = new ArrayList<UserRecognitionLanguage>();
				userLanguages.add(NumberLanguage.Numbers);

				config.setUserRecognitionLanguages("Numbers", userLanguages);
				
				RecognitionManager recognitionManager = ocrEngine.getRecognitionManager(config);
				MocrLayout recognitionResult = recognitionManager.recognizeText(whiteBitmap, this);
				
				Engine.destroyInstance();
				
				result = recognitionResult.getText();
				
				result = result.replaceAll("[^0-9]*", "");
			} catch (IOException e) {
				e.printStackTrace();
			} catch (BadLicenseException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (RecognitionFailedException e) {
				e.printStackTrace();
			}

			progress.dismiss();
			
			whiteBitmap = null;
			System.gc();
			
			Intent kameraIntent = new Intent(getApplicationContext(), ZaehlerstandKamera.class);
			kameraIntent.putExtra("value", result);
			startActivity(kameraIntent);
			finish();
		}
		
		@Override
		public void onPrebuiltWordsInfoReady(MocrPrebuiltLayoutInfo arg0) {
			// TODO Auto-generated method stub
			
		}
	
		@Override
		public boolean onRecognitionProgress(int arg0, int arg1) {
			// TODO Auto-generated method stub
			return false;
		}
	
		@Override
		public void onRotationTypeDetected(RotationType arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
