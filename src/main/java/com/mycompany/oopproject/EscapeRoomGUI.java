package com.mycompany.oopproject;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

/**
 * EscapeRoomGUI handles the User Interface for the Puzzle Room Game.
 * Reimagined with a high-tech Sci-Fi aesthetic, and synced with high-quality audio.
 * 
 * @author Osama Aslam & Antigravity
 */
public class EscapeRoomGUI {
    private JFrame frame;
    private JTextArea questionArea;
    private JTextField answerField;
    private JLabel feedbackLabel, scoreLabel, triesLabel, timerLabel;
    private JProgressBar progressBar;
    private JButton submitButton;
    private EscapeGame game;
    private int remainingTries;
    private Timer gameTimer;
    private int secondsElapsed;
    
    // Audio Files
    private final String SOUND_GRANTED = "Access Granted Sound - Tokio 936.mp3";
    private final String SOUND_DENIED = "Access Denied - Sound Effect (HD) - SOUNDS FX.mp3";

    // --- Sci-Fi / Cyberpunk Palette ---
    private final Color COLOR_BG = new Color(5, 5, 15);        // Deep void
    private final Color COLOR_ACCENT = new Color(0, 255, 255); // Neon Cyan
    private final Color COLOR_SECONDARY = new Color(255, 0, 255); // Neon Magenta
    private final Color COLOR_HUD_BG = new Color(0, 255, 255, 15); // Transparent HUD
    private final Color COLOR_TEXT = new Color(180, 255, 255); // Bio-luminescent text

