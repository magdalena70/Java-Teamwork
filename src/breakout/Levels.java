package breakout;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Levels {
	int level;
	int brickRows;
	int brickColumns;
	int timeInterval;
	Date startTime;
	Breakout currentBreakout;

	public Levels(int level, int timeInterval, int brickRows, int brickColumns, Date startTime) {
		this.level = level;
		this.brickColumns = brickColumns;
		this.brickRows = brickRows;
		this.timeInterval = timeInterval;
		this.startTime = startTime;
		this.currentBreakout = new Breakout(this);
	}

	public int getLevel() {
		return level;
	}

	public int getBrickRows() {
		return brickRows;
	}

	public int getBrickColumns() {
		return brickColumns;
	}

	public int gettimeInterval() {
		return timeInterval;
	}
	
	public Date getStartTime() {
		return startTime;
	}
	
	public String getCurrentTime() {
	    Date dNow = new Date();
	    SimpleDateFormat ft = new SimpleDateFormat ("mm:ss");
	    Date newTime = new Date(dNow.getTime()-startTime.getTime());
	    return ft.format(newTime);
	}
	
	public long getCurrentTimeLong() {
	    Date dNow = new Date();
	    return dNow.getTime()-startTime.getTime();
	}

	public void closeBreakout() {
		this.currentBreakout.setVisible(false);
	}

}