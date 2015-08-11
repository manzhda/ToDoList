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


public class ServiceActions {
    public static final String ACTION_CREATE_TODO_ITEM = ServiceActions.class.getClass().getPackage() + "ACTION_CREATE_TODO_ITEM";

    public static final String ACTION_CREATE_TODO_ITEM_SUCCESS = ServiceActions.class.getClass().getPackage() + "ACTION_CREATE_TODO_ITEM_SUCCESS";
    public static final String ACTION_CREATE_TODO_ITEM_FAILURE = ServiceActions.class.getClass().getPackage() + "ACTION_CREATE_TODO_ITEM_FAILURE";

    public static final String ACTION_UPDATE_TODO_ITEM = ServiceActions.class.getClass().getPackage() + "ACTION_UPDATE_TODO_ITEM";

    public static final String ACTION_UPDATE_TODO_ITEM_SUCCESS = ServiceActions.class.getClass().getPackage() + "ACTION_UPDATE_TODO_ITEM_SUCCESS";
    public static final String ACTION_UPDATE_TODO_ITEM_FAILURE = ServiceActions.class.getClass().getPackage() + "ACTION_UPDATE_TODO_ITEM_FAILURE";

    public static final String ACTION_DELETE_TODO_ITEM = ServiceActions.class.getClass().getPackage() + "ACTION_DELETE_TODO_ITEM";

    public static final String ACTION_DELETE_TODO_ITEM_SUCCESS = ServiceActions.class.getClass().getPackage() + "ACTION_DELETE_TODO_ITEM_SUCCESS";
    public static final String ACTION_DELETE_TODO_ITEM_FAILURE = ServiceActions.class.getClass().getPackage() + "ACTION_DELETE_TODO_ITEM_FAILURE";
}
