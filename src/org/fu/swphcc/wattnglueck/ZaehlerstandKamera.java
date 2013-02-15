package org.fu.swphcc.wattnglueck;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.abbyy.mobile.ocr4.AssetDataSource;
import com.abbyy.mobile.ocr4.DataSource;
import com.abbyy.mobile.ocr4.Engine;
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

public class ZaehlerstandKamera extends WattnActivity {
	
	private static final int RGB_MASK = 0x00FFFFFF;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zaehlerstand_kamera);
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		String root = Environment.getExternalStorageDirectory().toString();
		Bitmap bitmap = BitmapFactory.decodeFile(root + "/wattnglueck/picture.jpg");
		
		initViews();
		
		new WorkPictureTask().execute(bitmap);
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
	
	private class WorkPictureTask extends AsyncTask<Bitmap, Void, String>  implements RecognitionCallback {
	
		@Override
		public String doInBackground(Bitmap... params) {
			
			Bitmap bitmap = params[0];
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
			int width = (int)(origWidth * (6.0/8.0));
			
			System.gc();
			Bitmap newBitmap = Bitmap.createBitmap(bitmap, left, top, width, height);
			bitmap = null;
			System.gc();
			newBitmap = invert(newBitmap);
			
			try {
				String root = Environment.getExternalStorageDirectory().toString();
				FileOutputStream fos = new FileOutputStream(root + "/wattnglueck/picture_crop.jpg");
				newBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
				fos.close();
				
				DataSource dataSource = new AssetDataSource(getAssets());
				License license = new FileLicense(dataSource, "SMRT42000003000137604180.ABBYY.License", "org.fu.swphcc.wattnglueck.Stromzaehler");
				
				Engine ocrEngine = Engine.createInstance(Arrays.asList(dataSource), license);
				RecognitionConfiguration config = new RecognitionConfiguration();
				config.setRecognitionLanguages(ocrEngine.getLanguagesAvailableForOcr());
				RecognitionManager recognitionManager = ocrEngine.getRecognitionManager(config);
				MocrLayout result = recognitionManager.recognizeText(newBitmap, this);
				
				return result.getText();
			} catch (IOException e) {
				
			} catch (BadLicenseException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (RecognitionFailedException e) {
				e.printStackTrace();
			}
			
			return null;
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
		
		@Override
		protected void onPostExecute(String result) {
			TextView zaehlerstand = (TextView) findViewById(R.id.textKameraZaehlerstand);
			zaehlerstand.setText(result);
	    }
	}
}
