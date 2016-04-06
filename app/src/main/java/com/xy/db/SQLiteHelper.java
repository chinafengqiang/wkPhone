package com.xy.db;

import java.io.File;
import java.io.IOException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 执行创建数据表结构
 */
public class SQLiteHelper extends SQLiteOpenHelper {

	private File dbf = null;
	
	public SQLiteHelper(Context context) {
		super(context, DB.DATABASE_NAME, null, DB.DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DB.TABLES.COURSE.SQL.CREATE);
		db.execSQL(DB.TABLES.CHAPTER.SQL.CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}


	public void close(SQLiteDatabase db){
		if(db != null){  
            db.close();  
        }  
	}

}