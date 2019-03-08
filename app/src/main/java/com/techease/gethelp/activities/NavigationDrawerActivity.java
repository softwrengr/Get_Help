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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.login.LoginManager;
import com.techease.gethelp.R;
import com.techease.gethelp.fragments.AvailableSituationFragment;
import com.techease.gethelp.fragments.ClientRequestListFragment;
import com.techease.gethelp.fragments.DriverJobsListFragment;
import com.techease.gethelp.fragments.LanguagesFragment;
import com.techease.gethelp.fragments.SettingsFragment;
import com.techease.gethelp.fragments.UserProfileFragment;
import com.techease.gethelp.utils.GeneralUtils;

public class NavigationDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    NavigationView navigationView;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        type = GeneralUtils.getSharedPreferences(NavigationDrawerActivity.this).getString("userType", "");
        if (GeneralUtils.getSharedPreferences(NavigationDrawerActivity.this).getBoolean("loggedIn", false) && type.equals("Driver")) {
            Fragment fragment = new DriverJobsListFragment();
            getFragmentManager().beginTransaction().replace(R.id.main_container, fragment).commit();
            hideItem();
        } else {
            Fragment fragment = new AvailableSituationFragment();
            getFragmentManager().beginTransaction().replace(R.id.main_container, fragment).commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
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

        Log.d("zma user id", String.valueOf(GeneralUtils.getUserID(NavigationDrawerActivity.this)));
        // Handle navigation view item clicks here.
        if (type.equals("Driver")) {
            int id = item.getItemId();
            Fragment fragment = new DriverJobsListFragment();
            getFragmentManager().beginTransaction().replace(R.id.main_container, fragment).addToBackStack("").commit();
            if (id == R.id.nav_home) {
                getFragmentManager().beginTransaction().replace(R.id.main_container, fragment).addToBackStack("").commit();

            } else if (id == R.id.nav_logout) {
                GeneralUtils.putBooleanValueInEditor(this, "loggedIn", false).commit();
                startActivity(new Intent(NavigationDrawerActivity.this, MainActivity.class));
                LoginManager.getInstance().logOut();
                this.finish();
            } else if (id == R.id.nav_profile) {
                GeneralUtils.connectFragmentInDrawerActivity(this, new UserProfileFragment());
            } else if (id == R.id.nav_language) {
                GeneralUtils.connectFragmentInDrawerActivity(this, new LanguagesFragment());
            }
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        } else {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                Fragment fragment = new AvailableSituationFragment();
                getFragmentManager().beginTransaction().replace(R.id.main_container, fragment).addToBackStack("").commit();
            } else if (id == R.id.nav_profile) {
                GeneralUtils.connectFragmentInDrawerActivity(this, new UserProfileFragment());
            } else if (id == R.id.nav_language) {
                GeneralUtils.connectFragmentInDrawerActivity(this, new LanguagesFragment());
            } else if (id == R.id.nav_history) {
                GeneralUtils.connectFragmentInDrawerActivity(this, new ClientRequestListFragment());
            } else if (id == R.id.nav_logout) {
                GeneralUtils.putBooleanValueInEditor(this, "loggedIn", false).commit();
                startActivity(new Intent(NavigationDrawerActivity.this, MainActivity.class));
                LoginManager.getInstance().logOut();
                this.finish();
            } else if (id == R.id.nav_setting) {
                GeneralUtils.connectFragmentInDrawerActivity(this, new SettingsFragment());
            }

            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }

    private void hideItem() {
        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.nav_setting).setVisible(false);
        nav_Menu.findItem(R.id.nav_history).setVisible(false);
        nav_Menu.findItem(R.id.nav_setting).setVisible(false);
    }
}
