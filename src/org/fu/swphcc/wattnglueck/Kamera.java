package org.fu.swphcc.wattnglueck;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import com.abbyy.mobile.ocr4.RecognitionManager;
import com.abbyy.mobile.ocr4.RecognitionManager.RecognitionCallback;
import com.abbyy.mobile.ocr4.RecognitionManager.RotationType;
import com.abbyy.mobile.ocr4.layout.MocrLayout;
import com.abbyy.mobile.ocr4.layout.MocrPrebuiltLayoutInfo;

public class Kamera extends WattnActivity implements KameraPreview.KameraTapAction {
	
	private KameraPreview preview;
	private ProgressDialog progress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        Toast.makeText(getApplicationContext(), "Tippe auf den Bildschirm, um ein Foto zu machen.", Toast.LENGTH_LONG).show();
        
        setContentView(R.layout.view_kamera);
		
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
		progress.setMessage("Zählerstand wird erkannt...");
		progress.setCancelable(false);
		progress.show();
		
		new Thread(new pictureThread()).start();
	}
	
	private class pictureThread implements RecognitionCallback, Runnable {
		
		@Override
		public void run() {
			String result = "Fehler";
			
			Bitmap bitmap = preview.mBitmap;

			int origHeight = bitmap.getHeight();
			int origWidth = bitmap.getWidth();
			
			/**
			 * Im Kamerainterface werden 3/11 des Bildes von oben und unten abgeschnitten
			 * sowie 1/8 links und rechts.
			 * 
			 * 
			 */
			
			int top = (int)(origHeight * (3.0/11.0));
			int left = (int)(origWidth * (1.0 / 8.0));
			int height = (int)(origHeight * (5.0/11.0));
			int width = (int)(origWidth * (3.0/4.0));
			
			Matrix matrix = new Matrix();
			matrix.postScale((float)(500.0 / origWidth), (float)(220.0 / origHeight));
			
			Bitmap newBitmap = Bitmap.createBitmap(bitmap, left, top, width, height);
			bitmap = null;
			System.gc();
			newBitmap = Bitmap.createScaledBitmap(newBitmap, 500, 220, false);
			newBitmap = invert(newBitmap);
			
			try {
				String root = Environment.getExternalStorageDirectory().toString();
				FileOutputStream fos = new FileOutputStream(root + "/wattnglueck/picture_crop.jpg");
				newBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
				fos.close();
				
				Engine.loadNativeLibrary();
				
				DataSource dataSource = new AssetDataSource(getAssets());
				License license = new FileLicense(dataSource, "SMRT42000003000137604180.ABBYY.License", "org.fu.swphcc.wattnglueck");
				
				Engine ocrEngine = Engine.createInstance(Arrays.asList(dataSource), license, new DataFilesExtensions(".mp3", ".mp3", ".mp3"));
				
				RecognitionConfiguration config = new RecognitionConfiguration();
				config.setRecognitionLanguages(config.getRecognitionLanguages());
				
				RecognitionManager recognitionManager = ocrEngine.getRecognitionManager(config);
				MocrLayout recognitionResult = recognitionManager.recognizeText(newBitmap, this);
				
				Engine.destroyInstance();
				
				result = recognitionResult.getText();
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
			
			Intent kameraIntent = new Intent(getApplicationContext(), ZaehlerstandKamera.class);
			kameraIntent.putExtra("value", result);
			startActivity(kameraIntent);
			finish();
		}
		

		
		public Bitmap invert(Bitmap src) {
	        Bitmap output = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());
	        int A, R, G, B;
	        int pixelColor;
	        int height = src.getHeight();
	        int width = src.getWidth();
	
		    for (int y = 0; y < height; y++) {
		        for (int x = 0; x < width; x++) {
		            pixelColor = src.getPixel(x, y);
		            A = Color.alpha(pixelColor);
		            
		            R = 255 - Color.red(pixelColor);
		            G = 255 - Color.green(pixelColor);
		            B = 255 - Color.blue(pixelColor);
		            
		            output.setPixel(x, y, Color.argb(A, R, G, B));
		        }
		    }
	
	    return output;
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
