package org.fu.swphcc.wattnglueck;

import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.fu.swphcc.wattnglueck.utils.Database;
import org.fu.swphcc.wattnglueck.utils.Zaehlerstand;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
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

		TimeSeries series = new TimeSeries("Verbrauch");

		if(data.size()>0) {
			for(Zaehlerstand z : data) {
				series.add(z.getDate(),z.getZaehlerstand());
			}
			
			System.out.println(series.getItemCount());
			
			XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
			dataset.addSeries(series);

			XYSeriesRenderer renderer = new XYSeriesRenderer();
			renderer.setColor(Color.GREEN);
			
			XYMultipleSeriesRenderer mrenderer = new XYMultipleSeriesRenderer();
			mrenderer.addSeriesRenderer(renderer);

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
		//das hiermacht probleme warum auch immer...
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
