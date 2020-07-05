package com.yoonbae.planting.planner.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yoonbae.planting.planner.R;
import com.yoonbae.planting.planner.data.PlantEvent;

import java.util.ArrayList;
import java.util.List;

public class ListViewAdapter extends BaseAdapter {
    private List<PlantEvent> listViewItemList = new ArrayList<>();

    public ListViewAdapter() {}

    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item, parent, false);
        }

        TextView nameTextView = convertView.findViewById(R.id.name);
        TextView alarmTextView = convertView.findViewById(R.id.alarm);
        PlantEvent plantEvent = listViewItemList.get(position);
        nameTextView.setText(plantEvent.getName());
        alarmTextView.setText(plantEvent.getAlarmMessage());
        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public PlantEvent getItem(int position) {
        return listViewItemList.get(position);
    }

    public void addItem(PlantEvent plantEvent) {
        listViewItemList.add(plantEvent);
    }
}
