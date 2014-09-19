package breakout;

import javax.swing.ImageIcon;

public class Brick extends Sprite {

	String brickie = "../resources/brickie";

	boolean destroyed;

	public Brick(int x, int y, int imageNumber) {
		this.x = x;
		this.y = y;
		
		String imageAddress = brickie + imageNumber + ".png";
		ImageIcon ii = new ImageIcon(this.getClass().getResource(imageAddress));
		
		image = ii.getImage();

		width = image.getWidth(null);
		heigth = image.getHeight(null);

		destroyed = false;
	}

	public boolean isDestroyed() {
		return destroyed;
	}

	public void setDestroyed(boolean destroyed) {
		this.destroyed = destroyed;
	}

}
