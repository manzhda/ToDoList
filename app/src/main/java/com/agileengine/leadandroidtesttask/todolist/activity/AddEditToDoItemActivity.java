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

package com.agileengine.leadandroidtesttask.todolist.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.agileengine.leadandroidtesttask.todolist.fragment.AddEditToDoItemFragment;
import com.agileengine.leadandroidtesttask.todolist.framework.activity.ContentActivity;
import com.agileengine.leadandroidtesttask.todolist.model.ToDoItem;


public class AddEditToDoItemActivity extends ContentActivity<AddEditToDoItemFragment>{

    public static final String EXTRA_TODO_ITEM = AddEditToDoItemActivity.class.getClass().getPackage() + "TODO_ITEM";

    @Override
    protected AddEditToDoItemFragment onCreateContentFragment() {
        return new AddEditToDoItemFragment();
    }

    /**
     * If item null app create new in other case edit existed.
     * */
    public static void goThereForResult(Activity activity, @Nullable ToDoItem item, int requestCode){
        Intent intent = new Intent(activity, AddEditToDoItemActivity.class);
        intent.putExtra(EXTRA_TODO_ITEM, item);
        activity.startActivityForResult(intent, requestCode);
        onAppear(activity);
    }
}
