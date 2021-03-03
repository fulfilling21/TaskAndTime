package net.fulfilling21.android.taskandtime.db;

public class TaskListTable {

	private int id = 0;
	private String title = null;
	private int pausedSecondValue = 0;

	public int getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public int getPausedSecondValue() {
		return pausedSecondValue;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setPausedSecondValue(int pausedSecondValue) {
		this.pausedSecondValue = pausedSecondValue;
		
	}
	
}
