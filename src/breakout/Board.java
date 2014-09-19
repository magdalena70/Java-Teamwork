package breakout;

import java.awt.Color;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class Board extends JPanel implements Commons {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Image ii;
	Timer timer;
	Date startTime;
	String message = "Game Over";
	Ball ball;
	Paddle paddle;
	Background backgroundFirstPart;
	Background backgroundSecondPart;
	Brick bricks[];
	TotalPoints totalPoints;
	Ranking newRanking;

	boolean ingame = true;
	int timerId;
	int brickRows;
	int brickColumns;
	int timeInterval;
	private Levels currentLevel;

	public Board(Levels currentLevel, int timeInterval, int brickRows,
			int brickColumns) {

		this.brickRows = brickRows;
		this.brickColumns = brickColumns;
		this.timeInterval = timeInterval;
		this.currentLevel = currentLevel;
		this.startTime = currentLevel.getStartTime();
		
		addKeyListener(new TAdapter());
		setFocusable(true);

		bricks = new Brick[brickRows * brickColumns];
		setDoubleBuffered(true);
		timer = new Timer();
		timer.scheduleAtFixedRate(new ScheduleTask(), 1000, timeInterval);
	}

	public void addNotify() {
		super.addNotify();
		gameInit();
	}

	public void gameInit() {

		ball = new Ball();
		paddle = new Paddle();
		backgroundFirstPart = new Background(currentLevel.getLevel());
		totalPoints = new TotalPoints(currentLevel);
		
		int k = 0;
		for (int i = 0; i < brickRows; i++) {
			for (int j = 0; j < brickColumns; j++) {
				bricks[k] = new Brick(j * 40 + 30, i * 10 + 100,
						currentLevel.getLevel());
				k++;
			}
		}
	}

	public void paint(Graphics g) {
		super.paint(g);

		if (ingame) {
			g.drawImage(backgroundFirstPart.getImage(), backgroundFirstPart.getX(), backgroundFirstPart.getY(),
					backgroundFirstPart.getWidth(), backgroundFirstPart.getHeight(), this);
			
			g.drawImage(ball.getImage(), ball.getX(), ball.getY(),
					ball.getWidth(), ball.getHeight(), this);
			
			g.drawImage(ball.getImage(), ball.getX(), ball.getY(),
					ball.getWidth(), ball.getHeight(), this);
			g.drawImage(paddle.getImage(), paddle.getX(), paddle.getY(),
					paddle.getWidth(), paddle.getHeight(), this);

			Font font = new Font("Verdana", Font.BOLD, 13);
			
			g.setColor(Color.BLACK);
			g.setFont(font);
			g.drawString("Points:" + totalPoints.getPoints(), 0, 10);
			

		    g.drawString("Time:" + currentLevel.getCurrentTime() , 215, 10);
			
			
			for (int i = 0; i < brickColumns * brickRows; i++) {
				if (!bricks[i].isDestroyed())
					g.drawImage(bricks[i].getImage(), bricks[i].getX(),
							bricks[i].getY(), bricks[i].getWidth(),
							bricks[i].getHeight(), this);
			}
		} 

		Toolkit.getDefaultToolkit().sync();
		g.dispose();
	}

	private class TAdapter extends KeyAdapter {

		public void keyReleased(KeyEvent e) {
			paddle.keyReleased(e);
		}

		public void keyPressed(KeyEvent e) {
			paddle.keyPressed(e);
		}
	}

	class ScheduleTask extends TimerTask {
		public void run() {
			ball.move();
			paddle.move();
			checkCollision();
			repaint();
		}
	}

	public void stopGame(Boolean end) {
		ingame = false;

		if (end) {
			// prompt the user to enter their name
			String[] options = {"OK"};
			JPanel panel = new JPanel();
			JLabel lbl = new JLabel("Enter Your name: ");
			JTextField txt = new JTextField(15);
			panel.add(lbl);
			panel.add(txt);
			int selectedOption = JOptionPane.showOptionDialog(null, panel, "The Title", JOptionPane.NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options , options[0]);
	
			if(selectedOption == 0)
			{
				String name = txt.getText();
				name = name.replace(" ", "");
				this.newRanking = new Ranking(totalPoints, currentLevel, name);
				newRanking.addPoint();
			}
			currentLevel.closeBreakout();
			JFrame finalResults = new JFrame("SoftuniBreaker Results");
			
			OpenResultsFile(finalResults);
		}
		
		timer.cancel();


	}
	private void OpenResultsFile (JFrame frame) {
		  String workingDir = System.getProperty("user.dir");  
		  URI uri;
			try {
			    uri = new URI("file://" + workingDir + File.separator + "src" + File.separator + "resources" + File.separator + "ranking.txt");
			    class OpenUrlAction implements ActionListener {
			      @Override public void actionPerformed(ActionEvent e) {
			        open(uri);
			      }
			    }
				frame.setSize(Commons.WIDTH, Commons.HEIGTH);
				frame.setLocationRelativeTo(null);
				frame.setIgnoreRepaint(true);
				frame.setResizable(false);
				frame.setVisible(true);
			    frame.setFocusable(true);
			    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			    frame.setSize(300, 400);
			    Container container = frame.getContentPane();
			    container.setLayout(new GridBagLayout());
			    JButton button = new JButton();
			    button.setText("<HTML>"
			    		+ "<div><p>Your Name:" + newRanking.getUserName() + "</p></div>"
			    		+ "<div><p>Your Points:" + newRanking.getRankUserTotal() + "</p></div>"
			    		+ "<div><p>Your Time:" + newRanking.getRankUserTime() + "</p></div>"
			    		+ "<div><p>Your Position is:" + newRanking.getPosition() + "</p></div>"
			    		+ "<div><p></div></p><div><p>Click the <FONT color=\"#000099\"><U>link</U></FONT>"
			        + " to go open the whole ranking.</p></div></HTML>");
			    button.setPreferredSize(new Dimension(250, 250));
			    button.setBorderPainted(false);
			    button.setOpaque(false);
			    button.setBackground(Color.WHITE);
			    button.setToolTipText(uri.toString());
			    button.addActionListener(new OpenUrlAction());
			    container.add(button);
			    frame.setVisible(true);
			} catch (URISyntaxException e) {
				e.printStackTrace();
				System.out.println("file not found");
			}
		  }

	  private static void open(URI uri) {
		  if (Desktop.isDesktopSupported()) {
	      try {
	        Desktop.getDesktop().browse(uri);
	      } catch (IOException e) { System.out.println("Can't open the file"); }
	      } 
	  }

	public void checkCollision() {

		if (ball.getRect().getMaxY() > Commons.BOTTOM) {
			stopGame(true);
		}

		for (int i = 0, j = 0; i < brickColumns * brickRows; i++) {
			if (bricks[i].isDestroyed()) {
				j++;
			}
			if (j == brickColumns * brickRows) {
				message = "Next Level";
				if (currentLevel.getLevel() <= 2) {
					Levels newLevel = new Levels(currentLevel.getLevel() + 1,
							currentLevel.gettimeInterval() - 2,
							currentLevel.getBrickRows() + 1,
							currentLevel.getBrickColumns(),
							currentLevel.getStartTime());
					currentLevel = newLevel;
					stopGame(false);
				} else {
					stopGame(true);
				}

			}
		}

		if ((ball.getRect()).intersects(paddle.getRect())) {

			int paddleLPos = (int) paddle.getRect().getMinX();
			int ballLPos = (int) ball.getRect().getMinX();

			int first = paddleLPos + 8;
			int second = paddleLPos + 16;
			int third = paddleLPos + 24;
			int fourth = paddleLPos + 32;

			if (ballLPos < first) {
				ball.setXDir(-1);
				ball.setYDir(-1);
			}

			if (ballLPos >= first && ballLPos < second) {
				ball.setXDir(-1);
				ball.setYDir(-1 * ball.getYDir());
			}

			if (ballLPos >= second && ballLPos < third) {
				ball.setXDir(0);
				ball.setYDir(-1);
			}

			if (ballLPos >= third && ballLPos < fourth) {
				ball.setXDir(1);
				ball.setYDir(-1 * ball.getYDir());
			}

			if (ballLPos > fourth) {
				ball.setXDir(1);
				ball.setYDir(-1);
			}

		}

		for (int i = 0; i < brickColumns * brickRows; i++) {
			if ((ball.getRect()).intersects(bricks[i].getRect())) {

				int ballLeft = (int) ball.getRect().getMinX();
				int ballHeight = (int) ball.getRect().getHeight();
				int ballWidth = (int) ball.getRect().getWidth();
				int ballTop = (int) ball.getRect().getMinY();

				Point pointRight = new Point(ballLeft + ballWidth + 1, ballTop);
				Point pointLeft = new Point(ballLeft - 1, ballTop);
				Point pointTop = new Point(ballLeft, ballTop - 1);
				Point pointBottom = new Point(ballLeft, ballTop + ballHeight
						+ 1);

				if (!bricks[i].isDestroyed()) {
					if (bricks[i].getRect().contains(pointRight)) {
						ball.setXDir(-1);
					}

					else if (bricks[i].getRect().contains(pointLeft)) {
						ball.setXDir(1);
					}

					if (bricks[i].getRect().contains(pointTop)) {
						ball.setYDir(1);
					}

					else if (bricks[i].getRect().contains(pointBottom)) {
						ball.setYDir(-1);
					}

					bricks[i].setDestroyed(true);
					totalPoints.addNewPoints(currentLevel.getLevel());
				}
			}
		}
	}
}