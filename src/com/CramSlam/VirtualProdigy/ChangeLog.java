package com.CramSlam.VirtualProdigy;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ChangeLog extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.changelog);
		TextView text = (TextView) findViewById(R.id.changeLogText);
		Button close = (Button)findViewById(R.id.closeChangeLog);
	
		StringBuffer changeLogText = new StringBuffer();
		changeLogText.append("Changes Made 3.0.1V")
		.append("\n Improved Screen Size  \n Support")
	    .append("\nUpdated:")
	    .append("\n Jan-19-2013 \n\n\n")
		.append("Changes Made 2.0.0V")
		.append("\n Graphic Updates \n Special Thanks To:")
	    .append("\n Elite_A.gility")
	    .append("\n Released:")
	     .append("\n June-19-2012");
		text.setText(changeLogText);
		close.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// count.cancel();
				// Log.d(TAG, "stopping timer from StudyTimer");
				finish();
			}
		});
	}
	

}
