/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package assignment_3;

/**
 *
 * @author Ayxan
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class YogiBearGame extends JPanel implements KeyListener {
    private Park park;
    private Player player;
    private ArrayList<Ranger> rangers;
    private int picnicBasketsCollected;
    private int lives;
    private final Timer gameTimer;
    private final Timer elapsedTimeTimer;
    private int elapsedTimeInSeconds;
    private boolean gameOver;
    private final DataBase database;
    private JFrame frame;
    

    public YogiBearGame(JFrame frame) {
        this.frame = frame;
        this.lives = 3;
        this.picnicBasketsCollected = 0;
        this.elapsedTimeInSeconds = 0;
        this.gameOver = false;

        try {
            database = new DataBase();
        } catch (SQLException e) {
            throw new RuntimeException("Database connection failed: " + e.getMessage());
        }

        // Initialize game
        park = new Park("levels/level1.txt");
        player = new Player(park.getStartX(), park.getStartY());
        rangers = park.initializeRangers();

        setupMenu(frame);

        gameTimer = new Timer(500, e -> {
            for (Ranger ranger : rangers) {
                ranger.move(park);
                if (player.isNeighbor(ranger.getX(), ranger.getY())) {
                    lives--;
                    if (lives > 0) {
                        player.resetPosition(park.getStartX(), park.getStartY());
                    } else {
                        endGame(false);
                    }
                }
            }
            repaint();
        });

        elapsedTimeTimer = new Timer(1000, e -> {
            elapsedTimeInSeconds++;
            repaint();
        });

        gameTimer.start();
        elapsedTimeTimer.start();

        setFocusable(true);
        addKeyListener(this);
        setPreferredSize(new Dimension(500, 500));
    }

    private void setupMenu(JFrame frame) {
        JMenuBar menuBar = new JMenuBar();

        // Leaderboard menu item
        JMenuItem leaderboardItem = new JMenuItem("Leaderboard");
        leaderboardItem.addActionListener(e -> displayHighScores(frame));

        // Restart game menu item
        JMenuItem restartItem = new JMenuItem("Restart Game");
        restartItem.addActionListener(e -> restartGame(frame));

        frame.setJMenuBar(menuBar);
    }

    private void endGame(boolean won) {
        gameTimer.stop();
        elapsedTimeTimer.stop();
        gameOver = true;

        // Different messages for winning and losing
        String message = won ? "Congratulations! You won all levels!" : "You lose the game!";
        String playerName = JOptionPane.showInputDialog(this, message + "\nEnter your name:");

        if (playerName != null && !playerName.trim().isEmpty()) {
            try {
                database.addPlayerScore(playerName, picnicBasketsCollected);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Failed to save score: " + e.getMessage());
            }
        }

        // Display the leaderboard and restart option
        displayHighScores(frame);
    }



    private void displayHighScores(JFrame frame) {
    try {
        ArrayList<LeaderBoardEnt> leaderboard = database.getLeaderboard();
        StringBuilder scores = new StringBuilder("Leaderboard:\n\n");

        for (LeaderBoardEnt entry : leaderboard) {
            scores.append(entry).append("\n");
        }

        JOptionPane.showMessageDialog(this, scores.toString(), "Leaderboard", JOptionPane.INFORMATION_MESSAGE);

        int choice = JOptionPane.showOptionDialog(
                this,
                "Do you want to restart the game?",
                "Game Over",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new String[]{"Restart", "Exit"}, // Button labels
                "Restart" // Default
        );

        if (choice == JOptionPane.YES_OPTION) {
            restartGame(frame);
        } else {
            System.exit(0);
        }

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Failed to load high scores: " + e.getMessage());
    }
}


    private void restartGame(JFrame frame) {
    frame.dispose(); // Close
    SwingUtilities.invokeLater(() -> {
        JFrame newFrame = new JFrame("Yogi Bear Game");
        newFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        newFrame.add(new YogiBearGame(newFrame));
        newFrame.pack();
        newFrame.setVisible(true);
    });
}

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        park.draw(g, getWidth(), getHeight());
        player.draw(g, getWidth(), getHeight());
        for (Ranger ranger : rangers) {
            ranger.draw(g, getWidth(), getHeight());
        }

        g.setColor(Color.WHITE);
        g.drawString("Lives: " + lives, 10, 20);
        g.drawString("Baskets: " + picnicBasketsCollected, 10, 40);
        g.drawString("Time: " + elapsedTimeInSeconds + "s", 10, 60);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (gameOver) return;

        String direction = switch (e.getKeyCode()) {
            case KeyEvent.VK_W -> "UP";
            case KeyEvent.VK_A -> "LEFT";
            case KeyEvent.VK_S -> "DOWN";
            case KeyEvent.VK_D -> "RIGHT";
            default -> null;
        };

        if (direction != null) {
            player.move(direction, park);

            if (park.isBasket(player.getX(), player.getY())) {
                picnicBasketsCollected++;
                park.collectBasket(player.getX(), player.getY());

                if (park.allBasketsCollected()) {
                    if (!park.loadNextLevel()) {
                        endGame(true);
                    } else {
                        player.resetPosition(park.getStartX(), park.getStartY());
                        rangers = park.initializeRangers();
                        elapsedTimeInSeconds = 0;
                    }
                }
            }
            repaint();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}
}
