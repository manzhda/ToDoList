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


import android.content.CursorLoader;

import com.agileengine.leadandroidtesttask.todolist.model.ToDoItem;

public interface ToDoListApi {

    CursorLoader getToDoItems(String selection, String[] selectionArg, String sortOder);

    void addToDoItem(ToDoItem item);

    void updateToDoItem(ToDoItem itemId);

    void deleteToDoItem(ToDoItem itemId);

}
