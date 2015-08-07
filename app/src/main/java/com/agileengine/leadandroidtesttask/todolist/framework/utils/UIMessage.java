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

package com.agileengine.leadandroidtesttask.todolist.framework.utils;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;


public class UIMessage {

    public static void alert(Context context, int messageId) {
        alert(context, context.getString(messageId));
    }

    public static void alert(Context context, CharSequence message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    public static void info(Context context, int messageId) {
        info(context, context.getString(messageId));
    }

    public static void info(Context context, CharSequence message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void showFromBG(Context context, int messageId) {
        showFromBG(context, context.getString(messageId));
    }

    public static void showFromBG(final Context ctx, final CharSequence message) {
        new Handler(ctx.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                alert(ctx, message);
            }
        });
    }
}
