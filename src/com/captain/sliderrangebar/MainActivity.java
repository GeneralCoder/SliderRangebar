package com.captain.sliderrangebar;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

import com.captain.sliderrangebar.SliderRangeBar.OnSliderRangeBarChangeListener;

/**
 * @author captain
 *         <p>
 *         Email: captainjack6688@gmail.com
 *         </p>
 */
public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		final TextView textView = (TextView) findViewById(R.id.out_pannel);

		SliderRangeBar sliderRangeBar = (SliderRangeBar) findViewById(R.id.sliderrangerbar);
		final String[] values = new String[] { "10米", "20米", "30米", "40米", "50米" };
		sliderRangeBar.initScale(values);
		// sliderRangeBar.setScaleValue(minIndex, maxIndex);手动调整刻度区间
		sliderRangeBar.setOnSliderRangeBarChangeListener(new OnSliderRangeBarChangeListener() {

			@Override
			public void onChange(SliderRangeBar sliderRangeBar, int minIndex, int maxIndex) {
				// TODO Auto-generated method stub
				textView.setText("min:" + values[minIndex] + ", max:" + values[maxIndex]);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
