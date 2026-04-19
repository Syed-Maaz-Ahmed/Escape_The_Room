package com.mycompany.oopproject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * EscapeRoomGUI handles the User Interface for the Puzzle Room Game.
 * Separated from the backend logic (EscapeGame and Room classes).
 * 
 * @author Osama Aslam
 */
public class EscapeRoomGUI {
    private JFrame frame;
    private JPanel mainPanel;
    private JTextArea questionArea;
    private JTextField answerField;
    private JLabel feedbackLabel, scoreLabel, triesLabel;
    private JButton submitButton;
    private EscapeGame game;
    private int remainingTries;

    public EscapeRoomGUI() {
        frame = new JFrame("🧠 Escape the Puzzle Room Game 🔐");
        frame.setSize(700, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        showStartMenu();
    }

    private void showStartMenu() {
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(30, 30, 60));

        JLabel title = new JLabel("Escape Room Challenge", SwingConstants.CENTER);
        title.setFont(new Font("Verdana", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        title.setBorder(BorderFactory.createEmptyBorder(30, 10, 10, 10));
        mainPanel.add(title, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        buttonPanel.setBackground(new Color(30, 30, 60));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(50, 200, 50, 200));

        JButton startButton = new JButton("Start");
        JButton aboutButton = new JButton("About");
        JButton exitButton = new JButton("Exit");

        startButton.setFont(new Font("SansSerif", Font.BOLD, 18));
        aboutButton.setFont(new Font("SansSerif", Font.BOLD, 18));
        exitButton.setFont(new Font("SansSerif", Font.BOLD, 18));

        startButton.setBackground(new Color(0, 123, 255));
        startButton.setForeground(Color.WHITE);
        aboutButton.setBackground(new Color(40, 167, 69));
        aboutButton.setForeground(Color.WHITE);
        exitButton.setBackground(new Color(220, 53, 69));
        exitButton.setForeground(Color.WHITE);

        buttonPanel.add(startButton);
        buttonPanel.add(aboutButton);
        buttonPanel.add(exitButton);

        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        frame.setContentPane(mainPanel);
        frame.setVisible(true);

        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                game = new EscapeGame();
                createGameUI();
                loadRoom();
            }
        });

        aboutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame,
                    "You are stuck in a room.\n" +
                    "Answer each question correctly to escape to the next room.\n" +
                    "You only have 3 tries per room!",
                    "About the Game", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
    }

    private void createGameUI() {
        JPanel backgroundPanel = new JPanel(new BorderLayout(10, 10));
        backgroundPanel.setBackground(new Color(20, 20, 40));

        JLabel titleLabel = new JLabel("Escape Room Challenge", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Verdana", Font.BOLD, 24));
        titleLabel.setForeground(Color.CYAN);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        backgroundPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);

        questionArea = new JTextArea();
        questionArea.setEditable(false);
        questionArea.setLineWrap(true);
        questionArea.setWrapStyleWord(true);
        questionArea.setFont(new Font("Serif", Font.PLAIN, 20));
        questionArea.setForeground(Color.WHITE);
        questionArea.setOpaque(false);
        questionArea.setHighlighter(null);
        questionArea.setPreferredSize(new Dimension(600, 100));
        centerPanel.add(questionArea);
        backgroundPanel.add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setBackground(new Color(20, 20, 40));

        JPanel inputPanel = new JPanel(new BorderLayout(10, 10));
        inputPanel.setOpaque(false);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        answerField = new JTextField();
        answerField.setFont(new Font("SansSerif", Font.PLAIN, 16));
        answerField.setBackground(new Color(255, 255, 240));

        submitButton = new JButton("Submit");
        submitButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        submitButton.setBackground(new Color(0, 123, 255));
        submitButton.setForeground(Color.WHITE);

        inputPanel.add(answerField, BorderLayout.CENTER);
        inputPanel.add(submitButton, BorderLayout.EAST);

        feedbackLabel = new JLabel("Enter your answer and press Submit.", SwingConstants.CENTER);
        feedbackLabel.setFont(new Font("SansSerif", Font.ITALIC, 14));
        feedbackLabel.setForeground(Color.YELLOW);
        feedbackLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        scoreLabel = new JLabel("Score: 0", SwingConstants.CENTER);
        scoreLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        scoreLabel.setForeground(Color.GREEN);
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        triesLabel = new JLabel("Tries left: 3", SwingConstants.CENTER);
        triesLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        triesLabel.setForeground(Color.ORANGE);
        triesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        bottomPanel.add(inputPanel);
        bottomPanel.add(feedbackLabel);
        bottomPanel.add(scoreLabel);
        bottomPanel.add(triesLabel);

        backgroundPanel.add(bottomPanel, BorderLayout.SOUTH);

        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleAnswer();
            }
        });

        frame.setContentPane(backgroundPanel);
        frame.revalidate();
        frame.repaint();
    }

    private void loadRoom() {
        Room current = game.getCurrentRoom();
        remainingTries = 3;
        if (current != null) {
            questionArea.setText(current.getRoomName() + ":\n" + current.getQuestion());
            answerField.setText("");
            answerField.setEnabled(true);
            submitButton.setEnabled(true);
            feedbackLabel.setText("Answer the puzzle to proceed.");
            scoreLabel.setText("Score: " + game.getScore());
            triesLabel.setText("Tries left: " + remainingTries);
        } else {
            questionArea.setText("🎉 Congratulations! You've escaped all the rooms!");
            answerField.setEnabled(false);
            submitButton.setEnabled(false);
            feedbackLabel.setText("Game Completed!");
            scoreLabel.setText("Final Score: " + game.getScore());
            triesLabel.setText("");
        }
    }

    private void handleAnswer() {
        String userInput = answerField.getText();
        boolean correct = game.submitAnswer(userInput);
        if (correct) {
            feedbackLabel.setText("✅ Correct! Moving to next room...");
            loadRoom();
        } else {
            remainingTries--;
            if (remainingTries <= 0) {
                questionArea.setText("💀 Game Over! You've used all attempts.");
                answerField.setEnabled(false);
                submitButton.setEnabled(false);
                feedbackLabel.setText("Better luck next time!");
                triesLabel.setText("Tries left: 0");
            } else {
                feedbackLabel.setText("❌ Incorrect! Try again.");
                triesLabel.setText("Tries left: " + remainingTries);
            }
        }
    }

    public static void main(String[] args) {
        new EscapeRoomGUI();  // Launch the game
    }
}
