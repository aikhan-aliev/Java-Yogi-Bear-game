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
import java.util.Random;

public class Ranger {
    private int x, y;
    private final Random random;

    public Ranger(int startX, int startY) {
        this.x = startX;
        this.y = startY;
        this.random = new Random();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void move(Park park) {
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        int[] direction = directions[random.nextInt(directions.length)];
        int newX = x + direction[0];
        int newY = y + direction[1];

        if (park.isWalkable(newX, newY)) {
            x = newX;
            y = newY;
        }
    }

    public void draw(Graphics g, int panelWidth, int panelHeight) {
        int cellSize = Math.min(panelWidth / Park.COLS, panelHeight / Park.ROWS);
        g.setColor(Color.RED);
        g.fillRect(y * cellSize, x * cellSize, cellSize, cellSize);
    }
}
