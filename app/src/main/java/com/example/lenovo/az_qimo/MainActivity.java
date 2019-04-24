package com.example.lenovo.az_qimo;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.lenovo.az_qimo.bean.TabTest;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private DrawerLayout dl;
    private LinearLayout ll;
    private Toolbar toolbar;
    private TabLayout tab;
    private ViewPager vp;
    private NotificationManager manager;
    private Notification build;
    private TextView tv_xg;


    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            List<TabTest.DataBean> list = (List<TabTest.DataBean>) msg.obj;
            Log.i("tag",list.get(0).getName());
            initFragment(list);
        }
    };

    private void initFragment(List<TabTest.DataBean> list) {
        ArrayList<Fragment> fragments = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            BlankFragment fragment = new BlankFragment();
            int id = list.get(i).getId();
            Bundle bundle = new Bundle();
            bundle.putInt("id",id);
            fragment.setArguments(bundle);
            fragments.add(fragment);
        }
        Vp_Adapter vp_adapter = new Vp_Adapter(getSupportFragmentManager(), fragments, list);
        vp.setAdapter(vp_adapter);

        tab.setupWithViewPager(vp);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        tv_xg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initDataTab();
            }
        });

    }

    private void initDataTab() {

        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    URL url = new URL("http://www.wanandroid.com/project/tree/json");
                    HttpURLConnection oc = (HttpURLConnection) url.openConnection();
                    if (oc.getResponseCode() == 200){
                        StringBuffer sb = new StringBuffer();
                        InputStream in = oc.getInputStream();
                        BufferedReader bf = new BufferedReader(new InputStreamReader(in));
                        String len = null;
                        while ((len = bf.readLine())!=null){
                            sb.append(len);
                        }
                        Gson gson = new Gson();
                        TabTest tabTest = gson.fromJson(sb.toString(), TabTest.class);
                        List<TabTest.DataBean> list = tabTest.getData();

                        Message msg = new Message();
                        msg.obj = list;
                        handler.sendMessage(msg);

                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void initView() {

        dl = (DrawerLayout) findViewById(R.id.dl);
        ll = (LinearLayout) findViewById(R.id.ll);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tab = (TabLayout) findViewById(R.id.tab);
        vp = (ViewPager) findViewById(R.id.vp);
        tv_xg = findViewById(R.id.tv_xg);//项目

        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, dl, toolbar, R.string.app_name, R.string.app_name);
        dl.addDrawerListener(toggle);
        toggle.syncState();

    }
    //创建选项菜单

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_op,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item2:
                showPopupwindow();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showPopupwindow() {
        View inflate = LayoutInflater.from(this).inflate(R.layout.layout_popup, null);
        final PopupWindow popupWindow = new PopupWindow(inflate, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        popupWindow.setBackgroundDrawable(null);
        popupWindow.setOutsideTouchable(true);

        popupWindow.showAtLocation(dl,Gravity.CENTER,0,0);


        inflate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        TextView tv_music = inflate.findViewById(R.id.tv_music);
        TextView tv_video = inflate.findViewById(R.id.tv_video);

        tv_music.setOnClickListener(this);
        tv_video.setOnClickListener(this);
    }
        //通知
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_music:
                showNotification();
                manager.notify(1,build);
            break;
            case R.id.tv_video:
                showNotification();
                manager.cancel(1);
                break;
        }
    }
	
    //通知
    private void showNotification() {
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        String channlid="1";

        build = new NotificationCompat.Builder(this, channlid)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText("dansdas")
                .setContentTitle("新闻")
                .build();
    }
}
