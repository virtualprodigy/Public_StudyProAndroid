package com.virtualprodigy.studypro.GradeCalculator;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;


public class ExamDates extends Activity {
	  @Override
	public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);

	        TextView textview = new TextView(this);
	        textview.setText("This is the ExamDates tab");
	        setContentView(textview);
	   
	        
	  
	  }
}



