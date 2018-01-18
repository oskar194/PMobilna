package com.admin.budgetrook;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import java.util.HashMap;

/**
 * Created by Admin on 10.01.2018.
 */

public class StudentsProvider extends ContentProvider {
    public static final String PROVIDER_NAME = "com.admin.budgetrook.StudentsProvider";
    public static final String URL = "content://" + PROVIDER_NAME + "/students";
    public static final Uri CONTENT_URI = Uri.parse(URL);

    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String GRADE = "grade";

    public static HashMap<String, String> STUDENT_PROJECTION_MAP;

    public static final int STUDENTS = 1;
    public static final int STUDENT_ID = 2;

    private static final UriMatcher uriMatcher;
    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "students", STUDENTS);
        uriMatcher.addURI(PROVIDER_NAME, "students/#", STUDENT_ID);
    }

    private SQLiteDatabase db;
    public static final String DATABASE_NAME = "College";
    public static final String STUDENTS_TABLE = "students";
    public static final int DATABASE_VERSION = 1;
    public static final String CREATE_DB_TABLE =
            "CREATE TABLE " + STUDENTS_TABLE + " (id INTEGER PRIMARY KEY AUTOINCREMENT, "+
                    " name TEXT NOT NULL, "+
                    " grade TEXT NOT NULL);";


    private class Helper extends SQLiteOpenHelper {

        Helper(Context context){
            super(context,DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_DB_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + STUDENTS_TABLE);
            onCreate(db);
        }
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        Helper helper = new Helper(context);
        db = helper.getReadableDatabase();
        return (db == null) ? false : true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(STUDENTS_TABLE);

        switch(uriMatcher.match(uri)){
            case STUDENTS:
                qb.setProjectionMap(STUDENT_PROJECTION_MAP);
                break;
            case STUDENT_ID:
                qb.appendWhere(ID + "=" + uri.getPathSegments().get(1));
                break;
            default:
        }

        if(sortOrder == null || sortOrder == ""){
            sortOrder = NAME;
        }

        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public String getType(Uri uri) {
        switch(uriMatcher.match(uri)){
            case STUDENTS:
                return "vnd.android.cursor.dir/vnd.example.students";
            case STUDENT_ID:
                return "vnd.android.cursor.item/vnd.example.students";
            default:
                throw new IllegalStateException("Unknown URI " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long rowId = db.insert(STUDENTS_TABLE, "", values);
        if(rowId > 0 ){
            Uri uri_appended = ContentUris.withAppendedId(CONTENT_URI, rowId);
            getContext().getContentResolver().notifyChange(uri_appended, null);
            return uri_appended;
        }
        throw new IllegalStateException("Failed to add a record into " + uri);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;
        switch(uriMatcher.match(uri)){
            case STUDENTS:
                count = db.delete(STUDENTS_TABLE, selection, selectionArgs);
            break;

            case STUDENT_ID:
                String id = uri.getPathSegments().get(1);
                count = db.delete(STUDENTS_TABLE, ID + " = " + id  +
                        (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")"  : "" ), selectionArgs );
                break;
            default:
                throw new IllegalStateException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int count = 0;
        switch(uriMatcher.match(uri)){
            case STUDENTS:
                count = db.update(STUDENTS_TABLE, values, selection, selectionArgs);
                break;
            case STUDENT_ID:
                String id = uri.getPathSegments().get(1);
                count = db.update(STUDENTS_TABLE, values, ID + " = " + id  +
                        (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")"  : "" ), selectionArgs);
                break;
            default:
                throw new IllegalStateException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
