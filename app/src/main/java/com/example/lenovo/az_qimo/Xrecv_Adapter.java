package com.example.lenovo.az_qimo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lenovo.az_qimo.bean.ItemTest;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;

public class Xrecv_Adapter extends XRecyclerView.Adapter<Xrecv_Adapter.MyHolder> {
    private ArrayList<ItemTest.DataBean.DatasBean> newList;
    private Context context;
    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.layout_item, parent, false);
        MyHolder myHolder = new MyHolder(inflate);
        return myHolder;
    }

    public Xrecv_Adapter(ArrayList<ItemTest.DataBean.DatasBean> newList, Context context) {
        this.newList = newList;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.tvAuthor.setText(newList.get(position).getAuthor());
        holder.tvChapterName.setText(newList.get(position).getChapterName());
        holder.tvNiceDate.setText(newList.get(position).getNiceDate());
        holder.tvTitle.setText(newList.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return newList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private TextView tvAuthor;
        private TextView tvChapterName;
        private TextView tvTitle;
        private TextView tvNiceDate;
        public MyHolder(View itemView) {
            super(itemView);
            tvAuthor = itemView.findViewById(R.id.tv_author);
            tvChapterName = itemView.findViewById(R.id.tv_chapterName);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvNiceDate = itemView.findViewById(R.id.tv_niceDate);
        }
    }
}
