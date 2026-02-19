# Implementation Documentation

## Project: Basic Game Template
**Repository:** https://github.com/SeanOkafor/GameDevAssignment1.git

---

## Running the Application

### Prerequisites
- Java JDK 21 (or compatible version)
- Git (for version control)

### Compilation
To compile the project from the root directory:

```powershell
cd src
javac util/*.java
javac *.java
cd ..
```

### Running the Game
From the project root directory:

```powershell
java -classpath src MainWindow
```

### Game Controls
- **W** - Move up
- **A** - Move left
- **S** - Move down
- **D** - Move right
- **Space** - Shoot

---

## Project Structure
```
BasicGameTemplate/
├── src/
│   ├── MainWindow.java    - Main entry point and game loop
│   ├── Controller.java    - Handles keyboard input
│   ├── Model.java         - Game logic and state
│   ├── Viewer.java        - Rendering and display
│   └── util/
│       ├── GameObject.java
│       ├── Point3f.java
│       ├── Vector3f.java
│       └── UnitTests.java
├── res/                   - Game resources (images, sprites)
└── bin/                   - Compiled classes (gitignored)
```

---

## Implementation Changes

### Initial Setup (February 19, 2026)
- Initialized Git repository
- Created .gitignore for Java projects
- Connected to GitHub repository
- Initial commit with basic MVC game template

---

## Future Changes
*Document all implementation changes below with date, description, and rationale*

---
