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
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.agileengine.leadandroidtesttask.todolist.R;
import com.agileengine.leadandroidtesttask.todolist.adapter.base.CursorRecyclerViewAdapter;
import com.agileengine.leadandroidtesttask.todolist.framework.callback.OnItemClickListener;
import com.agileengine.leadandroidtesttask.todolist.framework.callback.PositionClickListener;
import com.agileengine.leadandroidtesttask.todolist.model.ToDoItem;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ToDoItemAdapter extends CursorRecyclerViewAdapter<ToDoItemAdapter.ViewHolder> {

    private OnItemClickListener<ToDoItem> mMenuClickListener;
    private OnItemClickListener<ToDoItem> mItemClickListener;

    public ToDoItemAdapter(Context context, Cursor cursor) {
        super(context, cursor);
    }

    @Override
    public ToDoItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_transitions, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.itemClickListener = new PositionClickListener() {
            @Override
            public void onPositionClick(View view, int position) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClicked(view, getItem(position), position);
                }
            }
        };
        viewHolder.menuListener = new PositionClickListener() {
            @Override
            public void onPositionClick(View view, int position) {
                if (mMenuClickListener != null) {
                    mMenuClickListener.onItemClicked(view, getItem(position), position);
                }
            }
        };

        view.setOnClickListener(viewHolder.itemClickListener);
//        viewHolder.menu.setOnClickListener(viewHolder.menuListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {

        ToDoItem item = new ToDoItem(cursor);

        viewHolder.itemClickListener.setPosition(cursor.getPosition());
        viewHolder.menuListener.setPosition(cursor.getPosition());

        viewHolder.description.setText(item.getDescription());
        viewHolder.title.setText(item.getTitle());
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.txt_title_value)
        TextView title;

        @InjectView(R.id.txt_description_value)
        TextView description;

        PositionClickListener menuListener;
        PositionClickListener itemClickListener;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
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

    public void setMenuClickListener(OnItemClickListener<ToDoItem> menuClickListener) {
        mMenuClickListener = menuClickListener;
    }

    public void setToDoItemClickListener(OnItemClickListener<ToDoItem> walletClickListener) {
        mItemClickListener = walletClickListener;
    }
}
