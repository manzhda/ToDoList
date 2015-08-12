package com.tac.cache.resolver;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Created by tac on 12/23/14.
 */
public interface DataResolverDao {

    Cursor doQuery(SQLiteDatabase db, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder);

    int doUpdate(SQLiteDatabase db, Uri uri, ContentValues values, String selection, String[] selectionArgs);

    int doDelete(SQLiteDatabase db, Uri uri, String selection, String[] selectionArgs);

    Uri doInsert(SQLiteDatabase db, Uri uri, ContentValues values);
}
