import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import util.UnitTests;

// MainWindow — primary JFrame handling screen navigation, input, and the game loop.
// Created by Abraham Campbell on 15/01/2020. Copyright (c) 2020 Abraham Campbell.
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE. (MIT LICENCE)

public class MainWindow {
	 private static  JFrame frame = new JFrame("Trail Blazers");   
	 private static   Model gameworld= new Model();
	 private static   Viewer canvas = new  Viewer( gameworld);
	 private KeyListener Controller =new Controller()  ; 
	 private static   int TargetFPS = 100;
	 private static boolean startGame= false; 
	 
	 // Screen labels
	 private JLabel mainScreenLabel;
	 private JLabel controlsScreenLabel;
	 private JLabel levelSelectionScreenLabel;
	 
	 // Buttons (transparent overlays on the screen images)
	 private JButton controlsButton;
	 private JButton singlePlayerButton;
	 private JButton multiPlayerButton;
	 private JButton backFromControlsButton;
	 private JButton backFromLevelSelectButton;
	 private JButton level1Button;
	 private JButton level2Button;
	 
	 // Level 1 parallax background panel
	 private static Level1Panel level1Panel;
	 private static boolean level1Running = false;
	 
	 // Level 2 parallax background panel
	 private static Level2Panel level2Panel;
	 private static boolean level2Running = false;
	 
	 // Multiplayer toggle
	 private static boolean multiplayerEnabled = false;
	 
	 // Menu music
	 private static Clip menuMusicClip;
	 
	 // Level 1 music
	 private static Clip level1MusicClip;
	 
	 // Level 2 music
	 private static Clip level2MusicClip;
	 
	 // Player movement state (set by keyboard input, read by Player1 instances)
	 private static boolean p1Up = false;
	 private static boolean p1Down = false;
	 private static boolean p2Up = false;
	 private static boolean p2Down = false;
	 
	 // Shoot key held state — prevents repeated firing while key is held
	 private static boolean p1ShootHeld = false;
	 private static boolean p2ShootHeld = false;
	  
