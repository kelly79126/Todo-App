package com.example.kelly79126.todo_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class ToDoListMainActivity extends AppCompatActivity {
    ArrayList<ToDoItem> mArrlItems;
    ToDoItemAdapter mArraItemsAdapter;
    ListView mLvItems;
    private final int REQUEST_CODE = 20;

    List<MyTable> mListTodoItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);
        getSupportActionBar().setTitle("Listly");

        mLvItems = (ListView) findViewById(R.id.lv_items);
        mArrlItems = new ArrayList<ToDoItem>();
        mArraItemsAdapter = new ToDoItemAdapter(this, mArrlItems);
        mLvItems.setAdapter(mArraItemsAdapter);
        setListViewListener();
        updateUI();
        mLvItems.setOnCreateContextMenuListener(this);
    }

    private void updateUI() {
        syncSQLite();
        mArraItemsAdapter.clear();
        for(int i=0; i < mListTodoItemList.size(); i++) {
            ToDoItem newItem = new ToDoItem(mListTodoItemList.get(i).name, mListTodoItemList.get(i).priority, getDueDateString(mListTodoItemList.get(i).year, mListTodoItemList.get(i).month, mListTodoItemList.get(i).day));
            mArrlItems.add(newItem);
        }
        mArraItemsAdapter.notifyDataSetChanged();
    }

    private String getDueDateString(int year, int month, int day) {

        Log.d("Kelly getDueDateString ", "Year: "+ year + ", Month: " + month + ", Day: " + day);

        Date chosenDate = new Date(year-1900, month, day);
        java.text.DateFormat dateFormat = DateFormat.getDateFormat(getApplicationContext());
        return dateFormat.format(chosenDate);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {

        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_delete_item, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId()){
            case(R.id.miDelete):
                Log.d("Kelly", "delete");
                MyTable newRow = new MyTable();
                newRow.id = mListTodoItemList.get((int)info.id).id;
                newRow.name = mListTodoItemList.get((int)info.id).name;
                newRow.priority = mListTodoItemList.get((int)info.id).priority;
                newRow.delete();

                updateUI();
                break;

        }
        return super.onContextItemSelected(item);
    }


    private void syncSQLite() {
        mListTodoItemList = SQLite.select().from(MyTable.class).queryList();
    }

    private void setListViewListener(){
        mLvItems.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // first parameter is the context, second is the class of the activity to launch
                Intent i = new Intent(ToDoListMainActivity.this, AddItemActivity.class);
                // put "extras" into the bundle for access in the second activity
                i.putExtra("additem", false);
                i.putExtra("id", mListTodoItemList.get(position).id);
                i.putExtra("name", mListTodoItemList.get(position).name);
                i.putExtra("note", mListTodoItemList.get(position).note);
                i.putExtra("year", mListTodoItemList.get(position).year);
                i.putExtra("month", mListTodoItemList.get(position).month);
                i.putExtra("day", mListTodoItemList.get(position).day);
                i.putExtra("priority", mListTodoItemList.get(position).priority);
                i.putExtra("status", mListTodoItemList.get(position).status);
                // brings up the second activity
                startActivityForResult(i, REQUEST_CODE);
                mArraItemsAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.miAdd:
                Intent i = new Intent(ToDoListMainActivity.this, AddItemActivity.class);
                i.putExtra("additem", true);
                startActivityForResult(i, REQUEST_CODE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            // Extract name value from result extras
            MyTable newRow = new MyTable();

            Log.d("Kelly onActivityResult ", "Year: "+ data.getExtras().getInt("year") + ", Month: " + data.getExtras().getInt("month") + ", Day: " + data.getExtras().getInt("day"));

            if(false == data.getExtras().getBoolean("additem")){
                newRow.id = data.getExtras().getInt("id");
            }
            newRow.name = data.getExtras().getString("name");
            newRow.note = data.getExtras().getString("note");
            newRow.year = data.getExtras().getInt("year");
            newRow.month = data.getExtras().getInt("month");
            newRow.day = data.getExtras().getInt("day");
            newRow.priority = data.getExtras().getString("priority");
            newRow.status = data.getExtras().getString("status");
            newRow.save();

            updateUI();
        }
    }
}
