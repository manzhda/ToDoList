package com.agileengine.leadandroidtesttask.todolist.framework.utils;

import android.content.Context;
import android.content.res.TypedArray;

public class ResourceUtils {
    public static int[] getIds(Context context, int arrayId) {
        TypedArray array = context.getResources().obtainTypedArray(arrayId);
        int length = array.length();
        int[] ids = new int[length];

        for (int i = 0; i < length; i++) {
            int id = array.getResourceId(i, 0);
            ids[i] = id;
        }

        return ids;
    }

}
