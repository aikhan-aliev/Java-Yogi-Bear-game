/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package assignment_3;

/**
 *
 * @author Ayxan
 */
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Park {
    public static final int ROWS = 11;
    public static final int COLS = 20;
    private char[][] grid;
    private int startX, startY;

    // all my levels
    private final String[] levels = {
        "levels/level1.txt",
        "levels/level2.txt",
//        "levels/level9.txt",
//        "levels/level4.txt",
//        "levels/level5.txt",
//        "levels/level6.txt",
//        "levels/level7.txt",
//        "levels/level8.txt",
//        "levels/level3.txt",
//        "levels/level10.txt"
    };
    private int currentLevelIndex = 0; // we tracking the current level

    public Park(String levelFile) {
        loadLevel(levelFile);
    }

    public void loadLevel(String levelFile) {
    grid = new char[ROWS][COLS];
    boolean entranceFound = false; // To track if the 'E' has been found

    try (BufferedReader reader = new BufferedReader(new FileReader(levelFile))) {
        for (int i = 0; i < ROWS; i++) {
            String line = reader.readLine();
            if (line == null || line.length() < COLS) {
                throw new RuntimeException("Invalid level file: Line " + (i + 1) + " is missing or too short.");
            }

            for (int j = 0; j < COLS; j++) {
                grid[i][j] = line.charAt(j);
                if (grid[i][j] == 'E') {
                    if (entranceFound) {
                        throw new RuntimeException("Invalid level file: Multiple entrances ('E') found.");
                    }
                    startX = i;
                    startY = j;
                    entranceFound = true;
                }
            }
        }

        if (!entranceFound) {
            throw new RuntimeException("Invalid level file: No entrance ('E') found.");
        }

        System.out.println("Level loaded: Start position at (" + startX + ", " + startY + ")");

    } catch (IOException e) {
        throw new RuntimeException("Failed to load level: " + e.getMessage());
    }
}

    public boolean loadNextLevel() {
        currentLevelIndex++;
        if (currentLevelIndex < levels.length) {
            loadLevel(levels[currentLevelIndex]);
            return true;
        }
        return false;
    }

    public ArrayList<Ranger> initializeRangers() {
        ArrayList<Ranger> rangers = new ArrayList<>();
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                if (grid[i][j] == 'R') { // 'R' for Ranger
                    rangers.add(new Ranger(i, j));
                }
            }
        }
        return rangers;
    }

    public boolean isBasket(int x, int y) {
        return grid[x][y] == 'B'; 
    }

    public void collectBasket(int x, int y) {
        if (grid[x][y] == 'B') {
            grid[x][y] = ' ';
        }
    }

    public boolean allBasketsCollected() {
        for (char[] row : grid) {
            for (char cell : row) {
                if (cell == 'B') return false;
            }
        }
        return true;
    }

    public boolean isWalkable(int x, int y) {
        return x >= 0 && x < ROWS && y >= 0 && y < COLS && (grid[x][y] == ' ' || grid[x][y] == 'B');
    }

    public void draw(Graphics g, int panelWidth, int panelHeight) {
        int cellSize = Math.min(panelWidth / COLS, panelHeight / ROWS);

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                if (grid[i][j] == 'B') g.setColor(Color.YELLOW); 
                else if (grid[i][j] == '#') g.setColor(Color.DARK_GRAY);
                else if (grid[i][j] == 'R') g.setColor(Color.RED); // Ranger position
                else g.setColor(Color.LIGHT_GRAY);

                g.fillRect(j * cellSize, i * cellSize, cellSize, cellSize);
                g.setColor(Color.BLACK);
                g.drawRect(j * cellSize, i * cellSize, cellSize, cellSize);
            }
        }
    }

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }
}