/*
 * Copyright (c) 2015. Dmitriy Manzhosov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.agileengine.leadandroidtesttask.todolist.fragment;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.agileengine.leadandroidtesttask.todolist.R;
import com.agileengine.leadandroidtesttask.todolist.activity.AddEditToDoItemActivity;
import com.agileengine.leadandroidtesttask.todolist.cache.service.ServiceHelper;
import com.agileengine.leadandroidtesttask.todolist.framework.fragment.base.ContentFragment;
import com.agileengine.leadandroidtesttask.todolist.model.ToDoItem;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;
import java.util.Date;

import butterknife.InjectView;
import butterknife.OnClick;


public class AddEditToDoItemFragment extends ContentFragment implements
        TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener{

    private static final String MODEL_KEY = "model_key";

    @InjectView(R.id.edt_title)
    protected EditText mTitle;

    @InjectView(R.id.edt_description)
    protected EditText mDescription;

    @InjectView(R.id.edt_tags)
    protected EditText mTags;

    @InjectView(R.id.txt_due_date)
    protected TextView mDueDate;

    @InjectView(R.id.txt_due_time)
    protected TextView mDueTime;

    @InjectView(R.id.switch_urgent)
    protected SwitchCompat mUrgent;

    private ToDoItem mToDoItem;

    @Override
    protected int getTargetLayout() {
        return R.layout.fragment_to_do_add_edit;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(MODEL_KEY, mToDoItem);
    }

    @Override
    protected void initComponents(LayoutInflater inflater, View root, Bundle savedInstanceState) {
        if(savedInstanceState != null){
            mToDoItem = savedInstanceState.getParcelable(MODEL_KEY);
        } else {
            Intent intent = getActivity().getIntent();
            mToDoItem = intent.getParcelableExtra(AddEditToDoItemActivity.EXTRA_TODO_ITEM);
            if(mToDoItem == null){
                mToDoItem = new ToDoItem();
                mToDoItem.setDueDate(Calendar.getInstance().getTime());

                onUpdateTitle(getString(R.string.create_new_todo_item_title));
            } else {
                onUpdateTitle(getString(R.string.edit_todo_item_title));
            }
        }

        mTitle.setText(mToDoItem.getTitle());
        mDescription.setText(mToDoItem.getDescription());
        mUrgent.setChecked(mToDoItem.isUrgent());
        onSetDateTime(mToDoItem.getDueDate());
        mTags.setText(mToDoItem.getTags());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_to_do_add_edit, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_edit) {
            save();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean isBackButtonNeeded() {
        return true;
    }

    @OnClick(R.id.switch_urgent)
    public void onUrgentSwitched(View urgent){
        mToDoItem.setUrgent(mUrgent.isChecked());
    }

    @OnClick(R.id.txt_due_date)
    public void onDueDateClick(View view){
        Calendar calendar = getCalendar(mToDoItem.getDueDate());
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        dpd.show(getFragmentManager(), "Datepickerdialog");
    }

    @OnClick(R.id.txt_due_time)
    public void onDueTimeClick(View view){
        Calendar calendar = getCalendar(mToDoItem.getDueDate());
        TimePickerDialog tpd = TimePickerDialog.newInstance(
                this,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE), true
        );
        tpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                Log.d("TimePicker", "Dialog was cancelled");
            }
        });

        tpd.show(getFragmentManager(), "Timepickerdialog");
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        onUpdateTime(hourOfDay, minute);

        Calendar calendar = getCalendar(mToDoItem.getDueDate());

        calendar.set(Calendar.HOUR, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);

        mToDoItem.setDueDate(calendar.getTime());
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        onUpdateDate(year, monthOfYear, dayOfMonth);

        Calendar calendar = getCalendar(mToDoItem.getDueDate());

        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, monthOfYear);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        mToDoItem.setDueDate(calendar.getTime());
    }

    private void onSetDateTime(Date date) {

        Calendar calendar = getCalendar(date);

        onUpdateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        onUpdateTime(calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE));
    }

    private void onUpdateDate(int year, int monthOfYear, int dayOfMonth) {
        String date =  dayOfMonth+"/"+(++monthOfYear)+"/"+year;
        mDueDate.setText(date);
    }

    @NonNull
    private Calendar getCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date.getTime());
        return calendar;
    }

    private void onUpdateTime(int hourOfDay, int minute) {
        String hourString = hourOfDay < 10 ? "0"+hourOfDay : ""+hourOfDay;
        String minuteString = minute < 10 ? "0"+minute : ""+minute;
        String time = hourString+"h"+minuteString;
        mDueTime.setText(time);
    }

    private void onUpdateTitle(String create) {
        getToolbar().setTitle(create);
    }

    private void save() {

        String title = getValue(mTitle);

        if("".equals(title)){
            mTitle.setError(getString(R.string.error_todo_item_title));
            return;
        }

        mToDoItem.setTitle(title);
        mToDoItem.setDescription(getValue(mDescription));
        mToDoItem.setTags(getValue(mTags));

        if(mToDoItem.getId() == -1){
            ServiceHelper.createToDoItem(getActivity(), mToDoItem);
        } else {
            ServiceHelper.updateToDoItem(getActivity(), mToDoItem);
        }

        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    @NonNull
    private String getValue(TextView textView) {
        CharSequence text = textView.getText();
        return text != null ? text.toString(): "";
    }
}
