package org.fu.swphcc.wattnglueck;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class ZaehlerstandKamera extends WattnActivity {
	
	private Bitmap bitmap;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zaehlerstand_kamera);
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		Intent intent = getIntent();
		bitmap = intent.getParcelableExtra("picture");
		
		initViews();
		
		workWithPicture();
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
	
	private void workWithPicture() {
		
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
		
		Bitmap newBitmap = Bitmap.createBitmap(bitmap, top, left, width, height);
		
		try {
			String root = Environment.getExternalStorageDirectory().toString();
			FileOutputStream fos = new FileOutputStream(root + "/wattnglueck/picture.jpg");
			newBitmap.compress(Bitmap.CompressFormat.JPEG, 50, fos);
			fos.close();
		} catch (IOException e) {
			
		}
	}

}
