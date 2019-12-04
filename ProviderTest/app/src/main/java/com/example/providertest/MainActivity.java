package com.example.providertest;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    public String newID;
    EditText editText,editText1;
    //TextView resText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText=findViewById(R.id.et);
        editText1=findViewById(R.id.et1);
        //resText=findViewById(R.id.res);


        Button addData =(Button)findViewById(R.id.add_data);
        addData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //添加数据
                Uri uri = Uri.parse("content://com.example.myapplicationdcb/words");
                ContentValues values =new ContentValues();
                values.put("word",editText.getText().toString());
                values.put("meaning",editText1.getText().toString());
                Uri newUri=getContentResolver().insert(uri,values);
                newID=newUri.getPathSegments().get(1);
    }
});


        Button queryData =(Button) findViewById(R.id.query_data);
        queryData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //查询数据
                Uri uri =Uri.parse("content://com.example.myapplicationdcb/words");
                Cursor cursor =getContentResolver().query(uri,null,null,null,null);
                if(cursor!=null){
                    while (cursor.moveToNext()){
                    String name =cursor.getString(cursor.getColumnIndex("word"));
                    String time =cursor.getString(cursor.getColumnIndex("meaning"));
                   // resText.setText(name+""+time);
                    Log.d("MainActivity","单词为："+name);
                    Log.d("MainActivity","翻译为："+time);
                }
                cursor.close();
            }
        };
         });

        Button updataData = (Button) findViewById(R.id.update_data);
        updataData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //更新数据
                Uri uri =Uri.parse("content://com.example.myapplicationdcb/words/"+newID);
                ContentValues values =new ContentValues();
                values.put("word",editText.getText().toString());
                values.put("meaning",editText1.getText().toString());
                getContentResolver().update(uri,values,null,null);
            }
        });
        Button deleteDate =(Button) findViewById(R.id.delete_data);
        deleteDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //删除数据
                Uri uri =Uri.parse("content://com.example.myapplicationdcb/words/"+newID);
                getContentResolver().delete(uri, null,null);

            }
        });

    }

    public String getTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date curDate = new Date();
        String str = format.format(curDate);
        return str;
    }

}

