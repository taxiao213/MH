package com.haxi.mh.contentprovider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.haxi.mh.utils.model.LogUtils;

/**
 * 内容提供者
 * Created by Han on 2018/6/25
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */

public class BookContentProvider extends ContentProvider {

    public static final String AUTHRITY = "com.haxi.mh.contentprovider.bookcontentprovider";
    public static final Uri BOOK_CONTENT_URI = Uri.parse("content://" + AUTHRITY + "/book");
    public static final Uri USER_CONTENT_URI = Uri.parse("content://" + AUTHRITY + "/user");
    public static final int CODE_BOOK = 0;
    public static final int CODE_USER = 1;
    public static UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTHRITY, "book", CODE_BOOK);
        uriMatcher.addURI(AUTHRITY, "user", CODE_USER);
    }

    private SQLiteDatabase database;
    private Context context;

    @Override
    public boolean onCreate() {
        LogUtils.e("---BookContentProvider--- onCreate --" + Thread.currentThread().getName());
        context = getContext();

        initData();
        return true;
    }

    private void initData() {
        DBOpenHelper sqLiteOpenHelper = new DBOpenHelper(context);
        database = sqLiteOpenHelper.getWritableDatabase();
        database.execSQL("delete from " + DBOpenHelper.BOOK_TABLE_NAME);
        database.execSQL("delete from " + DBOpenHelper.USER_TABLE_NAME);
        database.execSQL("insert into book values(1,'haha1')");
        database.execSQL("insert into book values(2,'haha2')");
        database.execSQL("insert into user values(1,'user2',1)");
        database.execSQL("insert into user values(2,'user2',2)");

    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        LogUtils.e("---BookContentProvider--- query --" + Thread.currentThread().getName());
        String tableName = getTableName(uri);
        if (tableName == null) {
            throw new IllegalArgumentException("unsupported URI:" + uri);
        }
        return database.query(tableName, projection, selection, selectionArgs, null, null, sortOrder, null);
    }

    @Nullable //类型比较复杂 暂时返回null 或者"*/*"
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        String tableName = getTableName(uri);
        if (tableName == null) {
            throw new IllegalArgumentException("unsupported URI:" + uri);
        }
        database.insert(tableName, null, values);
        context.getContentResolver().notifyChange(uri, null);
        return uri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        String tableName = getTableName(uri);
        if (tableName == null) {
            throw new IllegalArgumentException("unsupported URI:" + uri);
        }
        int count = database.delete(tableName, selection, selectionArgs);
        if (count > 0) {
            context.getContentResolver().notifyChange(uri, null);
        }
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        String tableName = getTableName(uri);
        if (tableName == null) {
            throw new IllegalArgumentException("unsupported URI:" + uri);
        }
        int update = database.update(tableName, values, selection, selectionArgs);
        if (update > 0) {
            context.getContentResolver().notifyChange(uri, null);
        }
        return update;
    }

    public String getTableName(Uri uri) {
        String tableName = null;
        switch (uriMatcher.match(uri)) {
            case CODE_BOOK:
                tableName = DBOpenHelper.BOOK_TABLE_NAME;
                break;
            case CODE_USER:
                tableName = DBOpenHelper.USER_TABLE_NAME;
                break;
            default:
                break;
        }
        return tableName;
    }
}
