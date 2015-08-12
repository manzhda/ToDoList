package com.tac.cache.resolver;

import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.tac.cache.db.CacheTable;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by tac on 12/24/14.
 */
public class BaseDataResolverImpl implements DataResolverDao {

    public final UriMatcher URI_MATCHER;

    private Context mContext;
    private CacheTable mTable;

    public BaseDataResolverImpl(Context context, CacheTable table) {
        mContext = context;
        mTable = table;
        URI_MATCHER = buildUriMatcher();
    }

    protected UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        if(mTable.getUriMap()!=null){
            Iterator it = mTable.getUriMap().entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, Integer> pairs = (Map.Entry) it.next();
                matcher.addURI(mTable.getAuthority(), pairs.getKey(), pairs.getValue());
                it.remove(); // avoids a ConcurrentModificationException
            }
        }
        return matcher;
    }

    @Override
    public Cursor doQuery(SQLiteDatabase db, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(mTable.getTableName());
        Cursor result = builder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        result.setNotificationUri(mContext.getContentResolver(), uri);
        return result;
    }

    @Override
    public int doUpdate(SQLiteDatabase db, Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int result = db.update(mTable.getTableName(), values, selection, selectionArgs);
//        mContext.getContentResolver().notifyChange(uri, null);
        return result;
    }

    @Override
    public int doDelete(SQLiteDatabase db, Uri uri, String selection, String[] selectionArgs) {
        int result = db.delete(mTable.getTableName(), selection, selectionArgs);
        mContext.getContentResolver().notifyChange(uri, null);
        return result;
    }

    @Override
    public Uri doInsert(SQLiteDatabase db, Uri uri, ContentValues values) {
        long id = db.insert(mTable.getTableName(), null, values);
        Uri result = mTable.getContentUri().buildUpon().appendPath(String.valueOf(id)).build();
        mContext.getContentResolver().notifyChange(uri, null);
        return result;
    }
}
