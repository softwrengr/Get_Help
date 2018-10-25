package com.techease.gethelp.activities;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.techease.gethelp.R;
import com.techease.gethelp.fragments.AvailableNowFragment;
import com.techease.gethelp.fragments.HistoryFragment;
import com.techease.gethelp.fragments.LanguagesFragment;
import com.techease.gethelp.fragments.UserProfileFragment;
import com.techease.gethelp.fragments.HomeFragment;
import com.techease.gethelp.utils.GeneralUtils;

public class NavigationDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Fragment fragment = new HomeFragment();
        getFragmentManager().beginTransaction().replace(R.id.main_container, fragment).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer,toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = new HomeFragment();
        getFragmentManager().beginTransaction().replace(R.id.main_container,fragment).addToBackStack("").commit();
        if (id == R.id.nav_home) {

        } else if (id == R.id.nav_profile) {
         GeneralUtils.connectFragmentInDrawerActivity(this,new UserProfileFragment());
        } else if (id == R.id.nav_home) {

        } else if (id == R.id.nav_language) {
            GeneralUtils.connectFragmentInDrawerActivity(this,new LanguagesFragment());
        } else if (id == R.id.nav_history) {
            GeneralUtils.connectFragmentInDrawerActivity(this,new AvailableNowFragment());
        } else if (id == R.id.nav_logout) {
           GeneralUtils.putBooleanValueInEditor(this,"loggedIn",false).commit();
           startActivity(new Intent(NavigationDrawerActivity.this,MainActivity.class));
           this.finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
