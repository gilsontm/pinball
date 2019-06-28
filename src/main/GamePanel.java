package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class GamePanel extends JPanel {
	
	private Ball ball;
	private Flipper leftFlipper, rightFlipper;
	private Integer leftRotation = 0, rightRotation = 0;
	private boolean leftRising = false, rightRising = false;
	private Background background;
	private BufferedImage backup;
	private JLabel imageLabel;
	private Dimension startPosition = new Dimension(410,600);
	private Integer flipperOffset = 79;
	private Integer backgroundOffset = 100;
	private boolean inGame = false;
	private boolean isLaunching = false;
	private Double initialSpeedX = 0.7, initialSpeedY = 1.0;
	private Polygon leftBase, rightBase;
	private Collision lastCollision = Collision.NONE; 
	
	public GamePanel() {
		try {
			this.backup = ImageIO.read(new File(getClass().getResource("background.png").getPath()));			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		this.background = new Background(backgroundOffset);
		this.background.setSprite(copyImage(this.backup));

		this.imageLabel = new JLabel();
		this.imageLabel.setIcon(new ImageIcon(this.background.getSprite()));
		this.add(imageLabel);
		
		this.ball = new Ball("ball.png", this.startPosition, this.initialSpeedX, this.initialSpeedY);
		this.leftFlipper = new Flipper("flipperLeft.png", flipperOffset, 550);
		this.rightFlipper = new Flipper("flipperRight.png", this.background.getWidth() - 
				leftFlipper.getWidth() - flipperOffset, 550);
		
		this.leftBase = new Polygon(new int[] {0, flipperOffset + 15, flipperOffset - 5, flipperOffset, 0}, 
				new int[] {480, 540, 560, background.getHeight() - 1, background.getHeight() - 1}, 5);
	
		this.rightBase = new Polygon(new int[] {400, 400 - flipperOffset - 15,400 - flipperOffset + 5, 400-flipperOffset, 400}, 
				new int[] {480, 540, 560, background.getHeight() - 1, background.getHeight() - 1}, 5);
	}
	
	public static BufferedImage copyImage(BufferedImage source){
	    BufferedImage b = new BufferedImage(source.getWidth(), source.getHeight(),
	    		source.getType());
	    Graphics g = b.getGraphics();
	    g.drawImage(source, 0, 0, null);
	    g.dispose();
	    return b;
	}
	
	public void update() {
		
		if (this.isLaunching && this.isBallInGame()) {
			this.startGame();
		}
		
		this.background.setSprite(copyImage(this.backup));
		
		if (inGame) {
			this.sideCollision();
		} else if (isLaunching) {
			this.lauchingSideCollision();
		} 
		
		Graphics2D g2d = (Graphics2D) this.background.getSprite().createGraphics();
		
		AffineTransform transform = g2d.getTransform();
		
		g2d.setColor(Color.DARK_GRAY);
		g2d.drawLine(0, 0, 500 - 1, 0);
		g2d.drawLine(0, 0, 0, 650 - 1);
		g2d.drawLine(0, 650 - 1, 500 - 1, 650 - 1);
		if (inGame) {
			g2d.drawLine(400, 0, 400, 650 - 1);
		} else {
			g2d.drawLine(400, 100, 400, 650 - 1);
		}
		
		
		g2d.drawLine(500 - 1, 0, 500 - 1, 650 - 1);
		
		g2d.fillPolygon(this.leftBase);
		g2d.fillPolygon(this.rightBase);
		
		g2d.setTransform(AffineTransform.getRotateInstance(Math.toRadians(leftRotation), 
				this.leftFlipper.getCenterX()-20, this.leftFlipper.getCenterY()-10));
		g2d.drawImage(this.leftFlipper.getSprite(), this.leftFlipper.getX(), this.leftFlipper.getY(), null);
		
		g2d.setTransform(AffineTransform.getRotateInstance(Math.toRadians(rightRotation), 
				this.rightFlipper.getCenterX()+20, this.rightFlipper.getCenterY()-10));
		g2d.drawImage(this.rightFlipper.getSprite(), this.rightFlipper.getX(), this.rightFlipper.getY(), null);

		g2d.setTransform(transform);
		
		if (inGame) {
			this.pixelCollision();
		}
		
		this.moveBall();
		
		g2d.drawImage(this.ball.getSprite(), this.ball.getX(), this.ball.getY(), null);
		
		this.imageLabel.setIcon(new ImageIcon(this.background.getSprite()));
		this.repaint();
	}
		
	public void sideCollision() {
		if (ball.getX() <= 1) {
			ball.setSpeedX(Math.abs(ball.getSpeedX()));
			this.lastCollision = Collision.BACKGROUND;
		}
		if (ball.getX() + ball.getWidth() >= background.getWidth()) {
			ball.setSpeedX((-1) * Math.abs(ball.getSpeedX()));
			this.lastCollision = Collision.BACKGROUND;
		}
		if (ball.getY() <= 1) {
			ball.setSpeedY(Math.abs(ball.getSpeedY()));
			this.lastCollision = Collision.BACKGROUND;
		}
		if (ball.getY() + ball.getHeight() >= background.getHeight()) {	
			this.resetBall();
			this.lastCollision = Collision.BACKGROUND;
		}
	}
	
	public void lauchingSideCollision() {
		if (ball.getX() <= background.getWidth()) {
			if (ball.getY() > 100) {
				ball.setSpeedX(Math.abs(ball.getSpeedX()));
				this.lastCollision = Collision.BACKGROUND;
			}
		}
		if (ball.getX() + ball.getWidth() >= background.getWidth() + backgroundOffset - 1) {
			ball.setSpeedX((-1) * Math.abs(ball.getSpeedX()));
			this.lastCollision = Collision.BACKGROUND;
		}
		if (ball.getY() <= 1) {
			ball.setSpeedY(Math.abs(ball.getSpeedY()));
			this.lastCollision = Collision.BACKGROUND;
		}
		if (ball.getY() + ball.getHeight() >= background.getHeight()) {	
			ball.setSpeedY(-1 * Math.abs(ball.getSpeedY()));
			this.lastCollision = Collision.BACKGROUND;
		}
	}
	
	public void resetBall() {
		ball.setSpeedX(0.0);
		ball.setSpeedY(0.0);
		ball.setPosition(this.startPosition);
		this.inGame = false;
		this.isLaunching = false;
	}
	
	public void launchBall() {
		this.inGame = false;
		this.isLaunching = true;
		ball.setSpeedX(3.0);
		ball.setSpeedY(-2.0);
	}
	
	public void startGame() {
		this.inGame = true;
		this.isLaunching = false;
		ball.setSpeedX(this.initialSpeedX);
		ball.setSpeedY(this.initialSpeedY);
	}
	
	public void setLastCollision(Integer RGB) {
		if (RGB == null) {
			lastCollision = Collision.NONE;
		} else {
			Color color = new Color(RGB);
			
			// detectar cor baseado no RGB e decidir sobre objetos
			
//			if (color == Color.BLACK) {
//				lastCollision = Collision.FLIPPER;
			if (RGB == Color.DARK_GRAY.getRGB()) {
			 	lastCollision = Collision.BACKGROUND;
			} else {
				lastCollision = Collision.FLIPPER;
			}
		}


//		System.out.println(lastCollision);
//		System.out.println(RGB);
//		System.out.println(Color.DARK_GRAY.getRGB());
	}
	
	public boolean isBallInGame() {
		if (ball.getX() < 0 || ball.getX() + ball.getWidth() > background.getWidth()) {
			return false;
		}
		if (ball.getY() < 0 || ball.getY() + ball.getHeight() > background.getHeight()) {
			return false;
		}
		return true;
	}
	
	public void pixelCollision() {
		Integer RGB = null;
		outerLoop:
		for (int x = 0; x < ball.getWidth(); x++) {
			for (int y = 0; y < ball.getHeight(); y++) {
				if ((ball.getSprite().getRGB(x, y) >> 24) != 0x00) {
					try {
						RGB = this.background.getSprite().getRGB(x + ball.getX(), y + ball.getY());
						if ((RGB >> 24) != 0x00) {
							this.updateSpeed(x, y, RGB);
							break outerLoop;
						}
					} catch (ArrayIndexOutOfBoundsException e) {
						//e.printStackTrace();
						continue;
					}
				}
				
			}
		}
	}
	
	public void updateSpeed(Integer x, Integer y, Integer RGB) {
		
		this.setLastCollision(RGB);
		
		Double speedX = ball.getSpeedX();
		Double speedY = ball.getSpeedY();
		
		if (x < ball.getWidth()/2) {
			// left collision, must go right
			speedX = Math.abs(speedX);
		} else {
			// right collision, must go left
			speedX = -1 * Math.abs(speedX);
		}
		
		if (y < ball.getHeight()/2) {
			// upper collision, must go down
			speedY = Math.abs(speedY);
		} else {
			// lower collision, must go up
			speedY = -1 * Math.abs(speedY);
		}
		
		switch (lastCollision) {
		case FLIPPER:
			if ((leftRising && x + this.ball.getX() <= background.getWidth()/2) ||
				(rightRising && x + this.ball.getX() > background.getWidth()/2)) {
				speedX = (this.initialSpeedX + 0.5) * speedX/Math.abs(speedX);
				speedY = (this.initialSpeedY + 0.5) * speedY/Math.abs(speedY);
			} else {
				speedX = Math.abs(Math.abs(speedX) - 0.1) * speedX/Math.abs(speedX);
				speedY = Math.abs(Math.abs(speedY) - 0.07)/1.5 * speedY/Math.abs(speedY);
			}
			break;
		case BACKGROUND:
			speedX = Math.abs(Math.abs(speedX) - 0.1) * speedX/Math.abs(speedX);
			speedY = Math.abs(Math.abs(speedY) - 0.07)/1.5 * speedY/Math.abs(speedY);
			break;
		case NONE:
			break;
		}
		
		ball.setSpeedX(speedX);
		ball.setSpeedY(speedY);
	}
	
	public void moveBall() {
		if (this.isLaunching) {
			ball.move(false);
		} else if (this.inGame) {
			ball.move(true);
		}
	}
		
	public Ball getBall() {
		return ball;
	}

	public void setBall(Ball ball) {
		this.ball = ball;
	}

	public Integer getLeftRotation() {
		return leftRotation;
	}

	public void setLeftRotation(Integer leftRotation) {
		this.leftRotation = leftRotation;
	}

	public Integer getRightRotation() {
		return rightRotation;
	}

	public void setRightRotation(Integer rightRotation) {
		this.rightRotation = rightRotation;
	}

	public boolean isLeftRising() {
		return leftRising;
	}

	public void setLeftRising(boolean leftRising) {
		this.leftRising = leftRising;
	}

	public boolean isRightRising() {
		return rightRising;
	}

	public void setRightRising(boolean rightRising) {
		this.rightRising = rightRising;
	}

	public boolean isInGame() {
		return inGame;
	}

	public void setInGame(boolean inGame) {
		this.inGame = inGame;
	}

	public boolean isLaunching() {
		return isLaunching;
	}

	public void setLauching(boolean isLaunching) {
		this.isLaunching = isLaunching;
	}
}
