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

import android.database.MatrixCursor;
import android.support.v7.widget.RecyclerView;

import com.agileengine.leadandroidtesttask.todolist.R;

import butterknife.InjectView;

public abstract class RecycleViewFragment  extends ContentFragment {

    @InjectView(R.id.recycle_list)
    protected RecyclerView mRecyclerView;

    @Override
    protected int getTargetLayout() {
        return R.layout.fragment_recycle_view;
    }

    protected MatrixCursor getDummyCursors() {
        MatrixCursor matrixCursor = new MatrixCursor(new String[]{"_id"});
      /*  matrixCursor.addRow(new Object[]{1});
        matrixCursor.addRow(new Object[]{2});
        matrixCursor.addRow(new Object[]{3});
        matrixCursor.addRow(new Object[]{4});
        matrixCursor.addRow(new Object[]{5});
        matrixCursor.addRow(new Object[]{6});
        matrixCursor.addRow(new Object[]{7});
        matrixCursor.addRow(new Object[]{8});*/
        return matrixCursor;
    }

}
