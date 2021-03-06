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

package com.agileengine.leadandroidtesttask.todolist.cache.service.commands;

import android.content.Context;
import android.os.Bundle;

import com.agileengine.leadandroidtesttask.todolist.App;
import com.agileengine.leadandroidtesttask.todolist.cache.service.ServiceActions;
import com.agileengine.leadandroidtesttask.todolist.model.ToDoItem;
import com.tac.cache.command.ServiceCommand;

public class UpdateToDoItemCommand extends ServiceCommand {

    public static final String BUNDLE_UPDATE_TODO_ITEM = "update_todo_item";
    public static final String BUNDLE_UPDATE_TODO_ITEM_NOTIFY = "update_todo_item_notify";

    public UpdateToDoItemCommand(Context context) {
        super(context, ServiceActions.ACTION_UPDATE_TODO_ITEM_SUCCESS, ServiceActions.ACTION_UPDATE_TODO_ITEM_FAILURE);
    }

    @Override
    protected Bundle perform(Bundle extras) throws Exception {

        ToDoItem item = extras.getParcelable(BUNDLE_UPDATE_TODO_ITEM);
        boolean notify = extras.getBoolean(BUNDLE_UPDATE_TODO_ITEM_NOTIFY);
        App.getToDoApi().updateToDoItem(item, notify);

        return null;
    }
}
