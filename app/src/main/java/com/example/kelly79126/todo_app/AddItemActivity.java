package com.example.kelly79126.todo_app;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.sql.Date;
import java.util.Calendar;

/**
 * Created by kelly79126 on 2017/1/9.
 */

public class AddItemActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    ViewGroup mVgDueDate;
    TextView mTxtDueDate;
    Spinner mSpPriority, mSpStatus;
    EditText mEtTaskName, mEtTaskNote;
    int miId, miYear, miMonth, miDay;
    boolean mbAddItem;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_add_item);
        getSupportActionBar().setTitle("Listly");

        mEtTaskName = (EditText) findViewById(R.id.et_task_name);
        mEtTaskNote = (EditText) findViewById(R.id.et_task_note);

        mVgDueDate = (ViewGroup) findViewById(R.id.vg_due_date);
        if(null != mVgDueDate){
            mVgDueDate.setOnClickListener(this);
        }

        mTxtDueDate = (TextView) findViewById(R.id.txt_due_date);
        mSpPriority = (Spinner) findViewById(R.id.sp_priority);
        mSpStatus = (Spinner) findViewById(R.id.sp_status);


        miId = getIntent().getIntExtra("id", 0);
        mbAddItem = getIntent().getBooleanExtra("additem", false);

        if(true == mbAddItem) {
            Calendar now = Calendar.getInstance();
            showDateFormat(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DATE));
        } else{
            initInfo(getIntent());
        }
    }

    private void initInfo(Intent intent) {
        mEtTaskName.setText(intent.getStringExtra("name"));
        mEtTaskNote.setText(intent.getStringExtra("note"));
        showDateFormat(intent.getIntExtra("year", 2017), intent.getIntExtra("month", 01), intent.getIntExtra("day", 01));
        setSpinnerToValue(mSpPriority, intent.getStringExtra("priority"));
        setSpinnerToValue(mSpStatus, intent.getStringExtra("status"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.miSave:
                onSubmit();
                return true;
            case R.id.miCancel:
                setResult(RESULT_CANCELED);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showDatePickerDialog(View v) {
        Bundle currentDate = new Bundle();
        currentDate.putLong("setDate", 1234);

        //Pass the bundle to the fragment
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.setArguments(currentDate);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.vg_due_date:
                showDatePickerDialog(view);
                break;
        }

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        showDateFormat(year, month, day);
    }

    public void showDateFormat(int year, int month, int day) {
        Date chosenDate = new Date(year-1900, month, day);
        java.text.DateFormat dateFormat = DateFormat.getDateFormat(getApplicationContext());
        mTxtDueDate.setText(dateFormat.format(chosenDate));
        miYear = year;
        miMonth = month;
        miDay = day;
    }

    public void setSpinnerToValue(Spinner spinner, String value) {
        int index = 0;
        SpinnerAdapter adapter = spinner.getAdapter();
        for (int i = 0; i < adapter.getCount(); i++) {
            if (adapter.getItem(i).equals(value)) {
                index = i;
                break; // terminate loop
            }
        }
        spinner.setSelection(index);
    }

    @Override
    public void onBackPressed() {
        showCancelDialog();
    }

    private void showCancelDialog(){
        // TODO
        setResult(RESULT_CANCELED);
        finish();
    }

    public void onSubmit() {
        // Prepare data intent
        Intent data = new Intent();
        // Pass relevant data back as a result
        data.putExtra("name", mEtTaskName.getText().toString());
        data.putExtra("note", mEtTaskNote.getText().toString());
        data.putExtra("year", miYear);
        data.putExtra("month", miMonth);
        data.putExtra("day", miDay);
        data.putExtra("priority", mSpPriority.getSelectedItem().toString());
        data.putExtra("status", mSpStatus.getSelectedItem().toString());
        data.putExtra("additem", mbAddItem);

        Log.d("Kelly onSubmit", "Year: "+ miYear + ", Month: " + miMonth + ", Day: " + miDay);

        if (false == mbAddItem){
            data.putExtra("id", miId);
        }

        // Activity finished ok, return the data
        setResult(RESULT_OK, data); // set result code and bundle data for response
        finish(); // closes the activity, pass data to parent
    }

}
