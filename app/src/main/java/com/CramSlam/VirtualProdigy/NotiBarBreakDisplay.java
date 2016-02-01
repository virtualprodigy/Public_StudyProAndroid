package com.CramSlam.VirtualProdigy;

import android.app.Activity;
import android.os.Bundle;

import android.widget.TextView;

public class NotiBarBreakDisplay extends Activity{
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	//	Log.d("displayBreak","it opened");
	setContentView(R.layout.nbdisplay);
	
	((TextView) findViewById(R.id.message))
	.setText(getIntent().getStringExtra("message"));
	}
}
