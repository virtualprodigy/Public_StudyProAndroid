package com.virtualprodigy.studypro;

//remember to add a map feature that finds maps specifically, nothing else

import com.virtualprodigy.studypro.GradeCalculator.CalcGrade;
import com.virtualprodigy.studypro.Notes.CramSlamNotesListAdapter;
import com.virtualprodigy.studypro.StudyTimer.StudyTimer;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

public class CramSlam extends TabActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

    
        Resources res = getResources(); 
        TabHost tabHost = getTabHost();  
        TabHost.TabSpec spec;  
        Intent intent;  
        
        /*intent = new Intent().setClass(this, ExamDates.class);
        spec = tabHost.newTabSpec("examDates").setIndicator(" Exam Dates",
                          res.getDrawable(R.drawable.ic_tab_examdates))
                      .setContent(intent);
        tabHost.addTab(spec);*/
        
        intent = new Intent().setClass(this, CalcGrade.class);
        spec = tabHost.newTabSpec("gradeCalc").setIndicator("Grade Calculator",
                          res.getDrawable(R.drawable.ic_tab_calc))
                      .setContent(intent);
        tabHost.addTab(spec);
     
        intent = new Intent().setClass(this, StudyTimer.class);
        spec = tabHost.newTabSpec("studytimer").setIndicator("Study Timer",
                          res.getDrawable(R.drawable.ic_tab_studytimer))
                      .setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, CramSlamNotesListAdapter.class);
        spec = tabHost.newTabSpec("notes").setIndicator("notes",
                          res.getDrawable(R.drawable.ic_tab_notes))
                      .setContent(intent);
        tabHost.addTab(spec);
      tabHost.setCurrentTabByTag("studytimer");
       

      /*  tabHost.setCurrentTab(2);//can not add until i feel like dealing with four square

        intent = new Intent().setClass(this, libraries.class);
        spec = tabHost.newTabSpec("maps").setIndicator("Libraries",
                          res.getDrawable(R.drawable.ic_tab_notes))
                      .setContent(intent);
        tabHost.addTab(spec);*/

    
    }
    public void fin (){
    	finish();
    }
}
