package com.example.myapplicationdcb;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.example.myapplicationdcb.WordsDBhelper;


public class MyContentProvider extends ContentProvider {
    public  static  final  int Notes_DIR =0;
    public  static  final  int Notes_ITEM =1;
    public static  final  String AUTHORITY ="com.example.myapplicationdcb";
    private static UriMatcher uriMatcher;
    private WordsDBhelper notesDB;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY,"words",Notes_DIR);
        uriMatcher.addURI(AUTHORITY,"words/#",Notes_ITEM);

    }


    public MyContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        SQLiteDatabase db = notesDB.getWritableDatabase();
        int deletedRows=0;
        switch (uriMatcher.match(uri)){
            case Notes_DIR:
                deletedRows = db.delete("words",selection,selectionArgs);
                break;
            case Notes_ITEM:
                String notesID = uri.getPathSegments().get(1);
                deletedRows =db.delete("words","_id=?",new String[]{notesID});
                break;
            default:
                break;
        }
        return  deletedRows;

    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        switch (uriMatcher.match(uri)){
            case  Notes_DIR:
                return "van.android.cursor.dir/van.com.example.myapplicationdcb.words";
            case Notes_ITEM:
                return  "van.android.cursor.item/van.com.example.myapplicationdcb.words";
        }
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        // 添加数据
        SQLiteDatabase db = notesDB.getWritableDatabase();
        Uri uriReturn = null;
        switch (uriMatcher.match(uri)){
            case Notes_DIR:
            case Notes_ITEM:
                long newNotesId = db.insert("words",null,values);
                uriReturn = Uri.parse("content://"+AUTHORITY + "/words/"+newNotesId);
                break;
            default:
                break;
        }

        return  uriReturn;
    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        notesDB = new WordsDBhelper(getContext(),"words",null,2);

        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.
        //查询数据
        SQLiteDatabase db = notesDB.getReadableDatabase();
        Cursor cursor = null;
        switch (uriMatcher.match(uri)){
            case Notes_DIR:
                cursor =db.query("words",projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case  Notes_ITEM:
                String notesId = uri.getPathSegments().get(1);
                cursor = db.query("words",projection,"_id = ?",new String[]{notesId},
                        null,null,sortOrder);
                break;
            default:
                break;
        }
        return cursor;


    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        SQLiteDatabase db= notesDB.getWritableDatabase();
        int updataRows =0;
        switch (uriMatcher.match(uri)){
            case Notes_DIR:
                updataRows =db.update("words",values,selection,selectionArgs);
                break;
            case  Notes_ITEM:
                String notesID = uri.getPathSegments().get(1);
                updataRows=db.update("words",values,"_id=? ",new  String[]{notesID});
                break;
            default:
                break;
        }
        return updataRows;

    }
}
