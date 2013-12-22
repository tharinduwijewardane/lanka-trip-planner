package com.lanka.tripplanner.ui;

import java.util.List;

import com.lanka.tripplanner.R;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * @author Tharindu Wijewardane
 * 
 */
public class DetailsDialog extends Dialog {

	private Context context;
	private List<String> details;

	public DetailsDialog(Context context, List<String> details) {
		super(context);
		this.context = context;
		this.details = details;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		// set the custom dialog components - text, image and button
		TextView text = (TextView) findViewById(R.id.text);
		text.setText("");

		TableLayout tableLayout = (TableLayout) findViewById(R.id.table_layout_details);
		// tableLayout.setWeightSum(3);
		tableLayout.setColumnStretchable(0, true);
		tableLayout.setColumnStretchable(1, true);
		tableLayout.setColumnStretchable(2, true);

		TableRow tbrow = new TableRow(context);
		tbrow.setGravity(Gravity.CENTER_HORIZONTAL);
		tbrow.setBackgroundResource(android.R.drawable.list_selector_background);
		TextView tv1 = new TextView(context);
		TextView tv2 = new TextView(context);
		TextView tv3 = new TextView(context);
		// tv1.setLayoutParams(new TableRow.LayoutParams( 0, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 1 ));
		// tv2.setLayoutParams(new TableRow.LayoutParams( 0, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 1 ));
		// tv3.setLayoutParams(new TableRow.LayoutParams( 0, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 1 ));
		tv1.setText("Day No. ");
		tv2.setText("Name of Place ");
		tv3.setText("Time to Visit ");
		tv1.setTypeface(null, Typeface.BOLD);
		tv2.setTypeface(null, Typeface.BOLD);
		tv3.setTypeface(null, Typeface.BOLD);
		tbrow.addView(tv1);
		tbrow.addView(tv2);
		tbrow.addView(tv3);
		tableLayout.addView(tbrow);

		tbrow = new TableRow(context);
		tbrow.setBackgroundResource(android.R.drawable.list_selector_background);
		tv1 = new TextView(context);
		tv2 = new TextView(context);
		tv3 = new TextView(context);
		tv1.setText(" ");
		tv2.setText(" ");
		tv3.setText(" ");
		tbrow.addView(tv1);
		tbrow.addView(tv2);
		tbrow.addView(tv3);
		tableLayout.addView(tbrow);

		for (String s : details) {

			String[] sArray = s.split(":");
			if (sArray[2].equalsIgnoreCase("null")) {
				// to avoid entries which don't have "time to visit" filed
				continue;
			}

			tbrow = new TableRow(context);
			tbrow.setBackgroundResource(android.R.drawable.list_selector_background);

			tv1 = new TextView(context);
			tv2 = new TextView(context);
			tv3 = new TextView(context);
			tv1.setText(sArray[0]);
			tv2.setText(sArray[1]);
			tv3.setText(sArray[2]);
			tbrow.addView(tv1);
			tbrow.addView(tv2);
			tbrow.addView(tv3);
			tableLayout.addView(tbrow);
		}

	}

}
