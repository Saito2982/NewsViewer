package com.example.hiroyki.newsviewer;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CheckBox;

import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterCore;

import java.util.ArrayList;

import static com.example.hiroyki.newsviewer.Tab_setting.loadList;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, TabHost.OnTabChangeListener {
    // TabHost
    private TabHost mTabHost;
    // Last selected tabId
    private String mLastTabId;

    public Tab_setting tab;
    public ArrayList alertList;
    public static ArrayList check_flag = new ArrayList<String>();

    private String CONSUMER_KEY ="PZBtgXybcWIo1jaRv89fk0WUD";
    private String CONSUMER_SECRET ="s8j5hhCw8q9tV6S7WcO5dKi2BfHc6Dn5sxKEOzicMsARJ5lDC9";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        Resources res = getResources();

        Log.v("LOGIN",CONSUMER_KEY);

        alertList = loadList(MainActivity.this, "data");
        if (alertList.size() == 0) {
            alertList.add("RSSが設定されていません");
            alertList.add("");
        }

        Log.v("TAG", String.valueOf(alertList));

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig(CONSUMER_KEY, CONSUMER_SECRET))
                .debug(true)
                .build();
        Twitter.initialize(config);


        mTabHost = (TabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup();


        /* Tab0 設定 */
        TabHost.TabSpec tab0 = mTabHost.newTabSpec("tab0");
        tab0.setContent(new DummyTabFactory(this));

        /* Tab1 設定 */
        TabHost.TabSpec tab1 = mTabHost.newTabSpec("tab1");
        tab1.setContent(new DummyTabFactory(this));

        // Tab2 設定
        TabHost.TabSpec tab2 = mTabHost.newTabSpec("tab2");
        tab2.setContent(new DummyTabFactory(this));

        // Tab3 設定
        TabHost.TabSpec tab3 = mTabHost.newTabSpec("tab3");
        tab3.setContent(new DummyTabFactory(this));

        // Tab4 設定
        TabHost.TabSpec tab4 = mTabHost.newTabSpec("tab4");
        tab4.setContent(new DummyTabFactory(this));

        /* Tab5 設定 */
        TabHost.TabSpec tab5 = mTabHost.newTabSpec("tab5");
        tab5.setContent(new DummyTabFactory(this));

        // Tab6 設定
        TabHost.TabSpec tab6 = mTabHost.newTabSpec("tab6");
        tab6.setContent(new DummyTabFactory(this));

        // Tab7 設定
        TabHost.TabSpec tab7 = mTabHost.newTabSpec("tab7");
        tab7.setContent(new DummyTabFactory(this));

        // Tab8 設定
        TabHost.TabSpec tab8 = mTabHost.newTabSpec("tab8");
        tab8.setContent(new DummyTabFactory(this));

        //check_flag確認
        check_flag = loadList(MainActivity.this, "flag");
        if(check_flag.size() != 0) {
            tab0.setIndicator("おすすめ記事");
            mTabHost.addTab(tab0);
            // 初期タブ選択
            onTabChanged("tab0");
        }else {
            // 初期タブ選択
            onTabChanged("tab1");
        }

        //tabの追加
        if(alertList.size() >= 2){
            tab1.setIndicator((CharSequence) alertList.get(0));
            mTabHost.addTab(tab1);
        }if(alertList.size() >= 4){
            tab2.setIndicator((CharSequence) alertList.get(2));
            mTabHost.addTab(tab2);
        }if(alertList.size() >= 6){
            tab3.setIndicator((CharSequence) alertList.get(4));
            mTabHost.addTab(tab3);
        }if(alertList.size() >= 8){
            tab4.setIndicator((CharSequence) alertList.get(6));
            mTabHost.addTab(tab4);
        }if(alertList.size() >= 10){
            tab5.setIndicator((CharSequence) alertList.get(8));
            mTabHost.addTab(tab5);
        }if(alertList.size() >= 12){
            tab6.setIndicator((CharSequence) alertList.get(10));
            mTabHost.addTab(tab6);
        }if(alertList.size() >= 14){
            tab7.setIndicator((CharSequence) alertList.get(12));
            mTabHost.addTab(tab7);
        }if(alertList.size() >= 16){
            tab8.setIndicator((CharSequence) alertList.get(14));
            mTabHost.addTab(tab8);
        }

        // タブ変更時イベントハンドラ
        mTabHost.setOnTabChangedListener(this);

    }


    /*
     * android:id/tabcontent のダミーコンテンツ
     */
    public static class DummyTabFactory implements TabHost.TabContentFactory {

        /* Context */
        private final Context mContext;

        DummyTabFactory(Context context) {
            mContext = context;
        }

        @Override
        public View createTabContent(String tag) {
            View v = new View(mContext);
            return v;
        }
    }

    /*
    * タブの選択が変わったときに呼び出される
    * @Override
    */
    public void onTabChanged(String tabId) {
        Log.d("TAB_FRAGMENT_LOG","tabId:" + tabId);
        if(mLastTabId != tabId){
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

            if("tab1" == tabId){
                fragmentTransaction.replace(R.id.realtabcontent, new Tab1Fragment((String) alertList.get(1)));

            }else if("tab2" == tabId){
                fragmentTransaction.replace(R.id.realtabcontent, new Tab1Fragment((String) alertList.get(3)));

            }else if("tab3" == tabId){
                fragmentTransaction.replace(R.id.realtabcontent, new Tab1Fragment((String) alertList.get(5)));

            }else if("tab4" == tabId){
                fragmentTransaction.replace(R.id.realtabcontent, new Tab1Fragment((String) alertList.get(7)));

            }else if("tab5" == tabId){
                fragmentTransaction.replace(R.id.realtabcontent, new Tab1Fragment((String) alertList.get(9)));

            }else if("tab6" == tabId){
                fragmentTransaction.replace(R.id.realtabcontent, new Tab1Fragment((String) alertList.get(11)));

            }else if("tab7" == tabId){
                fragmentTransaction.replace(R.id.realtabcontent, new Tab1Fragment((String) alertList.get(13)));

            }else if("tab8" == tabId){
                fragmentTransaction.replace(R.id.realtabcontent, new Tab1Fragment((String) alertList.get(15)));

            }else if("tab0" == tabId){
                fragmentTransaction.replace(R.id.realtabcontent, new RecomFragment(alertList));

            }
            mLastTabId = tabId;
            fragmentTransaction.commit();
        }
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
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
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

        if (id == R.id.nav_home) {
            // Handle the camera action
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_gallery) {
            Intent intent = new Intent(this, SettingActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_slideshow) {
            if (TwitterCore.getInstance().getSessionManager().getActiveSession() == null) {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
            } else {
                Toast toast = Toast.makeText(MainActivity.this, "ログイン中", Toast.LENGTH_LONG);
                toast.show();
                Intent intent = new Intent(this, TimelineActivity.class);
                startActivity(intent);
            }

        } else if (id == R.id.nav_setting) {
            Intent intent = new Intent(MainActivity.this, Tab_setting.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}