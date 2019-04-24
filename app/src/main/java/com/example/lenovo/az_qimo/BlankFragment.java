package com.example.lenovo.az_qimo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lenovo.az_qimo.bean.ItemTest;
import com.google.gson.Gson;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

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


public class BlankFragment extends Fragment {
    private int page = 0;

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            List<ItemTest.DataBean.DatasBean> list = (List<ItemTest.DataBean.DatasBean>) msg.obj;

            newList.addAll(list);

            xrecv_adapter.notifyDataSetChanged();

            xrecv.refreshComplete();
            xrecv.loadMoreComplete();
        }
    };
    private ArrayList<ItemTest.DataBean.DatasBean> newList;
    private Xrecv_Adapter xrecv_adapter;
    private XRecyclerView xrecv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blank, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        newList = new ArrayList<>();
        Bundle arguments = getArguments();
        final int id = arguments.getInt("id");

        xrecv = getActivity().findViewById(R.id.xrecv);

        xrecv.setLayoutManager(new LinearLayoutManager(getContext()));

        xrecv_adapter = new Xrecv_Adapter(newList, getContext());
        xrecv.setAdapter(xrecv_adapter);

        initData(page,id);

        xrecv.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                page++;
                initData(page,id);
            }
        });
    }

    private void initData(final int page, final int id) {
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    URL url = new URL("http://www.wanandroid.com/project/list/" + page + "/json?cid=" + id);
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
                        ItemTest itemTest = gson.fromJson(sb.toString(), ItemTest.class);
                        List<ItemTest.DataBean.DatasBean> list = itemTest.getData().getDatas();

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
}
