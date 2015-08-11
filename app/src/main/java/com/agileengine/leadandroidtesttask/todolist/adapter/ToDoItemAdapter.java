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
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.agileengine.leadandroidtesttask.todolist.R;
import com.agileengine.leadandroidtesttask.todolist.adapter.base.CursorRecyclerViewAdapter;
import com.agileengine.leadandroidtesttask.todolist.cache.service.ServiceHelper;
import com.agileengine.leadandroidtesttask.todolist.framework.paulburke.helper.ItemTouchHelperAdapter;
import com.agileengine.leadandroidtesttask.todolist.framework.paulburke.helper.ItemTouchHelperViewHolder;
import com.agileengine.leadandroidtesttask.todolist.framework.paulburke.helper.OnStartDragListener;
import com.agileengine.leadandroidtesttask.todolist.model.ToDoItem;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ToDoItemAdapter extends CursorRecyclerViewAdapter<ToDoItemAdapter.ViewHolder>
        implements ItemTouchHelperAdapter {

    private static final String TAG = ToDoItemAdapter.class.getSimpleName();

//    private OnItemClickListener<ToDoItem> mItemClickListener;
    private OnStartDragListener mOnStartDragListener;
    private ViewHolder.ClickListener clickListener;

    public ToDoItemAdapter(Context context, ViewHolder.ClickListener clickListener, Cursor cursor) {
        super(context, cursor);
        this.clickListener = clickListener;
    }

    @Override
    public ToDoItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.todo_item, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view, clickListener);
//        viewHolder.itemClickListener = new PositionClickListener() {
//            @Override
//            public void onPositionClick(View view, int position) {
//                if (mItemClickListener != null) {
//                    mItemClickListener.onItemClicked(view, getItem(position), position);
//                }
//            }
//        };
//
//        view.setOnClickListener(viewHolder.itemClickListener);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, Cursor cursor) {

        final ToDoItem item = new ToDoItem(cursor);

        final int position = cursor.getPosition();

        viewHolder.title.setText(item.getTitle(), TextView.BufferType.SPANNABLE);
        viewHolder.description.setText(item.getDescription(), TextView.BufferType.SPANNABLE);


        if(mOnStartDragListener != null) {
            // Start a drag whenever the handle view it touched
            viewHolder.itemView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                        mOnStartDragListener.onLongPress(position);
                    } else if(MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_SCROLL){
                        mOnStartDragListener.onStartDrag(viewHolder);
                    }
                    return false;
                }
            });

            final GestureDetector gestureDetector = new GestureDetector(mContext, new GestureDetector.OnGestureListener() {
                @Override
                public boolean onDown(MotionEvent e) {
                    return false;
                }

                @Override
                public void onShowPress(MotionEvent e) {

                }

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return false;
                }

                @Override
                public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                    mOnStartDragListener.onStartDrag(viewHolder);
                    return false;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    mOnStartDragListener.onLongPress(viewHolder.getAdapterPosition());
                }

                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                    return false;
                }
            });

            viewHolder.itemView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return gestureDetector.onTouchEvent(event);
                }
            });
        }

        if(item.isDone()){
            setStrike(viewHolder.title);
            setStrike(viewHolder.description);
        }

        // Highlight the item if it's selected
        viewHolder.selectedOverlay.setVisibility(isSelected(position) ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void onItemDismiss(int position) {
//        ServiceHelper.deleteToDoItem(mContext, getItem(position));
//        notifyItemRemoved(position);

        ToDoItem item = getItem(position);
        item.setDone(!item.isDone());
        ServiceHelper.updateToDoItem(mContext, item);
        notifyItemChanged(position);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
//        Collections.swap(mItems, fromPosition, toPosition);
//        notifyItemMoved(fromPosition, toPosition);
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

        @InjectView(R.id.txt_title_value)
        TextView title;

        @InjectView(R.id.txt_description_value)
        TextView description;

        @InjectView(R.id.txt_due_date)
        TextView dueDate;

        @InjectView(R.id.txt_due_time)
        TextView dueTime;

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
        }

        @Override
        public void onItemClear() {
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
                return listener.onItemLongClicked(getAdapterPosition());
            }

            return false;
        }

        public interface ClickListener {
            void onItemClicked(int position);
            boolean onItemLongClicked(int position);
        }
    }

    @Override
    public ToDoItem getItem(int position) {
        Cursor cursor = getCursor();
        ToDoItem transaction = null;
        if (cursor.moveToPosition(position)) {
            transaction = new ToDoItem(cursor);
        }

        return transaction;
    }

//    public void setToDoItemClickListener(OnItemClickListener<ToDoItem> walletClickListener) {
//        mItemClickListener = walletClickListener;
//    }

    public void setOnStartDragListener(OnStartDragListener onStartDragListener) {
        mOnStartDragListener = onStartDragListener;
    }
}
