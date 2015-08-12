package com.tac.cache.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import com.tac.cache.DataResolverMap;
import com.tac.cache.resolver.DataResolverDao;

/**
 * Created by tac on 12/23/14.
 */
public abstract class DataProviderDao extends ContentProvider {

    private SQLiteOpenHelper mDBHelper;

    @Override
    public boolean onCreate() {
        mDBHelper = getDBHelper();
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        DataResolverDao dataResolverDao = DataResolverMap.getUriResolver(uri);
        return dataResolverDao.doQuery(mDBHelper.getReadableDatabase(), uri,
                projection, selection, selectionArgs, sortOrder);
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        DataResolverDao dataResolverDao = DataResolverMap.getUriResolver(uri);
        return dataResolverDao.doInsert(mDBHelper.getWritableDatabase(), uri, values);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        DataResolverDao dataResolverDao = DataResolverMap.getUriResolver(uri);
        return dataResolverDao.doDelete(mDBHelper.getWritableDatabase(), uri, selection, selectionArgs);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        DataResolverDao dataResolverDao = DataResolverMap.getUriResolver(uri);
        return dataResolverDao.doUpdate(mDBHelper.getWritableDatabase(), uri, values, selection, selectionArgs);
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    public abstract SQLiteOpenHelper getDBHelper();
}
