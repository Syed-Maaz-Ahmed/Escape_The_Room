package com.mycompany.oopproject;

// Abstract Room class
public abstract class Room {
    protected String question;
    protected String answer;

    public Room(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    public abstract String getRoomName();

    public String getQuestion() {
        return question;
    }

    public boolean checkAnswer(String userAnswer) {
        if (userAnswer == null) return false;
        return answer.equalsIgnoreCase(userAnswer.trim());
    }
}

// PuzzleRoom class
class PuzzleRoom extends Room {
    private String name;

    public PuzzleRoom(String name, String question, String answer) {
        super(question, answer);
        this.name = name;
    }

    @Override
    public String getRoomName() {
        return name;
    }
}
