package net.fulfilling21.android.taskandtime.db;

public class DatabaseConst {
	// データベースに関する定数
	public static final String DATABASE_NAME = "taskandtime_data";
	public static final int DATABASE_VERSION = 1;

	// テーブル"tasklist"に関する定数
	public static final String TABLE_TASKLIST = "tasklist";
	public static final String COLUMN_TASKLIST_ID = "_id";
	public static final String COLUMN_TASKLIST_TITLE = "title";
	public static final String COLUMN_TASKLIST_PAUSED_SECOND_VALUE = "paused_second_value";
	public static final String[] COLUMNS_TASKLIST = { COLUMN_TASKLIST_ID,
			COLUMN_TASKLIST_TITLE, COLUMN_TASKLIST_PAUSED_SECOND_VALUE };

	// テーブル"time"に関する定数
	public static final String TABLE_TIME = "time";
	public static final String COLUMN_TIME_ID = "_id";
	public static final String COLUMN_TIME_TITLE = "title";
	public static final String COLUMN_TIME_IS_TIMING = "is_timing";
	public static final String COLUMN_TIME_PAUSED_SECOND_VALUE = "paused_second_value";
	public static final String COLUMN_TIME_PAUSED_TIME_MILLIS = "paused_time_millis";
	public static final String[] COLUMNS_TIME = { COLUMN_TIME_ID,
			COLUMN_TIME_TITLE, COLUMN_TIME_IS_TIMING,
			COLUMN_TIME_PAUSED_SECOND_VALUE, COLUMN_TIME_PAUSED_TIME_MILLIS };

	// テーブル"foreground_activity"に関する定数
	public static final String TABLE_FOREGROUND_ACTIVITY = "foreground_activity";
	public static final String COLUMN_FOREGROUND_ACTIVITY_ID = "_id";
	public static final String[] COLUMNS_FOREGROUND_ACTIVITY = { COLUMN_FOREGROUND_ACTIVITY_ID };
	public static final int VALUE_FOREGROUND_ACTIVITY_ID_TASKLIST = 0;
	public static final int VALUE_FOREGROUND_ACTIVITY_ID_TIME = 1;

}
