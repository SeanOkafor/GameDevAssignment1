import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * HealthPack - A floating pickup item that restores 1 HP when touched by a player.
 * 
 * ==================== HOW THE HEALTH PACK WORKS ====================
 * 
 * Health packs spawn at the right edge of the screen at a random Y position.
 * They float left across the screen in a sine-wave pattern, creating a
 * gentle bobbing motion as they drift.
 * 
 * SPAWN RATE:
 *   - Single player: every 15 seconds (1500 frames at 100 FPS)
 *   - Multiplayer:   every 7.5 seconds (750 frames at 100 FPS)
 * 
 * MOVEMENT:
 *   - Horizontal: drifts left at 2 px/frame
 *   - Vertical:   sine wave with amplitude 40px and period ~3 seconds
 *     y = baseY + 40 * sin(tick * 2π / 300)
 * 
 * COLLISION:
 *   Simple AABB overlap check against each player's bounding box.
 *   When a player touches the pack and has < 5 HP, they gain 1 HP
 *   and the pack is consumed (removed from the active list).
 *   If the player is already at max HP (5), the pack passes through.
 * 
 * DISPLAY:
 *   Sprite: res/New Graphics/Extra/Healthpack.png (612×408 native)
 *   Displayed at 50×33 (~1/12 scale) for a small pickup appearance.
 * ===================================================================
 */
public class HealthPack {
	
	// Sprite image (shared across all instances — loaded once, passed in)
	private BufferedImage image;
	
	// Display size (scaled down from 612×408 native)
	private static final int DISPLAY_WIDTH = 300;
	private static final int DISPLAY_HEIGHT = 200;
	
	// Position (doubles for smooth sine wave movement)
	private double x;
	private double baseY;  // centre of the sine wave
	private double y;      // actual drawn Y (baseY + wave offset)
	
	// Movement
	private static final double DRIFT_SPEED = 2.0;     // pixels per frame leftward
	private static final double WAVE_AMPLITUDE = 40.0;  // pixels up/down from baseY
	private static final double WAVE_PERIOD = 300.0;    // frames for one full sine cycle (~3s)
	
	// Frame counter for sine wave calculation
	private int tick = 0;
	
	// Whether this pack has been picked up
	private boolean consumed = false;
	
	// Panel bounds
	private static final int PANEL_WIDTH = 1000;
	private static final int PANEL_HEIGHT = 1000;
	
	/**
	 * Creates a new health pack at the right edge of the screen.
	 * 
	 * @param image  The health pack sprite image (pre-loaded, shared)
	 * @param startY The random Y position (centre of the wave path)
	 */
	public HealthPack(BufferedImage image, double startY) {
		this.image = image;
		this.x = PANEL_WIDTH + 10;  // start just off the right edge
		this.baseY = startY;
		this.y = startY;
	}
	
	/**
	 * Updates position: drift left + sine wave vertical oscillation.
	 */
	public void update() {
		tick++;
		x -= DRIFT_SPEED;
		y = baseY + WAVE_AMPLITUDE * Math.sin(tick * 2 * Math.PI / WAVE_PERIOD);
	}
	
	/**
	 * Draws the health pack at its current position.
	 */
	public void draw(Graphics2D g2d) {
		if (image != null && !consumed) {
			g2d.drawImage(image, (int) x, (int) y, DISPLAY_WIDTH, DISPLAY_HEIGHT, null);
		}
	}
	
	/**
	 * Checks if this health pack overlaps with the given player bounding box (AABB).
	 */
	public boolean collidesWithPlayer(int px, int py, int pw, int ph) {
		int hx = (int) x;
		int hy = (int) y;
		return hx < px + pw && hx + DISPLAY_WIDTH > px
		    && hy < py + ph && hy + DISPLAY_HEIGHT > py;
	}
	
	/**
	 * Returns true if the pack has drifted fully off the left edge of the screen.
	 */
	public boolean isOffScreen() {
		return x + DISPLAY_WIDTH < -10;
	}
	
	/**
	 * Marks this pack as consumed (picked up by a player).
	 */
	public void consume() {
		consumed = true;
	}
	
	public boolean isConsumed() {
		return consumed;
	}
	
	// ========== STATIC IMAGE LOADER ==========
	
	/** Loads and returns the shared health pack sprite image. */
	public static BufferedImage loadSharedImage() {
		try {
			return ImageIO.read(new File("res/New Graphics/Extra/Healthpack.png"));
		} catch (IOException e) {
			System.err.println("Error loading Healthpack.png: " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
}
