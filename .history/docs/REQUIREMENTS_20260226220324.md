# Trail Blazers - Game Requirements

## Game Overview
**Trail Blazers** is a horizontal side-scrolling action game inspired by *Jetpack Joyride* and *Geometry Dash*. Players fly horizontally through pre-made courses, controlling only vertical movement, while dodging obstacles (walls) and shooting destructible boxes out of their path. The game features **2-player local multiplayer** and **level-based progression** with a tutorial level.

---

## Core Mechanics

### Player Controls
| Control | Action |
|---------|--------|
| W / Up Arrow | Move Up |
| S / Down Arrow | Move Down |
| Space | Shoot |

- **No horizontal control** — the player automatically scrolls right through the level
- Vertical movement only (up/down)
- Shooting fires projectiles forward (rightward) to destroy boxes

### Game Flow
- Players fly horizontally through **pre-made courses** (levels)
- The screen auto-scrolls from left to right
- Players must **dodge walls/obstacles** that block the path
- Players can **shoot boxes** to clear a path
- Each level has a **finish line/goal** the player must reach (similar to Geometry Dash)
- Hitting a wall = death / level restart

### Level Design
1. **Level 1 — Tutorial**: Teaches the player:
   - How to move up and down
   - How to shoot and destroy boxes
   - How to dodge obstacles/walls
   - Reaching the end goal
2. **Level 2 — Full Course**: A complete challenge course with mixed obstacles and boxes

### Multiplayer
- **2-player local multiplayer** (same keyboard)
- Player 1: W/S/Space
- Player 2: Up Arrow/Down Arrow/Right Shift (or similar)
- Both players on screen simultaneously
- Independent lives — each player can die and respawn or wait

---

## Sprite Requirements

### Existing Sprites to Replace
| Current File | Current Use | Replacement Needed |
|---|---|---|
| `res/Lightning.png` | Player character | Player 1 character |
| `res/UFO.png` | Enemy (animated 4-frame) | Destructible box / Wall obstacle |
| `res/Bullet.png` | Bullet projectile | New projectile sprite |
| `res/spacebackground.png` | Background | Rail/tunnel/sky background |
| `res/startscreen.png` | Start menu image | Trail Blazers title screen |
| `res/blankSprite.png` | Default blank | Keep or update |
| `res/Ninja.png` | Unused | Remove or repurpose |

### New Sprites Needed

#### Characters (2 player models)
- [ ] **Player 1 sprite** — Character with jetpack/rocket (side-view, flying pose), ~50x50px, animated sprite sheet (idle/flying, 4 frames)
- [ ] **Player 2 sprite** — Visually distinct second character with jetpack/rocket (side-view, flying pose), ~50x50px, animated sprite sheet (idle/flying, 4 frames)

#### Projectiles
- [ ] **Bullet/Laser sprite** — Horizontal projectile fired forward, ~32x16px

#### Obstacles
- [ ] **Wall block sprite** — Indestructible wall segment, ~50x50px (used to build level geometry)
- [ ] **Destructible box sprite** — Breakable box that can be shot, ~50x50px (visually distinct from walls — e.g. crates with cracks)
- [ ] **Box destruction animation** — 3-4 frame explosion/break animation when a box is shot

#### Environment / Background
- [ ] **Background layer** — Scrolling background (sky/industrial/tunnel theme), 2000x1000px or tileable
- [ ] **Parallax mid-ground layer** (optional) — Additional scrolling layer for depth
- [ ] **Floor/ceiling rail sprites** — Top and bottom boundary tiles, ~50x50px tileable
- [ ] **Finish line / Goal sprite** — End-of-level marker, ~50x200px (tall banner or gate)

#### UI / Menus
- [ ] **Score display font/style** — Pixel art numbers or use system font
- [ ] **"Player 1" / "Player 2" label sprites** — Small labels near each player
- [ ] **Death/explosion animation** — When player hits a wall, 4-frame animation
- [ ] **Tutorial text overlays** — Arrow indicators, "SHOOT!", "DODGE!", etc.

