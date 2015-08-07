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

package com.agileengine.leadandroidtesttask.todolist.db.table;

import android.net.Uri;

import com.agileengine.leadandroidtesttask.todolist.db.ContentDescriptor;
import com.agileengine.leadandroidtesttask.todolist.db.table.base.BaseTable;


public class ToDoItemTable extends BaseTable {

    public static final String TABLE_NAME = "todo";

    public static final String PATH = "todo_path";
    public static final int PATH_TOKEN = 1;
    public static final Uri CONTENT_URI = ContentDescriptor.BASE_URI.buildUpon().appendPath(PATH).build();

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public String getAuthority() {
        return ContentDescriptor.AUTHORITY;
    }

    @Override
    public Uri getContentUri() {
        return CONTENT_URI;
    }

    public static class Cols {
        public static final String ID = "_id";
        public static final String TITLE = "title";
        public static final String DESCRIPTION = "description";
        public static final String URGENT = "urgent";
        public static final String DUE_DATE = "due_date";
        public static final String TAGS = "tags";
        public static final String DONE = "done";
    }

}
