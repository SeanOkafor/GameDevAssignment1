import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * LoserScreen - Draws the "You Lost" overlay centred on the panel when all
 * players have lost all their HP.
 * 
 * ==================== HOW THE LOSER SCREEN WORKS ====================
 * 
 * When a player's HP reaches 0, their sprite falls straight down off the
 * bottom of the screen. Once ALL players are dead (single-player: P1 dead;
 * multiplayer: both P1 AND P2 dead), this overlay appears.
 * 
 * Unlike the ScoreScreen, there are no stats to reveal — just the image
 * centred on the panel with a dim background. After a short delay (50 frames
 * / 0.5 seconds) to let the death animation finish, the screen becomes
 * active and the player can press SPACE to return to the main menu.
 * 
 * IMAGE: loserscreen.png (612x408), displayed at native size, centred on
 * the 1000x1000 panel.
 * ===================================================================
 */
public class LoserScreen {
	
	// The background image (loserscreen.png, 612x408)
	private BufferedImage image;
	
	// Display dimensions (native image size)
	private int imgWidth;
	private int imgHeight;
	
	// Whether the loser screen is currently active/visible
	private boolean active = false;
	
	// Frame counter since activation (small delay before accepting SPACE)
	private int activeTick = 0;
	
	// Frames before SPACE is accepted (0.5 second at 100 FPS)
	private static final int ACCEPT_DELAY = 50;
	
	// Panel dimensions (for centring)
	private static final int PANEL_WIDTH = 1000;
	private static final int PANEL_HEIGHT = 1000;
	
	public LoserScreen() {
		loadImage();
	}
	
	private void loadImage() {
		try {
			image = ImageIO.read(new File("res/New Graphics/loserscreen.png"));
			if (image != null) {
				imgWidth = image.getWidth();
				imgHeight = image.getHeight();
			}
		} catch (IOException e) {
			System.err.println("Error loading loserscreen.png: " + e.getMessage());
			e.printStackTrace();
			// Fallback dimensions if image fails to load
			imgWidth = 612;
			imgHeight = 408;
		}
	}
	
	/**
	 * Activates the loser screen.
	 * Called when all players have died and fallen off screen.
	 */
	public void activate() {
		this.active = true;
		this.activeTick = 0;
	}
	
	/**
	 * Deactivates and resets the loser screen.
	 * Called when returning to the main menu.
	 */
	public void deactivate() {
		active = false;
		activeTick = 0;
	}
	
	/**
	 * Called once per frame while active. Advances the accept timer.
	 */
	public void update() {
		if (!active) return;
		activeTick++;
	}
	
	/**
	 * Draws the loser screen overlay centred on the panel.
	 */
	public void draw(Graphics2D g2d) {
		if (!active) return;
		
		// Dim the background behind the loser screen
		g2d.setColor(new Color(0, 0, 0, 150));
		g2d.fillRect(0, 0, PANEL_WIDTH, PANEL_HEIGHT);
		
		// Centre the image on the 1000x1000 panel
		int drawX = (PANEL_WIDTH - imgWidth) / 2;
		int drawY = (PANEL_HEIGHT - imgHeight) / 2;
		
		// Draw background image
		if (image != null) {
			g2d.drawImage(image, drawX, drawY, imgWidth, imgHeight, null);
		}
	}
	
	// ========== GETTERS ==========
	
	public boolean isActive() { return active; }
	
	/** Returns true once the accept delay has passed (ready for SPACE to dismiss). */
	public boolean isReady() {
		return active && activeTick >= ACCEPT_DELAY;
	}
}
