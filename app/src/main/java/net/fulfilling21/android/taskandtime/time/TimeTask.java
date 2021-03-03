package net.fulfilling21.android.taskandtime.time;

import java.util.TimerTask;

import net.fulfilling21.android.taskandtime.time.TimeConst;

import android.os.Handler;
import android.widget.TextView;

/**
 * 時間を計測するクラス
 * 
 * @author HiroNao
 * 
 */
public class TimeTask extends TimerTask implements TimeConst {

	private TextView hour = null;
	private TextView minute = null;
	private TextView second = null;
	private TextView separator1 = null;
	private TextView separator2 = null;

	private Handler handler = null;

	private int secondValue = -1;

	/**
	 * コンストラクタ
	 * 
	 * @param hour
	 * @param minute
	 * @param second
	 * @param separator1
	 * @param separator2
	 */
	public TimeTask(TextView hour, TextView minute, TextView second,
			TextView separator1, TextView separator2) {
		this.hour = hour;
		this.minute = minute;
		this.second = second;
		this.separator1 = separator1;
		this.separator2 = separator2;

		// 画面の値から計測値（秒）を取得する
		secondValue = TimeUtil.getTimeSecond(hour, minute, second);
		handler = new Handler();

	}

	/**
	 * 計測を行う.
	 */
	public void run() {
		handler.post(new Runnable() {
			public void run() {

				if (secondValue < TIME_MAX_SECOND) {
					// 秒数が最大値未満であれば秒数をインクリメント
					secondValue++;

					// 画面に計測値をセットする
					TimeUtil.setTime(hour, minute, second,
							secondValue);

				} else {
					// 秒数が最大値に達していれば計測を停止
					cancel();

					// 文字色を変更する
					TimeUtil.setTextViewFontColor(new TextView[] {
							hour, minute, second, separator1, separator2 }, 91,
							91, 91);

				}

			}
		});
	}
}
