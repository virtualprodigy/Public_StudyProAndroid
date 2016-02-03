package com.virtualprodigy.studypro;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.virtualprodigy.studypro.Adapters.NavigationDrawerAdapter;
import com.virtualprodigy.studypro.StudyTimer.StudyTimerFragment;

import java.util.ArrayList;
import java.util.Arrays;

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

    private Fragment currentFragment;
    private fragmentsEnum currentFragEnum = fragmentsEnum.studyTimer;

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

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navDrawer = (ListView) findViewById(R.id.left_drawer);
        fragmentManager = getSupportFragmentManager();
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setupNavDrawer();
    }

    private void setupNavDrawer() {

        navTitles = new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.nav_items)));
        navImages = context.getResources().obtainTypedArray(R.array.nav_icons);

        navAdapter = new NavigationDrawerAdapter(context, navTitles, navImages);
        navDrawer.setAdapter(navAdapter);
        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_bar_open, R.string.nav_bar_close);
        mDrawerToggle.syncState();
        drawerLayout.setDrawerListener(mDrawerToggle);

        final Fragment studyTimer = new StudyTimerFragment();
        currentFragment = studyTimer;
        //Add fragments
        fragmentManager
                .beginTransaction()
                .add(R.id.DisplayFragment,
                        currentFragment,
                        fragmentsEnum.studyTimer.toString())
                .addToBackStack(null)
                .commitAllowingStateLoss();

        //Set the default selection to the homepage
        navDrawer.setItemChecked(0, true);
        navDrawer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                fragmentManager = getSupportFragmentManager();

                switch (position) {
                    case 0:
                        try {
                            Log.i(TAG, "Switch to Study Timer Fragment");
                            currentFragEnum = fragmentsEnum.studyTimer;
                            fragmentManager.beginTransaction().hide(currentFragment)
                                    .show(fragmentManager.findFragmentByTag(fragmentsEnum.studyTimer.toString())).commitAllowingStateLoss();
                            currentFragment = fragmentManager.findFragmentByTag(fragmentsEnum.studyTimer.toString());
                            navDrawer.setItemChecked(position, true);
                        } catch (Exception e) {
                            Log.e(TAG, e.getMessage(), e);
                        }

                        break;

                    case 1:
                        break;

                    case 2:
                        break;

                }
                drawerLayout.closeDrawers();
            }
        });

    }

}
