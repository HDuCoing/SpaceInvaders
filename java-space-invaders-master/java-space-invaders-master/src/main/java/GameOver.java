import javax.swing.ImageIcon;


public class GameOver extends Sprite implements Commons {

	private final String gameOver = "/img/gameover.png";
	private int width;
	Audio aud = new Audio();
	Audio.AudioClip song = aud.loadAudio("space invder sound/5. Romance on Neon.wav");
	/*
	 * Constructor
	 */
	public GameOver() {
		ImageIcon ii = new ImageIcon(this.getClass().getResource(gameOver));
		setWidth(ii.getImage().getWidth(null));
		setImage(ii.getImage());
		setX(0);
		setY(0);

	}

	/*
	 * Getters & Setters
	 */

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}
}
