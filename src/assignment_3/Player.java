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

public class Player {
    private int x, y;

    public Player(int startX, int startY) {
        this.x = startX;
        this.y = startY;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void resetPosition(int startX, int startY) {
        this.x = startX;
        this.y = startY;
    }

    public void move(String direction, Park park) {
        int newX = x;
        int newY = y;

        switch (direction) {
            case "UP" -> newX--;
            case "DOWN" -> newX++;
            case "LEFT" -> newY--;
            case "RIGHT" -> newY++;
        }

        if (park.isWalkable(newX, newY)) {
            x = newX;
            y = newY;
        }
    }

    public boolean isNeighbor(int rangerX, int rangerY) {
        int dx = Math.abs(x - rangerX);
        int dy = Math.abs(y - rangerY);
        return dx + dy == 1;
    }

    public void draw(Graphics g, int panelWidth, int panelHeight) {
        int cellSize = Math.min(panelWidth / Park.COLS, panelHeight / Park.ROWS);
        g.setColor(Color.BLUE);
        g.fillOval(y * cellSize, x * cellSize, cellSize, cellSize);
    }
}
