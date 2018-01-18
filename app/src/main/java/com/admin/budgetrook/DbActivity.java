package com.admin.budgetrook;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.net.URI;

/**
 * Created by Admin on 10.01.2018.
 */

public class DbActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db);
    }

    public void onClickAddName(View view){
        ContentValues contentValues = new ContentValues();
        contentValues.put(StudentsProvider.NAME, LayoutHelper.<EditText>getById(this, R.id.editText2).getText().toString());
        contentValues.put(StudentsProvider.GRADE, LayoutHelper.<EditText>getById(this,R.id.editText3).getText().toString());
        Uri uri = getContentResolver().insert(StudentsProvider.CONTENT_URI, contentValues);
        Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_SHORT).show();
    }

    public void onClickRetrieveStudents(View view){
        String URL = "content://com.admin.budgetrook.StudentsProvider";
        Uri students = Uri.parse(URL);
        CursorLoader cl = new CursorLoader(getBaseContext(), students, null , null, null, "name");
        Cursor c = cl.loadInBackground();
        if(c.moveToFirst()) {
            do{
                Toast.makeText(this,
                        c.getString(c.getColumnIndex(StudentsProvider.ID)) +
                                ", " +  c.getString(c.getColumnIndex( StudentsProvider.NAME)) +
                                ", " + c.getString(c.getColumnIndex( StudentsProvider.GRADE)),
                        Toast.LENGTH_SHORT).show();
            } while (c.moveToNext());
        }
    }





}
