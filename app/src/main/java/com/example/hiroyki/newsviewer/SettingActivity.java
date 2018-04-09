package com.example.hiroyki.newsviewer;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.twitter.sdk.android.core.TwitterCore;

import java.util.ArrayList;

import static com.example.hiroyki.newsviewer.Tab_setting.loadList;
import static com.example.hiroyki.newsviewer.Tab_setting.saveList;


public class SettingActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static ArrayList check_flag = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_setting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //check_flag確認
        check_flag = loadList(SettingActivity.this, "flag");
        if(check_flag.size() != 0) {
            CheckBox chkbox = (CheckBox)findViewById(R.id.checkbox1);
            chkbox.setChecked(true);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
            Intent intent = new Intent(SettingActivity.this, MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_gallery) {
            Intent intent = new Intent(this, SettingActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_slideshow) {
            if (TwitterCore.getInstance().getSessionManager().getActiveSession() == null) {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
            } else {
                Toast toast = Toast.makeText(SettingActivity.this, "ログイン中", Toast.LENGTH_LONG);
                toast.show();
                Intent intent = new Intent(this, TimelineActivity.class);
                startActivity(intent);
            }

        } else if (id == R.id.nav_setting) {
            Intent intent = new Intent(SettingActivity.this, Tab_setting.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onCheckboxClicked(View view) {
        final boolean checked = ((CheckBox) view).isChecked();
        switch(view.getId()) {
            case R.id.checkbox1:
                if (checked) {
                    // チェックボックス1がチェックされる
                    check_flag.add("1");
                    saveList(SettingActivity.this ,"flag" , check_flag);
                } else {
                    // チェックボックス1のチェックが外される
                    check_flag.clear();
                    saveList(SettingActivity.this, "flag", check_flag);
                }
                break;
        }
    }
}
