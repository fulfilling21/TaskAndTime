package net.fulfilling21.android.taskandtime.db;

public class TimeTable {

	private int id = 0;
	private String title = null;
	private boolean isTiming = false;
	private int pausedSecondValue = 0;
	private long pausedTimeMillis = 0;

	public int getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}
	
	public boolean getIsTiming() {
		return isTiming;
	}

	public int getPausedSecondValue() {
		return pausedSecondValue;
	}
	
	public long getPausedTimeMillis() {
		return pausedTimeMillis;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public void setIsTiming(boolean isTiming) {
		this.isTiming = isTiming;
	}

	public void setPausedSecondValue(int pausedSecondValue) {
		this.pausedSecondValue = pausedSecondValue;
		
	}
	
	public void setPausedTimeMillis(long pausedTimeMillis) {
		this.pausedTimeMillis = pausedTimeMillis;
	}

}
