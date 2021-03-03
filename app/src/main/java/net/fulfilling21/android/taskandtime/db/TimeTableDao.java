package net.fulfilling21.android.taskandtime.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class TimeTableDao {

	private SQLiteDatabase db = null;
	private DatabaseHelper dbHelper = null;

	/**
	 * Constructor
	 * 
	 * @param db
	 */
	public TimeTableDao(SQLiteDatabase db) {
		this.db = db;

	}
	
	public TimeTableDao(DatabaseHelper dbHelper) {
		this.dbHelper = dbHelper;
		
	}

	/**
	 * idをキーにレコードを抽出する.
	 * 
	 * @param id
	 * @return
	 */
	public TimeTable findById(int id) {
		Cursor cursor = null;
		TimeTable timeTable = new TimeTable();
		String selection = DatabaseConst.COLUMN_TIME_ID + " = " + id;

		try {
			db = dbHelper.getReadableDatabase();
		
			cursor = db.query(DatabaseConst.TABLE_TIME,
					DatabaseConst.COLUMNS_TIME, selection, null, null, null, null);
			while (cursor.moveToNext()) {
				timeTable.setId(cursor.getInt(0));
				timeTable.setTitle(cursor.getString(1));
				timeTable.setIsTiming(Boolean.valueOf(cursor.getString(2)));
				timeTable.setPausedSecondValue(cursor.getInt(3));
				timeTable.setPausedTimeMillis(Long.valueOf(cursor.getString(4)));
				
			}
			
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			
		} finally {
			if (cursor != null) cursor.close();
			if (db != null) db.close();
			
		}

		return timeTable;

	}

	/**
	 * レコードを抽出する.
	 * 
	 * @return
	 */
	public TimeTable find() {
		Cursor cursor = null;
		TimeTable timeTable = null;

		try {
			db = dbHelper.getReadableDatabase();
			
			cursor = db.query(DatabaseConst.TABLE_TIME,
					DatabaseConst.COLUMNS_TIME, null, null, null, null, null);
			timeTable = new TimeTable();
			while (cursor.moveToNext()) {
				timeTable.setId(cursor.getInt(0));
				timeTable.setTitle(cursor.getString(1));
				timeTable.setIsTiming(Boolean.valueOf(cursor.getString(2)));
				timeTable.setPausedSecondValue(cursor.getInt(3));
				timeTable.setPausedTimeMillis(Long.valueOf(cursor.getString(4)));
			}
			
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			
		} finally {
			if (cursor != null) cursor.close();
			if (db != null) db.close();
			
		}

		return timeTable;

	}

	/**
	 * レコードを追加する.
	 * 
	 * @param timeTable
	 * @return
	 */
	public long insert(TimeTable timeTable) {
		ContentValues values = new ContentValues();
		values.put(DatabaseConst.COLUMN_TIME_ID, timeTable.getId());
		values.put(DatabaseConst.COLUMN_TIME_TITLE, timeTable.getTitle());
		values.put(DatabaseConst.COLUMN_TIME_IS_TIMING,
				String.valueOf(timeTable.getIsTiming()));
		values.put(DatabaseConst.COLUMN_TIME_PAUSED_SECOND_VALUE,
				timeTable.getPausedSecondValue());
		values.put(DatabaseConst.COLUMN_TIME_PAUSED_TIME_MILLIS,
				String.valueOf(timeTable.getPausedTimeMillis()));

		long rowId = -1;
		
		try {
			db = dbHelper.getReadableDatabase();
			db.beginTransaction();
			rowId = db.insert(DatabaseConst.TABLE_TIME, null, values);
			db.setTransactionSuccessful();
			
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			
		} finally {
			if (db != null) {
				db.endTransaction();
				db.close();
				
			}

		}
		
		return rowId;

	}

	/**
	 * idをキーにレコードを更新する.
	 * 
	 * @param timeTable
	 * @return
	 */
	public int update(TimeTable timeTable) {
		ContentValues values = new ContentValues();
		values.put(DatabaseConst.COLUMN_TIME_ID, timeTable.getId());
		values.put(DatabaseConst.COLUMN_TIME_TITLE, timeTable.getTitle());
		values.put(DatabaseConst.COLUMN_TIME_IS_TIMING,
				String.valueOf(timeTable.getIsTiming()));
		values.put(DatabaseConst.COLUMN_TIME_PAUSED_SECOND_VALUE,
				timeTable.getPausedSecondValue());
		values.put(DatabaseConst.COLUMN_TIME_PAUSED_TIME_MILLIS,
				String.valueOf(timeTable.getPausedTimeMillis()));

		int num = -1;
		
		try {
			db = dbHelper.getReadableDatabase();
			db.beginTransaction();
			num = db.update(DatabaseConst.TABLE_TIME, values, null, null);
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

	/**
	 * 全ての行を削除する.
	 * 
	 * @return
	 */
	public int delete() {
		int num = 0;

		try{
			db = dbHelper.getReadableDatabase();
			db.beginTransaction();
			num = db.delete(DatabaseConst.TABLE_TIME, null, null);
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
