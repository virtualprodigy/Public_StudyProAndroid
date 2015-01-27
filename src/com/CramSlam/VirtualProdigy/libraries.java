package com.CramSlam.VirtualProdigy;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class libraries  extends Activity {
//finds local libraries
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView textview = new TextView(this);
        textview.setText("This is the Maps of libraraies tab");
        setContentView(textview);
}
}