package com.agileengine.leadandroidtesttask.todolist.framework.callback;

import android.view.View;

public interface OnItemClickListener<T extends Object> {
    void onItemClicked(View view, T item, int position);
}