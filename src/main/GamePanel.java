package main;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class GamePanel extends JPanel {
	
	private Ball ball;
	private Flipper leftFlipper, rightFlipper;
	//private Random random = new Random();
	
	private boolean collided = false;
	private int count = 0;
	
	private ArrayList<Collideable> objects = new ArrayList<Collideable>();
	
	public GamePanel(Ball ball) {
		this.ball = ball;
		this.leftFlipper = new Flipper("/home/100000000901491/Desktop/pinball-master/src/main/flipperLeft.png", 80, 550);
		this.rightFlipper = new Flipper("/home/100000000901491/Desktop/pinball-master/src/main/flipperRight.png", 220, 550);
		this.objects.add(this.rightFlipper);
		this.objects.add(this.leftFlipper);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);	
		
		this.sideCollision();
		if (!collided && count > 10) {
			this.objectCollision();
			count = 0;
		} else {
			count++;
		}
		
		g.setColor(this.ball.getColor());
		
		g.fillOval(this.ball.getX(), this.ball.getY(), this.ball.getRadius(), this.ball.getRadius());
		
		//g.drawImage(img, x, y, bgcolor, observer);
		g.drawImage(this.leftFlipper.getSprite(), this.leftFlipper.getX(), this.leftFlipper.getY(), null);
		g.drawImage(this.rightFlipper.getSprite(), this.rightFlipper.getX(), this.rightFlipper.getY(), null);
	}
	
	public void sideCollision() {
		if (this.ball.getX() < 2 || this.ball.getX() > this.getWidth() - 10) {
			this.ball.setSpeedX(this.ball.getSpeedX() * (-1));
		}
		
		if (this.ball.getY() < 2 || this.ball.getY() > this.getHeight() - 10) {
			this.ball.setSpeedY(this.ball.getSpeedY() * (-1));
		}
	}
	
	public void objectCollision() {
		Integer[] areaY, areaX;
		
		for (Collideable c : this.objects) {
			areaX = buildArea(c.getX(), c.getWidth());
			areaY = buildArea(c.getY(), c.getHeight());
			
			if (c.getX() <= ball.getX() && ball.getX() < areaX[0]) {
				if (c.getY() <= ball.getY() && ball.getY() < areaY[0]) {
					
					this.ball.setSpeedX(this.ball.getSpeedX() * -1);
					this.ball.setSpeedY(this.ball.getSpeedY() * -1);
					System.out.println("0,0");
					collided = true;
					
				} else if (areaY[0] <= ball.getY() && ball.getY() < areaY[1]) {
					
					//this.ball.setSpeedX(this.ball.getSpeedX() * -1);
					this.ball.setSpeedY(this.ball.getSpeedY() * -1);
					System.out.println("0,1");
					collided = true;
					
				} else if (areaY[1] <= ball.getY() && ball.getY() < areaY[2]) {
					
					this.ball.setSpeedX(this.ball.getSpeedX() * -1);
					this.ball.setSpeedY(this.ball.getSpeedY() * -1);
					System.out.println("0,2");
					collided = true;
					
				} else {
					collided = false;
				}
			} else if (areaX[0] <= ball.getX() && ball.getX() < areaX[1]) {
				
				if (c.getY() <= ball.getY() && ball.getY() < areaY[0]) {
					
					this.ball.setSpeedX(this.ball.getSpeedX() * -1);
			//		this.ball.setSpeedY(this.ball.getSpeedY() * -1);
					System.out.println("1,0");
					collided = true;
					
				} else if (areaY[0] <= ball.getY() && ball.getY() < areaY[1]) {
					
					//this.ball.setSpeedX(this.ball.getSpeedX() * -1); Nunca ocorre
					//this.ball.setSpeedY(this.ball.getSpeedY() * -1);
					System.out.println("1,1");
					collided = true;
					
				} else if (areaY[1] <= ball.getY() && ball.getY() < areaY[2]) {
					
					this.ball.setSpeedX(this.ball.getSpeedX() * -1);
					//this.ball.setSpeedY(this.ball.getSpeedY() * -1);
					System.out.println("1,2");
					collided = true;
					
				} else {
					collided = false;
				}
			} else if (areaX[1] <= ball.getX() && ball.getX() < areaX[2]) {
				if (c.getY() <= ball.getY() && ball.getY() < areaY[0]) {
					
					this.ball.setSpeedX(this.ball.getSpeedX() * -1);
					this.ball.setSpeedY(this.ball.getSpeedY() * -1);
					System.out.println("2,0");
					collided = true;
					
				} else if (areaY[0] <= ball.getY() && ball.getY() < areaY[1]) {
					
				//	this.ball.setSpeedX(this.ball.getSpeedX() * -1);
					this.ball.setSpeedY(this.ball.getSpeedY() * -1);
					System.out.println("2,1");
					collided = true;
					
				} else if (areaY[1] <= ball.getY() && ball.getY() < areaY[2]) {
					
					this.ball.setSpeedX(this.ball.getSpeedX() * -1);
					this.ball.setSpeedY(this.ball.getSpeedY() * -1);
					System.out.println("2,2");
					collided = true;
				}
				else {
					collided = false;
				}
			} else {
				collided = false;
			}
			
//			this.ball.setSpeedX(this.ball.getSpeedX() * -1);
//			this.ball.setSpeedY(this.ball.getSpeedY() * -1);
//			
//
//			
//			if (ball.getX() >= c.getX() && ball.getX() <= c.getX() + c.getWidth() && 
//				ball.getY() >= c.getY() && ball.getY() <= c.getY() + c.getHeight()) {
//				this.ball.setSpeedX(this.ball.getSpeedX() * -1);
//				this.ball.setSpeedY(this.ball.getSpeedY() * -1);
//			}
		}
	}
	
	public Integer[] buildArea(Integer position, Integer length) {
		Integer[] area = new Integer[3];
		area[0] = position + length * 1 / 3;
		area[1] = position + length * 2 / 3;
		area[2] = position + length * 3 / 3;
		return area;	
	}
	

	public Ball getBall() {
		return ball;
	}

	public void setBall(Ball ball) {
		this.ball = ball;
	}
	
	
}
