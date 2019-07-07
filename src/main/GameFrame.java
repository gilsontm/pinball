package main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class GameFrame extends JFrame {
	
	private GamePanel gamePanel;
	private JPanel rightPanel;
	private JLabel currentPoints;
	private boolean isPressedLeft = false;
	private boolean isPressedRight = false;
	private boolean rotateLeft = false, rotateRight = false;
	
	public GameFrame() {
		gamePanel = new GamePanel();
		gamePanel.setMaximumSize(new Dimension(500, 650));
		
		rightPanel = new JPanel();
		rightPanel.setMinimumSize(new Dimension(500, 500));
		rightPanel.setLayout(new BorderLayout());
		currentPoints = new JLabel(gamePanel.getTotalPoints().toString());
		currentPoints.setFont(new Font("Arial", Font.BOLD, 20));
		rightPanel.add(currentPoints, BorderLayout.NORTH);
		this.setLayout(new BorderLayout());
		this.add(gamePanel, BorderLayout.WEST);
		this.add(rightPanel, BorderLayout.CENTER);
		
		this.addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_LEFT) {
					isPressedLeft = true;
					rotateLeft = true;
				}
				if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
					isPressedRight = true;
					rotateRight = true;
				}
				if (e.getKeyCode() == KeyEvent.VK_SPACE) {
					if (gamePanel.isWaitingLaunch()) {
						gamePanel.setLaunch();
					}
				}
				if (e.getKeyCode() == KeyEvent.VK_P) {
					if (!gamePanel.isOver()) {
						gamePanel.togglePaused();
					}
				}
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					if (gamePanel.isOver()) {
						gamePanel.restartGame();
					}
				}
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_LEFT) {
					isPressedLeft = false;
				}
				if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
					isPressedRight = false;
				}
				if (e.getKeyCode() == KeyEvent.VK_SPACE) {
					if (gamePanel.isPreparingLaunch()) {
						gamePanel.launchBall();
					}
				}
			}
		});
	}
	
	public void setup() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(600, 690);
		this.setTitle("Pinball");
		this.setVisible(true);
	}
	
	public void computeRotations() {
		Integer rotation;
		if (this.rotateLeft) {
			rotation = this.gamePanel.getLeftRotation();
			if (rotation >= 0) {
				this.gamePanel.setLeftRising(true);
			}
			if (rotation <= -90) {
				this.gamePanel.setLeftRising(false);
			}
			if (rotation >= -2 && !this.gamePanel.isLeftRising()) {
				this.rotateLeft = false;
			}
			if (this.gamePanel.isLeftRising()) {
				rotation -= 2;
			} else if (!isPressedLeft || rotation != -90) {
				rotation += 2;
			}
			this.gamePanel.setLeftRotation(rotation);
		}
		
		if (this.rotateRight) {
			rotation = this.gamePanel.getRightRotation();
			if (rotation <= 0) {
				this.gamePanel.setRightRising(true);
			}
			if (rotation >= 90) {
				this.gamePanel.setRightRising(false);
			}
			if (rotation <= 2 && !this.gamePanel.isRightRising()) {
				this.rotateRight = false;
			}
			if (this.gamePanel.isRightRising()) {
				rotation += 2;
			} else if (!isPressedRight || rotation != 90) {
				rotation -= 2;
			}
			this.gamePanel.setRightRotation(rotation);
		}
	}
	
	public void run() throws InterruptedException {
		this.setup();
		
		while (true) {
			if (!gamePanel.isPaused() && !gamePanel.isOver()) {
				this.computeRotations();
				this.gamePanel.update();
				this.currentPoints.setText(gamePanel.getTotalPoints().toString());
			} else if (gamePanel.isOver()) {
				gamePanel.drawGameOver();
			} else if (gamePanel.isPaused()) {
				gamePanel.drawPaused();
			}
			//Thread.sleep(1);
			Thread.sleep(0, 50);
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		GameFrame game = new GameFrame();
		game.run();
	}

}
