package org.fu.swphcc.wattnglueck;

import java.util.List;

import android.app.Fragment;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public abstract class WattnFragment extends Fragment {
	
	public abstract TextView getWeiterButton();
	
	public abstract List<TextView> getTextViewsForFont();

	public abstract boolean onClick(View arg0, MotionEvent arg1);
}
