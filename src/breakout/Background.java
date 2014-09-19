package breakout;

import javax.swing.ImageIcon;

public class Background extends Sprite implements Commons {
	
	protected String background = "../resources/background";
	
	public Background(int imageNumber) {
		
		String backgroundFull = background + imageNumber + ".png";
		ImageIcon ii = new ImageIcon(this.getClass().getResource(backgroundFull));
		image = ii.getImage();
		
		width = image.getWidth(null);
		heigth = image.getHeight(null);
	}


}
