package com.virtualprodigy.studypro;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import com.virtualprodigy.studypro.Adapters.NavigationDrawerAdapter;

import java.util.ArrayList;

public class StudyProActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getName().toString();
    public Context context;
    private Resources res;

    private ListView navDrawer;
    private ActionBarDrawerToggle mDrawerToggle;
    private ArrayList<String> navTitles;
    private TypedArray navImages;

    private NavigationDrawerAdapter navAdapter;
    private FragmentManager fragmentManager;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;

    private fragmentsEnum currentFragment = fragmentsEnum.studyTimer;

    public enum fragmentsEnum {
        studyTimer,
        notes,
        gradeCalculator
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        context = this;
        res = context.getResources();
        ((StudyProApplication) getApplication()).getComponent().inject(this);
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
