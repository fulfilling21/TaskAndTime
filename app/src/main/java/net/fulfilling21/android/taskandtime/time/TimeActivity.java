package net.fulfilling21.android.taskandtime.time;

import java.util.Timer;

//import com.google.android.gms.AdRequest;
//import com.google.android.gms.AdView;

import net.fulfilling21.android.taskandtime.R;
import net.fulfilling21.android.taskandtime.TaskListActivity;
import net.fulfilling21.android.taskandtime.db.*;
import android.app.Activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

//import com.google.android.gms.ads.AdView;
//import com.google.android.gms.ads.AdRequest;

public class TimeActivity extends Activity implements TimeConst {

	private DatabaseHelper dbHelper;
	private ForegroundActivityTableDao foregroundActivityTableDao;
	private TaskListTable taskListTable;
	private TaskListTableDao taskListTableDao;
	private TimeTable timeTable;
	private TimeTableDao timeTableDao;

	private TextView id; // タスクid
	private TextView title; // タイトルを表すTextView

	private TextView hour; // 「時」を表すTextView
	private TextView minute; // 「分」を表すTextView
	private TextView second; // 「秒」を表すTextView
	private TextView separator1; // 「時」「分」の間の「:」
	private TextView separator2; // 「分」「秒」の間の「:」

	private ImageButton time; // 「Start」「Stop」ボタン
	private ImageButton reset; // 「Reset」ボタン
	private ImageButton back; // 「Back」ボタン

	private boolean isTiming; // 計測中であることを表すフラグ

	private Timer timer; // 計測用のタイマークラス

//	private AdView adView; // AdMobの広告
//	private AdRequest adRequest;

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.time);

		dbHelper = new DatabaseHelper(this);
		foregroundActivityTableDao = new ForegroundActivityTableDao(dbHelper);
		taskListTable = new TaskListTable();
		taskListTableDao = new TaskListTableDao(dbHelper);
		timeTable = new TimeTable();
		timeTableDao = new TimeTableDao(dbHelper);

		id = (TextView) findViewById(R.id.label_id);
		title = (TextView) findViewById(R.id.label_title);

		hour = (TextView) findViewById(R.id.label_hour);
		minute = (TextView) findViewById(R.id.label_minute);
		second = (TextView) findViewById(R.id.label_second);
		separator1 = (TextView) findViewById(R.id.label_separator1);
		separator2 = (TextView) findViewById(R.id.label_separator2);

		time = (ImageButton) findViewById(R.id.button_time);
		time.setOnTouchListener(timeListener);
		reset = (ImageButton) findViewById(R.id.button_reset);
		reset.setOnTouchListener(resetListener);
		back = (ImageButton) findViewById(R.id.button_back);
		back.setOnTouchListener(backListener);

