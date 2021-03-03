package net.fulfilling21.android.taskandtime;

import java.util.ArrayList;
import java.util.List;

import net.fulfilling21.android.taskandtime.db.*;
import net.fulfilling21.android.taskandtime.R;
import net.fulfilling21.android.taskandtime.time.*;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class TaskListActivity extends Activity {

	private DatabaseHelper dbHelper;
	private ForegroundActivityTableDao foregroundActivityTableDao;
	private TaskListTable taskListTable;
	private TaskListTableDao taskListTableDao;
	private TimeTable timeTable;
	private TimeTableDao timeTableDao;

	private TextView hourSum;
	private TextView minuteSum;
	private TextView secondSum;

	private TextView[] ids;
	private EditText[] titles;
	private ImageButton[] buttons;
	private TextView[] hours;
	private TextView[] minutes;
	private TextView[] seconds;
	private TextView[] separators;
	private TextView[] spaces;
	private TextView[] lines;

	private TitleTouchedListener titleTouchedListener;
	private TitleClearFocusListener titleClearFocusListener;

	// private AdView adView; // AdMobの広告

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getWindow().setSoftInputMode(
				LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		setContentView(R.layout.main);

		dbHelper = new DatabaseHelper(this);
		foregroundActivityTableDao = new ForegroundActivityTableDao(dbHelper);
		taskListTable = new TaskListTable();
		taskListTableDao = new TaskListTableDao(dbHelper);
		timeTable = new TimeTable();
		timeTableDao = new TimeTableDao(dbHelper);

		// テーブル"foreground_activity"の値が"1"であればTimeActivityへ遷移する.
		if (foregroundActivityTableDao.find() == DatabaseConst.VALUE_FOREGROUND_ACTIVITY_ID_TIME) {
			Intent intent = new Intent(TaskListActivity.this,
					TimeActivity.class);

			startActivity(intent);

			finish();

		} else {
			// 合計値を初期化
			hourSum = (TextView) findViewById(R.id.label_hour_sum);
			minuteSum = (TextView) findViewById(R.id.label_minute_sum);
			secondSum = (TextView) findViewById(R.id.label_second_sum);

			// 個々のパーツの配列を初期化
			ids = new TextView[TaskListConst.MAX_ARRAY_SIZE];
			titles = new EditText[TaskListConst.MAX_ARRAY_SIZE];
			buttons = new ImageButton[TaskListConst.MAX_ARRAY_SIZE];
			hours = new TextView[TaskListConst.MAX_ARRAY_SIZE];
			minutes = new TextView[TaskListConst.MAX_ARRAY_SIZE];
			seconds = new TextView[TaskListConst.MAX_ARRAY_SIZE];
			separators = new TextView[TaskListConst.MAX_ARRAY_SIZE * 2];
			spaces = new TextView[TaskListConst.MAX_ARRAY_SIZE * 2];
			lines = new TextView[TaskListConst.MAX_ARRAY_SIZE];

			LinearLayout listLayout = new LinearLayout(this);
			LayoutInflater inflater = (LayoutInflater) getApplicationContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			listLayout.setOrientation(LinearLayout.VERTICAL);
			ScrollView sv = (ScrollView) findViewById(R.id.main_scroll);

			titleTouchedListener = new TitleTouchedListener(this);
			titleClearFocusListener = new TitleClearFocusListener(this, titles);

			// パーツの初期化
			LinearLayout taskListLayout = null;
			for (int id = 1; id <= TaskListConst.MAX_ARRAY_SIZE; id++) {
				int index = id - 1;

				taskListLayout = (LinearLayout) inflater.inflate(
						R.layout.tasklist, null);

				ids[index] = (TextView) taskListLayout
						.findViewById(R.id.list_id);
				ids[index].setText(String.valueOf(id));

				titles[index] = (EditText) taskListLayout
						.findViewById(R.id.list_title);
				titles[index].setOnTouchListener(titleTouchedListener);

				buttons[index] = (ImageButton) taskListLayout
						.findViewById(R.id.button_toTime);
				buttons[index].setId(id);
				buttons[index].setOnClickListener(toTimeListener);

				hours[index] = (TextView) taskListLayout
						.findViewById(R.id.list_hour);
				hours[index].setOnTouchListener(titleClearFocusListener);
				minutes[index] = (TextView) taskListLayout
						.findViewById(R.id.list_minute);
				minutes[index].setOnTouchListener(titleClearFocusListener);

				seconds[index] = (TextView) taskListLayout
						.findViewById(R.id.list_second);
				seconds[index].setOnTouchListener(titleClearFocusListener);

				separators[2 * index] = (TextView) taskListLayout
						.findViewById(R.id.list_separator1);
				separators[2 * index]
						.setOnTouchListener(titleClearFocusListener);
				separators[2 * index + 1] = (TextView) taskListLayout
						.findViewById(R.id.list_separator2);
				separators[2 * index + 1]
						.setOnTouchListener(titleClearFocusListener);
				spaces[2 * index] = (TextView) taskListLayout
						.findViewById(R.id.list_space1);
				spaces[2 * index].setOnTouchListener(titleClearFocusListener);
				spaces[2 * index + 1] = (TextView) taskListLayout
						.findViewById(R.id.list_space2);
				spaces[2 * index + 1]
						.setOnTouchListener(titleClearFocusListener);
				lines[index] = (TextView) taskListLayout
						.findViewById(R.id.list_line);
				lines[index].setOnTouchListener(titleClearFocusListener);

				listLayout.addView(taskListLayout);

				if (id % 2 == 0)
					taskListLayout.setBackgroundColor(Color.rgb(235, 235, 235));

			}

			sv.addView(listLayout);

			// adView = (AdView) findViewById(R.id.adView);
			// adView.loadAd(new AdRequest());

		}
	}

	@Override
	public void onResume() {
		super.onResume();

		// テーブル"tasklist"から値をセット
		for (int id = 1; id <= TaskListConst.MAX_ARRAY_SIZE; id++) {
			int index = id - 1;
			taskListTable = taskListTableDao.findById(id);
			titles[index].setText(taskListTable.getTitle());
			TimeUtil.setTime(hours[index], minutes[index], seconds[index],
					taskListTable.getPausedSecondValue());

		}

		// 合計値を表示
		TimeUtil.setTimeSum(hourSum, minuteSum, secondSum, hours, minutes,
				seconds);

	}

	@Override
	public void onPause() {
		super.onPause();

		// テーブル"tasklist"へ書き込む
		for (int id = 1; id <= TaskListConst.MAX_ARRAY_SIZE; id++) {
			int index = id - 1;
			taskListTable.setId(id);
			taskListTable.setTitle(titles[index].getText().toString());
			taskListTable.setPausedSecondValue(TimeUtil.getTimeSecond(
					hours[index], minutes[index], seconds[index]));

			taskListTableDao.update(taskListTable);

		}

	}

	@Override
	public void onDestroy() {
		super.onDestroy();

	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

	}

	private View.OnClickListener toTimeListener = new View.OnClickListener() {
		@Override
		public void onClick(View button) {
			int id = button.getId();
			int index = id - 1;

			TimeUtil.clearTitleFocus(titles);
			button.setBackgroundResource(R.drawable.button_time);

			timeTable.setId(id);
			timeTable.setTitle(titles[index].getText().toString());
			timeTable.setIsTiming(false);
			timeTable.setPausedSecondValue(TimeUtil.getTimeSecond(hours[index],
					minutes[index], seconds[index]));
			timeTable.setPausedTimeMillis(0);
			timeTableDao.update(timeTable);

			Intent intent = new Intent(TaskListActivity.this,
					TimeActivity.class);

			startActivity(intent);

			// テーブル"foreground_activity"へ値"1"をセットする.
			foregroundActivityTableDao
					.update(DatabaseConst.VALUE_FOREGROUND_ACTIVITY_ID_TIME);

			finish();
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(Menu.NONE, 0, Menu.NONE, "time clear");
		menu.add(Menu.NONE, 1, Menu.NONE, "all clear");
		return true;
	}
	
	public boolean onOptionItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 0:
			// 確認のダイアログを入れたい
			// ToDo
			
			// 時間を全て00:00:00に戻す
			for (int id = 1; id <= TaskListConst.MAX_ARRAY_SIZE; id++) {
				int index = id - 1;
				TimeUtil.setTime(hours[index], minutes[index], seconds[index], 0);

			}
			
			return true;
			
		case 1:
			return true;
			
		}
		return false;
	}
	
	public List<View[]> getParts() {
		List<View[]> partsList = new ArrayList<View[]>();
		partsList.add(titles);
		partsList.add(buttons);
		partsList.add(hours);
		partsList.add(minutes);
		partsList.add(seconds);
		return partsList;
	}
}