#### Level Tiles
- [ ] **Empty space tile** — Open flyable space (may just be transparent)
- [ ] **Wall tile variants** — 2-3 variants for visual variety
- [ ] **Box tile** — Shootable/destructible tile

---

## AI Sprite Creation Tools

### Recommended Tools

#### 1. PixelLab (https://www.pixellab.ai/)
- AI-powered pixel art generator purpose-built for game sprites
- Generates sprite sheets, animations, rotations, and tilesets
- Style-consistent generation (keeps all sprites looking cohesive)
- Free trial: 40 fast generations, then 5 daily
- Paid tiers: $12/mo (2,000 images), $24/mo (5,000 images)

#### 2. Perchance AI Pixel Art Generator (https://perchance.org/ai-pixel-art-generator)
- Completely free, unlimited generations, no account needed
- Great for quick concept exploration and individual sprites

#### 3. Piskel (https://www.piskelapp.com/)
- Free, open-source, browser-based pixel art editor
- Perfect for editing and refining AI-generated sprites
- Built-in animation preview and sprite sheet export

#### 4. Leonardo.ai (https://leonardo.ai/)
- Powerful general-purpose AI image generator
- Good for backgrounds and larger artwork

### Recommended Workflow
1. **Concept Phase**: Use Perchance (free) to explore character/item ideas
2. **Production Phase**: Use PixelLab to generate final sprite sheets with consistent style
3. **Refinement Phase**: Use Piskel to manually edit and finalize all sprites
4. **Backgrounds/UI**: Use Leonardo.ai or Perchance for larger background art

---

## Technical Requirements

### Game Window
- Window size: 1000x1000 pixels
- Game title: "Trail Blazers"

### Architecture Changes Needed
- [ ] Convert vertical enemy movement → horizontal auto-scrolling
- [ ] Add level data system (tile-based level maps)
- [ ] Add level loading (read level layouts from data)
- [ ] Add collision detection with level geometry (walls)
- [ ] Change bullet direction from vertical (up) to horizontal (right)
- [ ] Add second player with separate controls
- [ ] Add level completion detection (reaching the goal)
- [ ] Add tutorial system for Level 1
- [ ] Add level transition / restart on death

### Controller Changes
- Player 1: W (up), S (down), Space (shoot)
- Player 2: Up Arrow (up), Down Arrow (down), Right Shift or Enter (shoot)
- Remove A/D (left/right) controls — no horizontal player movement

### Planned File Structure
```
res/
├── player1.png          # Player 1 sprite sheet
├── player2.png          # Player 2 sprite sheet
├── bullet.png           # Projectile sprite
├── wall.png             # Indestructible wall tile
├── box.png              # Destructible box tile
├── box_break.png        # Box destruction animation
├── background.png       # Scrolling background
├── floor_ceiling.png    # Rail/boundary tiles
├── goal.png             # Finish line sprite
├── death_anim.png       # Player death animation
├── tutorial_arrows.png  # Tutorial UI overlays
└── blankSprite.png      # Default blank
```

---

## Milestones

### Phase 1 — Art & Assets
- [ ] Create all sprites using AI tools + manual refinement
- [ ] Replace all existing template art

### Phase 2 — Core Mechanics
- [ ] Implement horizontal auto-scrolling
- [ ] Implement tile-based level system
- [ ] Update player controls (up/down/shoot only)
- [ ] Implement wall collision (death on contact)
- [ ] Implement destructible boxes
- [ ] Implement level goal/finish line

### Phase 3 — Multiplayer
- [ ] Add Player 2 with separate controls
- [ ] Render both players simultaneously
- [ ] Independent player state (lives, position)

### Phase 4 — Levels
- [ ] Design and implement Tutorial Level
- [ ] Design and implement Level 2
- [ ] Level transitions and restart

### Phase 5 — Polish
- [ ] Title screen / start menu
- [ ] Score and UI
- [ ] Sound effects (stretch goal)
- [ ] Additional levels (stretch goal)