//		adView = (AdView) findViewById(R.id.adView);
//		adRequest = new AdRequest.Builder().build();
//		adView.loadAd(adRequest);

	}

	@Override
	public void onResume() {
		super.onResume();

		timeTable = timeTableDao.find();
		isTiming = timeTable.getIsTiming();

		id.setText(String.valueOf(timeTable.getId()));
		title.setText(timeTable.getTitle());

		// 計測中に画面が隠れた後再表示されたときの処理
		if (isTiming) {
			// タイマーが中断していた時間を取得
			long count = System.currentTimeMillis()
					- timeTable.getPausedTimeMillis();

			// 中断時間分だけ待機し時間調整を行う
			try {
				Thread.sleep(TimeConst.TIME_INTERVAL - count
						% TimeConst.TIME_INTERVAL);
			} catch (InterruptedException ie) {
				// ignored
			}

			// 停止時の計測値に中断時間を加え、現在あるべき計測値（秒）を算出する
			int secondValue = (int) (count / TimeConst.TIME_INTERVAL + 1)
					+ timeTable.getPausedSecondValue();

			// 計測値をセットする
			TimeUtil.setTime(hour, minute, second, secondValue);

			// ボタンの状態を計測中にする
			setStateIsTiming();

			if (secondValue >= TIME_MAX_SECOND) {
				// 計測値が最大に達していた場合は、文字色を変える
				TimeUtil.setTextViewFontColor(new TextView[] { hour,
						minute, second, separator1, separator2 },
						COLOR_LIMIT);

			} else {
				// 計測値が最大値未満の場合はタイマーを再開する
				_time();

			}

		} else {
			int pausedSecondValue = timeTable.getPausedSecondValue();

			// 計測値をセットする
			TimeUtil.setTime(hour, minute, second, pausedSecondValue);

			// ボタンの状態を停止中にする
			setStateIsNotTiming();

			if (pausedSecondValue >= TIME_MAX_SECOND) {
				// 計測値が最大に達していた場合は、文字色を変える
				TimeUtil.setTextViewFontColor(new TextView[] { hour,
						minute, second, separator1, separator2 },
						COLOR_LIMIT);

			}
		}
	}

	@Override
	public void onPause() {
		super.onPause();

		// テーブル"time"をupdateするための値をセット
		timeTable.setId(Integer.parseInt(id.getText().toString()));
		timeTable.setTitle(title.getText().toString());
		timeTable.setIsTiming(isTiming);
		timeTable.setPausedSecondValue(TimeUtil.getTimeSecond(hour, minute,
				second));
		timeTable.setPausedTimeMillis(System.currentTimeMillis());

		// テーブル"time"に値を書き込む
		timeTableDao.update(timeTable);

		// タイマーを中断する
		if (isTiming && timer != null)
			timer.cancel();

	}

	@Override
	public void onDestroy() {
		super.onDestroy();

	}

	/**
	 * 計測を開始する.
	 */
	private void _time() {
		timer = new Timer();
		timer.schedule(new TimeTask(hour, minute, second, separator1,
				separator2), TimeConst.TIME_INTERVAL, TimeConst.TIME_INTERVAL);

	}

	/**
	 * 「Start」/「Stop」ボタンのクリックイベント・リスナ
	 */
	private View.OnTouchListener timeListener = new View.OnTouchListener() {
		public boolean onTouch(View v, MotionEvent e) {
			if (e.getAction() == MotionEvent.ACTION_DOWN) {
				((Vibrator)getSystemService(VIBRATOR_SERVICE)).vibrate(35);
				if (isTiming) time.setImageResource(R.drawable.button_stop_pressed);
				else time.setImageResource(R.drawable.button_start_pressed);

			} else if (e.getAction() == MotionEvent.ACTION_UP) {
				// 「スタート」ボタンをクリックしたら計測を開始する.
				if (!isTiming) {
					// ボタンの状態を切り替える
					setStateIsTiming();

					// 計測開始
					_time();

					// 「ストップ」ボタンをクリックしたら計測を停止する.
				} else {
					// ボタンの状態を切り替える
					setStateIsNotTiming();

					// 計測停止
					if (timer != null) timer.cancel();

					// 計測値が最大に達している場合は文字色を黒へ変更する.
					if (TimeUtil.getTimeSecond(hour, minute, second) >= TIME_MAX_SECOND)
						TimeUtil.setTextViewFontColor(new TextView[] { hour,
								minute, second, separator1, separator2 },
								COLOR_NORMAL);

				}
			}
			return true;
		}
	};

	/**
	 * 「Reset」ボタンのクリックイベント・リスナ
	 */
	private View.OnTouchListener resetListener = new View.OnTouchListener() {
		public boolean onTouch(View v, MotionEvent e) {
			if (e.getAction() == MotionEvent.ACTION_DOWN) {
				((Vibrator)getSystemService(VIBRATOR_SERVICE)).vibrate(35);
				reset.setImageResource(R.drawable.button_reset_pressed);

			} else if (e.getAction() == MotionEvent.ACTION_UP) {
				setStateIsNotTiming();

				// 計測値を0に戻す.
				TimeUtil.setTime(hour, TIME_DEFAULT, minute, TIME_DEFAULT, second,
						TIME_DEFAULT);

				// 文字色を黒へ変更する
				TimeUtil.setTextViewFontColor(new TextView[] { hour, minute,
						second, separator1, separator2 }, COLOR_NORMAL);

			}

			return true;
		}

	};

	/**
	 * 「Back」ボタンのクリックイベント・リスナ
	 */
	private View.OnTouchListener backListener = new View.OnTouchListener() {
		public boolean onTouch(View v, MotionEvent e) {
			if (e.getAction() == MotionEvent.ACTION_DOWN) {
				((Vibrator)getSystemService(VIBRATOR_SERVICE)).vibrate(35);
				back.setImageResource(R.drawable.button_back_pressed);

			} else if (e.getAction() == MotionEvent.ACTION_UP) {
				back.setImageResource(R.drawable.button_back);

				// テーブル"tasklist"をupdate
				taskListTable.setId(Integer.parseInt(id.getText().toString()));
				taskListTable.setTitle(title.getText().toString());
				taskListTable.setPausedSecondValue(TimeUtil.getTimeSecond(
						hour, minute, second));
				taskListTableDao.update(taskListTable);

				// テーブル"foreground_activity"へ値"0"をセットする.
				foregroundActivityTableDao
						.update(DatabaseConst.VALUE_FOREGROUND_ACTIVITY_ID_TASKLIST);

				Intent intent = new Intent(TimeActivity.this,
						TaskListActivity.class);

				startActivity(intent);

				finish();
			}

			return true;

		}

	};

	private void setStateIsTiming() {
		isTiming = true;

		// ボタンの画像を「Stop」へ切り替える
		time.setImageResource(R.drawable.button_stop);
//		time.setImageResource(R.style.style_button_stop);

		// 「Back」「Reset」ボタンを押せないようにする
		reset.setEnabled(false);
		reset.setImageResource(R.drawable.button_reset_disable);
		back.setEnabled(false);
		back.setImageResource(R.drawable.button_back_disable);

	}

	private void setStateIsNotTiming() {
		isTiming = false;

		// ボタンの画像を「Start」へ切り替える
		time.setImageResource(R.drawable.button_start);

		// 「Back」「Reset」ボタンを押せるようにする
		reset.setEnabled(true);
		reset.setImageResource(R.drawable.button_reset);
//		reset.setImageResource(R.style.style_button_reset);
		back.setEnabled(true);
		back.setImageResource(R.drawable.button_back);
//		back.setImageResource(R.style.style_button_back);

	}

	/**
	 * 端末のBackボタンが押された時の動作.強制終了したい.
	 * @param keyCode
	 * @param event
     * @return
     */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			onPause();
		}
		return super.onKeyDown(keyCode, event);
	}

}
