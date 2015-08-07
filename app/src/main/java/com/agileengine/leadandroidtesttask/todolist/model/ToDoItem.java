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

package com.agileengine.leadandroidtesttask.todolist.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.agileengine.leadandroidtesttask.todolist.db.table.ToDoItemTable;

import java.util.Date;


public class ToDoItem implements Parcelable {

    private String id;
    private String title;
    private String description;
    private boolean urgent;
    private Date dueDate;
    private String tags;
    private boolean done;

    public ToDoItem() {

    }

    public ToDoItem(Cursor cursor) {
        id = cursor.getString(cursor.getColumnIndex(ToDoItemTable.Cols.ID));
        title = cursor.getString(cursor.getColumnIndex(ToDoItemTable.Cols.TITLE));
        description = cursor.getString(cursor.getColumnIndex(ToDoItemTable.Cols.DESCRIPTION));
        urgent = cursor.getInt(cursor.getColumnIndex(ToDoItemTable.Cols.URGENT)) != 0;
        tags = cursor.getString(cursor.getColumnIndex(ToDoItemTable.Cols.TAGS));
        dueDate = new Date(cursor.getLong(cursor.getColumnIndex(ToDoItemTable.Cols.DUE_DATE)));
        done = cursor.getInt(cursor.getColumnIndex(ToDoItemTable.Cols.DONE)) != 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isUrgent() {
        return urgent;
    }

    public void setUrgent(boolean urgent) {
        this.urgent = urgent;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeByte(urgent ? (byte) 1 : (byte) 0);
        dest.writeLong(dueDate != null ? dueDate.getTime() : -1);
        dest.writeString(this.tags);
        dest.writeByte(done ? (byte) 1 : (byte) 0);
    }

    protected ToDoItem(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.description = in.readString();
        this.urgent = in.readByte() != 0;
        long tmpDueDate = in.readLong();
        this.dueDate = tmpDueDate == -1 ? null : new Date(tmpDueDate);
        this.tags = in.readString();
        this.done = in.readByte() != 0;
    }

    public static final Parcelable.Creator<ToDoItem> CREATOR = new Parcelable.Creator<ToDoItem>() {
        public ToDoItem createFromParcel(Parcel source) {
            return new ToDoItem(source);
        }

        public ToDoItem[] newArray(int size) {
            return new ToDoItem[size];
        }
    };
}
