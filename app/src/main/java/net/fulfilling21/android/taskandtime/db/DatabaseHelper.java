package net.fulfilling21.android.taskandtime.db;

import net.fulfilling21.android.taskandtime.TaskListConst;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	// テーブル"tasklist"作成SQL
	private static final String SQL_CREATE_TABLE_TASKLIST = "create table "
			+ DatabaseConst.TABLE_TASKLIST + " ("
			+ DatabaseConst.COLUMN_TASKLIST_ID
			+ " integer primary key not null, "
			+ DatabaseConst.COLUMN_TASKLIST_TITLE + " text, "
			+ DatabaseConst.COLUMN_TASKLIST_PAUSED_SECOND_VALUE + " integer )";

	// テーブル"time"作成SQL
	private static final String SQL_CREATE_TABLE_TIME = "create table "
			+ DatabaseConst.TABLE_TIME + " (" + DatabaseConst.COLUMN_TIME_ID
			+ " integer primary key not null, "
			+ DatabaseConst.COLUMN_TIME_TITLE + " text, "
			+ DatabaseConst.COLUMN_TIME_IS_TIMING + " text, "
			+ DatabaseConst.COLUMN_TIME_PAUSED_SECOND_VALUE + " integer, "
			+ DatabaseConst.COLUMN_TIME_PAUSED_TIME_MILLIS + " text )";

	// テーブル"foreground_activity"作成SQL
	private static final String SQL_CREATE_TABLE_FOREGOUND_ACTIVITY = "create table "
			+ DatabaseConst.TABLE_FOREGROUND_ACTIVITY
			+ " ("
			+ DatabaseConst.COLUMN_FOREGROUND_ACTIVITY_ID
			+ " integer primary key not null ) ";

	// テーブル"tasklist"削除SQL
	private static final String SQL_DROP_TABLE_TASKLIST = "drop table if exists "
			+ DatabaseConst.TABLE_TASKLIST;

	// テーブル"time"削除SQL
	private static final String SQL_DROP_TABLE_TIME = "drop table if exists "
			+ DatabaseConst.TABLE_TIME;

	// テーブル"foreground_activity"削除SQL
	private static final String SQL_DROP_TABLE_FOREGROUND_ACTIVITY = "drop table if exists "
			+ DatabaseConst.TABLE_FOREGROUND_ACTIVITY;

	/**
	 * コンストラクタ
	 * 
	 * @param context
	 */
	public DatabaseHelper(Context context) {
		super(context, DatabaseConst.DATABASE_NAME, null,
				DatabaseConst.DATABASE_VERSION);

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		ContentValues values = null;

		// テーブル"tasklist"作成
		db.execSQL(SQL_CREATE_TABLE_TASKLIST);
		values = new ContentValues();
		for (int i = 1; i <= TaskListConst.MAX_ARRAY_SIZE; i++) {
			values.put(DatabaseConst.COLUMN_TASKLIST_ID, i);
			values.put(DatabaseConst.COLUMN_TASKLIST_TITLE, "");
			values.put(DatabaseConst.COLUMN_TASKLIST_PAUSED_SECOND_VALUE, 0);
			db.insert(DatabaseConst.TABLE_TASKLIST, null, values);
		}

		// テーブル"time"作成
		db.execSQL(SQL_CREATE_TABLE_TIME);
		values = new ContentValues();
		values.put(DatabaseConst.COLUMN_TIME_ID, 1);
		values.put(DatabaseConst.COLUMN_TIME_TITLE, "");
		values.put(DatabaseConst.COLUMN_TIME_IS_TIMING, "false");
		values.put(DatabaseConst.COLUMN_TIME_PAUSED_SECOND_VALUE, 0);
		values.put(DatabaseConst.COLUMN_TIME_PAUSED_TIME_MILLIS, "0");
		db.insert(DatabaseConst.TABLE_TIME, null, values);

		// テーブル"foreground_activity"作成
		db.execSQL(SQL_CREATE_TABLE_FOREGOUND_ACTIVITY);
		values = new ContentValues();
		values.put(DatabaseConst.COLUMN_FOREGROUND_ACTIVITY_ID,
				DatabaseConst.VALUE_FOREGROUND_ACTIVITY_ID_TASKLIST);
		db.insert(DatabaseConst.TABLE_FOREGROUND_ACTIVITY, null, values);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(SQL_DROP_TABLE_TASKLIST);
		db.execSQL(SQL_DROP_TABLE_TIME);
		db.execSQL(SQL_DROP_TABLE_FOREGROUND_ACTIVITY);
		onCreate(db);

	}

}
