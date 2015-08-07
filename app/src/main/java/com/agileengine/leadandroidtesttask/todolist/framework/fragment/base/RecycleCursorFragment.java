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

package com.agileengine.leadandroidtesttask.todolist.framework.fragment.base;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.agileengine.leadandroidtesttask.todolist.R;
import com.agileengine.leadandroidtesttask.todolist.adapter.base.CursorRecyclerViewAdapter;

import butterknife.InjectView;

public abstract class RecycleCursorFragment extends CursorLoaderFragment {

    @InjectView(R.id.recycle_list)
    protected RecyclerView mRecyclerView;

    @Override
    protected int getTargetLayout() {
        return R.layout.fragment_recycle_view;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return getCursorLoader();
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        getAdapter().swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    protected abstract CursorLoader getCursorLoader();

    protected abstract CursorRecyclerViewAdapter getAdapter();

}
