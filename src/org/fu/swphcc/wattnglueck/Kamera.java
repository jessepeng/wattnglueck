package org.fu.swphcc.wattnglueck;

import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class Kamera extends WattnActivity implements KameraPreview.KameraTapAction {
	
	private KameraPreview preview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
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
		Bitmap picture = preview.mBitmap;
		Intent intent = new Intent(this, ZaehlerstandKamera.class);
		intent.putExtra("picture", picture);
		startActivity(intent);
		finish();
	}
}
