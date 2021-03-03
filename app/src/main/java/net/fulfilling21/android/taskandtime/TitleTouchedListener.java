package net.fulfilling21.android.taskandtime;

import net.fulfilling21.android.taskandtime.time.TimeUtil;
import android.app.Activity;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class TitleTouchedListener implements View.OnTouchListener {

	private Activity activity;

	public TitleTouchedListener(Activity activity) {
		this.activity = activity;

	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO 自動生成されたメソッド・スタブ
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			TimeUtil.requestTitleFocus((EditText) v);
			InputMethodManager imm = (InputMethodManager) activity
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(v.getWindowToken(),
					InputMethodManager.SHOW_FORCED);

		}
		return false;
	}

}
