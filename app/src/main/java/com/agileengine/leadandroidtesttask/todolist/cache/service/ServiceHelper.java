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

package com.agileengine.leadandroidtesttask.todolist.cache.service;

import android.content.Context;
import android.content.Intent;

import com.agileengine.leadandroidtesttask.todolist.cache.service.commands.AddToDoItemCommand;
import com.agileengine.leadandroidtesttask.todolist.cache.service.commands.DeleteToDoItemCommand;
import com.agileengine.leadandroidtesttask.todolist.cache.service.commands.UpdateToDoItemCommand;
import com.agileengine.leadandroidtesttask.todolist.model.ToDoItem;
import com.agileengine.leadandroidtesttask.todolist.utils.SyncUtils;


public class ServiceHelper {

    public static void updateItems() {
        SyncUtils.TriggerRefresh();
    }

    public static void createToDoItem(Context context, ToDoItem item) {
        Intent intent = new Intent(context, ToDoService.class);
        intent.setAction(ServiceActions.ACTION_CREATE_TODO_ITEM);
        intent.putExtra(AddToDoItemCommand.BUNDLE_CREATE_TODO_ITEM, item);
        context.startService(intent);
    }

    public static void updateToDoItem(Context context, ToDoItem item) {
        Intent intent = new Intent(context, ToDoService.class);
        intent.setAction(ServiceActions.ACTION_UPDATE_TODO_ITEM);
        intent.putExtra(UpdateToDoItemCommand.BUNDLE_UPDATE_TODO_ITEM, item);
        context.startService(intent);
    }

    public static void deleteToDoItem(Context context, ToDoItem item) {
        Intent intent = new Intent(context, ToDoService.class);
        intent.setAction(ServiceActions.ACTION_DELETE_TODO_ITEM);
        intent.putExtra(DeleteToDoItemCommand.BUNDLE_DELETE_TODO_ITEM, item);
        context.startService(intent);
    }
}
