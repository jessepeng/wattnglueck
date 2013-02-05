package org.fu.swphcc.wattnglueck;

import java.util.List;

import org.fu.swphcc.wattnglueck.utils.Constants;
import org.fu.swphcc.wattnglueck.utils.Database;
import org.fu.swphcc.wattnglueck.utils.Zaehlerstand;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;


public class ZaehlerstandActivity extends WattnActivity {
	TableLayout tl=null;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zaehlerstand);

		tl = (TableLayout) findViewById(R.id.TableLayout);

		Database db = new Database(this);
		
		TableRow.LayoutParams llp = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		llp.setMargins(0, 0, 4, 0);//2px right-margin
		
		for(Zaehlerstand z : db.getAll()) {
			
			//New Row
			TableRow tr = new TableRow(this);
			tr.setLayoutParams(new LayoutParams(
					LayoutParams.FILL_PARENT,
					LayoutParams.WRAP_CONTENT));
			
			//DATUM---------------------------------------------------------------------
			//New Cell
			TextView tv = new TextView(this);
			LinearLayout cell = new LinearLayout(this);
			cell.setBackgroundColor(Color.GRAY);
			cell.setLayoutParams(llp);//2px border on the right for the cell	
			//New Textview
			tv.setLayoutParams(new LayoutParams(
					LayoutParams.FILL_PARENT,
					LayoutParams.WRAP_CONTENT));
			tv.setText(Constants.DBDateFormat.format( z.getDate()));
			tv.setPadding(0, 0, 4, 3);		
			//add to Row
			cell.addView(tv);
			tr.addView(cell);
			
			//Zaehlerstand---------------------------------------------------------------
			//New Cell
			cell = new LinearLayout(this);
			cell.setBackgroundColor(Color.GRAY);
			cell.setLayoutParams(llp);//2px border on the right for the cell
			//New TextView
			tv = new TextView(this);
			tv.setLayoutParams(new LayoutParams(
					LayoutParams.FILL_PARENT,
					LayoutParams.WRAP_CONTENT));
			tv.setText(z.getZaehlerstand().toString());			
			tv.setPadding(0, 0, 4, 3);
			//Add to Row
			cell.addView(tv);
			tr.addView(cell);
			
			//Buttons-------------------------------------------------------------------
			//New Cell
			cell = new LinearLayout(this);
			//New Button
			ImageButton ib=new ImageButton(this);
			String uri = "drawable/delete.png";

			ib.setBackgroundDrawable(getResources().getDrawable(R.drawable.delete));
			//add to Row
			cell.addView(ib);
			tr.addView(cell);
			//Add Row to Table
			tl.addView(tr,new TableLayout.LayoutParams(
					LayoutParams.FILL_PARENT,
					LayoutParams.WRAP_CONTENT));
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

		return null;
	}

	@Override
	public boolean onClick(View arg0, MotionEvent arg1) {


		return false;
	}

}
