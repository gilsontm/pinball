package main;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class Ball {
	
	private Double x;
	private Double y;
	private Double speedX;
	private Double speedY;
	private BufferedImage sprite;
	
	public Ball(String path, Dimension d) {
		this.x = (double) d.getWidth();
		this.y = (double) d.getHeight();
		this.speedX = 0.5;
		this.speedY = 1.0;
		try {
			this.sprite = ImageIO.read(new File(getClass().getResource(path).getPath()));			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Dimension getDimension() {
		return new Dimension(this.sprite.getWidth(), this.sprite.getHeight());
	}
	
	public Integer getWidth() {
		return this.sprite.getWidth();
	}
	
	public Integer getHeight() {
		return this.sprite.getHeight();
	}
	
	public void move() {
		this.x += this.speedX;
		this.y += this.speedY;
	}
	
	public void setPosition(Dimension d) {
		this.x = d.getWidth();
		this.y = d.getHeight();
	}

	public Integer getX() {
		return x.intValue();
	}

	public void setX(Integer x) {
		this.x = (double) x;
	}

	public Integer getY() {
		return y.intValue();
	}

	public void setY(Integer y) {
		this.y = (double) y;
	}

	public Double getSpeedX() {
		return speedX;
	}

	public void setSpeedX(Double speedX) {
		this.speedX = speedX;
	}

	public Double getSpeedY() {
		return speedY;
	}

	public void setSpeedY(Double speedY) {
		this.speedY = speedY;
	}

	public BufferedImage getSprite() {
		return sprite;
	}

	public void setSprite(BufferedImage sprite) {
		this.sprite = sprite;
	}
	
	public Rectangle getRectangle() {
		return new Rectangle(this.getX(), this.getY(), 
				this.getWidth(), this.getHeight());
	}
}
