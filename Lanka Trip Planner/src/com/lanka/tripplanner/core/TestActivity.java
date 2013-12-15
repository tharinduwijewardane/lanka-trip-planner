package com.lanka.tripplanner.core;

import java.util.Iterator;
import java.util.LinkedList;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;

public class TestActivity extends Activity {

//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_test);
//
//		CoreMain aCoreMain = new CoreMain();
//		LinkedList<Location> road = aCoreMain.searchPath(5, 79.999413,
//				7.089895, null, null);
//		Iterator<Location> ittr = road.iterator();
//		String path = "-";
//		while (ittr.hasNext()) {
//			Location roadMark = ittr.next();
//
//			path = path + roadMark.getName() + "-";
//
//			// if a day ends print ***
//			if (roadMark.isDestinationForDay()) {
//				path = path + " *** ";
//			}
//		}
//
//		System.out.println(path);
//		TextView display = (TextView) findViewById(R.id.tvDisplay);
//		display.setText(path);
//	}
//
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.test, menu);
//		return true;
//	}

}
