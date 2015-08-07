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

package com.agileengine.leadandroidtesttask.todolist.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.agileengine.leadandroidtesttask.todolist.db.table.ToDoItemTable;

import java.text.MessageFormat;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String KEY_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS {0} ({1})";
    public static final String KEY_DROP_TABLE = "DROP TABLE IF EXISTS {0}";

    private Context context;

    private static final int CURRENT_DB_VERSION = 1;
    private static final String DB_NAME = "todo.db";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, CURRENT_DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createToDoItemTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        dropTable(db, ToDoItemTable.TABLE_NAME);
        onCreate(db);
    }

    public void dropTable(SQLiteDatabase db, String name) {
        String query = MessageFormat.format(DatabaseHelper.KEY_DROP_TABLE, name);
        db.execSQL(query);
    }

    public void createTable(SQLiteDatabase db, String name, String fields) {
        String query = MessageFormat.format(DatabaseHelper.KEY_CREATE_TABLE, name, fields);
        db.execSQL(query);
    }

    private void createToDoItemTable(SQLiteDatabase db) {
        StringBuilder todoTable = new StringBuilder();

        todoTable
                .append(ToDoItemTable.Cols.ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .append(ToDoItemTable.Cols.TITLE).append(" TEXT, ")
                .append(ToDoItemTable.Cols.DESCRIPTION).append(" TEXT, ")
                .append(ToDoItemTable.Cols.URGENT).append(" TEXT, ")
                .append(ToDoItemTable.Cols.DUE_DATE).append(" TEXT, ")
                .append(ToDoItemTable.Cols.TAGS).append(" TEXT, ")
                .append(ToDoItemTable.Cols.DONE).append(" TEXT");

        createTable(db, ToDoItemTable.TABLE_NAME, todoTable.toString());
    }
}