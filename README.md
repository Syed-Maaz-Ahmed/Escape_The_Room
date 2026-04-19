# Escape Room Puzzle Game 🧠🔐

A Java-based desktop application where you must solve riddles to escape from a series of locked rooms. This project demonstrates clean Object-Oriented Programming (OOP) principles by separating the logic (backend) from the user interface (frontend).

## Features
- **5 Challenging Riddles:** Solve classic puzzles to progress.
- **Scoring System:** Tracks your correct answers.
- **Attempts Limit:** You have 3 tries per room before it's Game Over!
- **Professional GUI:** A dark-themed, responsive Swing interface with dynamic feedback.

## Project Structure
- `EscapeRoomGUI.java`: The Frontend—handles the Swing window, layout, and event listeners.
- `EscapeGame.java`: The Backend engine—manages the game state, score, and room progression.
- `Room.java`: The Data Model—defines the structure of a puzzle room and answer verification logic.

## How to Run
### Prerequisites
- Java Development Kit (JDK) 8 or higher.
- Maven (optional, but recommended for dependency management).

### Compile and Run
1. Open your terminal in the project root.
2. Compile the project:
   ```bash
   javac -d bin src/main/java/com/mycompany/oopproject/*.java
   ```
3. Run the application:
   ```bash
   java -cp bin com.mycompany.oopproject.EscapeRoomGUI
   ```