    public EscapeRoomGUI() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        frame = new JFrame("Escape the Puzzle Room Game");
        frame.setSize(900, 650);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        
        showStartMenu();
    }

    private void showStartMenu() {
        SciFiPanel mainPanel = new SciFiPanel(true);
        mainPanel.setLayout(new GridBagLayout());

        JPanel menuPanel = new JPanel();
        menuPanel.setOpaque(false);
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBorder(new EmptyBorder(40, 60, 40, 60));

        JLabel title = new JLabel("ESCAPE ROOM CHALLENGE", SwingConstants.CENTER);
        title.setFont(new Font("Monospaced", Font.BOLD, 42));
        title.setForeground(COLOR_ACCENT);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        menuPanel.add(title);
        menuPanel.add(Box.createVerticalStrut(60));

        HudButton startBtn = new HudButton("START", COLOR_ACCENT);
        HudButton aboutBtn = new HudButton("ABOUT", Color.GRAY);
        HudButton exitBtn = new HudButton("EXIT", new Color(220, 53, 69));

        Dimension btnSize = new Dimension(250, 50);
        for (HudButton b : new HudButton[]{startBtn, aboutBtn, exitBtn}) {
            b.setPreferredSize(btnSize);
            b.setMaximumSize(btnSize);
            b.setMinimumSize(btnSize);
            menuPanel.add(b);
            menuPanel.add(Box.createVerticalStrut(15));
        }

        mainPanel.add(menuPanel);
        frame.setContentPane(mainPanel);
        frame.setVisible(true);

        startBtn.addActionListener(e -> {
            game = new EscapeGame();
            secondsElapsed = 0;
            createGameUI();
            loadRoom();
            startTimer();
        });

        aboutBtn.addActionListener(e -> JOptionPane.showMessageDialog(frame,
                "<html><body style='width: 300px; font-family: Monospaced; background-color: #05050F; color: #00FFFF; padding: 20px;'>" +
                "<h2 style='color: #FF00FF;'>ABOUT THE GAME</h2>" +
                "You are stuck in a room.<br>" +
                "Answer each question correctly to escape to the next room.<br><br>" +
                "<b>RULES:</b><br>" +
                "- 3 tries per room!<br>" +
                "- Solve all puzzles to win.<br><br>" +
                "Good luck!</body></html>",
                "About the Game", JOptionPane.PLAIN_MESSAGE));

        exitBtn.addActionListener(e -> System.exit(0));
    }

    private void createGameUI() {
        SciFiPanel bgPanel = new SciFiPanel(false);
        bgPanel.setLayout(new BorderLayout(25, 25));
        bgPanel.setBorder(new EmptyBorder(30, 30, 30, 30));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        
        JLabel titleLabel = new JLabel("ESCAPE ROOM CHALLENGE", SwingConstants.LEFT);
        titleLabel.setFont(new Font("Monospaced", Font.BOLD, 20));
        titleLabel.setForeground(COLOR_ACCENT);
        
        timerLabel = new JLabel("TIME: 00:00", SwingConstants.RIGHT);
        timerLabel.setFont(new Font("Monospaced", Font.BOLD, 22));
        timerLabel.setForeground(COLOR_SECONDARY);
        
        header.add(titleLabel, BorderLayout.WEST);
        header.add(timerLabel, BorderLayout.EAST);
        
        progressBar = new JProgressBar(0, 5);
        progressBar.setValue(0);
        progressBar.setForeground(COLOR_ACCENT);
        progressBar.setBackground(new Color(0, 255, 255, 30));
        progressBar.setBorder(BorderFactory.createLineBorder(COLOR_ACCENT, 1));
        progressBar.setPreferredSize(new Dimension(800, 4));
        
        JPanel topContainer = new JPanel(new BorderLayout(0, 10));
        topContainer.setOpaque(false);
        topContainer.add(header, BorderLayout.NORTH);
        topContainer.add(progressBar, BorderLayout.SOUTH);

        bgPanel.add(topContainer, BorderLayout.NORTH);

        HudPanel centerPanel = new HudPanel();
        centerPanel.setLayout(new BorderLayout(20, 20));
        centerPanel.setBorder(new EmptyBorder(40, 40, 40, 40));

        questionArea = new JTextArea();
        questionArea.setEditable(false);
        questionArea.setLineWrap(true);
        questionArea.setWrapStyleWord(true);
        questionArea.setFont(new Font("Monospaced", Font.PLAIN, 20));
        questionArea.setForeground(COLOR_TEXT);
        questionArea.setOpaque(false);
        questionArea.setHighlighter(null);
        centerPanel.add(questionArea, BorderLayout.CENTER);

        bgPanel.add(centerPanel, BorderLayout.CENTER);

        JPanel footer = new JPanel();
        footer.setLayout(new BoxLayout(footer, BoxLayout.Y_AXIS));
        footer.setOpaque(false);

        JPanel inputContainer = new JPanel(new BorderLayout(15, 0));
        inputContainer.setOpaque(false);

        answerField = new JTextField();
        answerField.setFont(new Font("Monospaced", Font.BOLD, 20));
        answerField.setOpaque(false);
        answerField.setForeground(Color.WHITE);
        answerField.setCaretColor(COLOR_ACCENT);
        
        JPanel fieldWrapper = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(0, 255, 255, 20));
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.setColor(COLOR_ACCENT);
                g2d.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
                g2d.setStroke(new BasicStroke(3));
                g2d.drawLine(0, 0, 10, 0); g2d.drawLine(0, 0, 0, 10);
                g2d.drawLine(getWidth()-1, 0, getWidth()-11, 0); g2d.drawLine(getWidth()-1, 0, getWidth()-1, 10);
                g2d.dispose();
            }
        };
        fieldWrapper.setOpaque(false);
        fieldWrapper.setBorder(new EmptyBorder(10, 15, 10, 15));
        fieldWrapper.add(answerField, BorderLayout.CENTER);

        submitButton = new HudButton("SUBMIT", COLOR_ACCENT);
        submitButton.setPreferredSize(new Dimension(150, 45));

        inputContainer.add(fieldWrapper, BorderLayout.CENTER);
        inputContainer.add(submitButton, BorderLayout.EAST);

        JPanel stats = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 10));
        stats.setOpaque(false);

        scoreLabel = createHudLabel("SCORE: 0", Color.GREEN);
        triesLabel = createHudLabel("TRIES LEFT: 3", Color.ORANGE);
        feedbackLabel = new JLabel("Enter your answer and press Submit.", SwingConstants.CENTER);
        feedbackLabel.setFont(new Font("Monospaced", Font.ITALIC, 14));
        feedbackLabel.setForeground(new Color(200, 200, 200));

        stats.add(scoreLabel);
        stats.add(triesLabel);

        footer.add(inputContainer);
        footer.add(feedbackLabel);
        footer.add(stats);
        bgPanel.add(footer, BorderLayout.SOUTH);

        submitButton.addActionListener(e -> handleAnswer());
        answerField.addActionListener(e -> handleAnswer());

        frame.setContentPane(bgPanel);
        frame.revalidate();
        frame.repaint();
    }

    private JLabel createHudLabel(String text, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Monospaced", Font.BOLD, 16));
        label.setForeground(color);
        return label;
    }

    private void startTimer() {
        if (gameTimer != null) gameTimer.stop();
        gameTimer = new Timer(1000, e -> {
            secondsElapsed++;
            int mins = secondsElapsed / 60;
            int secs = secondsElapsed % 60;
            timerLabel.setText(String.format("TIME: %02d:%02d", mins, secs));
        });
        gameTimer.start();
    }

    private void loadRoom() {
        Room current = game.getCurrentRoom();
        remainingTries = 3;
        if (current != null) {
            questionArea.setText(current.getRoomName().toUpperCase() + ":\n\n" + current.getQuestion());
            answerField.setText("");
            answerField.setEnabled(true);
            submitButton.setEnabled(true);
            answerField.requestFocusInWindow();
            triesLabel.setText("TRIES LEFT: " + remainingTries);
            scoreLabel.setText("SCORE: " + game.getScore());
            progressBar.setValue(game.getScore());
        } else {
            gameTimer.stop();
            progressBar.setValue(5);
            questionArea.setText("CONGRATULATIONS! YOU'VE ESCAPED ALL THE ROOMS!");
            answerField.setEnabled(false);
            submitButton.setEnabled(false);
            feedbackLabel.setText("GAME COMPLETED!");
            feedbackLabel.setForeground(Color.GREEN);
        }
    }

    private void handleAnswer() {
        String userInput = answerField.getText().trim();
        if (userInput.isEmpty()) return;

        boolean correct = game.submitAnswer(userInput);
        playFeedbackSound(correct);
        
        if (correct) {
            feedbackLabel.setText("ACCESS GRANTED.");
            feedbackLabel.setForeground(COLOR_ACCENT);
            Timer delay = new Timer(2000, e -> loadRoom());
            delay.setRepeats(false);
            delay.start();
        } else {
            remainingTries--;
            if (remainingTries <= 0) {
                gameTimer.stop();
                feedbackLabel.setText("ACCESS DENIED. SYSTEM LOCKDOWN.");
                feedbackLabel.setForeground(Color.RED);
                questionArea.setText("GAME OVER! YOU'VE USED ALL ATTEMPTS.");
                answerField.setEnabled(false);
                submitButton.setEnabled(false);
                triesLabel.setText("TRIES LEFT: 0");
            } else {
                feedbackLabel.setText("ACCESS DENIED. TRY AGAIN.");
                feedbackLabel.setForeground(Color.RED);
                triesLabel.setText("TRIES LEFT: " + remainingTries);
            }
        }
    }

    private void playFeedbackSound(boolean success) {
        String filePath = success ? SOUND_GRANTED : SOUND_DENIED;
        File file = new File(filePath);
        if (!file.exists()) return;

        new Thread(() -> {
            try {
                // Using PowerShell to play MP3 on Windows without external libraries
                // We use double quotes inside single quotes to handle spaces properly in the path
                String command = "powershell.exe -c \"Add-Type -AssemblyName PresentationCore; " +
                                 "$m = New-Object System.Windows.Media.MediaPlayer; " +
                                 "$m.Open('" + file.getAbsolutePath().replace("'", "''") + "'); " +
                                 "$m.Play(); Start-Sleep -s 5\"";
                Runtime.getRuntime().exec(command);
            } catch (Exception ignored) {}
        }).start();
    }

    // --- Custom Sci-Fi Components ---

    class SciFiPanel extends JPanel {
        private final boolean showGrid;
        public SciFiPanel(boolean showGrid) {
            this.showGrid = showGrid;
            setBackground(COLOR_BG);
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            if (showGrid) {
                g2d.setColor(new Color(0, 255, 255, 10));
                for (int x = 0; x < getWidth(); x += 40) g2d.drawLine(x, 0, x, getHeight());
                for (int y = 0; y < getHeight(); y += 40) g2d.drawLine(0, y, getWidth(), y);
            }
            RadialGradientPaint rgp = new RadialGradientPaint(
                new Point(getWidth()/2, getHeight()/2), getWidth(),
                new float[]{0.0f, 1.0f},
                new Color[]{new Color(0, 0, 0, 0), new Color(0, 255, 255, 30)}
            );
            g2d.setPaint(rgp);
            g2d.fillRect(0, 0, getWidth(), getHeight());
            g2d.dispose();
        }
    }

    class HudPanel extends JPanel {
        public HudPanel() { setOpaque(false); }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(COLOR_HUD_BG);
            g2d.fillRect(0, 0, getWidth(), getHeight());
            g2d.setColor(COLOR_ACCENT);
            g2d.setStroke(new BasicStroke(1));
            g2d.drawRect(5, 5, getWidth()-11, getHeight()-11);
            int size = 30;
            g2d.setStroke(new BasicStroke(4));
            g2d.drawLine(0, 0, size, 0); g2d.drawLine(0, 0, 0, size);
            g2d.drawLine(getWidth(), 0, getWidth()-size, 0); g2d.drawLine(getWidth(), 0, getWidth(), size);
            g2d.drawLine(0, getHeight(), size, getHeight()); g2d.drawLine(0, getHeight(), 0, getHeight()-size);
            g2d.drawLine(getWidth(), getHeight(), getWidth()-size, getHeight()); g2d.drawLine(getWidth(), getHeight(), getWidth(), getHeight()-size);
            g2d.dispose();
        }
    }

    class HudButton extends JButton {
        private Color color;
        private boolean hovered = false;
        public HudButton(String text, Color color) {
            super(text);
            this.color = color;
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setForeground(color);
            setFont(new Font("Monospaced", Font.BOLD, 18));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setAlignmentX(Component.CENTER_ALIGNMENT);
            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { hovered = true; repaint(); }
                public void mouseExited(MouseEvent e) { hovered = false; repaint(); }
            });
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            if (hovered) {
                g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 50));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
            g2d.setColor(color);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawRect(0, 0, getWidth()-1, getHeight()-1);
            super.paintComponent(g);
            g2d.dispose();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(EscapeRoomGUI::new);
    }
}
