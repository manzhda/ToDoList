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

package com.agileengine.leadandroidtesttask.todolist;

import android.app.Application;
import android.content.Context;

import com.agileengine.leadandroidtesttask.todolist.api.ToDoListApi;
import com.agileengine.leadandroidtesttask.todolist.api.ToDoListApiImpl;
import com.agileengine.leadandroidtesttask.todolist.api.ToDoListApiImplSupport;


public class App extends Application {

    private static Context sContext;
    private static ToDoListApi sToDoApi;
    private static ToDoListApiImplSupport sToDoApiSupport;

    public static Context getContext() {
        return sContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();

        sToDoApi = new ToDoListApiImpl();
        sToDoApiSupport = new ToDoListApiImplSupport();
    }

    public static ToDoListApi getToDoApi() {
        return sToDoApi;
    }

    public static ToDoListApiImplSupport getToDoApiSupport() {
        return sToDoApiSupport;
    }
}
