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

package com.agileengine.leadandroidtesttask.todolist.api;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.support.v4.content.CursorLoader;

import com.agileengine.leadandroidtesttask.todolist.App;
import com.agileengine.leadandroidtesttask.todolist.db.table.ToDoItemTable;
import com.agileengine.leadandroidtesttask.todolist.model.ToDoItem;

public class ToDoListApiImplSupport implements ToDoListApiSupport {
    private static final String TAG = ToDoListApiImplSupport.class.getName();

    Context context = App.getContext();

    @Override
    public CursorLoader getToDoItems(String selection, String[] selectionArg, String sortOder) {
        return new CursorLoader(context, ToDoItemTable.CONTENT_URI, null, selection, selectionArg, sortOder);
    }

    @Override
    public Uri addToDoItem(ToDoItem item) {
        return context.getContentResolver().insert(ToDoItemTable.CONTENT_URI, getToDoItemValues(item));
    }

    @Override
    public void updateToDoItem(ToDoItem item) {
        context.getContentResolver().update(ToDoItemTable.CONTENT_URI, getToDoItemValues(item),
                ToDoItemTable.Cols.ID + "=" + item.getId(), null);

    }

    @Override
    public void deleteToDoItem(ToDoItem item) {
        context.getContentResolver().delete(ToDoItemTable.CONTENT_URI, ToDoItemTable.Cols.ID + "=" + item.getId(), null);
    }

    private ContentValues getToDoItemValues(ToDoItem item) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ToDoItemTable.Cols.TITLE, item.getTitle());
        contentValues.put(ToDoItemTable.Cols.DESCRIPTION, item.getDescription());
        contentValues.put(ToDoItemTable.Cols.URGENT, item.isUrgent());
        contentValues.put(ToDoItemTable.Cols.DUE_DATE, item.getDueDate().getTime());
        contentValues.put(ToDoItemTable.Cols.TAGS, item.getTags());
        contentValues.put(ToDoItemTable.Cols.DONE, item.isDone());
        return contentValues;
    }
}
