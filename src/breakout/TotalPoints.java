package breakout;

public class TotalPoints {
	int total;
	
	public TotalPoints(Levels currentLevel) {
		int brokenRows = 0;
		int level = currentLevel.getLevel();
		
		while (level > 0) {
			brokenRows += level;
			level--;
		}
		
		this.total = currentLevel.getBrickColumns()*brokenRows*10;
	}
	public void addNewPoints (int level) {
		total += 10*(level+1);
	}
	public int getPoints() {
		return total;
	}
	
	
}
