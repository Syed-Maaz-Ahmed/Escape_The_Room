package com.mycompany.oopproject;

// Game engine logic
public class EscapeGame {
    private Room[] rooms;
    private int currentRoomIndex;
    private int score;

    public EscapeGame() {
        rooms = new Room[]{
            new PuzzleRoom("Room 1", "What has keys but can't open locks?", "Keyboard"),
            new PuzzleRoom("Room 2", "What has hands but can’t clap?", "Clock"),
            new PuzzleRoom("Room 3", "What runs but never walks?", "Water"),
            new PuzzleRoom("Room 4", "What can travel around the world while staying in a corner?", "Stamp"),
            new PuzzleRoom("Room 5", "The more you take, the more you leave behind. What are they?", "Footsteps")
        };
        currentRoomIndex = 0;
        score = 0;
    }

    public boolean isFinished() {
        return currentRoomIndex >= rooms.length;
    }

    public Room getCurrentRoom() {
        if (isFinished()) return null;
        return rooms[currentRoomIndex];
    }

    public int getScore() {
        return score;
    }

    public boolean submitAnswer(String answer) {
        Room room = getCurrentRoom();
        if (room == null) return false;
        if (room.checkAnswer(answer)) {
            score++;
            currentRoomIndex++;
            return true;
        }
        return false;
    }
}
