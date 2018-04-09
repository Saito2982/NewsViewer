package com.example.hiroyki.newsviewer;

import android.content.Intent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.twitter.sdk.android.core.TwitterCore;

import org.json.JSONArray;
import java.util.ArrayList;

public class Tab_setting extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ListView myListView;

    public static ArrayList alertList = new ArrayList<String>();
    public static ArrayList check_flag = new ArrayList<String>();
    ArrayAdapter adapter;

    /**
     * 画面生成時のハンドラ
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        //drawerの作成
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        Log.v("TAG", String.valueOf(alertList));

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //RSSリストの取得
        alertList = loadList(this, "data");
        Log.v("List", String.valueOf(alertList));

        //RSSリスト表示リストビューID
        myListView = (ListView) findViewById(R.id.rss_menu);

        Setting_Item item= new Setting_Item();
        item.setData(alertList);

        //アダプタのセット
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,alertList);

        // ListViewに表示
        myListView.setAdapter(adapter);

        //追加ボタン設定
        Button button = findViewById(R.id.button);
        button.setOnClickListener( new AddListener() );

        //AllDeleteボタン設定
        Button all_delete = findViewById(R.id.all_delete);
        all_delete.setOnClickListener( new DeleteListener() );

        //アイテムクリック時のイベントを追加
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                // カスタムビューを設定
                LayoutInflater inflater = (LayoutInflater) Tab_setting.this.getSystemService(LAYOUT_INFLATER_SERVICE);
                final View layout = inflater.inflate( R.layout.dialog_layoyt2, (ViewGroup) findViewById(R.id.dialogcustom));

                AlertDialog.Builder builder = new AlertDialog.Builder(Tab_setting.this);
                builder.setTitle("RSSの編集"); builder.setView(layout);
                final EditText text = (EditText) layout.findViewById(R.id.customDialog1);
                Log.v("TAG", (String) alertList.get(position));
                text.setText((String) alertList.get(position), TextView.BufferType.NORMAL);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // OK ボタンクリック処理
                        String msg = text.getText().toString();

                        // 一覧に追加するメッセージを組み立て
                        //String msg_3 = msg.substring(msg.length() - 3);
                        if(msg.length() == 0){
                            AlertDialog.Builder builder = new AlertDialog.Builder(Tab_setting.this);
                            builder.setMessage("入力してください")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            // ボタンをクリックしたときの動作
                                        }
                                    });
                            builder.show();
                        }else{
                            // ListViewに項目を追加
                            alertList.add(position, msg );
                            alertList.remove(position+1);
                            saveList(Tab_setting.this ,"data" , alertList);
                            myListView.setAdapter(adapter);
                        }
                    }
                });
                builder.setNegativeButton("キャンセル", null)
                        .setCancelable(true);
                // 表示
                builder.create().show();
            }
        });

        // アイテム長押しクリック時のイベントを追加
        myListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Tab_setting.this);
                builder.setMessage("削除しますか？")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (position % 2 == 0){
                                    // remove item from ArrayList
                                    alertList.remove(position+1);
                                    alertList.remove(position);
                                }else{
                                    alertList.remove(position);
                                    alertList.remove(position-1);
                                }
                                // update ListView
                                adapter.notifyDataSetChanged();
                                Toast.makeText(Tab_setting.this, "削除しました", Toast.LENGTH_SHORT).show();
                                saveList(Tab_setting.this,"data" , alertList);
                            }
                        })
                        .setNegativeButton("キャンセル", null)
                        .setCancelable(true);
                // show dialog
                builder.show();
                return true;
            }
        });

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
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
            Intent intent = new Intent(Tab_setting.this, MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_gallery) {
            Intent intent = new Intent(this, SettingActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_slideshow) {
            if (TwitterCore.getInstance().getSessionManager().getActiveSession() == null) {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
            } else {
                Toast toast = Toast.makeText(Tab_setting.this, "ログイン中", Toast.LENGTH_LONG);
                toast.show();
                Intent intent = new Intent(this, TimelineActivity.class);
                startActivity(intent);
            }

        } else if (id == R.id.nav_setting) {
            Intent intent = new Intent(Tab_setting.this, Tab_setting.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private  class  DeleteListener implements  View.OnClickListener {

        public void onClick(View view) {
            Log.v("TAG", String.valueOf(alertList));
            AlertDialog.Builder builder = new AlertDialog.Builder(Tab_setting.this);
            builder.setMessage("すべて削除してもよろしいですか？")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                    // ボタンをクリックしたときの動作
                            alertList.clear();
                            saveList(Tab_setting.this ,"data" , alertList);
                            alertList = loadList(Tab_setting.this, "data");

                            Intent intent = new Intent(Tab_setting.this, Tab_setting.class);
                            startActivity(intent);
                        }
                    });
            builder.setNegativeButton("Cancel", null);
            builder.setCancelable(true);

            // 表示
            builder.create().show();
        }
    }

    private class AddListener implements View.OnClickListener {

        public void onClick(View v) {

            // カスタムビューを設定
            LayoutInflater inflater = (LayoutInflater) Tab_setting.this.getSystemService(LAYOUT_INFLATER_SERVICE);
            final View layout = inflater.inflate( R.layout.dialog_layout, (ViewGroup) findViewById(R.id.dialogcustom));

            // アラートダイアログ を生成
            AlertDialog.Builder builder = new AlertDialog.Builder(Tab_setting.this);
            builder.setTitle("RSSの追加"); builder.setView(layout);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // OK ボタンクリック処理
                EditText id1 = (EditText) layout.findViewById(R.id.customDialog1);
                String strId1 = id1.getText().toString();
                EditText id2 = (EditText) layout.findViewById(R.id.customDialog2);
                String msg = id2.getText().toString();

                // 一覧に追加するメッセージを組み立て
                //String msg_3 = msg.substring(msg.length() - 3);
                if(msg.length() == 0){
                    AlertDialog.Builder builder = new AlertDialog.Builder(Tab_setting.this);
                    builder.setMessage("入力してください")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // ボタンをクリックしたときの動作
                                }
                            });
                    builder.show();
                }else{
                    if (msg.substring(msg.length() - 3).equals("rdf") || msg.substring(msg.length() - 3).equals("xml")) {
                        // ListViewに項目を追加
                        alertList.add(strId1);
                        alertList.add( msg );
                        saveList(Tab_setting.this ,"data" , alertList);
                        myListView.setAdapter(adapter);
                    }else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(Tab_setting.this);
                        builder.setMessage("RSSではありません")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // ボタンをクリックしたときの動作
                                    }
                                });
                        builder.show();
                    }
                }
            }
        });
            builder.setNegativeButton("キャンセル", null)
                    .setCancelable(true);
        // 表示
            builder.create().show();
        }
    }
    // 設定値 ArrayList<String> を保存（Context は Activity や Application や Service）
    public static void saveList(Context ctx, String key, ArrayList<String> list) {
        JSONArray jsonAry = new JSONArray();
        for(int i=0; i<list.size(); i++) {
            jsonAry.put(list.get(i));
        }
        SharedPreferences prefs = ctx.getSharedPreferences("NewsViewer", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, jsonAry.toString());
        editor.apply();
    }

    // 設定値 ArrayList<String> を取得（Context は Activity や Application や Service）
    public static ArrayList<String> loadList(Context ctx, String key) {
        ArrayList<String> list = new ArrayList<String>();
        SharedPreferences prefs = ctx.getSharedPreferences("NewsViewer", Context.MODE_PRIVATE);
        String strJson = prefs.getString(key, ""); // 第２引数はkeyが存在しない時に返す初期値
        if(!strJson.equals("")) {
            try {
                JSONArray jsonAry = new JSONArray(strJson);
                for(int i=0; i<jsonAry.length(); i++) {
                    list.add(jsonAry.getString(i));
                }
            } catch(Exception ex) {
                ex.printStackTrace();
            }
        }
        return list;
    }

    public void setList(){
        alertList = loadList(Tab_setting.this, "data");
    }

    public static ArrayList<String> getList(){
        return alertList;
    }

}
