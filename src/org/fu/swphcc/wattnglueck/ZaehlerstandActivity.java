package org.fu.swphcc.wattnglueck;

import java.util.List;

import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;


public class ZaehlerstandActivity extends WattnActivity {

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
		 setContentView(R.layout.activity_zaehlerstand);
		return true;
	}

}
