package org.fu.swphcc.wattnglueck;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

public class Kamera extends WattnActivity implements KameraPreview.KameraTapAction {
	
	private KameraPreview preview;

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
		new Thread(new Runnable() {

			@Override
			public void run() {
				Bitmap picture = preview.mBitmap;
				try {
					File pictureFile = new File(Environment.getExternalStorageDirectory() + "/wattnglueck/picture.jpg");
					pictureFile.mkdirs();
					if (pictureFile.exists()) pictureFile.delete();
					FileOutputStream fileStream = new FileOutputStream(pictureFile);
					picture.compress(Bitmap.CompressFormat.JPEG, 100, fileStream);
					fileStream.close();
					picture = null;
					System.gc();
				} catch (IOException e) {
					
				}
				startActivity(new Intent(getApplicationContext(), ZaehlerstandKamera.class));
				finish();
			}
			
		}).start();
	}
}
