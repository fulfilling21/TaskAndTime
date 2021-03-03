package net.fulfilling21.android.taskandtime.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class ForegroundActivityTableDao {

	private SQLiteDatabase db = null;
	private DatabaseHelper dbHelper = null;
	/**
	 * Constructor
	 * 
	 * @param db
	 */
	public ForegroundActivityTableDao(SQLiteDatabase db) {
		this.db = db;

	}

	public ForegroundActivityTableDao(DatabaseHelper dbHelper) {
		this.dbHelper = dbHelper;
		
	}
	
	
	/**
	 * レコードを抽出する.
	 * 
	 * @param id
	 * @return foregroundActivityId
	 */
	public int find() {
		Cursor cursor = null;
		int foregroundActivityId = -1;
		
		try {
			db = dbHelper.getReadableDatabase();
			
			cursor = db.query(DatabaseConst.TABLE_FOREGROUND_ACTIVITY,
					DatabaseConst.COLUMNS_FOREGROUND_ACTIVITY, null, null, null,
					null, null);

			while (cursor.moveToNext()) {
				foregroundActivityId = cursor.getInt(0);
				
			}
		
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			
		} finally {
			if (cursor != null) cursor.close();
			if (db != null) db.close();
			
		}
		
		return foregroundActivityId;

	}

	/**
	 * レコードを更新する.
	 * 
	 * @param foregroundActivityTable
	 * @return
	 */
	public int update(int foregroundActivityId) {
		int num = -1;
		
		try {
			db = dbHelper.getReadableDatabase();
			db.beginTransaction();
			
			ContentValues values = new ContentValues();
			values.put(DatabaseConst.COLUMN_FOREGROUND_ACTIVITY_ID,
					foregroundActivityId);
			num = db.update(DatabaseConst.TABLE_FOREGROUND_ACTIVITY, values, null, null);
			db.setTransactionSuccessful();
			
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			
		} finally {
			if (db != null) {
				db.endTransaction();
				db.close();
				
			}

		}
		
		return num;
		
	}

}
