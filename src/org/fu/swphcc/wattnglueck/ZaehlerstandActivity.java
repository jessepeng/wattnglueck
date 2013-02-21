package org.fu.swphcc.wattnglueck;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.fu.swphcc.wattnglueck.utils.Constants;
import org.fu.swphcc.wattnglueck.utils.Database;
import org.fu.swphcc.wattnglueck.utils.Zaehlerstand;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.LayoutInflater.Factory;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;


public class ZaehlerstandActivity extends WattnActivity {

	TextView edited=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zaehlerstand);

		initViews();
		
		TableLayout table = (TableLayout) findViewById( R.id.table);
		Typeface customFont = Typeface.createFromAsset(getAssets(), getString(R.string.setting_fontfilename));
		
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
				cell.setBackground(getResources().getDrawable(R.drawable.button_background));
				cell.setLayoutParams(llp);//2px border on the right for the cell

				TextView tv = new TextView(this);
				tv.setTypeface(customFont);
				tv.setTextColor(Color.WHITE);
				tv.setText(Constants.ViewDateFormat.format(z.getDate()));
				tv.setPadding(10, 0, 4, 3);
				tv.setClickable(true);

				final OnDateSetListener odsl=new OnDateSetListener()
				{
//					Zaehlerstand zstand=z;
					public void onDateSet(DatePicker arg0, int year, int month, int dayOfMonth) {
						if(edited!=null) {
							try {
								DecimalFormat df2 = new DecimalFormat( "00" );		
								edited.setText( df2.format(dayOfMonth)+"."+df2.format(month+1)+"."+year);

								z.setDate(Constants.ViewDateFormat.parse( df2.format(dayOfMonth)+"."+df2.format(month+1)+"."+year));
								Database db = new Database(ZaehlerstandActivity.this);
								db.updateZaehlerstand(z);
							} catch (ParseException e) {

							}

						}
					}

				};

				tv.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						edited=(TextView)v;
						Calendar cal=Calendar.getInstance();

						try {
							cal.setTime(Constants.ViewDateFormat.parse(edited.getText().toString()));
						} catch (ParseException e) {

						}
						DatePickerDialog datePickDiag=new DatePickerDialog(ZaehlerstandActivity.this,odsl,cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH));
						datePickDiag.getDatePicker().setCalendarViewShown(false);
						datePickDiag.setCancelable(true);
						datePickDiag.setTitle("");
						datePickDiag.getLayoutInflater().setFactory(new Factory() {

							@Override
							public View onCreateView(String name, Context context,
									AttributeSet attrs) {
								if (name.equalsIgnoreCase("Button")) {
				                	try {
				                        LayoutInflater li = LayoutInflater.from(context);
				                        final View view = li.createView(name, null, attrs);
				                        ((Button) view).setTextSize(20); 
				                        
				                        // set the text color
				                        Typeface face = Typeface.createFromAsset(
				                                getAssets(),getString(R.string.setting_fontfilename));     
				                        ((Button) view).setTypeface(face);
				                        ((Button) view).setTextColor(Color.parseColor("#5e625b"));
				                       
				                        return view;
				                    } catch (InflateException e) {
				                        //Handle any inflation exception here
				                    } catch (ClassNotFoundException e) {
				                        //Handle any ClassNotFoundException here
				                    }
				                }
				                return null;
							}
							
						});
						
						datePickDiag.show();

					}

				});
				cell.addView(tv);
				tr.addView(cell);

				//Zaehlerstand
				//New Cell
				cell = new LinearLayout(this);
				//cell.setBackgroundColor(Color.GRAY);
				cell.setBackground(getResources().getDrawable(R.drawable.button_background));
				cell.setLayoutParams(llp);//2px border on the right for the cell

				tv = new TextView(this);
				tv.setTypeface(customFont);
				tv.setTextColor(Color.WHITE);
				tv.setText(z.getZaehlerstand().toString());
				tv.setPadding(10, 0, 4, 3);
				tv.setClickable(true);
				tv.setOnClickListener(new OnClickListener() {
//					Zaehlerstand zstand=z;

					@Override
					public void onClick(View v) {

						Intent i = new Intent(v.getContext(), ZaehlerstandUpdate.class);
						i.putExtra("id", z.getId());
						i.putExtra("zaehlerstand", z.getZaehlerstand());
						i.putExtra("datum", z.getDate());
						startActivityForResult(i, 1);
						edited=(TextView) v;
					}
				});

				cell.addView(tv);
				tr.addView(cell);

				//Delete-Button
				cell = new LinearLayout(this);

				cell.setLayoutParams(llp);//2px border on the right for the cell

				ImageButton ib = new ImageButton(this);
				ib.setImageDrawable(getResources().getDrawable(R.drawable.delete));
				ib.setPadding(10, 3, 7, 3);
				ib.setBackground(null);
				ib.setOnClickListener(new OnClickListener() {
//					Zaehlerstand zstand=z;

					@Override
					public void onClick(View v) {
						db.deleteZaehlerstand(z);
						TableLayout table = (TableLayout) ((Activity) v.getContext()).findViewById( R.id.table);
						if( v.getParent().getParent() instanceof TableRow)
							table.removeView((TableRow) v.getParent().getParent());
					}
				});

				cell.addView(ib);

				tr.addView(cell);

				table.addView(tr);
			}
		}
	}

	@Override
	protected List<TextView> getTextViewsForFont() {
		return Arrays.asList((TextView) findViewById(R.id.zaehlerstand_datum), (TextView) findViewById(R.id.zaehlerstand_stand));
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

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == 1) {

			if(resultCode == RESULT_OK){
				Zaehlerstand z=new Zaehlerstand();
				z.setId(data.getIntExtra("id", -1));

				z.setDate((Date)data.getSerializableExtra("datum"));
				z.setZaehlerstand(data.getFloatExtra("zaehlerstand", 0f));
				if(edited!=null) {
					edited.setText(z.getZaehlerstand().toString());
					Database db = new Database(ZaehlerstandActivity.this);
					db.updateZaehlerstand(z);
					edited=null;
				}

			}

			if (resultCode == RESULT_CANCELED) {


			}
		}
	}
}
