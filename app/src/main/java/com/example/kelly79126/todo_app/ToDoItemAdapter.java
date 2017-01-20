package com.example.kelly79126.todo_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by kelly79126 on 2017/1/8.
 */

public class ToDoItemAdapter extends ArrayAdapter<ToDoItem> {
    public ToDoItemAdapter(Context context, ArrayList<ToDoItem> todoItem) {
        super(context, 0, todoItem);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ToDoItem todoItem = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_todo_item, parent, false);
        }

        TextView tvName = (TextView) convertView.findViewById(R.id.txt_name);
        TextView tvHome = (TextView) convertView.findViewById(R.id.txt_priority);
        TextView tvDueDate = (TextView) convertView.findViewById(R.id.txt_due_date);

        tvName.setText(todoItem.name);
        tvHome.setText(todoItem.priority);
        tvDueDate.setText(todoItem.dueDate);

        return convertView;
    }
}