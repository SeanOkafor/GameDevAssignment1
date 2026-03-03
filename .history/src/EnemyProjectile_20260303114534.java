import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * EnemyProjectile - A projectile fired by a boss that moves LEFT and damages players.
 * 
 * 3-frame looping animation, travels horizontally to the left.
 * On contact with a player, deals 1 heart of damage and triggers i-frames.
 * Can also be configured to move in other directions (for Attack2 vertical arrows).
 */
public class EnemyProjectile {
	
	private BufferedImage[] frames;
	private int currentFrame = 0;
	private int animationTick = 0;
	private static final int ANIMATION_DELAY = 8;
	
	private double x, y;
	private double speedX;
	private double speedY;
	private int displayWidth;
	private int displayHeight;
	
	// Whether this projectile has been consumed (hit a player)
	private boolean consumed = false;
	
	/**
	 * Creates an enemy projectile.
	 * @param frames      3 animation frames (shared reference, not copied)
	 * @param x           starting X
	 * @param y           starting Y
	 * @param speedX      horizontal speed (negative = moves left)
	 * @param speedY      vertical speed (0 for horizontal shots)
	 * @param displayW    rendered width
	 * @param displayH    rendered height
	 */
	public EnemyProjectile(BufferedImage[] frames, double x, double y,
	                        double speedX, double speedY, int displayW, int displayH) {
		this.frames = frames;
		this.x = x;
		this.y = y;
		this.speedX = speedX;
		this.speedY = speedY;
		this.displayWidth = displayW;
		this.displayHeight = displayH;
	}
	
	public void update() {
		x += speedX;
		y += speedY;
		
		// Animate
		animationTick++;
		if (animationTick >= ANIMATION_DELAY) {
			animationTick = 0;
			currentFrame = (currentFrame + 1) % frames.length;
		}
	}
	
	public void draw(Graphics2D g2d) {
		if (frames[currentFrame] != null) {
			g2d.drawImage(frames[currentFrame], (int) x, (int) y, displayWidth, displayHeight, null);
		}
	}
	
	/**
	 * AABB collision with a player.
	 */
	public boolean collidesWithPlayer(int px, int py, int pw, int ph) {
		return x < px + pw &&
		       x + displayWidth > px &&
		       y < py + ph &&
		       y + displayHeight > py;
	}
	
	/** Returns true if off the visible screen (1000x1000). */
	public boolean isOffScreen() {
		return x + displayWidth < -50 || x > 1050 ||
		       y + displayHeight < -50 || y > 1050;
	}
	
	public void consume() { consumed = true; }
	public boolean isConsumed() { return consumed; }
	
	public double getX() { return x; }
	public double getY() { return y; }
	public int getDisplayWidth() { return displayWidth; }
	public int getDisplayHeight() { return displayHeight; }
}
