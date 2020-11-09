package com.example.lab_test;


import android.widget.BaseAdapter;

/**
 * Created by lihongzheng on 2020/10/26.
 */

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ApplicationAdapter extends BaseAdapter {

    private List<ShowMsg> apps;
    private LayoutInflater inflater;

    public ApplicationAdapter (Context context, List<ShowMsg> infos) {
        this.apps = infos;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount () {
        return apps.size();
    }

    @Override
    public Object getItem (int position) {
        return position;
    }

    @Override
    public long getItemId (int position) {
        return position;
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item, null);
            holder.icon = (ImageView) convertView.findViewById(R.id.imgg);
            holder.name = (TextView) convertView.findViewById(R.id.tv11);
            holder.starttime=(TextView)convertView.findViewById(R.id.tvstarttime);
            holder.endtime=(TextView)convertView.findViewById(R.id.tvendtime);
            holder.alltime=(TextView)convertView.findViewById(R.id.tvalltime);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.icon.setImageDrawable(apps.get(position).getIcon());
        holder.name.setText(apps.get(position).getName());
        holder.starttime.setText("开始使用时间:"+apps.get(position).getStart_time());
        holder.endtime.setText("最后使用时间:"+apps.get(position).getEnd_time());
        holder.alltime.setText("总使用时间:"+apps.get(position).getAll_use_time());
        return convertView;
    }

    class ViewHolder {
        ImageView icon;
        TextView name;
        TextView starttime;
        TextView endtime;
        TextView alltime;
    }
}