	public MainWindow() {
	        frame.setSize(1000, 1000);
	      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        frame.setLayout(null);
	        frame.add(canvas);  
	        canvas.setBounds(0, 0, 1000, 1000); 
			canvas.setBackground(new Color(255,255,255));
		    canvas.setVisible(false);
		          
		    // Load main screen background image
		    try {
				BufferedImage mainPic = ImageIO.read(new File("res/New Graphics/Main Screen.png"));
				Image scaledMain = mainPic.getScaledInstance(1000, 1000, Image.SCALE_SMOOTH);
				mainScreenLabel = new JLabel(new ImageIcon(scaledMain));
				mainScreenLabel.setBounds(0, 0, 1000, 1000);
			} catch (IOException e) { 
				e.printStackTrace();
				mainScreenLabel = new JLabel("Main Screen - Image not found");
				mainScreenLabel.setBounds(0, 0, 1000, 1000);
			}
			
			// Load controls screen background image
			try {
				BufferedImage controlsPic = ImageIO.read(new File("res/New Graphics/ControlsScreen.png"));
				Image scaledControls = controlsPic.getScaledInstance(1000, 1000, Image.SCALE_SMOOTH);
				controlsScreenLabel = new JLabel(new ImageIcon(scaledControls));
				controlsScreenLabel.setBounds(0, 0, 1000, 1000);
			} catch (IOException e) {
				e.printStackTrace();
				controlsScreenLabel = new JLabel("Controls Screen - Image not found");
				controlsScreenLabel.setBounds(0, 0, 1000, 1000);
			}
			controlsScreenLabel.setVisible(false);  // hidden initially
			
			// Load level selection screen background image
			try {
				BufferedImage levelPic = ImageIO.read(new File("res/New Graphics/Level Selection.png"));
				Image scaledLevel = levelPic.getScaledInstance(1000, 1000, Image.SCALE_SMOOTH);
				levelSelectionScreenLabel = new JLabel(new ImageIcon(scaledLevel));
				levelSelectionScreenLabel.setBounds(0, 0, 1000, 1000);
			} catch (IOException e) {
				e.printStackTrace();
				levelSelectionScreenLabel = new JLabel("Level Selection - Image not found");
				levelSelectionScreenLabel.setBounds(0, 0, 1000, 1000);
			}
			levelSelectionScreenLabel.setVisible(false);  // hidden initially
			
			// Controls button — transparent overlay, bottom-right of main screen
			controlsButton = new JButton();
			controlsButton.setBounds(792, 897, 184, 70);  // scaled from original image coords
			controlsButton.setOpaque(false);
			controlsButton.setContentAreaFilled(false);
			controlsButton.setBorderPainted(false);
			controlsButton.setFocusPainted(false);
			controlsButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
			controlsButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					showControlsScreen();
				}
			});
			
			// Single player button
			singlePlayerButton = new JButton();
			singlePlayerButton.setBounds(228, 897, 258, 72);  // scaled from original (263,833)-(560,900)
			singlePlayerButton.setOpaque(false);
			singlePlayerButton.setContentAreaFilled(false);
			singlePlayerButton.setBorderPainted(false);
			singlePlayerButton.setFocusPainted(false);
			singlePlayerButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
			singlePlayerButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					multiplayerEnabled = false;
					showLevelSelectionScreen();
				}
			});
			
			// Multiplayer button
			multiPlayerButton = new JButton();
			multiPlayerButton.setBounds(509, 897, 230, 72);  // scaled from original (587,833)-(852,900)
			multiPlayerButton.setOpaque(false);
			multiPlayerButton.setContentAreaFilled(false);
			multiPlayerButton.setBorderPainted(false);
			multiPlayerButton.setFocusPainted(false);
			multiPlayerButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
			multiPlayerButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					multiplayerEnabled = true;
					showLevelSelectionScreen();
				}
			});
			
			// Back button on controls screen
			backFromControlsButton = new JButton();
			backFromControlsButton.setBounds(459, 830, 81, 30);  // scaled from original (529,770)-(622,798)
			backFromControlsButton.setOpaque(false);
			backFromControlsButton.setContentAreaFilled(false);
			backFromControlsButton.setBorderPainted(false);
			backFromControlsButton.setFocusPainted(false);
			backFromControlsButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
			backFromControlsButton.setVisible(false);
			backFromControlsButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					showMainScreen();
				}
			});
			
			// Level 1 button on level selection screen
			level1Button = new JButton();
			level1Button.setBounds(47, 290, 439, 444);  // scaled from original (63,232)-(653,587) on 1344x800 image
			level1Button.setOpaque(false);
			level1Button.setContentAreaFilled(false);
			level1Button.setBorderPainted(false);
			level1Button.setFocusPainted(false);
			level1Button.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
			level1Button.setVisible(false);
			level1Button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					showLevel1();
				}
			});
			
			// Level 2 button on level selection screen
			level2Button = new JButton();
			level2Button.setBounds(513, 291, 441, 443);  // scaled from original (690,233)-(1282,587) on 1344x800 image
			level2Button.setOpaque(false);
			level2Button.setContentAreaFilled(false);
			level2Button.setBorderPainted(false);
			level2Button.setFocusPainted(false);
			level2Button.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
			level2Button.setVisible(false);
			level2Button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					showLevel2();
				}
			});
			
			// Back button on level selection screen
			backFromLevelSelectButton = new JButton();
			backFromLevelSelectButton.setBounds(466, 913, 68, 36);  // scaled from original (626,730)-(718,759) on 1344x800 image
			backFromLevelSelectButton.setOpaque(false);
			backFromLevelSelectButton.setContentAreaFilled(false);
			backFromLevelSelectButton.setBorderPainted(false);
			backFromLevelSelectButton.setFocusPainted(false);
			backFromLevelSelectButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
			backFromLevelSelectButton.setVisible(false);
			backFromLevelSelectButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					showMainScreen();
				}
			});
			
			// Level 1 parallax panel
			level1Panel = new Level1Panel();
			level1Panel.setBounds(0, 0, 1000, 1000);
			level1Panel.setVisible(false);
			
			// Level 2 parallax panel
			level2Panel = new Level2Panel();
			level2Panel.setBounds(0, 0, 1000, 1000);
			level2Panel.setVisible(false);
			
			// Add components to frame (order matters - buttons on top of images)
			frame.add(controlsButton);
			frame.add(singlePlayerButton);
			frame.add(multiPlayerButton);
			frame.add(backFromControlsButton);
			frame.add(backFromLevelSelectButton);
			frame.add(level1Button);
			frame.add(level2Button);
			frame.add(level1Panel);
			frame.add(level2Panel);
			frame.add(mainScreenLabel);
			frame.add(controlsScreenLabel);
			frame.add(levelSelectionScreenLabel);
			
	        frame.setVisible(true);
	        
	        // Start menu music on loop
	        startMenuMusic();
	        
	        // Global key dispatcher — captures input regardless of component focus
	        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
	            @Override
	            public boolean dispatchKeyEvent(KeyEvent e) {
	                // Only handle events when a level is active
	                if (!level1Running && !level2Running) return false;
	                
	                boolean pressed = (e.getID() == KeyEvent.KEY_PRESSED);
	                boolean released = (e.getID() == KeyEvent.KEY_RELEASED);
	                
	                // Space returns to main menu from score/loser screens
	                if (e.getKeyCode() == KeyEvent.VK_SPACE && pressed) {
	                    // Check if score/loser screen is ready for dismissal
	                    ScoreScreen activeScoreScreen = null;
	                    LoserScreen activeLoserScreen = null;
	                    if (level1Running && level1Panel != null) {
	                        activeScoreScreen = level1Panel.getScoreScreen();
	                        activeLoserScreen = level1Panel.getLoserScreen();
	                    } else if (level2Running && level2Panel != null) {
	                        activeScoreScreen = level2Panel.getScoreScreen();
	                        activeLoserScreen = level2Panel.getLoserScreen();
	                    }
	                    if (activeScoreScreen != null && activeScoreScreen.isFullyRevealed()) {
	                        showMainScreen();
	                        return true;
	                    }
	                    if (activeLoserScreen != null && activeLoserScreen.isReady()) {
	                        showMainScreen();
	                        return true;
	                    }
	                }
	                
	                switch (e.getKeyCode()) {
	                    case KeyEvent.VK_W:
	                        if (pressed) p1Up = true;
	                        if (released) p1Up = false;
	                        break;
	                    case KeyEvent.VK_S:
	                        if (pressed) p1Down = true;
	                        if (released) p1Down = false;
	                        break;
	                    case KeyEvent.VK_UP:
	                        if (pressed) p2Up = true;
	                        if (released) p2Up = false;
	                        break;
	                    case KeyEvent.VK_DOWN:
	                        if (pressed) p2Down = true;
	                        if (released) p2Down = false;
	                        break;
	                    case KeyEvent.VK_G:
	                        // P1 shoot — fire on initial press only
	                        if (pressed && !p1ShootHeld) {
	                            p1ShootHeld = true;
	                            // Get active P1 from current level
	                            Player1 shooter1 = null;
	                            if (level1Running && level1Panel != null) {
	                                shooter1 = level1Panel.getPlayer1();
	                            } else if (level2Running && level2Panel != null) {
	                                shooter1 = level2Panel.getPlayer1();
	                            }
	                            if (shooter1 != null && shooter1.isAlive()) {
	                                Projectile proj = shooter1.shoot();
	                                if (level1Running && level1Panel != null) {
	                                    level1Panel.addProjectile(proj);
	                                } else if (level2Running && level2Panel != null) {
	                                    level2Panel.addProjectile(proj);
	                                }
	                            }
	                        }
	                        if (released) p1ShootHeld = false;
	                        break;
	                    case KeyEvent.VK_L:
	                        // P2 shoot — fire on initial press only
	                        if (pressed && !p2ShootHeld) {
	                            p2ShootHeld = true;
	                            // Get active P2 from current level
	                            Player2 shooter2 = null;
	                            if (level1Running && level1Panel != null) {
	                                shooter2 = level1Panel.getPlayer2();
	                            } else if (level2Running && level2Panel != null) {
	                                shooter2 = level2Panel.getPlayer2();
	                            }
	                            if (shooter2 != null && shooter2.isAlive()) {
	                                Projectile proj = shooter2.shoot();
	                                if (level1Running && level1Panel != null) {
	                                    level1Panel.addProjectile(proj);
	                                } else if (level2Running && level2Panel != null) {
	                                    level2Panel.addProjectile(proj);
	                                }
	                            }
	                        }
	                        if (released) p2ShootHeld = false;
	                        break;
	                }
	                
	                // Push P1 movement state to active level
	                Player1 activePlayer = null;
	                if (level1Running && level1Panel != null) {
	                    activePlayer = level1Panel.getPlayer1();
	                } else if (level2Running && level2Panel != null) {
	                    activePlayer = level2Panel.getPlayer1();
	                }
	                if (activePlayer != null) {
	                    activePlayer.setMovingUp(p1Up);
	                    activePlayer.setMovingDown(p1Down);
	                }
	                
	                // Push P2 movement state (multiplayer only)
	                Player2 activePlayer2 = null;
	                if (level1Running && level1Panel != null) {
	                    activePlayer2 = level1Panel.getPlayer2();
	                } else if (level2Running && level2Panel != null) {
	                    activePlayer2 = level2Panel.getPlayer2();
	                }
	                if (activePlayer2 != null) {
	                    activePlayer2.setMovingUp(p2Up);
	                    activePlayer2.setMovingDown(p2Down);
	                }
	                
	                return false;  // allow other listeners to also process the event
	            }
	        });
	}
	
	// Show the main menu screen
	private void showMainScreen() {
		mainScreenLabel.setVisible(true);
		controlsButton.setVisible(true);
		singlePlayerButton.setVisible(true);
		multiPlayerButton.setVisible(true);
		controlsScreenLabel.setVisible(false);
		backFromControlsButton.setVisible(false);
		levelSelectionScreenLabel.setVisible(false);
		backFromLevelSelectButton.setVisible(false);
		level1Button.setVisible(false);
		level2Button.setVisible(false);
		level1Panel.setVisible(false);
		level2Panel.setVisible(false);
		level1Running = false;
		level2Running = false;
		frame.setTitle("Trail Blazers");
		stopLevel1Music();
		stopLevel2Music();
		startMenuMusic();
	}
	
	// Show the controls screen
	private void showControlsScreen() {
		mainScreenLabel.setVisible(false);
		controlsButton.setVisible(false);
		singlePlayerButton.setVisible(false);
		multiPlayerButton.setVisible(false);
		controlsScreenLabel.setVisible(true);
		backFromControlsButton.setVisible(true);
		levelSelectionScreenLabel.setVisible(false);
		backFromLevelSelectButton.setVisible(false);
		level1Button.setVisible(false);
		level2Button.setVisible(false);
		level1Panel.setVisible(false);
		level2Panel.setVisible(false);
		level1Running = false;
		level2Running = false;
	}
	
	// Show the level selection screen
	private void showLevelSelectionScreen() {
		mainScreenLabel.setVisible(false);
		controlsButton.setVisible(false);
		singlePlayerButton.setVisible(false);
		multiPlayerButton.setVisible(false);
		controlsScreenLabel.setVisible(false);
		backFromControlsButton.setVisible(false);
		levelSelectionScreenLabel.setVisible(true);
		backFromLevelSelectButton.setVisible(true);
		level1Button.setVisible(true);
		level2Button.setVisible(true);
		level1Panel.setVisible(false);
		level2Panel.setVisible(false);
		level1Running = false;
		level2Running = false;
	}
	
	// Show Level 1 (parallax scrolling mountain background)
	private void showLevel1() {
		mainScreenLabel.setVisible(false);
		controlsButton.setVisible(false);
		singlePlayerButton.setVisible(false);
		multiPlayerButton.setVisible(false);
		controlsScreenLabel.setVisible(false);
		backFromControlsButton.setVisible(false);
		levelSelectionScreenLabel.setVisible(false);
		backFromLevelSelectButton.setVisible(false);
		level1Button.setVisible(false);
		level2Button.setVisible(false);
		level1Panel.setVisible(true);
		level2Panel.setVisible(false);
		level2Running = false;
		frame.setTitle("Trail Blazers - Level 1");
		stopMenuMusic();
		startLevel1Music();
		// Reset P1 spawn position
		if (level1Panel.getPlayer1() != null) {
			level1Panel.getPlayer1().resetPosition();
		}
		// Configure P2 based on multiplayer toggle
		level1Panel.setMultiplayer(multiplayerEnabled);
		if (level1Panel.getPlayer2() != null) {
			level1Panel.getPlayer2().resetPosition();
		}
		// Reset level timer
		level1Panel.resetTimer();
		// Spawn tutorial enemy (HP scales with player count)
		level1Panel.spawnTutorialEnemy(multiplayerEnabled);
		p1Up = false;
		p1Down = false;
		p2Up = false;
		p2Down = false;
		p1ShootHeld = false;
		p2ShootHeld = false;
		// Enable game loop last, after all state is initialised
		level1Running = true;
	}
	
	// Show Level 2 (parallax scrolling industrial background)
	private void showLevel2() {
		mainScreenLabel.setVisible(false);
		controlsButton.setVisible(false);
		singlePlayerButton.setVisible(false);
		multiPlayerButton.setVisible(false);
		controlsScreenLabel.setVisible(false);
		backFromControlsButton.setVisible(false);
		levelSelectionScreenLabel.setVisible(false);
		backFromLevelSelectButton.setVisible(false);
		level1Button.setVisible(false);
		level2Button.setVisible(false);
		level1Panel.setVisible(false);
		level2Panel.setVisible(true);
		level1Running = false;
		frame.setTitle("Trail Blazers - Level 2");
		stopMenuMusic();
		startLevel2Music();
		// Reset P1 spawn position
		if (level2Panel.getPlayer1() != null) {
			level2Panel.getPlayer1().resetPosition();
		}
		// Configure P2 based on multiplayer toggle
		level2Panel.setMultiplayer(multiplayerEnabled);
		if (level2Panel.getPlayer2() != null) {
			level2Panel.getPlayer2().resetPosition();
		}
		// Reset level timer
		level2Panel.resetTimer();
		// Spawn final boss (3-phase, HP scales with player count)
		level2Panel.spawnFinalBoss(multiplayerEnabled);
		p1Up = false;
		p1Down = false;
		p2Up = false;
		p2Down = false;
		p1ShootHeld = false;
		p2ShootHeld = false;
		// Enable game loop last, after all state is initialised
		level2Running = true;
	}
	
	// Getter for multiplayer state
	public static boolean isMultiplayerEnabled() {
		return multiplayerEnabled;
	}
	
	// Load and loop menu music from res/Music/
	private static void startMenuMusic() {
		if (menuMusicClip != null && menuMusicClip.isRunning()) return;
		
		try {
			File musicFile = new File("res/Music/awesomeness.wav");
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(musicFile);
			menuMusicClip = AudioSystem.getClip();
			menuMusicClip.open(audioIn);
			menuMusicClip.loop(Clip.LOOP_CONTINUOUSLY);
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			System.err.println("Error loading menu music: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	// Stop menu music and release audio resources
	private static void stopMenuMusic() {
		if (menuMusicClip != null && menuMusicClip.isRunning()) {
			menuMusicClip.stop();
			menuMusicClip.close();
			menuMusicClip = null;
		}
	}
	
	// Load and loop Level 1 music
	private static void startLevel1Music() {
		if (level1MusicClip != null && level1MusicClip.isRunning()) return;
		
		try {
			File musicFile = new File("res/Music/level1music.wav");
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(musicFile);
			level1MusicClip = AudioSystem.getClip();
			level1MusicClip.open(audioIn);
			level1MusicClip.loop(Clip.LOOP_CONTINUOUSLY);
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			System.err.println("Error loading level 1 music: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	// Stop Level 1 music
	private static void stopLevel1Music() {
		if (level1MusicClip != null && level1MusicClip.isRunning()) {
			level1MusicClip.stop();
			level1MusicClip.close();
			level1MusicClip = null;
		}
	}
	
	// Load and loop Level 2 music
	private static void startLevel2Music() {
		if (level2MusicClip != null && level2MusicClip.isRunning()) return;
		
		try {
			File musicFile = new File("res/Music/Orbital Colossus.wav");
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(musicFile);
			level2MusicClip = AudioSystem.getClip();
			level2MusicClip.open(audioIn);
			level2MusicClip.loop(Clip.LOOP_CONTINUOUSLY);
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			System.err.println("Error loading level 2 music: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	// Stop Level 2 music
	private static void stopLevel2Music() {
		if (level2MusicClip != null && level2MusicClip.isRunning()) {
			level2MusicClip.stop();
			level2MusicClip.close();
			level2MusicClip = null;
		}
	}

	public static void main(String[] args) {
		MainWindow hello = new MainWindow();  // Initialise game environment
		while(true)   // Main game loop — ~100 FPS busy-wait pattern
		{ 
			// Calculate frame timing (~10ms per frame at 100 FPS)
			int TimeBetweenFrames =  1000 / TargetFPS;
			long FrameCheck = System.currentTimeMillis() + (long) TimeBetweenFrames; 
			
			// Busy-wait until next frame
		 while (FrameCheck > System.currentTimeMillis()){} 
			
			
			if(startGame)
				 {
				 gameloop();
				 }
			
			// Update Level 1 parallax scrolling
			if(level1Running && level1Panel != null) {
				level1Panel.updateParallax();
				level1Panel.repaint();
			}
			
			// Update Level 2 parallax scrolling
			if(level2Running && level2Panel != null) {
				level2Panel.updateParallax();
				level2Panel.repaint();
			}
			
			// Verify frame rate matches target
		 UnitTests.CheckFrameRate(System.currentTimeMillis(),FrameCheck, TargetFPS); 
			  
		}
		
		
	} 
	//Basic Model-View-Controller pattern 
	private static void gameloop() { 
		// GAMELOOP  
		
		// controller input  will happen on its own thread 
		// So no need to call it explicitly 
		
		// model update   
		gameworld.gamelogic();
		// view update 
		
		  canvas.updateview(); 
		
		// Both these calls could be setup as  a thread but we want to simplify the game logic for you.  
		//score update  
		 frame.setTitle("Score =  "+ gameworld.getScore()); 
		
		 
	}

}

/*
 * 
 * 

Hand shake agreement 
::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::,=+++
::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::,,,,,,:::::,=+++????
::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::,,,,,,,,,,,,,,:++++????+??
:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::,:,:,,:,:,,,,,,,,,,,,,,,,,,,,++++++?+++++????
::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,=++?+++++++++++??????
:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::,:,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,~+++?+++?++?++++++++++?????
:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::,:::,,,,,,,,,,,,,,,,,,,,,,,,,,,~+++++++++++++++????+++++++???????
::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::,:,,,,,,,,,,,,,,,,,,,,,,:===+=++++++++++++++++++++?+++????????????????
:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::,,,,,,,,,,,,,,,,,,~=~~~======++++++++++++++++++++++++++????????????????
::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::,::::,,,,,,=~.,,,,,,,+===~~~~~~====++++++++++++++++++++++++++++???????????????
:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::,:,,,,,~~.~??++~.,~~~~~======~=======++++++++++++++++++++++++++????????????????II
:::::::::::::::::::::::::::::::::::::::::::::::::::::::,:,,,,:=+++??=====~~~~~~====================+++++++++++++++++++++?????????????????III
:::::::::::::::::::::::::::::::::::::::::::::::::::,:,,,++~~~=+=~~~~~~==~~~::::~~==+++++++==++++++++++++++++++++++++++?????????????????IIIII
::::::::::::::::::::::::::::::::::::::::::::::::,:,,,:++++==+??+=======~~~~=~::~~===++=+??++++++++++++++++++++++++?????????????????I?IIIIIII
::::::::::::::::::::::::::::::::::::::::::::::::,,:+????+==??+++++?++====~~~~~:~~~++??+=+++++++++?++++++++++??+???????????????I?IIIIIIII7I77
::::::::::::::::::::::::::::::::::::::::::::,,,,+???????++?+?+++???7?++======~~+=====??+???++++++??+?+++???????????????????IIIIIIIIIIIIIII77
:::::::::::::::::::::::::::::::::::::::,,,,,,=??????IIII7???+?+II$Z77??+++?+=+++++=~==?++?+?++?????????????III?II?IIIIIIIIIIIIIIIIIIIIIIIIII
::::::::::::::::::::::::::::::,,,,,,~=======++++???III7$???+++++Z77ZDZI?????I?777I+~~+=7+?II??????????????IIIIIIIIIIIIIIIIIIIIII??=:,,,,,,,,
::::::::,:,:,,,,,,,:::~==+=++++++++++++=+=+++++++???I7$7I?+~~~I$I??++??I78DDDO$7?++==~I+7I7IIIIIIIIIIIIIIIIII777I?=:,,,,,,,,,,,,,,,,,,,,,,,,
++=++=++++++++++++++?+????+??????????+===+++++????I7$$ZZ$I+=~$7I???++++++===~~==7??++==7II?~,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,
+++++++++++++?+++?++????????????IIIII?I+??I???????I7$ZOOZ7+=~7II?+++?II?I?+++=+=~~~7?++:,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,
+?+++++????????????????I?I??I??IIIIIIII???II7II??I77$ZO8ZZ?~~7I?+==++?O7II??+??+=====.,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,
?????????????III?II?????I?????IIIII???????II777IIII7$ZOO7?+~+7I?+=~~+???7NNN7II?+=+=++,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,
????????????IIIIIIIIII?IIIIIIIIIIII????II?III7I7777$ZZOO7++=$77I???==+++????7ZDN87I??=~,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,
IIII?II??IIIIIIIIIIIIIIIIIIIIIIIIIII???+??II7777II7$$OZZI?+$$$$77IIII?????????++=+.,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,
IIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII?+++?IIIII7777$$$$$$7$$$$7IIII7I$IIIIII???I+=,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,
IIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII???????IIIIII77I7777$7$$$II????I??I7Z87IIII?=,,,,,,,,,,,:,,::,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,
777777777777777777777I7I777777777~,,,,,,,+77IIIIIIIIIII7II7$$$Z$?I????III???II?,,,,,,,,,,::,::::::::,,:,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,
777777777777$77777777777+::::::::::::::,,,,,,,=7IIIII78ZI?II78$7++D7?7O777II??:,,,:,,,::::::::::::::,:,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,
$$$$$$$$$$$$$77=:,:::::::::::::::::::::::::::,,7II$,,8ZZI++$8ZZ?+=ZI==IIII,+7:,,,,:::::::::::::::::,:::,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,
$$$I~::::::::::::::::::::::::::::::::::::::::::II+,,,OOO7?$DOZII$I$I7=77?,,,,,,:::::::::::::::::::::,,,:,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,
::::::::::::::::::::::::::::::::::::::::::::::::::::::+ZZ?,$ZZ$77ZZ$?,,,,,::::::::::::::::::::::::::,::::,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,
:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::I$:::::::::::::::::::::::::::::::::::::::::::,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,
:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::,,,:,,,,,,,,,,,,,,,,,,,,,,,,,,,,,
:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::,,,,,,,,,,,,,,,,,,,,,,,,,,,
:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::,,,,,,,,,,,,,,,,,,,,,,,,,,,
:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::,,,,,,,,,,,,,,,,,,,,,,,,,
::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::,,,,,,,,,,,,,,,,,,,,,,
::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::,,,,,,,,,,,,,,,,,,,,,,
                                                                                                                             GlassGiant.com
 * 
 * 
 */
