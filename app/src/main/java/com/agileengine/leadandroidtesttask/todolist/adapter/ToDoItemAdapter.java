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

package com.agileengine.leadandroidtesttask.todolist.adapter;

import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.agileengine.leadandroidtesttask.todolist.App;
import com.agileengine.leadandroidtesttask.todolist.R;
import com.agileengine.leadandroidtesttask.todolist.adapter.base.CursorRecyclerViewAdapter;
import com.agileengine.leadandroidtesttask.todolist.cache.service.ServiceHelper;
import com.agileengine.leadandroidtesttask.todolist.db.table.ToDoItemTable;
import com.agileengine.leadandroidtesttask.todolist.framework.paulburke.helper.ItemTouchHelperAdapter;
import com.agileengine.leadandroidtesttask.todolist.framework.paulburke.helper.ItemTouchHelperViewHolder;
import com.agileengine.leadandroidtesttask.todolist.framework.utils.DateUtils;
import com.agileengine.leadandroidtesttask.todolist.model.ToDoItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ToDoItemAdapter extends CursorRecyclerViewAdapter<ToDoItemAdapter.ViewHolder>
        implements ItemTouchHelperAdapter {

    private static final String TAG = ToDoItemAdapter.class.getSimpleName();

    private ViewHolder.ClickListener clickListener;
    private ArrayList<ToDoItem> mItems;

    public ToDoItemAdapter(Context context, ViewHolder.ClickListener clickListener, Cursor cursor) {
        super(context, cursor);
        this.clickListener = clickListener;
    }

    @Override
    public ToDoItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.todo_item, parent, false);
        return new ViewHolder(view, clickListener);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, Cursor cursor) {

        final int position = cursor.getPosition();
//        final ToDoItem item = new ToDoItem(cursor);

        final ToDoItem item = getItem(position);

        viewHolder.title.setText(item.getTitle(), TextView.BufferType.SPANNABLE);
        viewHolder.description.setText(item.getDescription(), TextView.BufferType.SPANNABLE);

        viewHolder.dueDateTime.setText(DateUtils.getDateAsString(item.getDueDate(), DateUtils.Constants.ROUND_TIME_DATE_PATTERN));

        if(item.isDone()){
            setStrike(viewHolder.title);
            setStrike(viewHolder.description);
        }

        int status_text_id = R.string.status_none_title;
        int color_id = R.color.half_black;
        if(item.isDone()){
            status_text_id = R.string.status_done_title;
        } else if(item.isUrgent()){
            if(isOverdue(item)){
                status_text_id = R.string.status_urgent_overdue_title;
            } else {
                status_text_id = R.string.status_urgent_title;
            }
            color_id = R.color.color_red;

        } else if(isOverdue(item)){
            status_text_id = R.string.status_overdue_title;
            color_id = R.color.color_accent;
        }

        String string = mContext.getString(status_text_id);
        if(TextUtils.isEmpty(string)) {
            viewHolder.status.setText(string);
        } else {
            viewHolder.status.setText(string, TextView.BufferType.SPANNABLE);

            int color = mContext.getResources().getColor(color_id);

            Spannable spannable = (Spannable) viewHolder.status.getText();
            spannable.setSpan(new ForegroundColorSpan(color), 0, spannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        // Highlight the item if it's selected
        viewHolder.selectedOverlay.setVisibility(isSelected(position) ? View.VISIBLE : View.INVISIBLE);
    }

    private boolean isOverdue(ToDoItem item) {
        return item.getDueDate().before(new Date(System.currentTimeMillis()));
    }

    @Override
    public void onItemDismiss(int position) {
        ToDoItem item = getItem(position);
        item.setDone(!item.isDone());
        ServiceHelper.updateToDoItem(mContext, item);
        notifyItemChanged(position);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        mHandler.removeCallbacks(mUpdater);

        ToDoItem fromItem = getItem(fromPosition);

        ToDoItem toItem = getItem(toPosition);

        String tempOrder = fromItem.getOrder();

        fromItem.setOrder(toItem.getOrder());
        toItem.setOrder(tempOrder);

        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(mItems, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(mItems, i, i - 1);
            }
        }

        Log.v(TAG, "________________________");
        for(ToDoItem item: mItems){
            Log.v(TAG, item.toString());
        }

        ServiceHelper.updateToDoItem(mContext, fromItem, false);
        ServiceHelper.updateToDoItem(mContext, toItem, false);

        notifyItemMoved(fromPosition, toPosition);

        mHandler.postDelayed(mUpdater, 2000);

        return true;
    }

    private void setStrike(TextView textView) {
        Spannable spannable = (Spannable) textView.getText();
        spannable.setSpan(new StrikethroughSpan(), 0, spannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
            implements ItemTouchHelperViewHolder,
            View.OnClickListener,
            View.OnLongClickListener  {

        @InjectView(R.id.txt_status)
        TextView status;

        @InjectView(R.id.txt_title_value)
        TextView title;

        @InjectView(R.id.txt_description_value)
        TextView description;

        @InjectView(R.id.txt_due_date_time)
        TextView dueDateTime;

        @InjectView(R.id.selected_overlay)
        public View selectedOverlay;

        private ClickListener listener;

        public ViewHolder(View view, ClickListener listener) {
            super(view);
            ButterKnife.inject(this, view);

            this.listener = listener;

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onItemSelected() {
            selectedOverlay.setVisibility(View.VISIBLE);
        }

        @Override
        public void onItemClear() {
            selectedOverlay.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onItemClicked(getAdapterPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (listener != null) {
                return listener.onItemLongClicked(this, getAdapterPosition());
            }

            return false;
        }

        public interface ClickListener {

            void onItemClicked(int position);
            boolean onItemLongClicked(ViewHolder viewHolder, int position);
        }
    }

    @Override
    public ToDoItem getItem(int position) {
//        Cursor cursor = getCursor();
//        ToDoItem toDoItem = null;
//        if (cursor.moveToPosition(position)) {
//            toDoItem = new ToDoItem(cursor);
//        }

        return mItems.get(position);
    }

    private void onUpdateItems() {
        Cursor cursor = getCursor();
        if (cursor.moveToFirst()) {
            mItems = new ArrayList<>();
            do {
                mItems.add(new ToDoItem(cursor));
            } while (cursor.moveToNext());
        }
    }

    @Override
    public Cursor swapCursor(Cursor newCursor) {
        Cursor cursor = super.swapCursor(newCursor);
        onUpdateItems();

        return cursor;
    }

    Handler mHandler = new Handler();
    Runnable mUpdater = new Runnable() {
        @Override
        public void run() {
            App.getContext().getContentResolver().notifyChange(ToDoItemTable.CONTENT_URI, null);
        }
    };
}
