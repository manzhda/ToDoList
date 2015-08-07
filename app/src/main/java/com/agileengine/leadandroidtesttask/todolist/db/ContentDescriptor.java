package com.agileengine.leadandroidtesttask.todolist.db;

import android.net.Uri;

public class ContentDescriptor {

public static final String AUTHORITY = "com.agileengine.leadandroidtesttask.todolist";
    public static final Uri BASE_URI = Uri.parse("content://" + AUTHORITY);

}