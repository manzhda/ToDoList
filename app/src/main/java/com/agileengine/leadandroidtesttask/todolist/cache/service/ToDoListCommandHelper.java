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

import com.agileengine.leadandroidtesttask.todolist.cache.service.commands.AddToDoItemCommand;
import com.agileengine.leadandroidtesttask.todolist.cache.service.commands.UpdateToDoItemCommand;
import com.tac.cache.command.ServiceCommand;
import com.tac.cache.service.CommandHelper;

import java.util.HashMap;
import java.util.Map;


public class ToDoListCommandHelper implements CommandHelper {
    @Override
    public Map<String, ServiceCommand> getServiceCommandMap(final Context context) {
        return new HashMap<String, ServiceCommand>() {
            {
                put(ServiceActions.ACTION_CREATE_TODO_ITEM, new AddToDoItemCommand(context));
                put(ServiceActions.ACTION_UPDATE_TODO_ITEM, new UpdateToDoItemCommand(context));
            }
        };
    }
}
