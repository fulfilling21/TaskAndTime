package net.fulfilling21.android.taskandtime;

import net.fulfilling21.android.taskandtime.time.TimeUtil;
import android.app.Activity;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class TitleClearFocusListener implements View.OnTouchListener {

	private Activity activity;
	private EditText[] titles;

	public TitleClearFocusListener(Activity activity, EditText[] titles) {
		this.activity = activity;
		this.titles = titles;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO 自動生成されたメソッド・スタブ
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			TimeUtil.clearTitleFocus(titles);
			InputMethodManager imm = (InputMethodManager) activity
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(v.getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
		}
		return false;
	}

}
