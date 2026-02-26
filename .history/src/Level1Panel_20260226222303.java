import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * Level1Panel - Renders a parallax scrolling mountain background for Level 1.
 * Uses 5 layers from the parallax_mountain_pack, each scrolling at different speeds.
 */
public class Level1Panel extends JPanel {
	
	// Parallax layers (back to front)
	private BufferedImage bgLayer;           // sky background (slowest)
	private BufferedImage mountainFarLayer;   // distant mountains
	private BufferedImage mountainsLayer;     // closer mountains
	private BufferedImage treesLayer;         // trees
	private BufferedImage foregroundLayer;    // foreground trees (fastest)
	
	// Scroll offsets for each layer
	private double bgOffset = 0;
	private double mountainFarOffset = 0;
	private double mountainsOffset = 0;
	private double treesOffset = 0;
	private double foregroundOffset = 0;
	
	// Scroll speeds (pixels per frame) - back layers slower, front layers faster
	private static final double BG_SPEED = 0.2;
	private static final double MOUNTAIN_FAR_SPEED = 0.5;
	private static final double MOUNTAINS_SPEED = 1.0;
	private static final double TREES_SPEED = 1.5;
	private static final double FOREGROUND_SPEED = 2.5;
	
	public Level1Panel() {
		setDoubleBuffered(true);
		loadLayers();
	}
	
	private void loadLayers() {
		String basePath = "res/New Graphics/parallax_mountain_pack/parallax_mountain_pack/layers/";
		try {
			bgLayer = ImageIO.read(new File(basePath + "parallax-mountain-bg.png"));
			mountainFarLayer = ImageIO.read(new File(basePath + "parallax-mountain-montain-far.png"));
			mountainsLayer = ImageIO.read(new File(basePath + "parallax-mountain-mountains.png"));
			treesLayer = ImageIO.read(new File(basePath + "parallax-mountain-trees.png"));
			foregroundLayer = ImageIO.read(new File(basePath + "parallax-mountain-foreground-trees.png"));
		} catch (IOException e) {
			System.err.println("Error loading parallax layers: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Called each frame to advance the scroll offsets.
	 */
	public void updateParallax() {
		bgOffset += BG_SPEED;
		mountainFarOffset += MOUNTAIN_FAR_SPEED;
		mountainsOffset += MOUNTAINS_SPEED;
		treesOffset += TREES_SPEED;
		foregroundOffset += FOREGROUND_SPEED;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		
		int panelWidth = getWidth();
		int panelHeight = getHeight();
		
		// Draw each layer, tiled horizontally and scaled to fill the panel height
		drawLayer(g2d, bgLayer, bgOffset, panelWidth, panelHeight);
		drawLayer(g2d, mountainFarLayer, mountainFarOffset, panelWidth, panelHeight);
		drawLayer(g2d, mountainsLayer, mountainsOffset, panelWidth, panelHeight);
		drawLayer(g2d, treesLayer, treesOffset, panelWidth, panelHeight);
		drawLayer(g2d, foregroundLayer, foregroundOffset, panelWidth, panelHeight);
	}
	
	/**
	 * Draws a single parallax layer, tiled horizontally and scrolling.
	 * The layer is scaled to fill the panel height while maintaining aspect ratio for tiling width.
	 */
	private void drawLayer(Graphics2D g2d, BufferedImage layer, double offset, int panelWidth, int panelHeight) {
		if (layer == null) return;
		
		// Scale the layer to fill the panel height
		double scale = (double) panelHeight / layer.getHeight();
		int scaledWidth = (int) (layer.getWidth() * scale);
		
		// Wrap the offset so it doesn't grow forever
		double wrappedOffset = offset % scaledWidth;
		
		// Draw enough tiles to cover the panel width, scrolling left
		int startX = (int) -wrappedOffset;
		for (int x = startX; x < panelWidth; x += scaledWidth) {
			g2d.drawImage(layer, x, 0, scaledWidth, panelHeight, null);
		}
		// Draw one extra tile to the left to fill gaps
		if (startX > 0) {
			g2d.drawImage(layer, startX - scaledWidth, 0, scaledWidth, panelHeight, null);
		}
	}
}
