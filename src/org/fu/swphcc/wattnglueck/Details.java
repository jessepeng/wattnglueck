package org.fu.swphcc.wattnglueck;

import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.fu.swphcc.wattnglueck.utils.Database;
import org.fu.swphcc.wattnglueck.utils.Zaehlerstand;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

/**
 * 
 * Activity die mehr Details zum Stromverbrauch anzeigt
 * Beinhaltet eine Fieberkurve XD
 * @author Necros
 *
 */
public class Details extends WattnActivity{

	/**
	 * 
	 * Erstellt eine View mit dem Graphen über die vergangenen dokumentierten Zählerständen
	 * 
	 * @param ctx der aktuelle Context
	 * @return GraphicalView die einen Linearen Graphen beinhaltet
	 */
	public GraphicalView getGraphView(Context ctx) {

		//Data
		Database db = new Database(ctx);
		List<Zaehlerstand> data = db.getAll();

		TimeSeries series = new TimeSeries("durchschnittlicher Verbrauch pro Tag");

		Zaehlerstand oldz=null;
		if(data.size()>0) {
			for(Zaehlerstand z : data) {
				if(oldz!=null) {
					float tage = (z.getDate().getTime()-oldz.getDate().getTime())/ 86400000f;
					if(tage>0) {
						series.add(z.getDate(), Math.round(((z.getZaehlerstand()-oldz.getZaehlerstand())/tage)*100.0)/100.0);
					}
				}
				oldz=z;
			}


			XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
			dataset.addSeries(series);
			
			
			XYSeriesRenderer renderer = new XYSeriesRenderer();
			renderer.setColor(Color.parseColor("#68A423"));
			renderer.setDisplayChartValues(false);
			renderer.setChartValuesTextSize(25);
			renderer.setPointStyle(PointStyle.POINT);
			renderer.setFillBelowLine(true);
			renderer.setFillBelowLineColor(Color.parseColor("#97FF82"));

			XYMultipleSeriesRenderer mrenderer = new XYMultipleSeriesRenderer();
			mrenderer.addSeriesRenderer(renderer);
			mrenderer.setApplyBackgroundColor(true);
			mrenderer.setBackgroundColor(Color.WHITE);
			mrenderer.setMarginsColor(Color.WHITE);
			mrenderer.setAxesColor(Color.DKGRAY);
			mrenderer.setXLabelsColor(Color.DKGRAY);
			mrenderer.setYLabelsColor(0,Color.DKGRAY);
			mrenderer.setLabelsTextSize(30);
			mrenderer.setAxisTitleTextSize(35);
			mrenderer.setLegendTextSize(25);
			mrenderer.setMargins(new int[] { 50, 75, 75, 30 });
			
			mrenderer.setZoomButtonsVisible(true);
			mrenderer.setPointSize(10);

			mrenderer.setYLabelsAlign(Align.RIGHT);
			mrenderer.setYTitle("KWh");
			mrenderer.setXTitle("Datum");
			mrenderer.setZoomEnabled(true);
			mrenderer.setInScroll(false);
			mrenderer.setYAxisMin(0);
			mrenderer.setYAxisMax(10);

			GraphicalView view = ChartFactory.getTimeChartView(ctx, dataset, mrenderer,"dd.MM.yyyy");


			return view;
		}
		return null;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_details);

		LinearLayout layout = (LinearLayout) findViewById(R.id.chart);

		GraphicalView view = getGraphView(this);
		if(view!=null)
			layout.addView(view,LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT);

		getActionBar().setDisplayHomeAsUpEnabled(true);
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
