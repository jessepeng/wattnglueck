package org.fu.swphcc.wattnglueck;

import java.util.List;

import org.fu.swphcc.wattnglueck.utils.Constants;
import org.fu.swphcc.wattnglueck.utils.Database;
import org.fu.swphcc.wattnglueck.utils.Zaehlerstand;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;


public class ZaehlerstandActivity extends WattnActivity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zaehlerstand);

		TableLayout table = (TableLayout) findViewById( R.id.table);
		final Database db = new Database(this);
		if(db.getAll()!=null) {
			for(final Zaehlerstand z : db.getAll()) {
				TableRow tr = new TableRow(this);

				tr.setPadding(0, 0, 0, 2); //Border between rows

				TableRow.LayoutParams llp = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
				llp.setMargins(0, 0, 2, 0);//2px right-margin

				//Datum
				//New Cell
				LinearLayout cell = new LinearLayout(this);
				cell.setBackgroundColor(Color.GRAY);
				cell.setLayoutParams(llp);//2px border on the right for the cell

				TextView tv = new TextView(this);
				tv.setText(Constants.ViewDateFormat.format(z.getDate()));
				tv.setPadding(10, 0, 4, 3);

				cell.addView(tv);
				tr.addView(cell);

				//Zaehlerstand
				//New Cell
				cell = new LinearLayout(this);
				cell.setBackgroundColor(Color.GRAY);
				cell.setLayoutParams(llp);//2px border on the right for the cell

				tv = new TextView(this);
				tv.setText(z.getZaehlerstand().toString());
				tv.setPadding(5, 0, 4, 3);

				cell.addView(tv);
				tr.addView(cell);
				//Delete-Button
				cell = new LinearLayout(this);

				cell.setLayoutParams(llp);//2px border on the right for the cell

				ImageButton ib = new ImageButton(this);
				ib.setImageDrawable(getResources().getDrawable(R.drawable.delete));
				ib.setPadding(2, 0, 4, 0);
				ib.setBackground(null);
				ib.setOnClickListener(new OnClickListener() {
					Zaehlerstand zstand=z;

					@Override
					public void onClick(View v) {
						db.deleteZaehlerstand(z);
						TableLayout table = (TableLayout) ((Activity) v.getContext()).findViewById( R.id.table);
						if( v.getParent().getParent() instanceof TableRow)
							table.removeView((TableRow) v.getParent().getParent());
					}
				});

				cell.addView(ib);
				
				tr.addView(cell,20,20);

				table.addView(tr);
			}
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
