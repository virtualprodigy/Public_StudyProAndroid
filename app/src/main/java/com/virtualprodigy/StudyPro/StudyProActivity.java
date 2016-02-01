package com.virtualprodigy.studypro;

//remember to add a map feature that finds maps specifically, nothing else

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class StudyProActivity extends AppCompatActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

//
//        Resources res = getResources();
//        TabHost tabHost = getTabHost();
//        TabHost.TabSpec spec;
//        Intent intent;
//
//        intent = new Intent().setClass(this, CalcGrade.class);
//        spec = tabHost.newTabSpec("gradeCalc").setIndicator("Grade Calculator",
//                          res.getDrawable(R.drawable.ic_tab_calc))
//                      .setContent(intent);
//        tabHost.addTab(spec);
//
//        intent = new Intent().setClass(this, StudyTimer.class);
//        spec = tabHost.newTabSpec("studytimer").setIndicator("Study Timer",
//                          res.getDrawable(R.drawable.ic_tab_studytimer))
//                      .setContent(intent);
//        tabHost.addTab(spec);
//
//        intent = new Intent().setClass(this, CramSlamNotesListAdapter.class);
//        spec = tabHost.newTabSpec("notes").setIndicator("notes",
//                          res.getDrawable(R.drawable.ic_tab_notes))
//                      .setContent(intent);
//        tabHost.addTab(spec);
//      tabHost.setCurrentTabByTag("studytimer");
//
    }
    public void fin (){
    	finish();
    }
}
