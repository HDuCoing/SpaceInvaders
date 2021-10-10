import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * 
 * @author
 */
public class Board extends JPanel implements Runnable, Commons {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Dimension d;
	private ArrayList aliens;
	private Player player;
	private Shot shot;
	private GameOver gameend;
	private Won vunnet;
	private int alienX = 150;
	private int alienY = 25;
	private int direction = -1;
	private int deaths = 0;
    int lives = 3;
    int score = 0;
	private boolean ingame = true;
	private boolean havewon = true;
	private final String expl = "/img/explosion.png";
	private final String alienpix = "/img/alien.png";
	private String message = "Your planet belongs to us now...";
	Audio aud = new Audio();
	Images imgs = new Images();
	Audio.AudioClip song = aud.loadAudio("space invder sound/5. Romance on Neon.wav");
	Audio.AudioClip explosionSND = aud.loadAudio("space invder sound/explosion.wav");
	private Thread animator;

	/*
	 * Constructor
	 */
	public Board() {
		addKeyListener(new TAdapter());
		setFocusable(true);
		d = new Dimension(BOARD_WIDTH, BOARD_HEIGTH);
		setBackground(Color.black);

		gameInit();
		setDoubleBuffered(true);
		if(ingame){
			aud.playAudio(song);
		}
	}

	public void addNotify() {
		super.addNotify();
		gameInit();
	}

