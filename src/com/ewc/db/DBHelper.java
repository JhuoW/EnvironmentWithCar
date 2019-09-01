package com.ewc.db;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;







import com.ewc.bean.Environment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	private static SQLiteDatabase sqLiteDatabase;
	private static final String DBNAME = "ewc.db";
	private static final int VERSION = 1;

	public DBHelper(Context context) {
		super(context, DBNAME, null, VERSION);
		openSqLiteDatabase();
	}

	/**
	 * 初始化数据库
	 */
	public SQLiteDatabase openSqLiteDatabase() {
		if (sqLiteDatabase == null) {
			sqLiteDatabase = getWritableDatabase();
		}
		return sqLiteDatabase;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String post = "create table if not exists "
				+ EnvironmentTable.TABLENAME
				+ "(  _id integer primary key autoincrement , "
				+ EnvironmentTable.LNG + " text," 
				+ EnvironmentTable.LAT + " text," 
				+ EnvironmentTable.TIME + " text,"
				+ EnvironmentTable.PM25 + " text," 
				+ EnvironmentTable.PM10 + " text," 
				+ EnvironmentTable.SO + " text,"
				+ EnvironmentTable.HUM + " text,"
				+ EnvironmentTable.TEM + " text)";
		db.execSQL(post);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		String deleteschTable = "DROP TABLE " + EnvironmentTable.TABLENAME;
		db.execSQL(deleteschTable);
		String post = "create table if not exists "
				+ EnvironmentTable.TABLENAME
				+ "(  _id integer primary key autoincrement , "
				+ EnvironmentTable.LNG + " text," + EnvironmentTable.LAT
				+ " text," + EnvironmentTable.TIME + " text,"
				+ EnvironmentTable.PM25 + " text," + EnvironmentTable.PM10
				+ " text," + EnvironmentTable.SO + " text,"
				+ EnvironmentTable.HUM + " text," + EnvironmentTable.TEM
				+ " text)";
		db.execSQL(post);
	}

	public void insertEnvironment(JSONArray posts) {
		try {
			for (int i = 0; i < posts.length(); i++) {
				JSONObject obj = posts.getJSONObject(i);
				String Lng = obj.getString("Lng");
				String Lat = obj.getString("Lat");
				String time = obj.getString("Tim");
				String pm25 = obj.getString("PM25");
				String pm10 = obj.getString("PM10");
				String so = obj.getString("SO");
				String hum = obj.getString("Hum");
				String tem = obj.getString("Tem");
				ContentValues contentValues = new ContentValues();
				contentValues.put(EnvironmentTable.LNG, Lng);
				contentValues.put(EnvironmentTable.LAT, Lat);
				contentValues.put(EnvironmentTable.TIME, time);
				contentValues.put(EnvironmentTable.PM25, pm25);
				contentValues.put(EnvironmentTable.PM10, pm10);
				contentValues.put(EnvironmentTable.SO, so);
				contentValues.put(EnvironmentTable.HUM, hum);
				contentValues.put(EnvironmentTable.TEM, tem);
				sqLiteDatabase.insert(EnvironmentTable.TABLENAME, null, contentValues);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 清空某张表内数据
	 */
	public void deleteAllDataFromTable(String Tablename){
		sqLiteDatabase.execSQL("DELETE FROM " + Tablename);
	}
	public List<Environment> getEnvironmentList(){
		ArrayList<Environment> datas = null;
		Environment evm = null;
		Cursor cursor = sqLiteDatabase.query(EnvironmentTable.TABLENAME, null,
				null, null, null, null, null, null);
		if (cursor != null && cursor.getCount() > 0) {
			datas = new ArrayList<Environment>(cursor.getCount()); // 开辟对应的空间数
			while (cursor.moveToNext()) {
				evm = new Environment();
				evm.setLat(cursor.getString(cursor.getColumnIndex(EnvironmentTable.LAT)));
				evm.setLng(cursor.getString(cursor.getColumnIndex(EnvironmentTable.LNG)));
				evm.setHum(cursor.getString(cursor.getColumnIndex(EnvironmentTable.HUM)));
				evm.setPm10(cursor.getString(cursor.getColumnIndex(EnvironmentTable.PM10)));
				evm.setPm25(cursor.getString(cursor.getColumnIndex(EnvironmentTable.PM25)));
				evm.setSo(cursor.getString(cursor.getColumnIndex(EnvironmentTable.SO)));
				evm.setTem(cursor.getString(cursor.getColumnIndex(EnvironmentTable.TEM)));
				evm.setTime(cursor.getString(cursor.getColumnIndex(EnvironmentTable.TIME)));
				datas.add(evm);
			}
		}
		cursor.close();
		return datas;
	}
	
	
}
