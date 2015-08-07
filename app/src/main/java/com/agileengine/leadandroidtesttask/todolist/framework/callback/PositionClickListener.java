package com.agileengine.leadandroidtesttask.todolist.framework.callback;

import android.view.View;

public abstract class PositionClickListener implements View.OnClickListener {
    int position;

    public void setPosition(final int position) {
        this.position = position;
    }

    @Override
    public void onClick(final View v) {
        onPositionClick(v, position);
    }

    public abstract void onPositionClick(final View view, int position);
}