	public void gameInit() {
		aliens = new ArrayList();
		ImageIcon ii = new ImageIcon(this.getClass().getResource(alienpix));

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 6; j++) {
				Alien alien = new Alien(alienX + 18 * j, alienY + 18 * i);
				alien.setImage(ii.getImage());
				aliens.add(alien);
			}
		}

		player = new Player();
		shot = new Shot();

		if (animator == null || !ingame) {
			animator = new Thread(this);
			animator.start();
		}
	}

	public void drawAliens(Graphics g) {
		Iterator it = aliens.iterator();

		while (it.hasNext()) {
			Alien alien = (Alien) it.next();

			if (alien.isVisible()) {
				g.drawImage(alien.getImage(), alien.getX(), alien.getY(), this);
			}

			if (alien.isDying()) {
				alien.die();
			}
		}
	}

	public void drawPlayer(Graphics g) {
		if (player.isVisible()) {
			g.drawImage(player.getImage(), player.getX(), player.getY(), this);
		}

		if (player.isDying()) {
			player.die();
			havewon = false;
			ingame = false;
		}
	}

	public void drawGameEnd(Graphics g) {
		g.drawImage(gameend.getImage(), 0, 0, this);
	}

	public void drawShot(Graphics g) {
		if (shot.isVisible())
			g.drawImage(shot.getImage(), shot.getX(), shot.getY(), this);
	}

	public void drawBombing(Graphics g) {
		Iterator i3 = aliens.iterator();

		while (i3.hasNext()) {
			Alien a = (Alien) i3.next();

			Bomb b = a.getBomb();

			if (!b.isDestroyed()) {
				g.drawImage(b.getImage(), b.getX(), b.getY(), this);
			}
		}
	}
	public void paint(Graphics g) {
		super.paint(g);
		Image background = imgs.loadImage("java-space-invaders-master/src/main/java/img/Backgound.png");
		Image planet = imgs.loadImage("java-space-invaders-master/src/main/java/img/parallax-space-big-planet.png");
		Image ring = imgs.loadImage("java-space-invaders-master/src/main/java/img/parallax-space-ring-planet.png");
		Image stars = imgs.loadImage("java-space-invaders-master/src/main/java/img/parallax-space-stars.png");
		g.setColor(Color.green);
		if (ingame) {
			g.drawImage(background,0,0,d.width, d.height,this);
			g.drawImage(planet,0,50,100,100,this);
			g.drawImage(ring,BOARD_WIDTH-150,100,100,200,this);
			g.drawImage(stars,BOARD_WIDTH/2,BOARD_HEIGTH/2,200,200,this);
			g.drawLine(0, GROUND, BOARD_WIDTH, GROUND);
			drawAliens(g);
			drawPlayer(g);
			drawShot(g);
			drawBombing(g);
		}

		Toolkit.getDefaultToolkit().sync();
		g.dispose();
	}

	public void gameOver() {
		Graphics g = this.getGraphics();

		gameend = new GameOver();
		vunnet = new Won();
		g.setColor(Color.black);
		g.fillRect(0, 0, BOARD_WIDTH, BOARD_HEIGTH);
		if (havewon) {
			g.drawImage(vunnet.getImage(), 0, 0, this);
		} else {
			g.drawImage(gameend.getImage(), 0, 0, this);
		}
		g.setColor(new Color(0, 32, 48));
		g.fillRect(50, BOARD_WIDTH / 2 - 30, BOARD_WIDTH - 100, 50);
		g.setColor(Color.white);
		g.drawRect(50, BOARD_WIDTH / 2 - 30, BOARD_WIDTH - 100, 50);
		
		g.setColor(Color.white);
		g.setFont(new Font ("Helvetica",Font.PLAIN, 23));
		FontMetrics metricsReset = getFontMetrics(g.getFont());
		g.drawString ("Press P to Play Again", ((BOARD_WIDTH - metricsReset.stringWidth(message))/2)
				, BOARD_HEIGTH/2 + 150);
		
		Font small = new Font("Helvetica", Font.BOLD, 14);
		FontMetrics metr = this.getFontMetrics(small);
		g.setColor(Color.white);
		g.setFont(small);
		g.drawString(message, (BOARD_WIDTH - metr.stringWidth(message)) / 2,
				BOARD_WIDTH / 2);
	}

	public void animationCycle() {
		if (deaths == NUMBER_OF_ALIENS_TO_DESTROY) {
			ingame = false;
			message = "You died!! Rip Earth";
		}

		// player

		player.act();

		// shot
		if (shot.isVisible()) {
			Iterator it = aliens.iterator();
			int shotX = shot.getX();
			int shotY = shot.getY();

			while (it.hasNext()) {
				Alien alien = (Alien) it.next();
				int alienX = alien.getX();
				int alienY = alien.getY();

				if (alien.isVisible() && shot.isVisible()) {
					if (shotX >= (alienX) && shotX <= (alienX + ALIEN_WIDTH)
							&& shotY >= (alienY)
							&& shotY <= (alienY + ALIEN_HEIGHT)) {
						ImageIcon ii = new ImageIcon(getClass().getResource(
								expl));
						alien.setImage(ii.getImage());
						alien.setDying(true);
						deaths++;
						shot.die();
						aud.playAudio(explosionSND);
					}
				}
			}

			int y = shot.getY();
			y -= 8;
			if (y < 0)
				shot.die();
			else
				shot.setY(y);
		}

		// aliens

		Iterator it1 = aliens.iterator();

		while (it1.hasNext()) {
			Alien a1 = (Alien) it1.next();
			int x = a1.getX();

			if (x >= BOARD_WIDTH - BORDER_RIGHT && direction != -1) {
				direction = -1;
				Iterator i1 = aliens.iterator();
				while (i1.hasNext()) {
					Alien a2 = (Alien) i1.next();
					a2.setY(a2.getY() + GO_DOWN);
				}
			}

			if (x <= BORDER_LEFT && direction != 1) {
				direction = 1;

				Iterator i2 = aliens.iterator();
				while (i2.hasNext()) {
					Alien a = (Alien) i2.next();
					a.setY(a.getY() + GO_DOWN);
				}
			}
		}

		Iterator it = aliens.iterator();

		while (it.hasNext()) {
			Alien alien = (Alien) it.next();
			if (alien.isVisible()) {

				int y = alien.getY();

				if (y > GROUND - ALIEN_HEIGHT) {
					havewon = false;
					ingame = false;
					message = "Aliens are invading the galaxy!!";
				}

				alien.act(direction);
			}
		}

		// bombs

		Iterator i3 = aliens.iterator();
		Random generator = new Random();

		while (i3.hasNext()) {
			int shot = generator.nextInt(15);
			Alien a = (Alien) i3.next();
			Bomb b = a.getBomb();
			if (shot == CHANCE && a.isVisible() && b.isDestroyed()) {

				b.setDestroyed(false);
				b.setX(a.getX());
				b.setY(a.getY());
			}

			int bombX = b.getX();
			int bombY = b.getY();
			int playerX = player.getX();
			int playerY = player.getY();

			if (player.isVisible() && !b.isDestroyed()) {
				if (bombX >= (playerX) && bombX <= (playerX + PLAYER_WIDTH)
						&& bombY >= (playerY)
						&& bombY <= (playerY + PLAYER_HEIGHT)) {
					ImageIcon ii = new ImageIcon(this.getClass().getResource(
							expl));
					player.setImage(ii.getImage());
					player.setDying(true);
					b.setDestroyed(true);
					gameOver();
				}
			}

			if (!b.isDestroyed()) {
				b.setY(b.getY() + 1);
				if (b.getY() >= GROUND - BOMB_HEIGHT) {
					b.setDestroyed(true);
				}
			}
		}
	}

	public void run() {
		long beforeTime, timeDiff, sleep;

		beforeTime = System.currentTimeMillis();

		while (ingame) {
			repaint();
			animationCycle();

			timeDiff = System.currentTimeMillis() - beforeTime;
			sleep = DELAY - timeDiff;

			if (sleep < 0)
				sleep = 1;
			try {
				Thread.sleep(sleep);
			} catch (InterruptedException e) {
				System.out.println("interrupted");
			}
			beforeTime = System.currentTimeMillis();
		}
		gameOver();
	}

	private class TAdapter extends KeyAdapter {

		public void keyReleased(KeyEvent e) {
			player.keyReleased(e);
		}

		public void keyPressed(KeyEvent e) {

			player.keyPressed(e);

			int x = player.getX();
			int y = player.getY();

			if (ingame) {
				int key = e.getKeyCode();
				if (key == KeyEvent.VK_SPACE) {
					if (!shot.isVisible())
						shot = new Shot(x, y);
				}
			}
			// Play game again
				if((e.getKeyCode()== KeyEvent.VK_P)) {
					if(ingame == false) {
						gameInit();
						repaint(); //call paintComponent(Graphics g) method 
						new SpaceInvaders();
					}
					 
				}
		}
	}
}