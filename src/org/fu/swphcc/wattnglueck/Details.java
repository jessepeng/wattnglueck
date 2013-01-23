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

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

public class Details extends Activity {

	//create Graph
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

			GraphicalView view = ChartFactory.getLineChartView(ctx, dataset, mrenderer);


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
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_details, menu);



		return true;
	}

}
