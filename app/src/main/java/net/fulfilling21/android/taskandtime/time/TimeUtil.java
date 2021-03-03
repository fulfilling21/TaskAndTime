package net.fulfilling21.android.taskandtime.time;

import java.text.DecimalFormat;


import net.fulfilling21.android.taskandtime.TaskListConst;
import net.fulfilling21.android.taskandtime.time.TimeConst;

import android.graphics.Color;
import android.widget.EditText;
import android.widget.TextView;

/**
 * タイマーに関するユーティリティ.
 * 
 * @author HiroNao
 * 
 */
public class TimeUtil implements TimeConst {

	private static DecimalFormat df = new DecimalFormat(TIME_FORMAT);

	/**
	 * RGBに異なる値を使用してTextViewのフォントカラーを指定する.
	 * 
	 * @param textViews
	 * @param red
	 * @param green
	 * @param blue
	 */
	public static void setTextViewFontColor(TextView[] textViews, int red,
			int green, int blue) {
		for (int i = 0; i < textViews.length; i++) {
			textViews[i].setTextColor(Color.rgb(red, green, blue));

		}

	}

	/**
	 * RGBに同一の値をを使用してTextViewのフォントカラーを指定する.
	 * 
	 * @param textViews
	 * @param rgb
	 */
	public static void setTextViewFontColor(TextView[] textViews, int rgb) {
		for (int i = 0; i < textViews.length; i++) {
			textViews[i].setTextColor(Color.rgb(rgb, rgb, rgb));

		}

	}

	/**
	 * 計測値をセットする.
	 * 
	 * @param hour
	 * @param hourValue
	 * @param minute
	 * @param minuteValue
	 * @param second
	 * @param secondValue
	 */
	public static void setTime(TextView hour, int hourValue, TextView minute,
			int minuteValue, TextView second, int secondValue) {
		hour.setText(df.format(hourValue));
		minute.setText(df.format(minuteValue));
		second.setText(df.format(secondValue));
	}

	/**
	 * 計測値をセットする.
	 * 
	 * @param hour
	 * @param minute
	 * @param second
	 * @param secondCount
	 */
	public static void setTime(TextView hour, TextView minute, TextView second,
			int secondCount) {
		if (secondCount <= TIME_MAX_SECOND) {
			hour.setText(df.format(secondCount / 3600));
			minute.setText(df.format(secondCount % 3600 / 60));
			second.setText(df.format(secondCount % 3600 % 60));

		} else {
			hour.setText(df.format(HOUR_MAX));
			minute.setText(df.format(MINUTE_MAX));
			second.setText(df.format(SECOND_MAX));

		}

	}

	/**
	 * 計測値を秒で取得する.
	 * 
	 * @param hour
	 * @param minute
	 * @param second
	 * @return
	 */
	public static int getTimeSecond(TextView hour, TextView minute,
			TextView second) {
		return Integer.parseInt(hour.getText().toString()) * 3600
				+ Integer.parseInt(minute.getText().toString()) * 60
				+ Integer.parseInt(second.getText().toString());

	}

	/**
	 * 合計時間をセットする.
	 * 
	 * @param hourSum
	 * @param minuteSum
	 * @param secondSum
	 * @param hours
	 * @param minutes
	 * @param seconds
	 */
	public static void setTimeSum(TextView hourSum, TextView minuteSum,
			TextView secondSum, TextView[] hours, TextView[] minutes,
			TextView[] seconds) {

		int hourSumValue = 0;
		int minuteSumValue = 0;
		int secondSumValue = 0;

		for (int i = 0; i < TaskListConst.MAX_ARRAY_SIZE; i++) {
			secondSumValue += Integer.parseInt(seconds[i].getText().toString());

			minuteSumValue += secondSumValue / 60;
			secondSumValue = secondSumValue % 60;

			minuteSumValue += Integer.parseInt(minutes[i].getText().toString());

			hourSumValue += minuteSumValue / 60;
			minuteSumValue = minuteSumValue % 60;

			hourSumValue += Integer.parseInt(hours[i].getText().toString());

		}

		hourSum.setText(df.format(hourSumValue));
		minuteSum.setText(df.format(minuteSumValue));
		secondSum.setText(df.format(secondSumValue));

	}

	public static void requestTitleFocus(EditText title) {
		title.setFocusable(true);
		title.setFocusableInTouchMode(true);
		title.requestFocus();
	}
	
	public static void clearTitleFocus(EditText[] titles) {
		for (int i = 0; i < titles.length; i++) {
			if (titles[i].hasFocus()) {
				titles[i].setFocusable(false);
				titles[i].setFocusableInTouchMode(false);
				titles[i].clearFocus();
				break;
			}
		}
	}
}
