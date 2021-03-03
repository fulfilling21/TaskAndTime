package net.fulfilling21.android.taskandtime.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class TaskListTableDao {

	private SQLiteDatabase db = null;
	private DatabaseHelper dbHelper;

	/**
	 * Constructor
	 * 
	 * @param db
	 */
	public TaskListTableDao(SQLiteDatabase db) {
		this.db = db;

	}

	public TaskListTableDao(DatabaseHelper dbHelper) {
		this.dbHelper = dbHelper;
		
	}
	
	/**
	 * idをキーにレコードを抽出する.
	 * 
	 * @param id
	 * @return
	 */
	public TaskListTable findById(int id) {
		Cursor cursor = null;
		TaskListTable taskListTable = new TaskListTable();
		String selection = DatabaseConst.COLUMN_TASKLIST_ID + " = " + id;
		
		try {
			db = dbHelper.getReadableDatabase();

			cursor = db.query(DatabaseConst.TABLE_TASKLIST,
					DatabaseConst.COLUMNS_TASKLIST, selection, null, null, null,
					null);
			while (cursor.moveToNext()) {
				taskListTable.setId(cursor.getInt(0));
				taskListTable.setTitle(cursor.getString(1));
				taskListTable.setPausedSecondValue(cursor.getInt(2));
			}
			
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			
		} finally {
			if (cursor != null) cursor.close();
			if (db != null) db.close();
			
		}
		
		return taskListTable;

	}

	/**
	 * レコードを追加する.
	 * 
	 * @param timeTable
	 * @return
	 */
	public long insert(TaskListTable taskListTable) {
		ContentValues values = new ContentValues();
		values.put(DatabaseConst.COLUMN_TASKLIST_ID, taskListTable.getId());
		values.put(DatabaseConst.COLUMN_TASKLIST_TITLE,
				taskListTable.getTitle());
		values.put(DatabaseConst.COLUMN_TASKLIST_PAUSED_SECOND_VALUE,
				taskListTable.getPausedSecondValue());
		
		long rowId = -1;

		try {
			db = dbHelper.getReadableDatabase();
			db.beginTransaction();

			rowId = db.insert(DatabaseConst.TABLE_TASKLIST, null, values);
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
	 * @param taskListTable
	 * @return
	 */
	public int update(TaskListTable taskListTable) {
		ContentValues values = new ContentValues();
		values.put(DatabaseConst.COLUMN_TASKLIST_ID, taskListTable.getId());
		values.put(DatabaseConst.COLUMN_TASKLIST_TITLE,
				taskListTable.getTitle());
		values.put(DatabaseConst.COLUMN_TASKLIST_PAUSED_SECOND_VALUE,
				taskListTable.getPausedSecondValue());
		String whereClause = DatabaseConst.COLUMN_TASKLIST_ID + " = "
				+ taskListTable.getId();
		
		int num = -1;
		
		try {
			db = dbHelper.getReadableDatabase();
			db.beginTransaction();

			num = db.update(DatabaseConst.TABLE_TASKLIST, values, whereClause,
					null);
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
